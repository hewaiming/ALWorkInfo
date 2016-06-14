package com.hewaiming.ALWorkInfo.config;

import com.hewaiming.ALWorkInfo.R;

import android.os.Environment;

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
	
	public static final String[] pic_address = { "http://l-pics.livejournal.com/drugoi/pic/016122t7.jpg",
			"http://a1.att.hudong.com/36/53/300000764046129956537520286_950.jpg",
			"http://www.sxaz.com.cn/images/yejingongcheng.jpg", "http://p0.so.qhimg.com/t0153ac11436adb683f.jpg",
			"http://img2.imgtn.bdimg.com/it/u=3658918502,1121333212&fm=21&gp=0.jpg" };
	
	public static final String[] iconName = { "常用参数", "日报", "效应报表", "槽龄", "槽压曲线", "故障记录", "实时记录", "操作记录", 
			"槽状态","测量数据", "效应槽", "效应记录","槽故障最多","工艺曲线", "报警记录" ,"实时曲线"};
	
	public static final int[] drawable = { R.drawable.set_params, R.drawable.day_table, R.drawable.ae_table, R.drawable.pot_age,
			R.drawable.potv, R.drawable.fault_list, R.drawable.jx_record, R.drawable.update_params,
			R.drawable.pot_status, R.drawable.measure, R.drawable.ae_nums, R.drawable.ae_long,
			R.drawable.fault_more,R.drawable.nb_abnorm ,R.drawable.alarm_list,R.drawable.real_potv};
	
	public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";// 注册成功之后登陆页面退出

	public static final String[] Areas_ALL = { "所有厂房","一厂房","二厂房","一厂房一区", "一厂房二区", "一厂房三区", "二厂房一区", "二厂房二区", "二厂房三区" };;
}
