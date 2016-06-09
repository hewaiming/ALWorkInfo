package com.hewaiming.ALWorkInfo.ui;

import java.util.ArrayList;
import java.util.List;

import com.hewaiming.ALWorkInfo.R;
import com.hewaiming.ALWorkInfo.InterFace.HttpGetListener;
import com.hewaiming.ALWorkInfo.adapter.Params_Adapter;
import com.hewaiming.ALWorkInfo.bean.SetParams;
import com.hewaiming.ALWorkInfo.config.MyConst;
import com.hewaiming.ALWorkInfo.json.JsonToBean;
import com.hewaiming.ALWorkInfo.net.HttpPost_area;
import com.hewaiming.ALWorkInfo.view.HeaderListView_Params;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ParamsActivity extends Activity implements HttpGetListener, OnClickListener {
	private Spinner spinner;
	private Button findBtn, backBtn;
	private TextView tv_title;
	private int areaId = 11;
	private ListView lv_params;
	private ArrayAdapter<String> Area_adapter;
	private HttpPost_area http_post;
	private HeaderListView_Params headerView;
	private String url = "http://125.64.59.11:8000/scgy/android/odbcPhP/PotSetValueTable.php";
	private List<SetParams> listBean = null;
	private Params_Adapter mAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_params);
		init();
		init_title();
	}

	private void init_title() {
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_title.setText("常用槽参数");
		backBtn = (Button) findViewById(R.id.btn_back);
		backBtn.setOnClickListener(this);

	}

	private void init() {
		lv_params = (ListView) findViewById(R.id.lv_params);
		spinner = (Spinner) findViewById(R.id.spinner_area);
		Area_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MyConst.Areas);
		// 设置下拉列表的风格
		Area_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter 添加到spinner中
		spinner.setAdapter(Area_adapter);
		// 添加事件Spinner事件监听
		// 设置默认值
		spinner.setVisibility(View.VISIBLE);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0:
					areaId = 11;
					break;
				case 1:
					areaId = 12;
					break;
				case 2:
					areaId = 13;
					break;
				case 3:
					areaId = 21;
					break;
				case 4:
					areaId = 22;
					break;
				case 5:
					areaId = 23;
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				areaId = 11;
			}

		});
		findBtn = (Button) findViewById(R.id.btn_params);
		findBtn.setOnClickListener(this);
	}

	@Override
	public void GetDataUrl(String data) {
		if (data.equals("") || data == null) {
			Toast.makeText(getApplicationContext(), "没有获取到【常用参数】数据！", Toast.LENGTH_LONG).show();
			if (listBean != null) {
				if (listBean.size() > 0) {
					listBean.clear(); // 清除LISTVIEW 以前的内容
					mAdapter.onDateChange(listBean);
				}
			}
		} else {
			if (lv_params.getHeaderViewsCount() > 0) {
				lv_params.removeHeaderView(headerView);
			}
			headerView = new HeaderListView_Params(this);// 添加表头
			headerView.setTvPotNo("槽号");
			headerView.setTvSetV("设定电压");
			headerView.setTvNBTime("NB时间");
			headerView.setTvAETime("AE间隔");
			headerView.setTvALF("氟化铝下料量");
			lv_params.addHeaderView(headerView);

			listBean = new ArrayList<SetParams>();
			listBean = JsonToBean.JsonArrayToSetParamsBean(data);
			/*
			 * List<Map<String, SetParams>> listBeanMap = new
			 * ArrayList<Map<String,SetParams>>(); listBeanMap.clear();;
			 * listBeanMap = JsonToBeanMap.JsonArrayToSetParamsBean(data);
			 * SimpleAdapter sadapter = new SimpleAdapter(this, listBeanMap,
			 * R.layout.item_params, new String[] { "PotNo", "SetV", "NBTime",
			 * "AETime", "ALF" }, new int[] { R.id.tv_PotNo, R.id.tv_SetV,
			 * R.id.tv_NbTime, R.id.tv_AeTime, R.id.tv_ALF });
			 */
			// ArrayAdapter adapter = new ArrayAdapter<SetParams>(this,
			// android.R.layout.simple_list_item_1, listBean);
			// lv_params.setAdapter(adapter);
			// lv_params.setAdapter(sadapter);
			mAdapter = new Params_Adapter(this, listBean);
			lv_params.setAdapter(mAdapter);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_params:
			http_post = (HttpPost_area) new HttpPost_area(url, this, this, Integer.toString(areaId)).execute();
		}
	}

}
