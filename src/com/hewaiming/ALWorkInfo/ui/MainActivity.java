package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hewaiming.allwork.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity implements OnItemClickListener {

	private GridView gridView;
	private SimpleAdapter adapter;
	private List<Map<String, Object>> dataList;
	private String[] iconName = { "���ò���", "�ձ�", "ЧӦ����", "����", "��ѹ����", "���ϱ�", "������¼", "�޸Ĳ�����¼", 
			"��״̬","��������", "ЧӦ�������", "ЧӦʱ���","�۹������","�����쳣", "������¼" ,"ʵʱ����"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		gridView = (GridView) findViewById(R.id.gridView);
		dataList = new ArrayList<Map<String, Object>>();
		adapter = new SimpleAdapter(this, getData(), R.layout.item_action, new String[] { "pic", "name" },
				new int[] { R.id.pic, R.id.name });
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);

	}

	private List<Map<String, Object>> getData() {

		int[] drawable = { R.drawable.set_params, R.drawable.day_table, R.drawable.ae_table, R.drawable.pot_age,
				R.drawable.potv, R.drawable.fault_list, R.drawable.jx_record, R.drawable.update_params,
				R.drawable.pot_status, R.drawable.measure, R.drawable.ae_nums, R.drawable.ae_long,
				R.drawable.fault_more,R.drawable.nb_abnorm ,R.drawable.alarm_list,R.drawable.real_potv};
		for (int i = 0; i < drawable.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pic", drawable[i]);
			map.put("name", iconName[i]);
			dataList.add(map);
		}
		Log.i("Main", "size=" + dataList.size());
		return dataList;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (position) {
		case 0:
//			Intent phone_intent = new Intent(MainActivity.this, ContactsActivity.class);
//			startActivity(phone_intent);
			break;
		case 1:
			break;
		case 11:
//			Intent youtubeIntent = new Intent(MainActivity.this, YoutubeActivity.class);
//			startActivity(youtubeIntent);
			break;
		case 12:
//			Intent news_intent = new Intent(MainActivity.this, NewsActivity.class);
//			startActivity(news_intent);
			break;
		}

	}
}
