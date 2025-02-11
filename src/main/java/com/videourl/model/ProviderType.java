package com.videourl.model;

public enum ProviderType {
    PROVIDER_TYPE_CLOUDFLARE_R2(1);

    private final int value;

    ProviderType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ProviderType fromValue(int value) {
        for (ProviderType type : ProviderType.values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + value);
    }
}
