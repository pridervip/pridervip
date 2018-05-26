package com.pridervip.gate.trade.entity;

import java.io.Serializable;

public class MarketHuobiEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3579014993808988974L;
	private String p_high;
	private String p_low;
	private String p_new;
	private String amount;
	private String p_open;

	public MarketHuobiEntity() {
	}

	public String getP_high() {
		return p_high;
	}

	public void setP_high(String p_high) {
		this.p_high = p_high;
	}

	public String getP_low() {
		return p_low;
	}

	public void setP_low(String p_low) {
		this.p_low = p_low;
	}

	public String getP_new() {
		return p_new;
	}

	public void setP_new(String p_new) {
		this.p_new = p_new;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getP_open() {
		return p_open;
	}

	public void setP_open(String p_open) {
		this.p_open = p_open;
	}
}
