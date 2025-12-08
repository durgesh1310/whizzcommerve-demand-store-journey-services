package com.ouat.checkout.constant;

public class DownStreamConstant {
    public static final String CUSTOMER_SERVICE = "CustomerService";
    public static final String CART_SERVICE = "CartService";
    public static final String PLP_PDP_SERVICE = "PlpPdpService";
    public static final String PROMOTION_SERVICE = "PromotionService";
    public static final String RAZORPAY = "Razorpay";

    public static class RequestType {
        public static final String GET_CUSTOMER_ADDRESS_RESPONSE = "getCustomerAddressResponse";
        public static final String GET_CUSTOMER_CREDIT_RESPONSE = "getCustomerCreditResponse";
        public static final String GET_PROMO_CODE_RESPONSE = "getPromoCodeResponse";
        public static final String GET_CUSTOMER_SHOW_SHOPPING_CART_ITEM_RESPONSE =
                "getCustomerShowShoppingCartItemResponse";
        public static final String GET_SKUS_INVENTORY_COD_DETAILS_RESPONSE = "getSkusInventoryCodDetailsResponse";
        
        public static final String CREATE_ORDER_ON_RAZORPAY_RESPONSE = "createOrderOnRazorpayResponse";
        public static final String CREATE_CUSTOMER_ON_RAZORPAY_RESPONSE = "createCustomerOnRazorpayResponse";
        public static final String FETCH_SAVED_CARDS = "fetchSavedCards";

    }
}
