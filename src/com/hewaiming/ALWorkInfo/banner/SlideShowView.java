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
 * ViewPager实现的轮播图广告自定义视图，如京东首页的广告轮播图效果； 既支持自动轮播页面也支持手势滑动切换页面
 *
 */

public class SlideShowView extends FrameLayout {

	private static DisplayImageOptions options;

	private JazzyViewPager mJazzy;

	// 使用universal-image-loader插件读取网络图片，需要工程导入universal-image-loader-1.8.6-with-sources.jar
	private ImageLoader imageLoader = ImageLoader.getInstance();

	// 自动轮播的时间间隔
	private final static int TIME_INTERVAL = 4;
	// 自动轮播启用开关
	private final static boolean isAutoPlay = true;
	// 滚动框是否滚动着
	private boolean isScrolling = false;

	// 自定义轮播图的资源
	private List<String> imageUrls;
	// 放轮播图片的ImageView 的list
	private List<ImageView> imageViewsList;
	// 放圆点的View的list
	private List<View> dotViewsList;
	
	private boolean[] firstRun;

	// 当前轮播页
	private int currentItem = 1;
	// 定时任务
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
	 * 开始轮播图切换
	 */
	private void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, TIME_INTERVAL, TimeUnit.SECONDS);
	}

	/**
	 * 停止轮播图切换
	 */
	private void stopPlay() {
		scheduledExecutorService.shutdown();
	}

	/**
	 * 初始化相关Data
	 */
	private void initData() {
		
		imageUrls = new ArrayList<String>();
		imageViewsList = new ArrayList<ImageView>();
		dotViewsList = new ArrayList<View>();
		// TODO 异步任务获取图片，这里可以改写为通过网络获取图片地址
		new GetListTask().execute("");
	}

	/**
	 * 初始化Views等UI
	 */
	private void initUI(Context context) {
		if (imageUrls == null || imageUrls.size() == 0)
			return;
		LayoutInflater.from(context).inflate(R.layout.layout_slideshow, this, true);

		LinearLayout dotLayout = (LinearLayout) findViewById(R.id.dotLayout);
		dotLayout.removeAllViews();
		
		firstRun=new boolean[imageUrls.size()+2];  //初始化 第一次运行标记数组
        for(int i=0;i<firstRun.length;i++){
        	firstRun[i]=true;
        }
		// 热点个数与图片特殊相等
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
		// begin 这里主要是为了解决viewpager流畅循环问题，分别在最后一张图后加上第一张图、第一张图前加上最后一张图
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
		// TODO 设置切换动画类型
		mJazzy.setTransitionEffect(TransitionEffect.Standard);
		mJazzy.setAdapter(new MainAdapter());
		mJazzy.setCurrentItem(1);
		mJazzy.setPageMargin(0);
		mJazzy.setOnPageChangeListener(new MyPageChangeListener());
	}

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 * 
	 */
	private class MyPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:// 正在滑动
				isScrolling = true;
				break;
			case 2:// 滑动完毕
				isScrolling = false;
				break;
			case 0:// 什么都没做
					// 当从最后一个滑动至第一个或者从第一个滑动至最后一个后，默默地将当前view变为真正的第一个或最后一个，
					// 使用setCurrentItem(int item, boolean smoothScroll)
					// smoothScroll为flase时不会有切换动画，所以可以欺骗用户，使其感觉不到页面的切换
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
			// 由于adapter中实际有n+2张图片，而圆点的View中只有n个圆点，所以需要处理pos与i之间的对应关系
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
	 * JazzyViewPager 的适配器
	 * 
	 * @author yuyang 2015年6月9日
	 *
	 */
	private class MainAdapter extends PagerAdapter {
	

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			ImageView imageView = imageViewsList.get(position);
			if(firstRun[position]){
				imageLoader.displayImage(imageView.getTag() + "", imageView); //节省流量 ，第一次运行才从网络去图片
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
	 * 执行轮播图切换任务
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
	 * 销毁ImageView资源，回收内存
	 * 
	 */
	private void destoryBitmaps() {

		for (int i = 0; i < imageViewsList.size(); i++) {
			ImageView imageView = imageViewsList.get(i);
			Drawable drawable = imageView.getDrawable();
			if (drawable != null) {
				// 解除drawable对view的引用
				drawable.setCallback(null);
			}
		}
	}

	/**
	 * 异步任务,获取数据
	 * 
	 */
	class GetListTask extends AsyncTask<String, Integer, Boolean> {
		@Override
		protected Boolean doInBackground(String... params) {
			try {
				// 这里一般调用服务端接口获取一组轮播图片，下面是从百度找的几个图片
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
	 * ImageLoader 图片组件初始化
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
													// height，即保存的每个缓存文件的最大长宽
				.threadPoolSize(3)// 线程池内加载的数量
				.threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You
																				// can
																				// pass
																				// your
																				// own
																				// memory
																				// cache

				// implementation/你可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024).discCacheSize(50 * 1024 * 1024)

				.tasksProcessingOrder(QueueProcessingType.LIFO).discCacheFileCount(100) // 缓存的文件数量
				.discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
//				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.defaultDisplayImageOptions(options)
				.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout
																						// (5
																						// s),
																						// readTimeout
																						// (30
																						// s)超时时间
				.writeDebugLogs() // Remove for releaseapp
				.build();// 开始构建

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