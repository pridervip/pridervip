package com.pridervip.gate.mobile.entity;

public class PriceNoticeFormatConfig {

	private String lang;
	private String formatLastPriceGreat;
	private String formatLastPriceLess;
	private String title;

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFormatLastPriceGreat() {
		return formatLastPriceGreat;
	}

	public void setFormatLastPriceGreat(String formatLastPriceGreat) {
		this.formatLastPriceGreat = formatLastPriceGreat;
	}

	public String getFormatLastPriceLess() {
		return formatLastPriceLess;
	}

	public void setFormatLastPriceLess(String formatLastPriceLess) {
		this.formatLastPriceLess = formatLastPriceLess;
	}

}
