package com.hewaiming.ALWorkInfo.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetDetector {

	private Context _context;
	int IsNet = 0;

	public NetDetector(Context context) {
		this._context = context;
	}

	public int isConnectingToInternet() {

		ConnectivityManager connectivity = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if ((info[i].getState() == NetworkInfo.State.CONNECTED)
							&& (info[i].getType() == ConnectivityManager.TYPE_WIFI)) {
						Toast.makeText(_context, "��ǰ����:WIFI", Toast.LENGTH_LONG).show();
						IsNet = 1;
						break;
					}
					if ((info[i].getState() == NetworkInfo.State.CONNECTED)
							&& (info[i].getType() == ConnectivityManager.TYPE_MOBILE)) {
						Toast.makeText(_context, "��ǰ����:3G�����4G���磬��ע�����г������ܻ�����ϴ�������", Toast.LENGTH_LONG).show();
						IsNet = 2;
						break;
					}
				}
			}

		}
		return IsNet;
	}
}