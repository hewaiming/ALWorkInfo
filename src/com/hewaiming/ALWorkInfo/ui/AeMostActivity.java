package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.MyPageAdapter;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AeCntAdapter;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AeTimeAdapter;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.fragment.Fragment_AeCnt;
import com.hewaiming.ALWorkInfo.fragment.Fragment_AeTime;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AeMostActivity extends  FragmentActivity implements HttpGetListener, OnScrollListener, OnClickListener {
	private Spinner spinner_area,spinner_PotNo, spinner_beginDate, spinner_endDate;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ArrayAdapter<String> Area_adapter, Date_adapter;
	// private ArrayAdapter<String> PotNo_adapter;

	private HttpPost_BeginDate_EndDate http_post;
	private String AeCnt_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/AeRecord_potno_date.php";
	private String AeTime_url = "http://125.64.59.11:8000/scgy/android/odbcPhP/AeRecord_area_date.php";

	private String BeginDate, EndDate;
	private List<String> dateBean = new ArrayList<String>();
	// private List<String> PotNoList=null;
	private List<AeRecord> listBean_AeTime = null;
	private List<AeRecord> listBean_AeCnt = null;
	private HSView_AeTimeAdapter AeTime_Adapter = null;
	private HSView_AeCntAdapter AeCnt_Adapter = null;
	private RelativeLayout mHead_AeCnt, mHead_AeTime;
	private ListView lv_AeCnt, lv_AeTime;
	private ArrayList<Fragment> fragments;
	private ViewPager pager;
	private MyPageAdapter adapter;
	private RadioGroup group;
	private RadioButton button0;
	private RadioButton button1;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ae_most);
		dateBean = getIntent().getStringArrayListExtra("date_record");		
		init_area();	
		init_date();
		init_title();
