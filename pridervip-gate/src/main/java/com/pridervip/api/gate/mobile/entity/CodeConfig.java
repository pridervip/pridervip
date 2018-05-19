package com.bighuobi.api.gate.mobile.entity;

public class CodeConfig {

	private String name;
	private String nameEn;
	private int coinType;
	private String code;
	private String isHuobi;
	private Boolean isFutures;
	private Boolean isStop;
	private String currency;
	private String url;

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

	public String getNameEn() {
		return nameEn;
	}

	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	
	public Boolean getIsStop() {
		return isStop;
	}

	public void setIsStop(Boolean isStop) {
		this.isStop = isStop;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

}
