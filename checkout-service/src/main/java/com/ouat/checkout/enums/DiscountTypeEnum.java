package com.ouat.checkout.enums;

public enum DiscountTypeEnum {
    FREESHIPPING("FREESHIPPING", 0.0);

    private String discountType;
    private Double promoValue;

    DiscountTypeEnum(String discountType, Double promoValue) {
        this.discountType = discountType;
        this.promoValue = promoValue;
    }

    public static Double getPromoValue(String userDiscountType) {
        for (DiscountTypeEnum promo : DiscountTypeEnum.values()) {
            if (promo.getDiscountType().equalsIgnoreCase(userDiscountType))
                return promo.getPromoValue();
        }
        return null;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public Double getPromoValue() {
        return promoValue;
    }

    public void setPromoValue(Double promoValue) {
        this.promoValue = promoValue;
    }


}
