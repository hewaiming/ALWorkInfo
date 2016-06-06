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

public class HttpPost_BeginDate_EndDate_Area extends AsyncTask<String, Void, String> {
	private ProgressDialog pDialog;
	private Context mContext;
	private String url;
	private String Area;
	private String BeginDate, EndDate;
	// 声明接口
	private HttpGetListener listener;
	private JSONArrayParser jsonParser = new JSONArrayParser();

	public HttpPost_BeginDate_EndDate_Area() {

	}

	public HttpPost_BeginDate_EndDate_Area(String url, String area, String beginDate, String endDate,
			HttpGetListener listener, Context mContext) {

		this.mContext = mContext;
		this.url = url;
		this.Area = area;

		this.BeginDate = beginDate;
		this.EndDate = endDate;
		this.listener = listener;
	}

	public HttpPost_BeginDate_EndDate_Area(String url) {
		this.url = url;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(mContext);
		pDialog.setMessage("数据下载....");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();

	}

	@Override
	protected String doInBackground(String... params) {
		// Building Parameters
		List<NameValuePair> mparams = new ArrayList<NameValuePair>();
		mparams.clear();

		mparams.add(new BasicNameValuePair("areaID", Area)); // 全部槽号
		mparams.add(new BasicNameValuePair("areaBeginDate", BeginDate));
		mparams.add(new BasicNameValuePair("areaEndDate", EndDate));

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
