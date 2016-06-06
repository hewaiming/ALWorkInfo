package com.hewaiming.ALWorkInfo.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.bean.SetParams;

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

public class Params_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<SetParams> mList;
//	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
//	DisplayImageOptions options;

	public Params_Adapter(Context mContext, List<SetParams> mList) {
		this.inflater = LayoutInflater.from(mContext);
		this.mList = mList;
//		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.nopic).showImageOnLoading(R.drawable.loading)
//				.showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading(true).cacheOnDisc(true)
//				.cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565)
//				.considerExifParams(true).displayer(new FadeInBitmapDisplayer(300)).build();		
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
		SetParams entity = mList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_params, null);
			holder.PotNo_tv = (TextView) convertView.findViewById(R.id.tv_PotNo);
			holder.SetV_tv = (TextView) convertView.findViewById(R.id.tv_SetV);
			holder.NBTime_tv = (TextView) convertView.findViewById(R.id.tv_NbTime);
			holder.AETime_tv = (TextView) convertView.findViewById(R.id.tv_AeTime);
			holder.ALF_tv = (TextView) convertView.findViewById(R.id.tv_ALF);
		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TextPaint tPaint = holder.PotNo_tv.getPaint();
		tPaint.setFakeBoldText(true);
		holder.PotNo_tv.setText(entity.getPotNo()+"");
		holder.SetV_tv.setText(entity.getSetV()+"");
		holder.NBTime_tv.setText(entity.getNBTime()+"");
		holder.AETime_tv.setText(entity.getAETime()+"");
		holder.ALF_tv.setText(entity.getALF()+"");
		return convertView;
	}

	class ViewHolder {
		TextView PotNo_tv;		
		TextView SetV_tv;
		TextView NBTime_tv;
		TextView AETime_tv;
		TextView ALF_tv;		
	}

	public void onDateChange(List<SetParams> mList) {
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
