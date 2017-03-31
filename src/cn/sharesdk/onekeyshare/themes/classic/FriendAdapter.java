/*
 * 官网地站:http://www.mob.com
 * �?术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第�?时间通过微信将版本更新内容推送给您�?�如果使用过程中有任何问题，也可以�?�过微信与我们取得联系，我们将会�?24小时内给予回复）
 *
 * Copyright (c) 2013�? mob.com. All rights reserved.
 */

package cn.sharesdk.onekeyshare.themes.classic;

import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import com.mob.tools.gui.PullToRequestListAdapter;
import com.mob.tools.gui.PullToRequestView;
import com.mob.tools.utils.UIHandler;

/** 好友列表的�?�配�? */
public class FriendAdapter extends PullToRequestListAdapter implements PlatformActionListener {
	private FriendListPage activity;
	private boolean hasNext;
	private Platform platform;
	/** 请求好友列表时，每页15�? */
	private final int pageCount = 15;
	/** 当前的好友列表是第几�? */
	private int curPage;
	/** 好友列表数据 */
	private ArrayList<Following> follows;
	/** 判断当前的好友列表数据与请求的新数据是否有重�? */
	private HashMap<String, Boolean> map;
	/** 好友列表的头部View */
	private PRTHeader llHeader;
	/** 根据设计，按照比例来布局，以此来适配�?有手�? */
	private float ratio;

	public FriendAdapter(FriendListPage activity, PullToRequestView view) {
		super(view);
		this.activity = activity;

		curPage = -1;
		hasNext = true;
		map = new HashMap<String, Boolean>();
		follows = new ArrayList<Following>();

		getListView().setDivider(new ColorDrawable(0xffeaeaea));
	}

	public void setRatio(float ratio) {
		this.ratio = ratio;
		getListView().setDividerHeight((int) (ratio < 1 ? 1 : ratio));
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		getListView().setOnItemClickListener(listener);
	}

	public void setPlatform(Platform platform) {
		this.platform = platform;
		platform.setPlatformActionListener(this);
	}

	private void next() {
		if (hasNext) {
			platform.listFriend(pageCount, curPage + 1, null);
		}
	}

	public void onComplete(Platform plat, int action, HashMap<String, Object> res) {
		final FollowersResult followersResult = parseFollowers(platform.getName(), res, map);
		if (followersResult == null) {
			UIHandler.sendEmptyMessage(0, new Callback() {
				public boolean handleMessage(Message msg) {
					notifyDataSetChanged();
					return false;
				}
			});
			return;
		}

		hasNext = followersResult.hasNextPage;
		if (followersResult.list != null && followersResult.list.size() > 0) {
			curPage++;
			Message msg = new Message();
			msg.what = 1;
			msg.obj = followersResult.list;
			UIHandler.sendMessage(msg, new Callback() {
				public boolean handleMessage(Message msg) {
					if (curPage <= 0) {
						follows.clear();
					}
					follows.addAll(followersResult.list);
					notifyDataSetChanged();
					return false;
				}
			});
		}
	}

