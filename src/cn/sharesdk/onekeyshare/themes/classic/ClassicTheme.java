/*
 * 官网地站:http://www.mob.com
 * �?术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第�?时间通过微信将版本更新内容推送给您�?�如果使用过程中有任何问题，也可以�?�过微信与我们取得联系，我们将会�?24小时内给予回复）
 *
 * Copyright (c) 2013�? mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic;

import android.content.Context;
import android.content.res.Configuration;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShareThemeImpl;
import cn.sharesdk.onekeyshare.themes.classic.land.EditPageLand;
import cn.sharesdk.onekeyshare.themes.classic.land.PlatformPageLand;
import cn.sharesdk.onekeyshare.themes.classic.port.EditPagePort;
import cn.sharesdk.onekeyshare.themes.classic.port.PlatformPagePort;

/** 九宫格经典主题样式的实现�?*/
public class ClassicTheme extends OnekeyShareThemeImpl {

	/** 展示平台列表*/
	protected void showPlatformPage(Context context) {
		PlatformPage page;
		int orientation = context.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			page = new PlatformPagePort(this);
		} else {
			page = new PlatformPageLand(this);
		}
		page.show(context, null);
	}

	/** 展示编辑界面*/
	protected void showEditPage(Context context, Platform platform, ShareParams sp) {
		EditPage page;
		int orientation = context.getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			page = new EditPagePort(this);
		} else {
			page = new EditPageLand(this);
		}
		page.setPlatform(platform);
		page.setShareParams(sp);
		page.show(context, null);
	}

}
