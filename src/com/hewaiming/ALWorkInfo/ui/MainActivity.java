package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hewaiming.ALWorkInfo.config.MyConst;
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
		
		for (int i = 0; i < MyConst.drawable.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("pic", MyConst.drawable[i]);
			map.put("name", MyConst.iconName[i]);
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
			 Intent Paramsintent = new Intent(MainActivity.this,
			 ParamsActivity.class);
			 startActivity(Paramsintent);
			break;
		case 1:
			Intent DayTable_intent = new Intent(MainActivity.this,
					 DayTableActivity.class);
					 startActivity(DayTable_intent);
			break;
		case 11:
			// Intent youtubeIntent = new Intent(MainActivity.this,
			// YoutubeActivity.class);
			// startActivity(youtubeIntent);
			break;
		case 3:
			 Intent Potage_intent = new Intent(MainActivity.this,
			 PotAgeActivity.class);
			 startActivity(Potage_intent);
			break;
		}

	}
}
