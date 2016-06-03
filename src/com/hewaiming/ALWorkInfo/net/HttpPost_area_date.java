package com.hewaiming.ALWorkInfo.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.json.JSONArrayParser;

import android.R.string;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class HttpPost_area_date extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog;
	private Context mContext;
	private String url;
	private String areaID;
	private String ddate;
	// 声明接口
	private HttpGetListener listener;
	private JSONArrayParser jsonParser = new JSONArrayParser();

	public HttpPost_area_date() {

	}

	public HttpPost_area_date(String url) {
		this.url = url;
	}

	public HttpPost_area_date(String url, HttpGetListener listener, Context context, String areaId,String ddate) {
		this.url = url;
		this.listener = listener;
		this.mContext = context;
		this.areaID = areaId;
		this.ddate=ddate;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("槽日报，正在数据下载中....");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();

	}

	@Override
	protected String doInBackground(String... params) {
		// Building Parameters
		List<NameValuePair> mparams = new ArrayList<NameValuePair>();
		mparams.add(new BasicNameValuePair("areaID", areaID));
		mparams.add(new BasicNameValuePair("Ddate",ddate)); 

		JSONArray json = jsonParser.makeHttpRequest(url, "POST", mparams);
		// full json response
		Log.d("Login attempt", json.toString());
		return json.toString();
	}

	@Override
	protected void onPostExecute(String result) {
		pDialog.dismiss();
		listener.GetDataUrl(result);
		super.onPostExecute(result);
	}

}
