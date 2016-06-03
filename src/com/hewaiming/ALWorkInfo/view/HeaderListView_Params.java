package com.hewaiming.ALWorkInfo.view;

import com.hewaiming.allwork.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HeaderListView_Params extends LinearLayout {
	private Context context;
	private TextView tvPotNo, tvSetV,tvNBTime,tvAETime,tvALF;

	public void setTvPotNo(String tvPotNo) {
		this.tvPotNo.setText(tvPotNo);
	}
	public void setTvSetV(String SetV) {
		this.tvSetV.setText(SetV);
	}
	public void setTvNBTime(String NBTime) {
		this.tvNBTime.setText(NBTime);
	}
	public void setTvAETime(String AETime) {
		this.tvAETime.setText(AETime);
	}
	public void setTvALF(String ALF) {
		this.tvALF.setText(ALF);
	}

	public HeaderListView_Params(Context context) {
		super(context);
		this.context = context;
		View view = LayoutInflater.from(this.context).inflate(R.layout.header_params, null);
		// ���������˳���ܵ�����Ҫ��addView��Ȼ�����ͨ��findViewById�ҵ���TextView
		addView(view);
		tvPotNo=(TextView) view.findViewById(R.id.tv_PotNo_head);
		tvSetV=(TextView) view.findViewById(R.id.tv_SetV_head);
		tvNBTime=(TextView) view.findViewById(R.id.tv_NbTime_head);
		tvAETime=(TextView) view.findViewById(R.id.tv_AeTime_head);
		tvALF=(TextView) view.findViewById(R.id.tv_ALF_head);
		
	}
}
