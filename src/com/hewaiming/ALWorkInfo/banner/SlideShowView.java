package com.hewaiming.ALWorkInfo.banner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.banner.JazzyViewPager.TransitionEffect;
import com.hewaiming.ALWorkInfo.config.ImageLoadOptions;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * ViewPagerʵ�ֵ��ֲ�ͼ����Զ�����ͼ���義����ҳ�Ĺ���ֲ�ͼЧ���� ��֧���Զ��ֲ�ҳ��Ҳ֧�����ƻ����л�ҳ��
 *
 */

public class SlideShowView extends FrameLayout {

	private static DisplayImageOptions options;

	private JazzyViewPager mJazzy;

	// ʹ��universal-image-loader�����ȡ����ͼƬ����Ҫ���̵���universal-image-loader-1.8.6-with-sources.jar
	private ImageLoader imageLoader = ImageLoader.getInstance();

	// �Զ��ֲ���ʱ����
	private final static int TIME_INTERVAL = 4;
	// �Զ��ֲ����ÿ���
	private final static boolean isAutoPlay = true;
	// �������Ƿ������
	private boolean isScrolling = false;

	// �Զ����ֲ�ͼ����Դ
	private List<String> imageUrls;
	// ���ֲ�ͼƬ��ImageView ��list
	private List<ImageView> imageViewsList;
	// ��Բ���View��list
	private List<View> dotViewsList;
	
	private boolean[] firstRun;

	// ��ǰ�ֲ�ҳ
	private int currentItem = 1;
	// ��ʱ����
	private ScheduledExecutorService scheduledExecutorService;

	private Context context;

