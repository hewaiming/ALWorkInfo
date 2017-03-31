package com.hewaiming.ALWorkInfo.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener_other;
import com.hewaiming.ALWorkInfo.InterFace.LoadAeCntInterface;
import com.hewaiming.ALWorkInfo.SlideBottomPanel.SlideBottomPanel;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AeCntAdapter;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AeRecAdapter;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_RealRecordAdapter;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.bean.FaultMost;
import com.hewaiming.ALWorkInfo.bean.RealRecord;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.net.HttpPost_BeginDate_EndDate_other;
import com.hewaiming.ALWorkInfo.ui.AeMostActivity;
import com.hewaiming.ALWorkInfo.ui.AeRecActivity;
import com.hewaiming.ALWorkInfo.ui.MainActivity;

import com.hewaiming.ALWorkInfo.view.FooterListView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_AeCnt extends Fragment implements LoadAeCntInterface, OnClickListener, OnScrollListener {

	private Context mContext;
	private RelativeLayout mHead_AeCnt;
	private ListView lv_AeCnt;
	private String ip;
	private int port;

	private View mView;
	private List<AeRecord> listBean_AeCnt = null;
	private List<String> dateBean = new ArrayList<String>();
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private HSView_AeCntAdapter AeCnt_Adapter = null;

	private AeMostActivity mActivity;
	protected String PotNo, BeginDate, EndDate;
	private FooterListView footView;
	private ListViewAndHeadViewTouchLinstener lvAndHVTouchListener;
	private TextView tv_Total;

	public Fragment_AeCnt(Context mContext, List<String> dateBean, List<Map<String, Object>> jXList, String mip,
			int mport) {
		this.mContext = mContext;
		this.dateBean = dateBean;
		this.JXList = jXList;
		this.ip = mip;
		this.port = mport;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_aecnt, container, false);		
		return mView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (AeMostActivity) activity;
		mActivity.setHandler(mHandler);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		lvAndHVTouchListener = new ListViewAndHeadViewTouchLinstener();

		mHead_AeCnt = (RelativeLayout) mView.findViewById(R.id.head_AeCnt); // 表头处理
		mHead_AeCnt.setFocusable(true);
		mHead_AeCnt.setClickable(true);
		mHead_AeCnt.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead_AeCnt.setOnTouchListener(lvAndHVTouchListener);		

		lv_AeCnt = (ListView) mView.findViewById(R.id.lv_AeCnt);
		lv_AeCnt.setOnTouchListener(lvAndHVTouchListener);		
		lv_AeCnt.setCacheColorHint(0);
		lv_AeCnt.setOnScrollListener(this);
		lv_AeCnt.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position != listBean_AeCnt.size()) {
					Intent aeRec_intent = new Intent(getActivity(), AeRecActivity.class);
					Bundle bundle_AeRec = new Bundle();
					bundle_AeRec.putStringArrayList("date_record", (ArrayList<String>) dateBean);
					bundle_AeRec.putBoolean("Hide_Action", true);
					bundle_AeRec.putString("PotNo", String.valueOf(listBean_AeCnt.get(position).getPotNo()));
					bundle_AeRec.putString("Begin_Date", BeginDate);
					bundle_AeRec.putString("End_Date", EndDate);
					bundle_AeRec.putSerializable("JXList", (Serializable) JXList);
					bundle_AeRec.putString("ip", ip);
					bundle_AeRec.putInt("port", port);
					aeRec_intent.putExtras(bundle_AeRec);
					startActivity(aeRec_intent); // 效应记录
				}
			}
		});
		init_footer();

	}

	private void init_footer() {
		footView = new FooterListView(mContext);// 添加表end
		lv_AeCnt.addFooterView(footView);
		tv_Total = (TextView) mView.findViewById(R.id.tv_footTotal);

	}

	@Override
	public void onClick(View v) {

	}

	public Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				init_adapter(msg.obj.toString());
				break;
			case 2:
				BeginDate = msg.obj.toString();
				break;
			case 3:
				EndDate = msg.obj.toString();
				break;
			}
		}
	};

	private void init_adapter(String data) {
		if (data.equals("")) {
			Toast.makeText(this.getActivity(), "没有获取到[效应槽：次数最多]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			tv_Total.setText("0次");
			if (listBean_AeCnt != null) {
				if (listBean_AeCnt.size() > 0) {
					listBean_AeCnt.clear(); // 清除LISTVIEW 以前的内容
					AeCnt_Adapter.onDateChange(listBean_AeCnt);
				}
			}
		} else {
			listBean_AeCnt = new ArrayList<AeRecord>(); // 初始化效应次数适配器
			listBean_AeCnt.clear();
			listBean_AeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(data);
			int total = 0;
			for (AeRecord tmp : listBean_AeCnt) {
				total = total + tmp.getWaitTime(); // 统计总数
			}
			AeCnt_Adapter = new HSView_AeCntAdapter(this.getActivity(), R.layout.item_hsview_aecnt, listBean_AeCnt,
					mHead_AeCnt);
			lv_AeCnt.setAdapter(AeCnt_Adapter);
			tv_Total.setText(total + "次");
		}

	};

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		public boolean onTouch(View arg0, MotionEvent arg1) {
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView_AeCnt = (HorizontalScrollView) mHead_AeCnt
					.findViewById(R.id.horizontalScrollView_AeCnt);
			headSrcrollView_AeCnt.onTouchEvent(arg1);

			return false;
		}
	}

	@Override
	public void GetAeCntDataUrl(String data) {

		if (data.equals("")) {
			Toast.makeText(this.getActivity(), "没有获取到[效应槽：次数最多]数据，可能无符合条件数据！", Toast.LENGTH_LONG).show();
			tv_Total.setText("0次");
			if (listBean_AeCnt != null) {
				if (listBean_AeCnt.size() > 0) {
					listBean_AeCnt.clear(); // 清除LISTVIEW 以前的内容
					AeCnt_Adapter.onDateChange(listBean_AeCnt);
				}
			}
		} else {
			listBean_AeCnt = new ArrayList<AeRecord>(); // 初始化适配器
			listBean_AeCnt.clear();
			listBean_AeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(data);
			int total = 0;
			for (AeRecord tmp : listBean_AeCnt) {
				total = total + tmp.getWaitTime(); // 统计总数
			}
			AeCnt_Adapter = new HSView_AeCntAdapter(this.getActivity(), R.layout.item_hsview_aecnt, listBean_AeCnt,
					mHead_AeCnt);
			lv_AeCnt.setAdapter(AeCnt_Adapter);			
			tv_Total.setText(total + "次");
		}

	}

}
