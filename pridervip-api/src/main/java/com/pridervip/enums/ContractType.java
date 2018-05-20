package com.pridervip.enums;

/**
 * Created by Administrator on 2014/11/21.
 */
public enum ContractType {
    WEEK("week", 1), WEEK2("week2", 4), QUARTER("quarter", 2), QUARTER2("quarter2", 3);

    private String name;

    private int week;

    ContractType(String name, int week) {
        this.name = name;
        this.week = week;
    }

    public static ContractType findByName(String name) {
        for (ContractType contractType : ContractType.values()) {
            if (contractType.getName().equals(name)) {
                return contractType;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
