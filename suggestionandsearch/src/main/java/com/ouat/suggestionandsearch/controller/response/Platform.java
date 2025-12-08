package com.ouat.suggestionandsearch.controller.response;

public enum Platform {

    ANDROID("ANDROID"), IOS("IOS"), WEB("WEB"), MWEB("MWEB"), MOBILE("MOBILE"), mobile("mobile"), ALL("ALL");

    private String value;

    private Platform(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
