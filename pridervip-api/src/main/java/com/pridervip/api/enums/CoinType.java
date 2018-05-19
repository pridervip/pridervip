package com.pridervip.api.enums;

/**
 * Created by Administrator on 2014/11/18.
 */
public enum CoinType {

    ALL(0, "all"), BTC(1, "btc"), LTC(2, "ltc");

    private int value;

    private String name;

    CoinType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CoinType findByValue(int coinTypeValue) {
        for (CoinType coinType : CoinType.values()) {
            if (coinType.getValue() == coinTypeValue) {
                return coinType;
            }
        }
        return null;
    }
    public static CoinType findByName(String coinTypeName) {
        for (CoinType coinType : CoinType.values()) {
            if (coinType.getName().equalsIgnoreCase(coinTypeName)) {
                return coinType;
            }
        }
        return null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
