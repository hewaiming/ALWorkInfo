package com.hewaiming.ALWorkInfo.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import bean.PotStatus;
import bean.PotStatusDATA;
import bean.RealTime;
import bean.RequestAction;

/**
 * Socket鏀跺彂鍣? 閫氳繃Socket鍙戦?佹暟鎹紝骞朵娇鐢ㄦ柊绾跨▼鐩戝惉Socket鎺ユ敹鍒扮殑鏁版嵁
 * 
 */
public abstract class SocketTransceiver implements Runnable {

	protected Socket socket;
	protected InetAddress addr;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected ObjectInputStream objectInputStream;
	private boolean runFlag;
	private int GetNoData = 0;

	public SocketTransceiver(Socket socket) {
		this.socket = socket;
		this.addr = socket.getInetAddress();
	}

	public InetAddress getInetAddress() {
		return addr;
	}

	public void start() {
		runFlag = true;
		new Thread(this).start();
	}
	
	public void stop() {
		runFlag = false;
		try {
			socket.shutdownInput();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 鍚戞湇鍔＄鍙戦?佹搷浣滃懡浠?
	public boolean send(RequestAction action) {
		if (out != null) {
			try {
				out.writeInt(action.getActionId());
				out.writeUTF(action.getPotNo_Area());
				out.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 鐩戝惉Socket鎺ユ敹鐨勬暟鎹?(鏂扮嚎绋嬩腑杩愯)
	 */
	@Override
	public void run() {
		try {
			in = new DataInputStream(this.socket.getInputStream());
			out = new DataOutputStream(this.socket.getOutputStream());
			objectInputStream = new ObjectInputStream(this.socket.getInputStream());
			GetNoData=0;
		} catch (IOException e) {
			e.printStackTrace();
			runFlag = false;
		}
		while (runFlag) {
			try {
				if (objectInputStream != null) {
					int actionId = objectInputStream.readInt();
					if (actionId == 1) {
						final RealTime rTime = (RealTime) objectInputStream.readObject();
						if (rTime != null) {
							GetNoData = 0;
							this.onReceive(addr, rTime);
						}
					} else if (actionId == 2) {
						final PotStatusDATA pList = (PotStatusDATA) objectInputStream.readObject();
						if (pList != null) {
							GetNoData = 0;
							this.onReceive(addr, pList);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();				
				if (GetNoData > 200) {
					this.onReconnect(addr);
					// runFlag=false; //连续多次没有获取到服务器传送过来的数据				
				} else {
					GetNoData++;
				}

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

		}
		// 鏂紑杩炴帴
		try {
			in.close();
			out.close();
			socket.close();
			in = null;
			out = null;
			socket = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.onDisconnect(addr);
	}

	/**
	 * 鎺ユ敹鍒版暟鎹? 娉ㄦ剰锛氭鍥炶皟鏄湪鏂扮嚎绋嬩腑鎵ц鐨? 杩炴帴鍒扮殑Socket鍦板潃
	 */
	// 鎺ュ彈鏈嶅姟绔彂閫佽繃鏉ョ殑瀹炴椂鏇茬嚎鏁版嵁
	public abstract void onReceive(InetAddress addr, RealTime rTime);

	// 鎺ュ彈鏈嶅姟绔彂閫佽繃鏉ョ殑妲界姸鎬佹暟鎹?
	public abstract void onReceive(InetAddress addr, PotStatusDATA potStatus);

	public abstract void onDisconnect(InetAddress addr);

	public abstract void onReconnect(InetAddress addr);
}
