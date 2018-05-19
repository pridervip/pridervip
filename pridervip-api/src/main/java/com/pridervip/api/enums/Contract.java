package com.pridervip.api.enums;

public enum Contract {
	
	WEEK(7,"week"),WEEK2(10,"week2"), MONTH(30, "month"), QUARTER(90,"quarter"),QUARTER2(91,"quarter2");

    private int value;
    private String name;

    private Contract(int value, String name) {
        this.name = name;
        this.value = value;
    }
    public static Contract findByName(String name) {
        for (Contract en : Contract.values()) {
            if (en.getName().equals(name)) {
                return en;
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