	private FollowersResult parseFollowers(String platform, HashMap<String, Object> res, HashMap<String, Boolean> uidMap) {
		if (res == null || res.size() <= 0) {
			return null;
		}

		boolean hasNext = false;
		ArrayList<Following> data = new ArrayList<Following>();
		if ("SinaWeibo".equals(platform)) {
			// users[id, name, description]
			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, Object>> users = (ArrayList<HashMap<String,Object>>) res.get("users");
			for (HashMap<String, Object> user : users) {
				String uid = String.valueOf(user.get("id"));
				if (!uidMap.containsKey(uid)) {
					Following following = new Following();
					following.uid = uid;
					following.screenName = String.valueOf(user.get("name"));
					following.description = String.valueOf(user.get("description"));
					following.icon = String.valueOf(user.get("profile_image_url"));
					following.atName = following.screenName;
					uidMap.put(following.uid, true);
					data.add(following);
				}
			}
			hasNext = (Integer) res.get("total_number") > uidMap.size();
		} else if ("TencentWeibo".equals(platform)) {
			hasNext = ((Integer)res.get("hasnext") == 0);
			// info[nick, name, tweet[text]]
			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, Object>> infos = (ArrayList<HashMap<String,Object>>) res.get("info");
			for (HashMap<String, Object> info : infos) {
				String uid = String.valueOf(info.get("name"));
				if (!uidMap.containsKey(uid)) {
					Following following = new Following();
					following.screenName = String.valueOf(info.get("nick"));
					following.uid = uid;
					following.atName = uid;
					@SuppressWarnings("unchecked")
					ArrayList<HashMap<String, Object>> tweets = (ArrayList<HashMap<String,Object>>) info.get("tweet");
					for (HashMap<String, Object> tweet : tweets) {
						following.description = String.valueOf(tweet.get("text"));
						break;
					}
					following.icon = String.valueOf(info.get("head")) + "/100";
					uidMap.put(following.uid, true);
					data.add(following);
				}
			}
		} else if ("Facebook".equals(platform)) {
			// data[id, name]
			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, Object>> datas = (ArrayList<HashMap<String,Object>>) res.get("data");
			for (HashMap<String, Object> d : datas) {
				String uid = String.valueOf(d.get("id"));
				if (!uidMap.containsKey(uid)) {
					Following following = new Following();
					following.uid = uid;
					following.atName = "[" + uid + "]";
					following.screenName = String.valueOf(d.get("name"));
					@SuppressWarnings("unchecked")
					HashMap<String, Object> picture = (HashMap<String, Object>) d.get("picture");
					if (picture != null) {
						@SuppressWarnings("unchecked")
						HashMap<String, Object> pData = (HashMap<String, Object>) picture.get("data");
						following.icon = String.valueOf(pData.get("url"));
					}
					uidMap.put(following.uid, true);
					data.add(following);
				}
			}
			@SuppressWarnings("unchecked")
			HashMap<String, Object> paging = (HashMap<String, Object>) res.get("paging");
			hasNext = paging.containsKey("next");
		} else if ("Twitter".equals(platform)) {
			// users[screen_name, name, description]
			@SuppressWarnings("unchecked")
			ArrayList<HashMap<String, Object>> users = (ArrayList<HashMap<String,Object>>) res.get("users");
			for (HashMap<String, Object> user : users) {
				String uid = String.valueOf(user.get("screen_name"));
				if (!uidMap.containsKey(uid)) {
					Following following = new Following();
					following.uid = uid;
					following.atName = uid;
					following.screenName = String.valueOf(user.get("name"));
					following.description = String.valueOf(user.get("description"));
					following.icon = String.valueOf(user.get("profile_image_url"));
					uidMap.put(following.uid, true);
					data.add(following);
				}
			}
		}

		FollowersResult ret = new FollowersResult();
		ret.list = data;
		ret.hasNextPage = hasNext;
		return ret;
	}

	public void onError(Platform plat, int action, Throwable t) {
		t.printStackTrace();
	}

	public void onCancel(Platform plat, int action) {
		UIHandler.sendEmptyMessage(0, new Callback() {
			public boolean handleMessage(Message msg) {
				activity.finish();
				return false;
			}
		});
	}

	public Following getItem(int position) {
		return follows.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getCount() {
		return follows == null ? 0 : follows.size();
	}

	public View getHeaderView() {
		if (llHeader == null) {
			llHeader = new PRTHeader(getContext());
		}
		return llHeader;
	}

	public void onPullDown(int percent) {
		llHeader.onPullDown(percent);
	}

	public void onRefresh() {
		llHeader.onRequest();
		curPage = -1;
		hasNext = true;
		map.clear();
		next();
	}

	public void onReversed() {
		llHeader.reverse();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			FriendListItem llItem = new FriendListItem(parent.getContext(), ratio);
			convertView = llItem;
		}
		FriendListItem llItem = (FriendListItem) convertView;
		llItem.update(getItem(position), isFling());

		if (position == getCount() - 1) {
			next();
		}
		return convertView;
	}

	public static class Following {
		public boolean checked;
		public String screenName;
		public String description;
		public String uid;
		public String icon;
		//@Name 用于微博等提示或关联某个�?
		public String atName;
	}

	private static class FollowersResult {
		public ArrayList<Following> list;
		public boolean hasNextPage = false;
	}

	public View getFooterView() {
		LinearLayout footerView = new LinearLayout(getContext());
		footerView.setMinimumHeight(10);
		return footerView;
	}

}
