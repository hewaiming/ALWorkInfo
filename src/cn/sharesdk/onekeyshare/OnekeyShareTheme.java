/*
 * 官网地站:http://www.mob.com
 * �?术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第�?时间通过微信将版本更新内容推送给您�?�如果使用过程中有任何问题，也可以�?�过微信与我们取得联系，我们将会�?24小时内给予回复）
 *
 * Copyright (c) 2013�? mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare;

import cn.sharesdk.onekeyshare.themes.classic.ClassicTheme;

/** 快捷分享的主题样�?  */
public enum OnekeyShareTheme {
	/** 九格宫的主题样式 ,对应的实现类ClassicTheme */
	CLASSIC(0, new ClassicTheme());

	private final int value;
	private final OnekeyShareThemeImpl impl;

	private OnekeyShareTheme(int value, OnekeyShareThemeImpl impl) {
		this.value = value;
		this.impl = impl;
	}

	public int getValue() {
		return value;
	}

	public OnekeyShareThemeImpl getImpl() {
		return impl;
	}

	public static OnekeyShareTheme fromValue(int value) {
		for (OnekeyShareTheme theme : OnekeyShareTheme.values()) {
			if (theme.value == value) {
				return theme;
			}
		}
		return CLASSIC;
	}

}
