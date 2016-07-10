package com.hewaiming.ALWorkInfo.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import bean.PotStatus;
import bean.RealTime;
import bean.RequestAction;


/**
 * Socket�շ��� ͨ��Socket�������ݣ���ʹ�����̼߳���Socket���յ�������
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
	 * ʵ����
	 * 
	 * @param socket
	 *            �Ѿ��������ӵ�socket
	 */
	public SocketTransceiver(Socket socket) {
		this.socket = socket;
		this.addr = socket.getInetAddress();
	}

	/**
	 * ��ȡ���ӵ���Socket��ַ
	 * 
	 * @return InetAddress����
	 */
	public InetAddress getInetAddress() {
		return addr;
	}

	/**
	 * ����Socket�շ�
	 * <p>
	 * �������ʧ�ܣ���Ͽ����Ӳ��ص�{@code onDisconnect()}
	 */
	public void start() {
		runFlag = true;
		new Thread(this).start();
	}

	/**
	 * �Ͽ�����(����)
	 * <p>
	 * ���ӶϿ��󣬻�ص�{@code onDisconnect()}
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
	 * �����ַ���
	 * 
	 * @param s
	 *            �ַ���
	 * @return ���ͳɹ�����true
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

	// �����˷��Ͳ�������
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
	 * ����Socket���յ�����(���߳�������)
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
			
			
		}
		// �Ͽ�����
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
	 * ���յ�����
	 * <p>
	 * ע�⣺�˻ص��������߳���ִ�е�
	 * 
	 * @param addr
	 *            ���ӵ���Socket��ַ
	 * @param s
	 *            �յ����ַ���
	 */
	public abstract void onReceive(InetAddress addr, String s);

	// ���ܷ���˷��͹�����ʵʱ��������
	public abstract void onReceive(InetAddress addr, RealTime rTime);
	
	// ���ܷ���˷��͹�����ʵʱ��������
	public abstract void onReceive(InetAddress addr, ArrayList<PotStatus> potStatus);

	/**
	 * ���ӶϿ�
	 * <p>
	 * ע�⣺�˻ص��������߳���ִ�е�
	 * 
	 * @param addr
	 *            ���ӵ���Socket��ַ
	 */
	public abstract void onDisconnect(InetAddress addr);
}
