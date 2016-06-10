package com.hewaiming.ALWorkInfo.bean;

public class dayTable {
	private int PotNo;
	private String PotSt;
	private int RunTime;
	private double AverageV;
	private double RealSetV;
	private double SetV;
	private double WorkV;
	private double AeV;
	private int AeTime;
	private int AeCnt;
	private int DybTime;
	private String Ddate;

	public dayTable(int potNo, String potSt, int runTime, double averageV, double realSetV, double setV, double workV,
			double aeV, int aeTime, int aeCnt, int dybTime, String ddate) {
		super();
		PotNo = potNo;
		PotSt = potSt;
		RunTime = runTime;
		AverageV = averageV;
		RealSetV = realSetV;
		SetV = setV;
		WorkV = workV;
		AeV = aeV;
		AeTime = aeTime;
		AeCnt = aeCnt;
		DybTime = dybTime;
		Ddate = ddate;
	}

	public int getPotNo() {
		return PotNo;
	}

	public void setPotNo(int potNo) {
		PotNo = potNo;
	}

	public String getPotSt() {
		return PotSt;
	}

	public void setPotSt(String potSt) {
		PotSt = potSt;
	}

	public double getAverageV() {
		return AverageV;
	}

	public void setAverageV(double averageV) {
		AverageV = averageV;
	}

	public double getRealSetV() {
		return RealSetV;
	}

	public void setRealSetV(double realSetV) {
		RealSetV = realSetV;
	}

	public double getSetV() {
		return SetV;
	}

	public void setSetV(double setV) {
		SetV = setV;
	}

	public double getWorkV() {
		return WorkV;
	}

	public void setWorkV(double workV) {
		WorkV = workV;
	}

	public double getAeV() {
		return AeV;
	}

	public void setAeV(double aeV) {
		AeV = aeV;
	}

	public int getAeTime() {
		return AeTime;
	}

	public void setAeTime(int aeTime) {
		AeTime = aeTime;
	}

	public String getDdate() {
		return Ddate;
	}

	public void setDdate(String ddate) {
		if (ddate.length() > 10) {
			String temp = ddate.substring(0, 10);
			Ddate = temp;
		}
	}

	public dayTable() {

	}

	public int getRunTime() {
		return RunTime;
	}

	public void setRunTime(int runTime) {
		RunTime = runTime;
	}

	public int getAeCnt() {
		return AeCnt;
	}

	public void setAeCnt(int aeCnt) {
		AeCnt = aeCnt;
	}

	public int getDybTime() {
		return DybTime;
	}

	public void setDybTime(int dybTime) {
		DybTime = dybTime;
	}

	@Override
	public String toString() {
		return "dayTable [PotNo=" + PotNo + ", PotSt=" + PotSt + ", RunTime=" + RunTime + ", AverageV=" + AverageV
				+ ", RealSetV=" + RealSetV + ", SetV=" + SetV + ", WorkV=" + WorkV + ", AeV=" + AeV + ", AeTime="
				+ AeTime + ", AeCnt=" + AeCnt + ", DybTime=" + DybTime + ", Ddate=" + Ddate + "]";
	}

}
