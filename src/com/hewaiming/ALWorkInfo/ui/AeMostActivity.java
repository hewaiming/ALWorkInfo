package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.BackHandlerInterface;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.InterFace.LoadAeCntInterface;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener_other;
import com.hewaiming.ALWorkInfo.adapter.MyPageAdapter;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.fragment.BackHandledFragment;
import com.hewaiming.ALWorkInfo.fragment.Fragment_AeCnt;
import com.hewaiming.ALWorkInfo.fragment.Fragment_AeTime;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate_other;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AeMostActivity extends FragmentActivity
		implements BackHandlerInterface, HttpGetListener, HttpGetListener_other, OnClickListener {
	private Spinner spinner_area, spinner_PotNo, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;

	private HttpPost_BeginDate_EndDate http_post;
	private HttpPost_BeginDate_EndDate_other http_post_other;
	private String AeCnt_url = ":8000/scgy/android/odbcPhP/AeCnt_area_date.php";
	private String AeTime_url = ":8000/scgy/android/odbcPhP/AeTime_area_date.php";

	private String BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();

	private ArrayList<Fragment> fragments;

	private ViewPager pager;
	private MyPageAdapter adapter;
	private RadioGroup group;
	private RadioButton button0;
	private RadioButton button1;
	private int AE_CNT_TIME = 88;
	// 声明接口
	private LoadAeCntInterface listener_AeCnt = null;
	private HttpGetListener_other listener_AeTime = null;
	private Handler mHandler, mHandler_AeTime;
	private View layout_Ae;
	private ImageButton isShowingBtn;
	private LinearLayout showArea = null;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private Context mContext;

	private BackHandledFragment selectedFragment;
	private String ip;
	private int port;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ae_most);
		mContext = this;
		layout_Ae = findViewById(R.id.AeMost);
		GetDataFromIntent();
		init_area();
		init_date();
		init_title();
		init_Tab();
	}
	private void GetDataFromIntent() {
		dateBean = getIntent().getStringArrayListExtra("date_record");
		JXList = (List<Map<String, Object>>) getIntent().getSerializableExtra("JXList");
		ip=getIntent().getStringExtra("ip");
		port=getIntent().getIntExtra("port", 1234);
		AeCnt_url="http://"+ip+AeCnt_url;
		AeTime_url="http://"+ip+AeTime_url;
	}
	
	private void init_Tab() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new Fragment_AeCnt(mContext,dateBean,JXList,ip,port));
		fragments.add(new Fragment_AeTime(JXList,ip,port));

		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPageAdapter(getSupportFragmentManager(), fragments);
		pager.setAdapter(adapter);
		pager.setOffscreenPageLimit(fragments.size() - 1);// 缓存页面,显示第一个缓存最后一个
		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				Log.v("asdf", "onPageSelected");
				getTabState(arg0);

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

		group = (RadioGroup) findViewById(R.id.radioGroup1);
		button0 = (RadioButton) findViewById(R.id.radio_AeCnt);
		button1 = (RadioButton) findViewById(R.id.radio_AeTime);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_AeCnt:
					pager.setCurrentItem(0);
					break;
				case R.id.radio_AeTime:
					pager.setCurrentItem(1);
					break;
				default:
					break;
				}

			}
		});

	}

	protected void getTabState(int index) {

		button0.setChecked(false);
		button1.setChecked(false);

		switch (index) {
		case 0:
			button0.setChecked(true);
			break;
		case 1:
			button1.setChecked(true);
			break;
		default:
			break;
		}

	}

	private void init_date() {
		spinner_PotNo = (Spinner) findViewById(R.id.spinner_PotNo);
		spinner_PotNo.setVisibility(View.GONE);
		spinner_beginDate = (Spinner) findViewById(R.id.spinner_Begindate);
		spinner_endDate = (Spinner) findViewById(R.id.spinner_Enddate);
		Date_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateBean);
		// 设置下拉列表的风格
		Date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_beginDate.setAdapter(Date_adapter);
		spinner_endDate.setAdapter(Date_adapter);
		spinner_beginDate.setVisibility(View.VISIBLE);
		spinner_endDate.setVisibility(View.VISIBLE);
		BeginDate = spinner_beginDate.getItemAtPosition(0).toString();
		EndDate = spinner_endDate.getItemAtPosition(0).toString();

		spinner_beginDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				BeginDate = spinner_beginDate.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// 截止时间
		spinner_endDate.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				EndDate = spinner_endDate.getItemAtPosition(position).toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	private void init_title() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("效应槽");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

		isShowingBtn = (ImageButton) findViewById(R.id.btn_isSHOW);
		showArea = (LinearLayout) findViewById(R.id.Layout_selection);
		isShowingBtn.setOnClickListener(this);

	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);
		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas_ALL);
		// 设置下拉列表的风格
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner_area.setAdapter(Area_adapter);
		spinner_area.setVisibility(View.VISIBLE);
		spinner_area.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					areaId = 66;
					break;
				case 1:
					areaId = 61;
					break;
				case 2:
					areaId = 62;
					break;
				case 3:
					areaId = 11;
					break;
				case 4:
					areaId = 12;
					break;
				case 5:
					areaId = 13;
					break;
				case 6:
					areaId = 21;
					break;
				case 7:
					areaId = 22;
					break;
				case 8:
					areaId = 23;
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		findBtn = (Button) findViewById(R.id.btn_ok);
		findBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_isSHOW: // 显示或隐藏
			if (showArea.getVisibility() == View.GONE) {
				showArea.setVisibility(View.VISIBLE);
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_up));
			} else {
				showArea.setVisibility(View.GONE);
				isShowingBtn.setImageDrawable(getResources().getDrawable(R.drawable.btn_down));
			}
			break;
		case R.id.btn_ok:
			if (EndDate.compareTo(BeginDate) < 0) {
				Toast.makeText(getApplicationContext(), "日期选择不对：截止日期小于开始日期", 1).show();
			} else {
				http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(AeCnt_url, 1,
						Integer.toString(areaId), BeginDate, EndDate, this, this).execute();
				http_post_other = (HttpPost_BeginDate_EndDate_other) new HttpPost_BeginDate_EndDate_other(AeTime_url, 1,
						Integer.toString(areaId), BeginDate, EndDate, this, this).execute();
				layout_Ae.setVisibility(View.VISIBLE);
			}
			break;
		}
	}

	@Override
	public void GetDataUrl(String data) {
		if (data != null) {
			Message msg = new Message();
			msg.obj = data;
			msg.what = 1;
			mHandler.sendMessage(msg);
			Message msg_date1 = new Message();
			msg_date1.obj = BeginDate;
			msg_date1.what = 2;
			mHandler.sendMessage(msg_date1);
			Message msg_date2 = new Message();
			msg_date2.obj = EndDate;
			msg_date2.what = 3;
			mHandler.sendMessage(msg_date2);
		}	

	}

	public void setHandler(Handler handler) {
		mHandler = handler;

	}

	@Override
	public void GetOtherDataUrl(String data) {
		if (data != null) {
			Message msg = new Message();
			msg.obj = data;
			msg.what = 2;
			mHandler_AeTime.sendMessage(msg);
			// listener_AeTime.GetAeTimeDataUrl(data);
		}
	}

	public void setHandler_AeTime(Handler mHandler_AeTime2) {
		mHandler_AeTime = mHandler_AeTime2;

	}

	@Override
	public void setSelectedFragment(BackHandledFragment backHandledFragment) {
		this.selectedFragment =backHandledFragment;

	}

	@Override
	public void onBackPressed() {
		 if (getFragmentManager().findFragmentByTag("Fragment_AeCnt") != null
	                && getFragmentManager().findFragmentByTag("Fragment_AeCnt")
	                        .isVisible()) {		 
			 
		 }			 


		super.onBackPressed();
	}
}
