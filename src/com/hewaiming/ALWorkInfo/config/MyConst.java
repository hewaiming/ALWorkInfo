package com.hewaiming.ALWorkInfo.config;

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
	public static final String[] pic_address = { "http://l-pics.livejournal.com/drugoi/pic/016122t7.jpg",
			"http://a1.att.hudong.com/36/53/300000764046129956537520286_950.jpg",
			"http://www.sxaz.com.cn/images/yejingongcheng.jpg", "http://p0.so.qhimg.com/t0153ac11436adb683f.jpg",
			"http://img2.imgtn.bdimg.com/it/u=3658918502,1121333212&fm=21&gp=0.jpg" };
	

	public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";// 注册成功之后登陆页面退出
}
