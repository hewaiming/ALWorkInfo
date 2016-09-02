package com.hewaiming.ALWorkInfo.config;

import com.hewaiming.ALWorkInfo.R;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class MyConst {

	/**
	 * 存放发送图片的目录
	 */
	public static String ALWorkInfo_PICTURE_PATH = Environment.getExternalStorageDirectory() + "/ALWorkInfo/image/";

	/**
	 * 我的头像保存目录
	 */
	public static String MyAvatarDir = "/sdcard/ALWorkInfo/avatar/";
	/**
	 * 拍照回调
	 */
	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;// 拍照修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;// 本地相册修改头像
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;// 系统裁剪头像

	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;// 拍照
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;// 本地图片
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;// 位置
	public static final String EXTRA_STRING = "extra_string";
	
	public static final String[] Areas = { "一厂房一区", "一厂房二区", "一厂房三区", "二厂房一区", "二厂房二区", "二厂房三区" };
	
	public static final String[] pic_address = { ":8000/scgy/android/banner/1.jpg",
			":8000/scgy/android/banner/2.jpg",
			":8000/scgy/android/banner/3.jpg", 
			":8000/scgy/android/banner/4.jpg",
			":8000/scgy/android/banner/5.jpg" };
	
	public static final String[] iconName = { "常用参数", "日报", "效应报表", "槽龄", "槽压曲线", "故障记录", "实时记录", "操作记录", 
			"槽状态","测量数据", "效应槽", "效应记录","槽故障最多","工艺曲线", "报警记录" ,"实时曲线"};
	
	public static final int[] drawable = { R.drawable.params, R.drawable.daytable, R.drawable.aetable, R.drawable.ages,
			R.drawable.potv, R.drawable.faultrecord, R.drawable.realrecord, R.drawable.operaterecord,
			R.drawable.potstatus, R.drawable.measue, R.drawable.aemost, R.drawable.aerecord,
			R.drawable.faultmost,R.drawable.gongyi ,R.drawable.alertrecord,R.drawable.realtime};
	
	public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";// 注册成功之后登陆页面退出

	public static final String[] Areas_ALL = { "所有厂房","一厂房","二厂房","一厂房一区", "一厂房二区", "一厂房三区", "二厂房一区", "二厂房二区", "二厂房三区" };;
	
	public static void GuideDialog_show(final Context mContext,final String showName) {
		final Dialog dialog = new Dialog(mContext, R.style.Dialog_Fullscreen);
		dialog.setContentView(R.layout.activity_guide_dialog);
		ImageView iv = (ImageView) dialog.findViewById(R.id.ivNavigater_click);
		iv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				SharedPreferences sp;
				sp = mContext.getSharedPreferences("GuideDiaLogIsShow", mContext.MODE_PRIVATE);
				if (sp!=null){
					Editor editor = sp.edit();
					editor.putBoolean(showName, true);				
					if (!editor.commit()){
						Toast.makeText(mContext.getApplicationContext(), "保存【引导界面显示】参数失败"+showName, 1).show();
					}
				}
			}
		});
		dialog.show();
	}
	
	public static boolean GetDataFromSharePre(Context mContext,String showName) {		
		SharedPreferences sp;
		sp = mContext.getSharedPreferences("GuideDiaLogIsShow", mContext.MODE_PRIVATE);
		if(sp!=null){
			 return sp.getBoolean(showName, false);			
		}	
		return false;
	}

}
