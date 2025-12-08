package com.ouat.suggestionandsearch.enums.autosearch;

public enum SortOptionsEnum {
	RECOMMENDED("Recommended"), 
	PRICE_LOW_TO_HIGH("Price: Low to High"), 
	PRICE_HIGH_TO_LOW("Price: High to Low"),
	BETTER_DISCOUNT("Better Discount"),
	NEW_ARRIVAL("What's New"),
	BEST_SELLER("Bestseller");

	private final String name;

	private SortOptionsEnum(String string) {
		name = string;
	}

	public boolean equalsName(String otherName) {
		return name.equals(otherName);
	}

	public String toString() {
		return this.name;
	}
}
