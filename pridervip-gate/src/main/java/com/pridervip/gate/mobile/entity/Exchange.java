package com.pridervip.gate.mobile.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Exchange {
	
	private String code;
	private String name;
	private String amount;
	private String lprice;
	private String hprice;
	private String cprice;
	private String oprice;
	private String cprice2;
	@JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")  
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getLprice() {
		return lprice;
	}
	public void setLprice(String lprice) {
		this.lprice = lprice;
	}
	public String getHprice() {
		return hprice;
	}
	public void setHprice(String hprice) {
		this.hprice = hprice;
	}
	public String getCprice() {
		return cprice;
	}
	public void setCprice(String cprice) {
		this.cprice = cprice;
	}
	public String getOprice() {
		return oprice;
	}
	public void setOprice(String oprice) {
		this.oprice = oprice;
	}
	public String getCprice2() {
		return cprice2;
	}
	public void setCprice2(String cprice2) {
		this.cprice2 = cprice2;
	}
	
}
