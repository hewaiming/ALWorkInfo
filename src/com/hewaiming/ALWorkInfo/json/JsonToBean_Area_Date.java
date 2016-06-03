package com.hewaiming.ALWorkInfo.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hewaiming.ALWorkInfo.bean.PotV;
import com.hewaiming.ALWorkInfo.bean.dayTable;

public class JsonToBean_Area_Date {

	public static List<dayTable> JsonArrayToDayTableBean(String data) {

		ArrayList<dayTable> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<dayTable>();
			System.out.println("jsonarray.DayTable length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);
				dayTable mday = new dayTable();
				mday.setPotNo(jsonobj.getInt("PotNo"));
//				mday.setPotSt(jsonobj.getString("PotST"));
				
				String Pot_status = jsonobj.getString("PotST").toUpperCase();
				switch (Pot_status) {
				case "NORM":
					mday.setPotSt("正常");
					break;
				case "STOP":
					mday.setPotSt("停槽");
					break;
				case "PREHEAT":
					mday.setPotSt("预热");
					break;
				case "START":
					mday.setPotSt("启动");
					break;				
				}
				
				if (Pot_status.equals("STOP")) {
					mday.setAeTime(0);
					mday.setAeV(0);
					mday.setAeCnt(0);
					mday.setDybTime(0);
					mday.setRunTime(0);
					mday.setSetV(0);
					mday.setRealSetV(0);
					mday.setWorkV(0);
				} else {
					mday.setAeTime(jsonobj.getInt("AeTime"));
					mday.setAeV(jsonobj.getDouble("AeV"));
					mday.setAeCnt(jsonobj.getInt("AeCnt"));
					mday.setDybTime(jsonobj.getInt("DybTime"));
					mday.setRunTime(jsonobj.getInt("RunTime"));
					mday.setSetV(jsonobj.getDouble("SetV"));
					mday.setRealSetV(jsonobj.getDouble("RealSetV"));
					mday.setWorkV(jsonobj.getDouble("WorkV"));
				}
				mday.setAverageV(jsonobj.getDouble("AverageV"));

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

	public static List<String> JsonArrayToDate(String data) {

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

}
