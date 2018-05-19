package com.bighuobi.api.gate.trade.entity;

import java.io.Serializable;

public class UsdMarketEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -688341295996373619L;
	private String high;
	private String low;
	private String last;
	private String vol;
	private String open;
	
	public UsdMarketEntity() {
	}

	public String getHigh() {
		return high;
	}

	public void setHigh(String high) {
		this.high = high;
	}

	public String getLow() {
		return low;
	}

	public void setLow(String low) {
		this.low = low;
	}

	public String getLast() {
		return last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	public String getVol() {
		return vol;
	}

	public void setVol(String vol) {
		this.vol = vol;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	
}
