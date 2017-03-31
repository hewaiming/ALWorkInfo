/*
 * 官网地站:http://www.mob.com
 * �?术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第�?时间通过微信将版本更新内容推送给您�?�如果使用过程中有任何问题，也可以�?�过微信与我们取得联系，我们将会�?24小时内给予回复）
 *
 * Copyright (c) 2013�? mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;

/** 在At好友页面中，下拉刷新列表头部的旋转箭�? */
public class RotateImageView extends ImageView {
	private float rotation;

	public RotateImageView(Context context) {
		super(context);
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
		invalidate();
	}

	protected void onDraw(Canvas canvas) {
		canvas.rotate(rotation, getWidth() / 2, getHeight() / 2);
		super.onDraw(canvas);
	}

}
