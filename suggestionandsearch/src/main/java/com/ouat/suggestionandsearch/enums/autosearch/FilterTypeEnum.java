package com.ouat.suggestionandsearch.enums.autosearch;

public enum FilterTypeEnum {
    RANGE("range"), TERM("term");

    private String filterName;

    private FilterTypeEnum(String filterName) {
        this.filterName = filterName;
    }

    public String toString() {
        return this.filterName;
    }

}
