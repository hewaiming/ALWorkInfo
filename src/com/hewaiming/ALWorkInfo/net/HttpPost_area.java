package com.hewaiming.ALWorkInfo.net;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.json.JSONArrayParser;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpPost_area extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog;
	private Context mContext;
	private String url;
	private String areaID;
	
	// �����ӿ�
	private HttpGetListener listener;
	private JSONArrayParser jsonParser = new JSONArrayParser();

	public HttpPost_area() {

	}

	public HttpPost_area(String url) {
		this.url = url;
	}

	public HttpPost_area(String url, HttpGetListener listener, Context context, String areaId) {
		this.url = url;
		this.listener = listener;
		this.mContext = context;
		this.areaID = areaId;
	
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("������....");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();

	}

	@Override
	protected String doInBackground(String... params) {
		// Building Parameters
		List<NameValuePair> mparams = new ArrayList<NameValuePair>();
		mparams.add(new BasicNameValuePair("areaID", areaID));	

		JSONArray json = jsonParser.makeHttpRequest(url, "POST", mparams);		
		Log.d("Login attempt", json.toString());// full json response

		return json.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		pDialog.dismiss();
		listener.GetDataUrl(result);
		super.onPostExecute(result);
	}

}
