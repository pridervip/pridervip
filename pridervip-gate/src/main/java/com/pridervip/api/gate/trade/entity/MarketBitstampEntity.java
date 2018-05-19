package com.bighuobi.api.gate.trade.entity;

import java.io.Serializable;

public class MarketBitstampEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -317644376486051947L;
	
	private String high;
	private String low;
	private String last;
	private String volume;
	private String last2;
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
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getLast2() {
		return last2;
	}
	public void setLast2(String last2) {
		this.last2 = last2;
	}
	
}
