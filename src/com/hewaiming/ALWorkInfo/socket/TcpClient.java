package com.hewaiming.ALWorkInfo.socket;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import bean.PotStatus;
import bean.RealTime;

/**
 * TCP Socket�ͻ���
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
	 * ��������
	 * <p>
	 * ���ӵĽ����������߳��н���
	 * <p>
	 * ���ӽ����ɹ����ص�{@code onConnect()}
	 * <p>
	 * ���ӽ���ʧ�ܣ��ص�{@code onConnectFailed()}
	 * 
	 * @param hostIP
	 *            ����������IP
	 * @param port
	 *            �˿�
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
					System.out.println("receive:"+rTime.toString());
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
	 * �Ͽ�����
	 * <p>
	 * ���ӶϿ����ص�{@code onDisconnect()}
	 */
	public void disconnect() {
		if (transceiver != null) {
			transceiver.stop();
			transceiver = null;
		}
	}

	/**
	 * �ж��Ƿ�����
	 * 
	 * @return ��ǰ��������״̬���򷵻�true
	 */
	public boolean isConnected() {
		return connect;
	}

	/**
	 * ��ȡSocket�շ���
	 * 
	 * @return δ�����򷵻�null
	 */
	public SocketTransceiver getTransceiver() {
		return isConnected() ? transceiver : null;
	}

	/**
	 * ���ӽ���
	 * 
	 * @param transceiver
	 *            SocketTransceiver����
	 */
	public abstract void onConnect(SocketTransceiver transceiver);

	/**
	 * ���ӽ���ʧ��
	 */
	public abstract void onConnectFailed();

	/**
	 * ���յ�����
	 * <p>
	 * ע�⣺�˻ص��������߳���ִ�е�
	 * 
	 * @param transceiver
	 *            SocketTransceiver����
	 * @param s
	 *            �ַ���
	 */
	public abstract void onReceive(SocketTransceiver transceiver, String s);
	
	public abstract void onReceive(SocketTransceiver transceiver, RealTime realTime);
	
	public abstract void onReceive(SocketTransceiver transceiver, ArrayList<PotStatus> potStatus);
	/**
	 * ���ӶϿ�
	 * <p>
	 * ע�⣺�˻ص��������߳���ִ�е�
	 * 
	 * @param transceiver
	 *            SocketTransceiver����
	 */
	public abstract void onDisconnect(SocketTransceiver transceiver);
}
