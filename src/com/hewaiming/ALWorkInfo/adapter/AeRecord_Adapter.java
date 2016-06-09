package com.hewaiming.ALWorkInfo.adapter;

import java.util.List;

import com.hewaiming.ALWorkInfo.bean.AeRecord;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AeRecord_Adapter extends BaseAdapter {

	private LayoutInflater inflater;
	private List<AeRecord> mList;

	public AeRecord_Adapter(Context mContext, List<AeRecord> mList) {
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
		AeRecord entity = mList.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.item_ae_record, null);
//			holder.PotNo_tv = (TextView) convertView.findViewById(R.id.tv_PotNo);
//			holder.DDate_tv = (TextView) convertView.findViewById(R.id.tv_DDate);
//			holder.AverageV_tv = (TextView) convertView.findViewById(R.id.tv_AverageV);
//			
//			holder.ContinueTime_tv = (TextView) convertView.findViewById(R.id.tv_ContinueTime);
//			holder.WaitTime_tv = (TextView) convertView.findViewById(R.id.tv_WaitTime);
//			holder.Status_tv = (TextView) convertView.findViewById(R.id.tv_Status);			
//			holder.MaxV_tv = (TextView) convertView.findViewById(R.id.tv_MaxV);			
		
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TextPaint tPaint = holder.PotNo_tv.getPaint();
		tPaint.setFakeBoldText(true);
		holder.PotNo_tv.setText(entity.getPotNo()+"");
		holder.DDate_tv.setText(entity.getDdate());
		holder.AverageV_tv.setText(entity.getAverageV()+"");
		
		holder.ContinueTime_tv.setText(entity.getContinueTime()+"");
		holder.WaitTime_tv.setText(entity.getWaitTime()+"");
		holder.Status_tv.setText(entity.getStatus());
		holder.MaxV_tv.setText(entity.getMaxV()+"");		
		
		return convertView;
	}	

	class ViewHolder {
		TextView PotNo_tv;	
		TextView DDate_tv;
		TextView AverageV_tv;
		TextView ContinueTime_tv;
		TextView WaitTime_tv;		
		TextView Status_tv;
		TextView MaxV_tv;
		
	}

	public void onDateChange(List<AeRecord> mList) {
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
