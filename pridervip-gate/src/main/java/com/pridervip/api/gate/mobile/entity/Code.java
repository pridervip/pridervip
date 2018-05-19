package com.bighuobi.api.gate.mobile.entity;

public class Code {

	private String code;
	private String name;
	private int[] leverage;
	private int coinType;
	private String isHuobi;
	private Boolean isFutures;
	private String fastQuotation;
	private String slowQuodation;
	private String currency;
	private String currency2;
	private String contractType;
	private String contractName;

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

	public int[] getLeverage() {
		return leverage;
	}

	public void setLeverage(int[] leverage) {
		this.leverage = leverage;
	}

	public int getCoinType() {
		return coinType;
	}

	public void setCoinType(int coinType) {
		this.coinType = coinType;
	}

	public String getIsHuobi() {
		return isHuobi;
	}

	public void setIsHuobi(String isHuobi) {
		this.isHuobi = isHuobi;
	}

	public Boolean getIsFutures() {
		return isFutures;
	}

	public void setIsFutures(Boolean isFutures) {
		this.isFutures = isFutures;
	}

	public String getFastQuotation() {
		return fastQuotation;
	}

	public void setFastQuotation(String fastQuotation) {
		this.fastQuotation = fastQuotation;
	}

	public String getSlowQuodation() {
		return slowQuodation;
	}

	public void setSlowQuodation(String slowQuodation) {
		this.slowQuodation = slowQuodation;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getContractType() {
		return contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getCurrency2() {
		return currency2;
	}

	public void setCurrency2(String currency2) {
		this.currency2 = currency2;
	}
	
	
}
