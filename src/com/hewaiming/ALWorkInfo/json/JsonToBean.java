package com.hewaiming.ALWorkInfo.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hewaiming.ALWorkInfo.bean.PotV;
import com.hewaiming.ALWorkInfo.bean.SetParams;
import com.hewaiming.ALWorkInfo.bean.dayTable;

public class JsonToBean {

	public List<JSONObject> JsonArrayToList(String data) {

		ArrayList<JSONObject> lists = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			lists = new ArrayList<JSONObject>();
			System.out.println("jsonarray.length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				lists.add(jsonobj);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return lists;
	}

	public List<dayTable> JsonArrayToDayTableBean(String data) {

		ArrayList<dayTable> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<dayTable>();
			System.out.println("jsonarray.length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				dayTable mday = new dayTable();
				mday.setPotNo(jsonobj.getInt("PotNo"));
				mday.setPotSt(jsonobj.getString("PotST"));
				if ((jsonobj.getString("PotST").toUpperCase()).equals("STOP")) {
					mday.setAeTime(0);
					mday.setAeV(0);
					mday.setSetV(0);
					mday.setRealSetV(0);
				} else {
					mday.setAeTime(jsonobj.getInt("AeTime"));
					mday.setAeV(jsonobj.getDouble("AeV"));
					mday.setSetV(jsonobj.getDouble("SetV"));
					mday.setRealSetV(jsonobj.getDouble("RealSetV"));
				}
				String mdata = jsonobj.getString("Ddate");
				int location = mdata.indexOf(" ");
				mday.setDdate(mdata.substring(0, location));
				listBean.add(mday);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return listBean;
	}

	public List<String> JsonArrayToDate(String data) {

		ArrayList<String> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<String>();
			System.out.println("jsonarray.length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				String mdata = jsonobj.getString("Ddate");
				int location = mdata.indexOf(" ");
				listBean.add(mdata.substring(0, location));
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return listBean;
	}

	public List<PotV> JsonArrayToPotVBean(String data) {
		ArrayList<PotV> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<PotV>();
			System.out.println("jsonarray.length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				PotV mPotV = new PotV();
				mPotV.setDdate(jsonobj.getString("DDate"));
				mPotV.setCur(jsonobj.getInt("Cur"));
				mPotV.setPotV(jsonobj.getInt("PotNoV"));
				listBean.add(mPotV);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return listBean;
	}
	
	public static List<SetParams> JsonArrayToSetParamsBean(String data) {
		ArrayList<SetParams> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<SetParams>();
			System.out.println("jsonarray SetParams.length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				SetParams mSetParams = new SetParams();
				mSetParams.setPotNo(jsonobj.getInt("PotNo"));
				mSetParams.setNBTime(jsonobj.getInt("NBTime"));
				mSetParams.setAETime(jsonobj.getInt("AETime")/60);
				mSetParams.setALF(jsonobj.getInt("ALF"));
				mSetParams.setSetV(jsonobj.getInt("SetV")/1000.0);
				listBean.add(mSetParams);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listBean;
	}
}