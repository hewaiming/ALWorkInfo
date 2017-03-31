/*
 * 官网地站:http://www.mob.com
 * �?术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第�?时间通过微信将版本更新内容推送给您�?�如果使用过程中有任何问题，也可以�?�过微信与我们取得联系，我们将会�?24小时内给予回复）
 *
 * Copyright (c) 2013�? mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView.ScaleType;
import cn.sharesdk.onekeyshare.OnekeySharePage;
import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;

import com.mob.tools.gui.ScaledImageView;

/** 图片浏览的视图类 */
public class PicViewerPage extends OnekeySharePage implements OnGlobalLayoutListener {
	private Bitmap pic;
	/** 图片浏览的缩放控�? */
	private ScaledImageView sivViewer;

	public PicViewerPage(OnekeyShareThemeImpl impl) {
		super(impl);
	}

	/** 设置图片用于浏览 */
	public void setImageBitmap(Bitmap pic) {
		this.pic = pic;
	}

	public void onCreate() {
		activity.getWindow().setBackgroundDrawable(new ColorDrawable(0x4c000000));

		sivViewer = new ScaledImageView(activity);
		sivViewer.setScaleType(ScaleType.MATRIX);
		activity.setContentView(sivViewer);
		if (pic != null) {
			sivViewer.getViewTreeObserver().addOnGlobalLayoutListener(this);
		}
	}

	public void onGlobalLayout() {
		sivViewer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		sivViewer.post(new Runnable() {
			public void run() {
				sivViewer.setBitmap(pic);
			}
		});
	}

}
