package com.ouat.suggestionandsearch.enums;

public enum RecommendationType {
    SIMILIAR_PRODUCTS("Similar Products"),
    BEST_DEAL("Best Deal"),
    NEW_ARRIVAL("New Arrival");
    
    private final String name;

    private RecommendationType(String string) {
        name = string;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
