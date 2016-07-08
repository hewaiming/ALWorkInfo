package com.hewaiming.ALWorkInfo.socket;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import com.hewaiming.ALWorkInfo.bean.PotStatus;
import com.hewaiming.ALWorkInfo.bean.RealTime;


/**
 * TCP Socket客户�?
 * 
 * @author jzj1993
 * @since 2015-2-22
 */
public abstract class TcpClient implements Runnable {

	private int port;
	private String hostIP;
	private boolean connect = false;
	private SocketTransceiver transceiver;

	/**
	 * 建立连接
	 * <p>
	 * 连接的建立将在新线程中进�?
	 * <p>
	 * 连接建立成功，回调{@code onConnect()}
	 * <p>
	 * 连接建立失败，回调{@code onConnectFailed()}
	 * 
	 * @param hostIP
	 *            服务器主机IP
	 * @param port
	 *            端口
	 */
	public void connect(String hostIP, int port) {
		this.hostIP = hostIP;
		this.port = port;
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			Socket socket = new Socket(hostIP, port);
			transceiver = new SocketTransceiver(socket) {

				@Override
				public void onReceive(InetAddress addr, String s) {
					TcpClient.this.onReceive(this, s);
				}

				@Override
				public void onDisconnect(InetAddress addr) {
					connect = false;
					TcpClient.this.onDisconnect(this);
				}

				@Override
				public void onReceive(InetAddress addr, RealTime rTime) {
					TcpClient.this.onReceive(this, rTime);  //import
					
				}

				@Override
				public void onReceive(InetAddress addr, ArrayList<PotStatus> potStatus) {
					TcpClient.this.onReceive(this, potStatus);  //import
					
				}
			};
			transceiver.start();
			connect = true;
			this.onConnect(transceiver);
		} catch (Exception e) {
			e.printStackTrace();
			this.onConnectFailed();
		}
	}

	/**
	 * 断开连接
	 * <p>
	 * 连接断开，回调{@code onDisconnect()}
	 */
	public void disconnect() {
		if (transceiver != null) {
			transceiver.stop();
			transceiver = null;
		}
	}

	/**
	 * 判断是否连接
	 * 
	 * @return 当前处于连接状�?�，则返回true
	 */
	public boolean isConnected() {
		return connect;
	}

	/**
	 * 获取Socket收发�?
	 * 
	 * @return 未连接则返回null
	 */
	public SocketTransceiver getTransceiver() {
		return isConnected() ? transceiver : null;
	}

	/**
	 * 连接建立
	 * 
	 * @param transceiver
	 *            SocketTransceiver对象
	 */
	public abstract void onConnect(SocketTransceiver transceiver);

	/**
	 * 连接建立失败
	 */
	public abstract void onConnectFailed();

	/**
	 * 接收到数�?
	 * <p>
	 * 注意：此回调是在新线程中执行�?
	 * 
	 * @param transceiver
	 *            SocketTransceiver对象
	 * @param s
	 *            字符�?
	 */
	public abstract void onReceive(SocketTransceiver transceiver, String s);
	
	public abstract void onReceive(SocketTransceiver transceiver, RealTime realTime);
	
	public abstract void onReceive(SocketTransceiver transceiver, ArrayList<PotStatus> potStatus);
	/**
	 * 连接断开
	 * <p>
	 * 注意：此回调是在新线程中执行�?
	 * 
	 * @param transceiver
	 *            SocketTransceiver对象
	 */
	public abstract void onDisconnect(SocketTransceiver transceiver);
}
