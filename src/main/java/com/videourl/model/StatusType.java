package com.videourl.model;

public enum StatusType {
    STATUS_TYPE_VIOLATION(1);

    private final int value;

    StatusType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static StatusType fromValue(int value) {
        for (StatusType type : StatusType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
