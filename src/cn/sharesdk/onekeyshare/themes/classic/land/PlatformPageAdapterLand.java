/*
 * 官网地站:http://www.mob.com
 * �?术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第�?时间通过微信将版本更新内容推送给您�?�如果使用过程中有任何问题，也可以�?�过微信与我们取得联系，我们将会�?24小时内给予回复）
 *
 * Copyright (c) 2013�? mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic.land;

import java.util.ArrayList;

import android.content.Context;
import cn.sharesdk.onekeyshare.themes.classic.PlatformPage;
import cn.sharesdk.onekeyshare.themes.classic.PlatformPageAdapter;

import com.mob.tools.utils.ResHelper;

/** 横屏的九宫格页面适配�? */
public class PlatformPageAdapterLand extends PlatformPageAdapter {
	private static final int DESIGN_SCREEN_WIDTH_L = 1280;
	private static final int DESIGN_CELL_WIDTH_L = 160;
	private static final int DESIGN_SEP_LINE_WIDTH = 1;
	private static final int DESIGN_LOGO_HEIGHT = 76;
	private static final int DESIGN_PADDING_TOP = 20;

	public PlatformPageAdapterLand(PlatformPage page, ArrayList<Object> cells) {
		super(page, cells);
	}

	protected void calculateSize(Context context, ArrayList<Object> plats) {
		int screenWidth = ResHelper.getScreenWidth(context);
		float ratio = ((float) screenWidth) / DESIGN_SCREEN_WIDTH_L;
		int cellWidth = (int) (DESIGN_CELL_WIDTH_L * ratio);
		lineSize = screenWidth / cellWidth;

		sepLineWidth = (int) (DESIGN_SEP_LINE_WIDTH * ratio);
		sepLineWidth = sepLineWidth < 1 ? 1 : sepLineWidth;
		logoHeight = (int) (DESIGN_LOGO_HEIGHT * ratio);
		paddingTop = (int) (DESIGN_PADDING_TOP * ratio);
		bottomHeight = (int) (DESIGN_BOTTOM_HEIGHT * ratio);
		cellHeight = (screenWidth - sepLineWidth * 3) / (lineSize - 1);
		panelHeight = cellHeight + sepLineWidth;
	}

	protected void collectCells(ArrayList<Object> plats) {
		int count = plats.size();
		if (count < lineSize) {
			int lineCount = (count / lineSize);
			if (count % lineSize != 0) {
				lineCount++;
			}
			cells = new Object[1][lineCount * lineSize];
		} else {
			int pageCount = (count / lineSize);
			if (count % lineSize != 0) {
				pageCount++;
			}
			cells = new Object[pageCount][lineSize];
		}

		for (int i = 0; i < count; i++) {
			int p = i / lineSize;
			cells[p][i - lineSize * p] = plats.get(i);
		}
	}

}
