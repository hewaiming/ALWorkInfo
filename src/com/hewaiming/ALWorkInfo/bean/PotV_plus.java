package com.hewaiming.ALWorkInfo.bean;

public class PotV_plus {
	private int PotV;
	private int Cur;
	private int Action;
	private String Ddate;	
	
	public PotV_plus() {
		super();
	}

	public PotV_plus(int potV, int cur, int action, String ddate) {
		super();
		PotV = potV;
		Cur = cur;
		Action = action;
		Ddate = ddate;
	}

	public int getAction() {
		return Action;
	}

	public void setAction(int action) {
		Action = action;
	}	

	public int getPotV() {
		return PotV;
	}

	public void setPotV(int potV) {
		PotV = potV;
	}

	public int getCur() {
		return Cur;
	}

	public void setCur(int cur) {
		Cur = cur;
	}
	

	public String getDdate() {
		return Ddate;
	}

	public void setDdate(String ddate) {
		if (ddate.length() > 10) {
			String temp = ddate.substring(8, ddate.length() - 7);
			Ddate = temp;
		}
	}

	@Override
	public String toString() {
		return "PotV_plus [PotV=" + PotV + ", Cur=" + Cur + ", Action=" + Action + ", Ddate=" + Ddate + "]";
	}
	

}