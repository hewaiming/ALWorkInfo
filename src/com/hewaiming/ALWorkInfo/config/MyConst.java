package com.hewaiming.ALWorkInfo.config;

import android.os.Environment;

public class MyConst {

	/**
	 * ��ŷ���ͼƬ��Ŀ¼
	 */
	public static String ALWorkInfo_PICTURE_PATH = Environment.getExternalStorageDirectory() + "/ALWorkInfo/image/";

	/**
	 * �ҵ�ͷ�񱣴�Ŀ¼
	 */
	public static String MyAvatarDir = "/sdcard/ALWorkInfo/avatar/";
	/**
	 * ���ջص�
	 */
	public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;// �����޸�ͷ��
	public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;// ��������޸�ͷ��
	public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;// ϵͳ�ü�ͷ��

	public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;// ����
	public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;// ����ͼƬ
	public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;// λ��
	public static final String EXTRA_STRING = "extra_string";
	public static final String[] pic_address = { "http://l-pics.livejournal.com/drugoi/pic/016122t7.jpg",
			"http://a1.att.hudong.com/36/53/300000764046129956537520286_950.jpg",
			"http://www.sxaz.com.cn/images/yejingongcheng.jpg", "http://p0.so.qhimg.com/t0153ac11436adb683f.jpg",
			"http://img2.imgtn.bdimg.com/it/u=3658918502,1121333212&fm=21&gp=0.jpg" };
	

	public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";// ע��ɹ�֮���½ҳ���˳�
}