//		init_HSView();
		init_Tab();
	}

	private void init_Tab() {
		fragments = new ArrayList<Fragment>();
		fragments.add(new Fragment_AeCnt());
		fragments.add(new Fragment_AeTime());		

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
//				case R.id.radio2:
//					pager.setCurrentItem(2);
//					break;
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


	private void init_HSView() {
		mHead_AeCnt = (RelativeLayout) findViewById(R.id.head_AeCnt); // 表头处理
		mHead_AeCnt.setFocusable(true);
		mHead_AeCnt.setClickable(true);
		mHead_AeCnt.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead_AeCnt.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		mHead_AeTime = (RelativeLayout) findViewById(R.id.head_AeTime); // 表头处理
		mHead_AeTime.setFocusable(true);
		mHead_AeTime.setClickable(true);
		mHead_AeTime.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead_AeTime.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_AeCnt = (ListView) findViewById(R.id.lv_AeCnt);
		lv_AeCnt.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_AeCnt.setCacheColorHint(0);
		lv_AeCnt.setOnScrollListener(this);

		lv_AeTime = (ListView) findViewById(R.id.lv_AeTime);
		lv_AeTime.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_AeTime.setCacheColorHint(0);
		lv_AeTime.setOnScrollListener(this);
	}

	/*
	 * private void init_potNo() { spinner_potno = (Spinner)
	 * findViewById(R.id.spinner_PotNo); PotNoList = new ArrayList<String>();
	 * for (int i = 1101; i <= 1136; i++) { PotNoList.add(i + ""); }
	 * PotNoList.add(0, "全部槽号"); PotNo_adapter = new ArrayAdapter<String>(this,
	 * android.R.layout.simple_spinner_item, PotNoList);
	 * PotNo_adapter.setDropDownViewResource(android.R.layout.
	 * simple_spinner_dropdown_item); spinner_potno.setAdapter(PotNo_adapter);
	 * spinner_potno.setVisibility(View.VISIBLE);
	 * spinner_potno.setOnItemSelectedListener(new OnItemSelectedListener() {
	 * 
	 * @Override public void onItemSelected(AdapterView<?> parent, View view,
	 * int position, long id) { PotNo = PotNoList.get(position).toString();
	 * 
	 * }
	 * 
	 * @Override public void onNothingSelected(AdapterView<?> parent) {
	 * 
	 * }
	 * 
	 * });
	 * 
	 * }
	 */
	private void init_date() {
		spinner_PotNo=(Spinner)findViewById(R.id.spinner_PotNo);
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

	}

	private void init_area() {
		spinner_area = (Spinner) findViewById(R.id.spinner_area);

		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
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
					areaId = 11;
					break;
				case 1:
					areaId = 12;
					break;
				case 2:
					areaId = 13;
					break;
				case 3:
					areaId = 21;
					break;
				case 4:
					areaId = 22;
					break;
				case 5:
					areaId = 23;
					break;
				}

				// PotNoChanged(areaId);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}

		});
		findBtn = (Button) findViewById(R.id.btn_ok);
		findBtn.setOnClickListener(this);
	}

	/*
	 * protected void PotNoChanged(int areaId2) { switch (areaId2) { case 11:
	 * PotNoList.clear(); for (int i = 1101; i <= 1136; i++) { PotNoList.add(i +
	 * ""); } break; case 12: PotNoList.clear(); for (int i = 1201; i <= 1237;
	 * i++) { PotNoList.add(i + ""); } break; case 13: PotNoList.clear(); for
	 * (int i = 1301; i <= 1337; i++) { PotNoList.add(i + ""); } break; case 21:
	 * PotNoList.clear(); for (int i = 2101; i <= 2136; i++) { PotNoList.add(i +
	 * ""); } break; case 22: PotNoList.clear(); for (int i = 2201; i <= 2237;
	 * i++) { PotNoList.add(i + ""); } break; case 23: PotNoList.clear(); for
	 * (int i = 2301; i <= 2337; i++) { PotNoList.add(i + ""); } break; }
	 * PotNoList.add(0, "全部槽号"); PotNo_adapter.notifyDataSetChanged();// 通知数据改变
	 * spinner_potno.setId(0); }
	 */

	@Override
	public void GetDataUrl(String data) {

		if (data.equals("")) {
			Toast.makeText(getApplicationContext(), "没有获取到[效应槽]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			if (listBean_AeCnt != null) {
				if (listBean_AeCnt.size() > 0) {
					listBean_AeCnt.clear(); // 清除LISTVIEW 以前的内容
					AeCnt_Adapter.onDateChange(listBean_AeCnt);
				}
			}
		} else {

			listBean_AeCnt = new ArrayList<AeRecord>();
			listBean_AeCnt.clear();
			listBean_AeCnt = JsonToBean_Area_Date.JsonArrayToAeRecordBean(data);
			AeCnt_Adapter = new HSView_AeCntAdapter(this, R.layout.item_hsview_aecnt, listBean_AeCnt, mHead_AeCnt);
			lv_AeCnt.setAdapter(AeCnt_Adapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_ok:
			if (EndDate.compareTo(BeginDate) < 0) {
				Toast.makeText(getApplicationContext(), "日期选择不对：截止日期小于开始日期", 1).show();
			} else {
				http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(AeCnt_url, 1,
						Integer.toString(areaId), BeginDate, EndDate, this, this).execute();
				http_post = (HttpPost_BeginDate_EndDate) new HttpPost_BeginDate_EndDate(AeTime_url, 1,
						Integer.toString(areaId), BeginDate, EndDate, this, this).execute();

			}
			break;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		public boolean onTouch(View arg0, MotionEvent arg1) {
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView_AeCnt = (HorizontalScrollView) mHead_AeCnt
					.findViewById(R.id.horizontalScrollView_AeCnt);
			HorizontalScrollView headSrcrollView_AeTime = (HorizontalScrollView) mHead_AeTime
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView_AeCnt.onTouchEvent(arg1);
			headSrcrollView_AeTime.onTouchEvent(arg1);

			return false;
		}
	}

}
