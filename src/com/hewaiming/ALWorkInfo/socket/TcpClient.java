package com.hewaiming.ALWorkInfo.socket;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import android.util.Log;
import bean.PotStatus;
import bean.PotStatusDATA;
import bean.RealTime;

/**
 * TCP Socket�ͻ���
 */
public abstract class TcpClient implements Runnable {

	private int port;
	private String hostIP;
	private boolean connect = false;
	private SocketTransceiver transceiver;

	/**
	 * ��������	
	 * ���ӵĽ����������߳��н���
	 * ���ӽ����ɹ����ص�{@code onConnect()}
	 * ���ӽ���ʧ�ܣ��ص�{@code onConnectFailed()}	
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
				public void onDisconnect(InetAddress addr) {
					connect = false;
					TcpClient.this.onDisconnect(this);
				}

				@Override
				public void onReceive(InetAddress addr, RealTime rTime) {
					//System.out.println("���ܵ�ʵʱ����"+rTime.toString());					
					TcpClient.this.onReceive(this, rTime);  //import
				
					
				}
				@Override
				public void onReceive(InetAddress addr, PotStatusDATA potStatus) {
					//System.out.println("���ܵ� ��״̬����"+potStatus.toString());
					TcpClient.this.onReceive(this, potStatus);  //import
					/*if (potStatus!=null){
						TcpClient.this.onReceive(this, potStatus);  //import
					}	*/				
				}				
				
			};
			transceiver.start();
			connect = true;
			this.onConnect(transceiver);
		} catch (Exception e) {
			Log.e("TcpClient","����SOCKET�����ʱ����");	
			this.onConnectFailed();
		}
	}

	/**
	 * �Ͽ�����	
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
	 * @return ��ǰ��������״̬���򷵻�true
	 */
	public boolean isConnected() {
		return connect;
	}

	/**
	 * ��ȡSocket�շ���	
	 * @return δ�����򷵻�null
	 */
	public SocketTransceiver getTransceiver() {
		return isConnected() ? transceiver : null;
	}

	/**
	 * ���ӽ���
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
	 * ע�⣺�˻ص��������߳���ִ�е�
	 * @param transceiver
	 *            SocketTransceiver����	
	 */	
	
	public abstract void onReceive(SocketTransceiver transceiver, RealTime realTime);
	
	public abstract void onReceive(SocketTransceiver transceiver, PotStatusDATA potStatus);
	/**
	 * ���ӶϿ�
	 * ע�⣺�˻ص��������߳���ִ�е�
	 * @param transceiver
	 *            SocketTransceiver����
	 */
	public abstract void onDisconnect(SocketTransceiver transceiver);	
}
