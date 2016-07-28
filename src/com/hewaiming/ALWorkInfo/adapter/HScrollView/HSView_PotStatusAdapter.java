package com.hewaiming.ALWorkInfo.adapter.HScrollView;

import java.util.ArrayList;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView;
import com.hewaiming.ALWorkInfo.HScrollListView.MyHScrollView.OnScrollChangedListener;
import com.hewaiming.ALWorkInfo.bean.dayTable;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import bean.PotStatus;

public class HSView_PotStatusAdapter extends BaseAdapter {
	private static final String TAG = "HolderAdapter";

	private List<PotStatus> mList;
	/**
	 * ListView头部
	 */
	private RelativeLayout mHead;
	/**
	 * layout ID
	 */
	private int id_row_layout;
	private LayoutInflater mInflater;
    //e0f1f2  b3e5fc
	int[] colors = { Color.rgb(224, 241, 242), Color.rgb(179, 213, 252) };
	// int[] colors = { Color.BLACK, Color.BLACK };

	public HSView_PotStatusAdapter(Context context, int id_row_layout, List<PotStatus> currentData,
			RelativeLayout mHead) {
		Log.v(TAG + ".HolderAdapter", " 初始化");

		this.id_row_layout = id_row_layout;
		this.mInflater = LayoutInflater.from(context);
		this.mList = currentData;
		this.mHead = mHead;

	}

	public int getCount() {
		return this.mList.size();
	}

	public Object getItem(int position) {

		return mList.get(position);
	}

	public long getItemId(int position) {

		return position;
	}

	/**
	 * 向List中添加数据
	 * 
	 * @param items
	 */
	public void addItem(List<PotStatus> items) {
		for (PotStatus item : items) {
			mList.add(item);
		}
	}

	/**
	 * 清空当List中的数据
	 */
	public void cleanAll() {
		this.mList.clear();
	}

	public View getView(int position, View convertView, ViewGroup parentView) {
		ViewHolder holder = null;
		PotStatus entity = mList.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(id_row_layout, null);
			holder = new ViewHolder();
			MyHScrollView scrollView1 = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView1);

			holder.scrollView = scrollView1;
			holder.tvPotNo = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.tvPotSt = (TextView) convertView.findViewById(R.id.tv_PotSt);
			holder.tvAutoRun = (TextView) convertView.findViewById(R.id.tv_AutoRun);
			holder.tvOperation = (TextView) convertView.findViewById(R.id.tv_Operation);
			holder.tvFaultNo = (TextView) convertView.findViewById(R.id.tv_FaultNo);
			holder.tvSetV = (TextView) convertView.findViewById(R.id.tv_SetV);
			holder.tvWorkV = (TextView) convertView.findViewById(R.id.tv_WorkV);					
			
			holder.tvSetNb = (TextView) convertView.findViewById(R.id.tv_SetNb);
			holder.tvWorkNb = (TextView) convertView.findViewById(R.id.tv_WorkNb);
			holder.tvNbTime = (TextView) convertView.findViewById(R.id.tv_NBTime);
			holder.tvAeSpan = (TextView) convertView.findViewById(R.id.tv_AeSpan);		
			holder.tvYJWZ = (TextView) convertView.findViewById(R.id.tv_YJWZ);			

			MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));

			convertView.setTag(holder);
			// 隔行变色
			//convertView.setBackgroundColor(colors[position % 2]);
			// mHolderList.add(holder);
		} else {
			// 隔行变色
			//convertView.setBackgroundColor(colors[position % 2]);
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvPotNo.setText(entity.getPotNo()+"");
		holder.tvPotSt.setText(entity.getStatus());
		if(entity.isAutoRun()){
			holder.tvAutoRun.setText("");
		}else{
			holder.tvAutoRun.setText("手动");
		}		
		holder.tvOperation.setText(entity.getOperation());
		holder.tvSetV.setText(entity.getSetV()+"");	
		holder.tvWorkV.setText(entity.getWorkV()+"");
		holder.tvSetNb.setText(entity.getSetNb()+"");
		holder.tvWorkNb.setText(entity.getWorkNb()+"");
		if(entity.getNbTime()==null){
			holder.tvNbTime.setText("");
		}else{
			holder.tvNbTime.setText(entity.getNbTime().toString());
		}
		
		holder.tvAeSpan.setText(entity.getAeSpan()+"");
		holder.tvFaultNo.setText(entity.getFaultNo()+"");
		holder.tvYJWZ.setText(entity.getYJWJ()+"");		

		return convertView;
	}

	class OnScrollChangedListenerImp implements OnScrollChangedListener {
		MyHScrollView mScrollViewArg;

		public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
			mScrollViewArg = scrollViewar;
		}

		public void onScrollChanged(int l, int t, int oldl, int oldt) {
			mScrollViewArg.smoothScrollTo(l, t);
		}
	};

	class ViewHolder {
		TextView tvPotNo;
		TextView tvPotSt;
		TextView tvAutoRun;
		TextView tvOperation;
		TextView tvSetV;	
		TextView tvWorkV;
		TextView tvSetNb;
		TextView tvWorkNb;
		TextView tvNbTime;
		TextView tvAeSpan;
		TextView tvFaultNo;
		TextView tvYJWZ;  //阳极位置
	
		HorizontalScrollView scrollView;
	}

	public void onDateChange(List<PotStatus> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

}
