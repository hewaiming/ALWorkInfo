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
 * Socket收发�? 通过Socket发�?�数据，并使用新线程监听Socket接收到的数据
 * 
 */
public abstract class SocketTransceiver implements Runnable {

	protected Socket socket;
	protected InetAddress addr;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected ObjectInputStream objectInputStream;
	private boolean runFlag;	

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
			objectInputStream = new ObjectInputStream(this.socket.getInputStream());			
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
							this.onReceive(addr, rTime);
						}
					} else if (actionId == 2) {
						final PotStatusDATA pList = (PotStatusDATA) objectInputStream.readObject();
						if (pList != null) {							
							this.onReceive(addr, pList);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();				

			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

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

	/**
	 * 接收到数�? 注意：此回调是在新线程中执行�? 连接到的Socket地址
	 */
	// 接受服务端发送过来的实时曲线数据
	public abstract void onReceive(InetAddress addr, RealTime rTime);

	// 接受服务端发送过来的槽状态数�?
	public abstract void onReceive(InetAddress addr, PotStatusDATA potStatus);

	public abstract void onDisconnect(InetAddress addr);
	
}