	// Handler
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mJazzy.setCurrentItem(currentItem);
		}
	};

	public SlideShowView(Context context) {
		this(context, null);
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initImageLoader(context);
		initData();
		if (isAutoPlay) {
			startPlay();
		}

	}

	/**
	 * ��ʼ�ֲ�ͼ�л�
	 */
	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, TIME_INTERVAL, TimeUnit.SECONDS);
	}

	/**
	 * ֹͣ�ֲ�ͼ�л�
	 */
	private void stopPlay() {
		scheduledExecutorService.shutdown();
	}

	/**
	 * ��ʼ�����Data
	 */
	private void initData() {
		
		imageUrls = new ArrayList<String>();
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();
		// TODO �첽�����ȡͼƬ��������Ը�дΪͨ�������ȡͼƬ��ַ
		new GetListTask().execute("");
	}

	/**
	 * ��ʼ��Views��UI
	 */
	private void initUI(Context context) {
		if (imageUrls == null || imageUrls.size() == 0)
			return;
		LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);

		LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();
		
		firstRun=new boolean[imageUrls.size()+2];  //��ʼ�� ��һ�����б������
        for(int i=0;i<firstRun.length;i++){
        	firstRun[i]=true;
        }
		// �ȵ������ͼƬ�������
		for (int i = 0; i < imageUrls.size(); i++) {
		
			ImageView view = new ImageView(context);
			view.setTag(imageUrls.get(i));
			view.setBackgroundResource(R.drawable.banner_default);
			view.setScaleType(ScaleType.FIT_XY);
			imageViewsList.add(view);

			ImageView dotView = new ImageView(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
			params.leftMargin = 4;
			params.rightMargin = 4;
			dotLayout.addView(dotView, params);
			dotViewsList.add(dotView);
		}
		// begin ������Ҫ��Ϊ�˽��viewpager����ѭ�����⣬�ֱ������һ��ͼ����ϵ�һ��ͼ����һ��ͼǰ�������һ��ͼ
		ImageView viewFirst = new ImageView(context);
		viewFirst.setTag(imageUrls.get(imageUrls.size() - 1));
		viewFirst.setBackgroundResource(R.drawable.banner_default);
		viewFirst.setScaleType(ScaleType.FIT_XY);
		imageViewsList.add(0, viewFirst);
		ImageView viewLast = new ImageView(context);
		viewLast.setTag(imageUrls.get(0));
		viewLast.setBackgroundResource(R.drawable.banner_default);
		viewLast.setScaleType(ScaleType.FIT_XY);
		imageViewsList.add(viewLast);
		// end
		mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_pager);
		// TODO �����л���������
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		mJazzy.setAdapter(new MainAdapter());
		mJazzy.setCurrentItem(1);
		mJazzy.setPageMargin(0);
		mJazzy.setOnPageChangeListener(new MyPageChangeListener());
	}

	/**
	 * ViewPager�ļ����� ��ViewPager��ҳ���״̬�����ı�ʱ����
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:// ���ڻ���
				isScrolling = true;
				break;
			case 2:// �������
				isScrolling = false;
				break;
			case 0:// ʲô��û��
					// �������һ����������һ�����ߴӵ�һ�����������һ����ĬĬ�ؽ���ǰview��Ϊ�����ĵ�һ�������һ����
					// ʹ��setCurrentItem(int item, boolean smoothScroll)
					// smoothScrollΪflaseʱ�������л����������Կ�����ƭ�û���ʹ��о�����ҳ����л�
				if (mJazzy.getCurrentItem() == mJazzy.getAdapter().getCount() - 1) {
					mJazzy.setCurrentItem(1, false);
				} else if (mJazzy.getCurrentItem() == 0) {
					mJazzy.setCurrentItem(mJazzy.getAdapter().getCount() - 2, false);
				}
				isScrolling = false;
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int pos) {
			// ����adapter��ʵ����n+2��ͼƬ����Բ���View��ֻ��n��Բ�㣬������Ҫ����pos��i֮��Ķ�Ӧ��ϵ
			currentItem = pos;
			if (pos == 0) {
				pos = dotViewsList.size() - 1;
			} else if (pos == mJazzy.getAdapter().getCount() - 1) {
				pos = 0;
			} else {
				pos -= 1;
			}
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == pos) {
					dotViewsList.get(pos).setBackgroundResource(R.drawable.dot_focus);
				} else {
					dotViewsList.get(i).setBackgroundResource(R.drawable.dot_blur);
				}
			}
		}
	}

	/**
	 * JazzyViewPager ��������
	 * 
	 * @author yuyang 2015��6��9��
	 *
	 */
	private class MainAdapter extends PagerAdapter {
	

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			ImageView imageView = imageViewsList.get(position);
			if(firstRun[position]){
				imageLoader.displayImage(imageView.getTag() + "", imageView); //��ʡ���� ����һ�����вŴ�����ȥͼƬ
				firstRun[position]=false;
			}
			
//			imageLoader.displayImage(imageView.getTag() + "", imageView);
			
			((ViewPager) container).addView(imageViewsList.get(position));
			mJazzy.setObjectForPosition(imageViewsList.get(position), position);
			return imageViewsList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object obj) {
			container.removeView(mJazzy.findViewFromObject(position));
		}

		@Override
		public int getCount() {
			return imageViewsList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof OutlineContainer) {
				return ((OutlineContainer) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}
	}

	/**
	 * ִ���ֲ�ͼ�л�����
	 *
	 */
	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			synchronized (mJazzy) {
				if (!isScrolling) {
					currentItem = (currentItem + 1) % imageViewsList.size();
					handler.obtainMessage().sendToTarget();
				}
			}
		}

	}

	/**
	 * ����ImageView��Դ�������ڴ�
	 * 
	 */
	private void destoryBitmaps() {

		for (int i = 0; i < imageViewsList.size(); i++) {
			ImageView imageView = imageViewsList.get(i);
			Drawable drawable = imageView.getDrawable();
			if (drawable != null) {
				// ���drawable��view������
				drawable.setCallback(null);
			}
		}
	}

	/**
	 * �첽����,��ȡ����
	 * 
	 */
	class GetListTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				// ����һ����÷���˽ӿڻ�ȡһ���ֲ�ͼƬ�������ǴӰٶ��ҵļ���ͼƬ
				String[] picAddress = MyConst.pic_address;
				for (int i = 0; i< picAddress.length; i++) {
					imageUrls.add(picAddress[i]);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result) {
				initUI(context);
			}
		}
	}

	/**
	 * ImageLoader ͼƬ�����ʼ��
	 * 
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		options=new ImageLoadOptions().getOptions();
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.memoryCacheExtraOptions(480, 800) // maxwidth, max
													// height���������ÿ�������ļ�����󳤿�
				.threadPoolSize(3)// �̳߳��ڼ��ص�����
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You
																				// can
																				// pass
																				// your
																				// own
																				// memory
																				// cache

				// implementation/�����ͨ���Լ����ڴ滺��ʵ��
				.memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)

				.tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100) // ������ļ�����
				.discCache(new UnlimitedDiscCache(cacheDir))// �Զ��建��·��
//				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.defaultDisplayImageOptions(options)
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																						// (5
																						// s),
																						// readTimeout
																						// (30
																						// s)��ʱʱ��
				.writeDebugLogs() // Remove for releaseapp
				.build();// ��ʼ����

		/*
		 * ImageLoaderConfiguration config = new
		 * ImageLoaderConfiguration.Builder(context)
		 * .threadPriority(Thread.NORM_PRIORITY - 2)
		 * .denyCacheImageMultipleSizesInMemory()
		 * 
		 * .discCacheFileNameGenerator(new Md5FileNameGenerator())
		 * .tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs()
		 * .build();
		 */
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
}