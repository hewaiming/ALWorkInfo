package com.hewaiming.ALWorkInfo.fragment;

import java.util.ArrayList;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AeTimeAdapter;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.json.JsonToBean_Area_Date;
import com.hewaiming.ALWorkInfo.ui.AeMostActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Fragment_AeTime extends Fragment implements OnScrollListener {
	private View mView;
	private RelativeLayout mHead_AeTime;
	private ListView lv_AeTime;
	private List<AeRecord> listBean_AeTime = null;
	private HSView_AeTimeAdapter AeTime_Adapter = null;
	private AeMostActivity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_aetime, container, false);
		return mView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (AeMostActivity) activity;
		mActivity.setHandler_AeTime(mHandler_AeTime);
	}

	public Handler mHandler_AeTime = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				init_adapter(msg.obj.toString());
				break;
			}
		}
	};

	private void init_adapter(String data) {
		if (data.equals("")) {
			Toast.makeText(this.getActivity(), "û�л�ȡ��[ЧӦ�ۣ�����ʱ���]���ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();
			if (listBean_AeTime != null) {
				if (listBean_AeTime.size() > 0) {
					listBean_AeTime.clear(); // ���LISTVIEW ��ǰ������
					AeTime_Adapter.onDateChange(listBean_AeTime);
				}
			}
		} else {
			listBean_AeTime = new ArrayList<AeRecord>(); // ��ʼ��ЧӦʱ�䳤 ������
			listBean_AeTime.clear();
			listBean_AeTime = JsonToBean_Area_Date.JsonArrayToAeTimeBean(data);
			AeTime_Adapter = new HSView_AeTimeAdapter(this.getActivity(), R.layout.item_hsview_aetime, listBean_AeTime,
					mHead_AeTime);
			lv_AeTime.setAdapter(AeTime_Adapter);
		}

	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		mHead_AeTime = (RelativeLayout) mView.findViewById(R.id.head_AeTime); // ��ͷ����
		mHead_AeTime.setFocusable(true);
		mHead_AeTime.setClickable(true);
		mHead_AeTime.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead_AeTime.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_AeTime = (ListView) mView.findViewById(R.id.lv_AeTime);
		lv_AeTime.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_AeTime.setCacheColorHint(0);
		lv_AeTime.setOnScrollListener(this);

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
			// ������ͷ �� listView�ؼ���touchʱ�������touch���¼��ַ��� ScrollView
			HorizontalScrollView headSrcrollView_AeTime = (HorizontalScrollView) mHead_AeTime
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView_AeTime.onTouchEvent(arg1);
			return false;
		}
	}

	/*
	 * @Override public void GetAeTimeDataUrl(String data) { if
	 * (data.equals("")) { Toast.makeText(this.getActivity(),
	 * "û�л�ȡ��[ЧӦ��]���ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show(); if (listBean_AeTime
	 * != null) { if (listBean_AeTime.size() > 0) { listBean_AeTime.clear(); //
	 * ���LISTVIEW ��ǰ������ AeTime_Adapter.onDateChange(listBean_AeTime); } } } else
	 * { listBean_AeTime = new ArrayList<AeRecord>(); listBean_AeTime.clear();
	 * listBean_AeTime = JsonToBean_Area_Date.JsonArrayToAeRecordBean(data);
	 * AeTime_Adapter = new HSView_AeTimeAdapter(this.getActivity(),
	 * R.layout.item_hsview_ae_rec, listBean_AeTime, mHead_AeTime);
	 * lv_AeTime.setAdapter(AeTime_Adapter); }
	 * 
	 * }
	 */
}