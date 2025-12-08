package com.ouat.checkout.response;

import java.util.EnumSet;

public enum Platform {

    ANDROID("ANDROID"), IOS("IOS"), WEB("WEB"), MWEB("MWEB"), MOBILE("MOBILE"), mobile("mobile"), ALL("ALL");

    private String value;

    private Platform(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    public static boolean contains(String value) {
        try {
            return EnumSet.allOf(Platform.class).contains(Enum.valueOf(Platform.class, value));
        } catch (Exception e) {
            return false;
        }
    }

    
}
