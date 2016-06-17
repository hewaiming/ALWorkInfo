package com.hewaiming.ALWorkInfo.net;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.hewaiming.ALWorkInfo.InterFace.LoadAeTimeInterface;
import com.hewaiming.ALWorkInfo.json.JSONArrayParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpPost_BeginDate_EndDate_other_Latch extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog;
	private Context mContext;
	private String url;
	private String Area_PotNo;
	private int type;
	private String BeginDate, EndDate;
	// �����ӿ�
	private LoadAeTimeInterface listener;
	private JSONArrayParser jsonParser = new JSONArrayParser();
	CountDownLatch latch;

	public HttpPost_BeginDate_EndDate_other_Latch() {

	}

	public HttpPost_BeginDate_EndDate_other_Latch(CountDownLatch mlatch,String url, int type, String area, String beginDate, String endDate,
			LoadAeTimeInterface listener, Context mContext) {

		this.mContext = mContext;
		this.url = url;
		this.Area_PotNo = area;
		this.type = type;
		this.BeginDate = beginDate;
		this.EndDate = endDate;
		this.listener = listener;
		this.latch=mlatch;
	}

	public HttpPost_BeginDate_EndDate_other_Latch(String url) {
		this.url = url;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("��������....");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();

	}

	@Override
	protected String doInBackground(String... params) {
		// Building Parameters
		List<NameValuePair> mparams = new ArrayList<NameValuePair>();
		mparams.clear();
		if (type == 1) {
			mparams.add(new BasicNameValuePair("areaID", Area_PotNo)); // ȫ���ۺ�
			mparams.add(new BasicNameValuePair("BeginDate", BeginDate));
			mparams.add(new BasicNameValuePair("EndDate", EndDate));
		} else if (type == 2) {
			mparams.add(new BasicNameValuePair("PotNo", Area_PotNo)); // �ۺ�
			mparams.add(new BasicNameValuePair("BeginDate", BeginDate));
			mparams.add(new BasicNameValuePair("EndDate", EndDate));

		}
        
		JSONArray json = jsonParser.makeHttpRequest(url, "POST", mparams);
		if (json != null) {
			Log.d("json_area_potno", json.toString());// �ӷ���������������
			return json.toString();
		} else {
			Log.i("PHP���������ݷ��������---", "��PHP�����������ݷ��أ�");
			return "";
		}

	}

	@Override
	protected void onPostExecute(String result) {
		pDialog.dismiss();
		listener.GetAeTimeDataUrl(result);
		latch.countDown();
		System.out.println("Measuetable Latch finish!");
		super.onPostExecute(result);
		
	}

}
