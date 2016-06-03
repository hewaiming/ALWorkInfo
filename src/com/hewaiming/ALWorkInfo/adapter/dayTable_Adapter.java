package com.hewaiming.ALWorkInfo.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.hewaiming.ALWorkInfo.bean.dayTable;
import com.hewaiming.allwork.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class dayTable_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<dayTable> mList;

	public dayTable_Adapter(Context mContext, List<dayTable> mList) {
		this.inflater = LayoutInflater.from(mContext);
		this.mList = mList;	
	}

	@Override
	public int getCount() {

		return mList.size();
	}
	
	@Override
	public Object getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		dayTable entity = mList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_daytable, null);
			holder.PotNo_tv = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.PotSt_tv = (TextView) convertView.findViewById(R.id.tv_PotSt);
			holder.RunTime_tv = (TextView) convertView.findViewById(R.id.tv_RunTime);
			
			holder.SetV_tv = (TextView) convertView.findViewById(R.id.tv_SetV);
			holder.RealSetV_tv = (TextView) convertView.findViewById(R.id.tv_RealSetV);
			holder.WorkV_tv = (TextView) convertView.findViewById(R.id.tv_WorkV);
			holder.AverageV_tv = (TextView) convertView.findViewById(R.id.tv_AverageV);
			holder.AeV_tv = (TextView) convertView.findViewById(R.id.tv_AeV);
			holder.AeTime_tv = (TextView) convertView.findViewById(R.id.tv_AeTime);
			holder.AeCnt_tv = (TextView) convertView.findViewById(R.id.tv_AeCnt);
			
			holder.DybTime_tv = (TextView) convertView.findViewById(R.id.tv_DybTime);
			holder.Ddate_tv = (TextView) convertView.findViewById(R.id.tv_Ddate);
		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TextPaint tPaint = holder.PotNo_tv.getPaint();
		tPaint.setFakeBoldText(true);
		holder.PotNo_tv.setText(entity.getPotNo()+"");
		holder.PotSt_tv.setText(entity.getPotSt());
		holder.RunTime_tv.setText(entity.getRunTime()+"");
		
		holder.SetV_tv.setText(entity.getSetV()+"");
		holder.RealSetV_tv.setText(entity.getRealSetV()+"");
		holder.WorkV_tv.setText(entity.getWorkV()+"");
		holder.AverageV_tv.setText(entity.getAverageV()+"");
		holder.AeV_tv.setText(entity.getAeV()+"");
		
		holder.AeTime_tv.setText(entity.getAeTime()+"");
		holder.AeCnt_tv.setText(entity.getAeCnt()+"");
		holder.DybTime_tv.setText(entity.getDybTime()+"");
		holder.Ddate_tv.setText(entity.getDdate());
		
		return convertView;
	}
	
//	PotNo, PotSt,RunTime,SetV,RealSetV,WorkV,AverageV,AeV,AeTime,AeCnt,DybTime,Ddate
	class ViewHolder {
		TextView PotNo_tv;	
		TextView PotSt_tv;
		TextView RunTime_tv;
		TextView SetV_tv;
		TextView RealSetV_tv;
		TextView WorkV_tv;
		TextView AverageV_tv;
		TextView AeV_tv;
		TextView AeTime_tv;
		TextView AeCnt_tv;	
		TextView DybTime_tv;
		TextView Ddate_tv;
	}

	public void onDateChange(List<dayTable> mList) {
		this.mList = mList;
		this.notifyDataSetChanged();

	}

	/*private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}*/
}
