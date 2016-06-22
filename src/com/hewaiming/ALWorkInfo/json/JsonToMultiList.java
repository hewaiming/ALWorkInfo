package com.hewaiming.ALWorkInfo.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.hewaiming.ALWorkInfo.bean.AeRecord;
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
					mBean.setAlCntZSL(0); // 指示出铝量
				} else {
					mBean.setAlCntZSL(jsonobj.getInt("AlCntZSL"));
				}
				if (jsonobj.get("ZF").equals(null)) {
					mBean.setZF(0); // 噪音
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

	public static List<Map<String, List<AeRecord>>> JsonArrayToAeRecord_5DayBean(String data) {
		List<Map<String, List<AeRecord>>> listBean = null;
		List<AeRecord> list1 = null;
		List<AeRecord> list2 = null;
		List<AeRecord> list3 = null;
		List<AeRecord> list4 = null;
		List<AeRecord> list5 = null;
		try {
			JSONArray jsonarray = new JSONArray(data);

			listBean = new ArrayList<Map<String, List<AeRecord>>>();
			list1 = new ArrayList<AeRecord>();
			list2 = new ArrayList<AeRecord>();
			list3 = new ArrayList<AeRecord>();
			list4 = new ArrayList<AeRecord>();
			list5 = new ArrayList<AeRecord>();
			System.out.println("jsonarray. AE5day_Table---length()---" + jsonarray.length());
			for (int i = 0; i < jsonarray.length(); i++) {

				JSONObject jsonobj = jsonarray.getJSONObject(i);
				AeRecord mBean = new AeRecord();
				if (jsonobj.get("POTNO").equals(null)) {
					mBean.setPotNo(0);
				} else {
					mBean.setPotNo(jsonobj.getInt("POTNO"));
				}

				if (jsonobj.get("DDate").equals(null)) {
					mBean.setDdate("");
				} else {
					mBean.setDdate(jsonobj.getString("DDate"));
				}
				if (jsonobj.get("AverageVoltage").equals(null)) {
					mBean.setAverageV(0);
				} else {
					mBean.setAverageV(jsonobj.getDouble("AverageVoltage"));
				}
				if (jsonobj.get("ContinuanceTime").equals(null)) {
					mBean.setContinueTime(0);
				} else {
					mBean.setContinueTime(jsonobj.getInt("ContinuanceTime"));
				}

				if (jsonobj.get("WaitTime").equals(null)) {
					mBean.setWaitTime(0);
				} else {
					mBean.setWaitTime(jsonobj.getInt("WaitTime"));
				}

				if (jsonobj.get("Status").equals(null)) {
					mBean.setStatus("");
				} else {
					mBean.setStatus(jsonobj.getString("Status"));
				}
				if (jsonobj.get("MaxVoltage").equals(null)) {
					mBean.setMaxV(0);
				} else {
					mBean.setMaxV(jsonobj.getDouble("MaxVoltage"));
				}
				switch (i % 5) {
				case 0:
					list1.add(mBean);
					break;
				case 1:
					list2.add(mBean);
					break;
				case 2:
					list3.add(mBean);
					break;
				case 3:
					list4.add(mBean);
					break;
				case 4:
					list5.add(mBean);
					break;
				}
			}
			Map<String, List<AeRecord>> map1 = new HashMap<String, List<AeRecord>>();
			map1.put("ae1", list1);
			Map<String, List<AeRecord>> map2 = new HashMap<String, List<AeRecord>>();
			map2.put("ae2", list2);
			Map<String, List<AeRecord>> map3 = new HashMap<String, List<AeRecord>>();
			map3.put("ae3", list3);
			Map<String, List<AeRecord>> map4 = new HashMap<String, List<AeRecord>>();
			map4.put("ae4", list4);
			Map<String, List<AeRecord>> map5 = new HashMap<String, List<AeRecord>>();
			map5.put("ae5", list5);
			listBean.add(map1);
			listBean.add(map2);
			listBean.add(map3);
			listBean.add(map4);
			listBean.add(map5);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return listBean;
	}
}
