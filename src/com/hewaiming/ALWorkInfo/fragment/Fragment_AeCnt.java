package com.hewaiming.ALWorkInfo.fragment;

import java.util.ArrayList;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.LoadAeCntInterface;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AeCntAdapter;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Fragment_AeCnt extends Fragment implements LoadAeCntInterface, OnClickListener, OnScrollListener {

	private RelativeLayout mHead_AeCnt;
	private ListView lv_AeCnt;
	private View mView;
	private List<AeRecord> listBean_AeCnt = null;
	private HSView_AeCntAdapter AeCnt_Adapter = null;
	private AeMostActivity mActivity;

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
		mHead_AeCnt = (RelativeLayout) mView.findViewById(R.id.head_AeCnt); // ��ͷ����
		mHead_AeCnt.setFocusable(true);
		mHead_AeCnt.setClickable(true);
		mHead_AeCnt.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead_AeCnt.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_AeCnt = (ListView) mView.findViewById(R.id.lv_AeCnt);
		lv_AeCnt.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_AeCnt.setCacheColorHint(0);
		lv_AeCnt.setOnScrollListener(this);

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
			}
		}
	};

	private void init_adapter(String data) {
		if (data.equals("")) {
			Toast.makeText(this.getActivity(), "û�л�ȡ��[ЧӦ�ۣ��������]���ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();
			if (listBean_AeCnt != null) {
				if (listBean_AeCnt.size() > 0) {
					listBean_AeCnt.clear(); // ���LISTVIEW ��ǰ������
					AeCnt_Adapter.onDateChange(listBean_AeCnt);
				}
			}
		} else {
			listBean_AeCnt = new ArrayList<AeRecord>(); // ��ʼ��ЧӦ����������
			listBean_AeCnt.clear();
			listBean_AeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(data);
			AeCnt_Adapter = new HSView_AeCntAdapter(this.getActivity(), R.layout.item_hsview_aecnt, listBean_AeCnt,
					mHead_AeCnt);
			lv_AeCnt.setAdapter(AeCnt_Adapter);
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
			// ������ͷ �� listView�ؼ���touchʱ�������touch���¼��ַ��� ScrollView
			HorizontalScrollView headSrcrollView_AeCnt = (HorizontalScrollView) mHead_AeCnt
					.findViewById(R.id.horizontalScrollView_AeCnt);
			// HorizontalScrollView headSrcrollView_AeTime =
			// (HorizontalScrollView) mHead_AeTime
			// .findViewById(R.id.horizontalScrollView1);
			headSrcrollView_AeCnt.onTouchEvent(arg1);
			// headSrcrollView_AeTime.onTouchEvent(arg1);

			return false;
		}
	}

	@Override
	public void GetAeCntDataUrl(String data) {
		if (data.equals("")) {
			Toast.makeText(this.getActivity(), "û�л�ȡ��[ЧӦ�ۣ��������]���ݣ������޷����������ݣ�", Toast.LENGTH_LONG).show();
			if (listBean_AeCnt != null) {
				if (listBean_AeCnt.size() > 0) {
					listBean_AeCnt.clear(); // ���LISTVIEW ��ǰ������
					AeCnt_Adapter.onDateChange(listBean_AeCnt);
				}
			}
		} else {
			listBean_AeCnt = new ArrayList<AeRecord>(); // ��ʼ��������
			listBean_AeCnt.clear();
			listBean_AeCnt = JsonToBean_Area_Date.JsonArrayToAeCntBean(data);
			AeCnt_Adapter = new HSView_AeCntAdapter(this.getActivity(), R.layout.item_hsview_aecnt, listBean_AeCnt,
					mHead_AeCnt);
			lv_AeCnt.setAdapter(AeCnt_Adapter);
		}

	}
}
