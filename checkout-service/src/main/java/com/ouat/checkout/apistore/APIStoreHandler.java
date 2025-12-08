package com.ouat.checkout.apistore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class APIStoreHandler {
    
    @Autowired
    private APIStoreConfiguration apiStoreConfiguration;
    
    private static final String CUSTOMER_ID_PLACEHOLDER = "{customerId}";
    private static final String PROMOCODE_PLACEHOLDER = "{promoCode}";
    private static final String PLATFORM = "{platform}";
    
    public String fetchAPIStoreCustomerCreditUrl(Integer customerId) {
        return fetchBaseConfigurationWithoutPort()
                .concat(apiStoreConfiguration.getApi().getCustomerCredit().getPath())
                .replace(CUSTOMER_ID_PLACEHOLDER, String.valueOf(customerId));
    }
    
    public String fetchAPIStoreCustomerAddressDetailsUrl(Integer customerId) {
        return fetchBaseConfigurationWithoutPort()
                .concat(apiStoreConfiguration.getApi().getCustomerAddress().getPath())
                .replace(CUSTOMER_ID_PLACEHOLDER, String.valueOf(customerId));
    }
    
    public String fetchAPIStoreCustomerShoppingCartDetailsUrl(String customerId, String platform) {
        return fetchBaseConfigurationWithoutPort()
                .concat(apiStoreConfiguration.getApi().getCustomerShoppingCartDetails().getPath())
                .replace(CUSTOMER_ID_PLACEHOLDER, customerId)
                .replace(PLATFORM, platform);
    }
    
    public String fetchAPIStorePromoCodeDetailsUrl(String promoCode) {
        return fetchBaseConfigurationWithoutPort()
                .concat(apiStoreConfiguration.getApi().getCustomerPromoCodeDetails().getPath());
    }
    
    public String fetchAPIStoreSkusInventoryCodDetailsUrl() {
        return fetchBaseConfigurationWithoutPort()
                .concat(apiStoreConfiguration.getApi().getSkusInventoryCodDetails().getPath());
    }
    
    private String fetchBaseConfigurationWithoutPort() {
        return apiStoreConfiguration.getProtocol().concat("://")
                .concat(apiStoreConfiguration.getHost())
                .concat(apiStoreConfiguration.getBasePath());
    }
    
    private String fetchBaseConfiguration() {
        return apiStoreConfiguration.getProtocol().concat("://")
                .concat(apiStoreConfiguration.getHost()).concat(":")
                .concat(apiStoreConfiguration.getPort())
                .concat(apiStoreConfiguration.getBasePath());
    }
    
}
