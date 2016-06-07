package com.hewaiming.ALWorkInfo.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hewaiming.ALWorkInfo.bean.FaultRecord;
import com.hewaiming.ALWorkInfo.bean.OperateRecord;
import com.hewaiming.ALWorkInfo.bean.PotV;
import com.hewaiming.ALWorkInfo.bean.RealRecord;
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
				// mday.setPotSt(jsonobj.getString("PotST"));

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

	public static List<Map<String, Object>> JsonArrayToJXRecord(String data) {

		List<Map<String, Object>> RXList = new ArrayList<Map<String, Object>>();
		try {
			JSONArray jsonarray = new JSONArray(data);

			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject jsonobj = jsonarray.getJSONObject(i);

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", jsonobj.getString("RecordNo"));
				map.put("jx_name", jsonobj.getString("Name1"));
				RXList.add(map);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return RXList;
	}

	public static List<FaultRecord> JsonArrayToFaultRecordBean(String data, List<Map<String, Object>> JXList) {
		List<FaultRecord> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<FaultRecord>();
			listBean.clear();
			System.out.println("jsonarray.FaultRecord---length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				FaultRecord mFault = new FaultRecord();
				mFault.setPotNo(jsonobj.getInt("PotNo"));
				mFault.setRecTime(jsonobj.getString("DDate"));
				
				int recNo = jsonobj.getInt("RecordNo");
				recNo=recNo-1;
				Map<String, Object> mMap = JXList.get(recNo);
				System.out.println("jx_name"+mMap.get("jx_name").toString());
				mFault.setRecordNo(mMap.get("jx_name").toString());
				listBean.add(mFault);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return listBean;
	}

	public static List<RealRecord> JsonArrayToRealRecordBean(String data, List<Map<String, Object>> JXList) {
	
		List<RealRecord> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<RealRecord>();
			listBean.clear();
			System.out.println("jsonarray.RealRecord---length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				RealRecord mReal = new RealRecord();
				mReal.setPotNo(jsonobj.getInt("PotNo"));
				mReal.setRecTime(jsonobj.getString("DDate"));
				
				int recNo = jsonobj.getInt("RecordNo");
				recNo=recNo-1;
				Map<String, Object> mMap = JXList.get(recNo);
				System.out.println("jx_name:"+mMap.get("jx_name").toString());
				mReal.setRecordNo(mMap.get("jx_name").toString());					
				
				if (jsonobj.get("Val2").equals(null)){
					mReal.setParam1("");
				}else{
					mReal.setParam1(jsonobj.getInt("Val2")+"");
				}
				if (jsonobj.get("Val3").equals(null)){
					mReal.setParam2("");
				}else{
					mReal.setParam2(jsonobj.getInt("Val3")+"");
				}			
				
				listBean.add(mReal);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return listBean;
	}

	
	public static List<OperateRecord> JsonArrayToOperateRecordBean(String data) {
		
		List<OperateRecord> listBean = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<OperateRecord>();
			listBean.clear();
			System.out.println("jsonarray.RealRecord---length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				
				OperateRecord mOp = new OperateRecord();
				mOp.setObjectName(jsonobj.getString("ObjectName"));
				mOp.setParamNameCH(jsonobj.getString("ParaNameCH"));
				mOp.setDescription(jsonobj.getString("Description"));
				mOp.setUserName(jsonobj.getString("UserName"));
				mOp.setRecTime(jsonobj.getString("DDate"));			
									
				/*
				if (jsonobj.get("Val2").equals(null)){
					mReal.setParam1("");
				}else{
					mReal.setParam1(jsonobj.getInt("Val2")+"");
				}
				if (jsonobj.get("Val3").equals(null)){
					mReal.setParam2("");
				}else{
					mReal.setParam2(jsonobj.getInt("Val3")+"");
				}			*/
				
				listBean.add(mOp);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}
		return listBean;
	}

}
