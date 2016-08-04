package com.hewaiming.ALWorkInfo.net;

import org.json.JSONArray;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetJXRecord_Listener;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpGetData_JXRecord extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog;
	private Context mContext;
	private String url;

	// �����ӿ�
	private HttpGetJXRecord_Listener listener;

	private HttpPost_JsonArray jsonParser = new HttpPost_JsonArray();

	public HttpGetData_JXRecord() {

	}

	public HttpGetData_JXRecord(String url, HttpGetJXRecord_Listener listener) {
		super();
		this.url = url;
		this.listener = listener;
	}

	public HttpGetData_JXRecord(String url) {
		this.url = url;
	}	

	public HttpGetData_JXRecord(String url, HttpGetJXRecord_Listener listener, Context context) {
		this.url = url;
		this.listener = listener;
		this.mContext = context;		

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();

		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("��ʼ��������¼������....");
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
//			Log.d("������¼��json.toString()", json.toString());// full json response
			return json.toString();
		}else{
			Log.i("PHP���������ݷ��������---", "��PHP�����������ݷ��أ�");
			return "";
		}			
		
	}

	@Override
	protected void onPostExecute(String result) {
		pDialog.dismiss();
		listener.GetJXRecordUrl(result);
		super.onPostExecute(result);
	}

}
