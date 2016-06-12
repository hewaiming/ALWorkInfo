package com.hewaiming.ALWorkInfo.fragment;

import com.hewaiming.ALWorkInfo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.TextView;

public class Fragment_AeCnt extends Fragment implements OnClickListener {

//	private TextView tv_text;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_aecnt, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		tv_text=(TextView)getActivity().findViewById(R.id.tv_text);
//		tv_text.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.tv_text:
//			tv_text.setText("¿¨¿¨¿¨");
//			break;
//
//		default:
//			break;
//		}
		
	}
}
