package com.pridervip.api.enums;

/**
 * Created by Administrator on 2014/11/18.
 */
public enum CurrencyType {

    ALL(0, "all"), CNY(1, "cny"), USD(2, "usd");

    private int value;

    private String name;

    CurrencyType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static CurrencyType findByValue(int coinTypeValue) {
        for (CurrencyType coinType : CurrencyType.values()) {
            if (coinType.getValue() == coinTypeValue) {
                return coinType;
            }
        }
        return null;
    }
    public static CurrencyType findByName(String coinTypeName) {
        for (CurrencyType coinType : CurrencyType.values()) {
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
