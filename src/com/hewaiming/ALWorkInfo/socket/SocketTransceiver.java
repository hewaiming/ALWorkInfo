package com.hewaiming.ALWorkInfo.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import com.hewaiming.ALWorkInfo.bean.PotStatus;
import com.hewaiming.ALWorkInfo.bean.RealTime;
import com.hewaiming.ALWorkInfo.bean.RequestAction;

/**
 * Socket收发�? 通过Socket发�?�数据，并使用新线程监听Socket接收到的数据
 * 
 * @author jzj1993
 * @since 2015-2-22
 */
public abstract class SocketTransceiver implements Runnable {

	protected Socket socket;
	protected InetAddress addr;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected ObjectInputStream objectInputStream;
	private boolean runFlag;

	/**
	 * 实例�?
	 * 
	 * @param socket
	 *            已经建立连接的socket
	 */
	public SocketTransceiver(Socket socket) {
		this.socket = socket;
		this.addr = socket.getInetAddress();
	}

	/**
	 * 获取连接到的Socket地址
	 * 
	 * @return InetAddress对象
	 */
	public InetAddress getInetAddress() {
		return addr;
	}

	/**
	 * �?启Socket收发
	 * <p>
	 * 如果�?启失败，会断�?连接并回调{@code onDisconnect()}
	 */
	public void start() {
		runFlag = true;
		new Thread(this).start();
	}

	/**
	 * 断开连接(主动)
	 * <p>
	 * 连接断开后，会回调{@code onDisconnect()}
	 */
	public void stop() {
		runFlag = false;
		try {
			socket.shutdownInput();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发�?�字符串
	 * 
	 * @param s
	 *            字符�?
	 * @return 发�?�成功返回true
	 */
	public boolean send(String s) {
		if (out != null) {
			try {
				out.writeUTF(s);
				out.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// 向服务端发�?�操作命�?
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
	 * 监听Socket接收的数�?(新线程中运行)
	 */
	@Override
	public void run() {
		try {
			in = new DataInputStream(this.socket.getInputStream());
			out = new DataOutputStream(this.socket.getOutputStream());
			objectInputStream=new ObjectInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			runFlag = false;
		}
		while (runFlag) {
		    try {
				int actionId=objectInputStream.readInt();
				if (actionId==1){
					final RealTime rTime = (RealTime) objectInputStream.readObject();
					this.onReceive(addr, rTime);
				}else if(actionId==2){
					final ArrayList<PotStatus> pList=(ArrayList<PotStatus>) objectInputStream.readObject();
					this.onReceive(addr, pList); 
				}
			} catch (IOException e) {			
				e.printStackTrace();
			} catch (ClassNotFoundException e) {			
				e.printStackTrace();
			}    
			/*final ArrayList<PotStatus> pList=GetPotStatusFromServer(objectInputStream);
			final RealTime rTime = GetRealTimeFromServer(in);   //以下可能???
			
			this.onReceive(addr, pList);     
			this.onReceive(addr, rTime);*/
			
		}
		// 断开连接
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

	//从服务器读取实时曲线数据
	private RealTime GetRealTimeFromServer(DataInputStream in) {
		RealTime rTime = new RealTime();
		try {
			rTime.setCur(in.readInt());
			rTime.setPotv(in.readInt());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return rTime;
	}
	
	//从服务器读取槽状态数�?
	private ArrayList<PotStatus> GetPotStatusFromServer(ObjectInputStream objectInputStream) {
		ArrayList<PotStatus> list = new ArrayList<PotStatus>();
		
		try {
			try {
				list=(ArrayList<PotStatus>) objectInputStream.readObject();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return list;
	}

	/**
	 * 接收到数�?
	 * <p>
	 * 注意：此回调是在新线程中执行�?
	 * 
	 * @param addr
	 *            连接到的Socket地址
	 * @param s
	 *            收到的字符串
	 */
	public abstract void onReceive(InetAddress addr, String s);

	// 接受服务端发送过来的实时曲线数据
	public abstract void onReceive(InetAddress addr, RealTime rTime);
	
	// 接受服务端发送过来的实时曲线数据
	public abstract void onReceive(InetAddress addr, ArrayList<PotStatus> potStatus);

	/**
	 * 连接断开
	 * <p>
	 * 注意：此回调是在新线程中执行�?
	 * 
	 * @param addr
	 *            连接到的Socket地址
	 */
	public abstract void onDisconnect(InetAddress addr);
}
