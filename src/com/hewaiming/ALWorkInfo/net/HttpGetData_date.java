package com.hewaiming.ALWorkInfo.net;

import org.json.JSONArray;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetDate_Listener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpGetData_date extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog;
	private Context mContext;
	private String url;
	// �����ӿ�
	private HttpGetDate_Listener listener;

	private HttpPost_JsonArray jsonParser = new HttpPost_JsonArray();

	public HttpGetData_date() {

	}

	public HttpGetData_date(String url, HttpGetDate_Listener listener) {
		super();
		this.url = url;
		this.listener = listener;
	}

	public HttpGetData_date(String url) {
		this.url = url;
	}	

	public HttpGetData_date(String url, HttpGetDate_Listener listener, Context context) {
		this.url = url;
		this.listener = listener;
		this.mContext = context;	
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("��ʼ����������....");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		// List<NameValuePair> mparams = new ArrayList<NameValuePair>(); //
		// Building Parameters
		// mparams.add(new BasicNameValuePair("date","" ));
		JSONArray json = jsonParser.makeHttpRequest(url, "POST");
		
		if(json!=null){
//			Log.d("�������ݣ�json.toString()", json.toString());// full json response
			return json.toString();
		}else{
			Log.i("PHP���������ݷ��������---", "��PHP�����������ݷ��أ�");
			return "";
		}			
		
	}

	@Override
	protected void onPostExecute(String result) {
		pDialog.dismiss();
		listener.GetALLDayUrl(result);
		super.onPostExecute(result);
	}

}
