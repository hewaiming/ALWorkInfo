package com.hewaiming.ALWorkInfo.json;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hewaiming.ALWorkInfo.bean.dayTable;

public class JsonToMultiList {		
	
	public static List<dayTable> JsonArrayToDayTableBean(String data) {

		List<dayTable> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<dayTable>();
			listBean.clear();
			System.out.println("jsonarray. DayTable---length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				dayTable mBean = new dayTable();

				if (jsonobj.get("PotNo").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("PotNo"));
				}		

				if (jsonobj.get("SetNB").equals(null)) {
					mBean.setSetNB(0);
				} else {
					mBean.setSetNB(jsonobj.getInt("SetNB"));
				}				
				
				if (jsonobj.get("SetV").equals(null)) {
					mBean.setSetV(0);
				} else {
					mBean.setSetV(jsonobj.getDouble("SetV"));
				}		
				if (jsonobj.get("AverageV").equals(null)) {
					mBean.setAverageV(0);
				} else {
					mBean.setAverageV(jsonobj.getDouble("AverageV"));
				}

				if (jsonobj.get("WorkV").equals(null)) {
					mBean.setWorkV(0);
				} else {
					mBean.setWorkV(jsonobj.getDouble("WorkV"));
				}
				if (jsonobj.get("YhlCnt").equals(null)) {
					mBean.setYhlCnt(0);
				} else {
					mBean.setYhlCnt(jsonobj.getInt("YhlCnt"));
				}		
				if (jsonobj.get("FhlCnt").equals(null)) {
					mBean.setFhlCnt(0);
				} else {
					mBean.setFhlCnt(jsonobj.getInt("FhlCnt"));
				}	
				
				if (jsonobj.get("DybTime").equals(null)) {
					mBean.setDybTime(0);
				} else {
					mBean.setDybTime(jsonobj.getInt("DybTime"));
				}
				if (jsonobj.get("AeCnt").equals(null)) {
					mBean.setAeCnt(0);
				} else {
					mBean.setAeCnt(jsonobj.getInt("AeCnt"));
				}			
				if (jsonobj.get("AlCntZSL").equals(null)) {
					mBean.setAlCntZSL(0); //指示出铝量
				} else {
					mBean.setAlCntZSL(jsonobj.getInt("AlCntZSL"));
				}	
				if (jsonobj.get("ZF").equals(null)) {
					mBean.setZF(0);    //噪音
				} else {
					mBean.setZF(jsonobj.getInt("ZF"));
				}
				if (jsonobj.get("Ddate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("Ddate"));
				}
				listBean.add(mBean);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return listBean;
	}	

}
