package com.hewaiming.ALWorkInfo.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.adapter.HScrollView.HSView_AeRecAdapter;
import com.hewaiming.ALWorkInfo.bean.AeRecord;
import com.hewaiming.ALWorkInfo.ui.Ae5DayActivity;
import com.hewaiming.ALWorkInfo.ui.PotVLineActivity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Fragment_Ae2 extends Fragment implements OnScrollListener {
	private View mView;
	private RelativeLayout mHead_Ae;
	private ListView lv_Ae;
	private List<AeRecord> listBean_Ae = null;
	private HSView_AeRecAdapter Ae_Adapter = null;
	private Ae5DayActivity mActivity;
	private List<Map<String, Object>> JXList = new ArrayList<Map<String, Object>>();
	private String ip;
	private int port;
	
	public Fragment_Ae2(List<Map<String, Object>> jXList,String mip,int mport) {
		this.JXList=jXList;
		this.ip=mip;
		this.port=mport;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.fragment_ae1, container, false);
		return mView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (Ae5DayActivity) activity;
		mActivity.setHandler_Ae2(mHandler_Ae);
	}

	public Handler mHandler_Ae = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				init_adapter(msg.obj);
				break;
			}
		}
	};

	private void init_adapter(Object data) {
		if (data.equals("")) {
			Toast.makeText(this.getActivity(), "û�л�ȡ��[ЧӦ�鱨��]ǰһ��AE�������޷����������ݣ�", Toast.LENGTH_LONG).show();
			if (listBean_Ae != null) {
				if (listBean_Ae.size() > 0) {
					listBean_Ae.clear(); // ���LISTVIEW ��ǰ������
					Ae_Adapter.onDateChange(listBean_Ae);
				}
			}
		} else {
			listBean_Ae = new ArrayList<AeRecord>(); // ��ʼ��ЧӦ��¼ ������
			listBean_Ae.clear();
			listBean_Ae = (List<AeRecord>) (data);
			Ae_Adapter = new HSView_AeRecAdapter(this.getActivity(), R.layout.item_hsview_ae_rec, listBean_Ae,
					mHead_Ae);
			lv_Ae.setAdapter(Ae_Adapter);
		}

	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		mHead_Ae = (RelativeLayout) mView.findViewById(R.id.head_Ae1); // ��ͷ����
		mHead_Ae.setFocusable(true);
		mHead_Ae.setClickable(true);
		mHead_Ae.setBackgroundColor(Color.parseColor("#fffffb"));
		mHead_Ae.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		lv_Ae = (ListView) mView.findViewById(R.id.lv_Ae1);
		lv_Ae.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());
		lv_Ae.setCacheColorHint(0);
		lv_Ae.setOnScrollListener(this);
		lv_Ae.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent potv_intent = new Intent( getActivity(), PotVLineActivity.class);
				Bundle potv_bundle = new Bundle();
				potv_bundle.putString("PotNo", String.valueOf(listBean_Ae.get(position).getPotNo()));
				potv_bundle.putString("Begin_Date", listBean_Ae.get(position).getDdate().substring(0, 10));
				potv_bundle.putString("End_Date", listBean_Ae.get(position).getDdate().substring(0, 10));
				potv_bundle.putSerializable("JXList", (Serializable) JXList);
				potv_bundle.putString("ip", ip);
				potv_bundle.putInt("port", port);
				potv_intent.putExtras(potv_bundle);
				startActivity(potv_intent); // ��ѹ����ͼ
				
			}
		});

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
			HorizontalScrollView headSrcrollView_Ae = (HorizontalScrollView) mHead_Ae
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView_Ae.onTouchEvent(arg1);
			return false;
		}
	}
}
