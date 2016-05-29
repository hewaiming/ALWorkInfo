package com.hewaiming.ALWorkInfo.bean;

public class dayTable {
	private int PotNo;
	private String PotSt;
	private double AverageV;
	private double RealSetV;
	private double SetV;
	private double WorkV;
	private double AeV;
	private int AeTime;
	private String Ddate;
	public dayTable(int potNo, String potSt, double averageV, double realSetV, double setV, double workV, double aeV,
			int aeTime, String ddate) {		
		PotNo = potNo;
		PotSt = potSt;
		AverageV = averageV;
		RealSetV = realSetV;
		SetV = setV;
		WorkV = workV;
		AeV = aeV;
		AeTime = aeTime;
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
		Ddate = ddate;
	}
	public dayTable() {
		
	}
	@Override
	public String toString() {
		return "[PotNo=" + PotNo + ", PotSt=" + PotSt + ", AverageV=" + AverageV + ", RealSetV=" + RealSetV
				+ ", SetV=" + SetV + ", WorkV=" + WorkV + ", AeV=" + AeV + ", AeTime=" + AeTime + ", Ddate=" + Ddate
				+ "]";
	}
}
