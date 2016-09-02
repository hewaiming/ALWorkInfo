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
	
	public static final String[] Areas = { "һ����һ��", "һ��������", "һ��������", "������һ��", "����������", "����������" };
	
	public static final String[] pic_address = { ":8000/scgy/android/banner/1.jpg",
			":8000/scgy/android/banner/2.jpg",
			":8000/scgy/android/banner/3.jpg", 
			":8000/scgy/android/banner/4.jpg",
			":8000/scgy/android/banner/5.jpg" };
	
	public static final String[] iconName = { "���ò���", "�ձ�", "ЧӦ����", "����", "��ѹ����", "���ϼ�¼", "ʵʱ��¼", "������¼", 
			"��״̬","��������", "ЧӦ��", "ЧӦ��¼","�۹������","��������", "������¼" ,"ʵʱ����"};
	
	public static final int[] drawable = { R.drawable.params, R.drawable.daytable, R.drawable.aetable, R.drawable.ages,
			R.drawable.potv, R.drawable.faultrecord, R.drawable.realrecord, R.drawable.operaterecord,
			R.drawable.potstatus, R.drawable.measue, R.drawable.aemost, R.drawable.aerecord,
			R.drawable.faultmost,R.drawable.gongyi ,R.drawable.alertrecord,R.drawable.realtime};
	
	public static final String ACTION_REGISTER_SUCCESS_FINISH = "register.success.finish";// ע��ɹ�֮���½ҳ���˳�

	public static final String[] Areas_ALL = { "���г���","һ����","������","һ����һ��", "һ��������", "һ��������", "������һ��", "����������", "����������" };;
	
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
						Toast.makeText(mContext.getApplicationContext(), "���桾����������ʾ������ʧ��"+showName, 1).show();
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
