package com.ouat.suggestionandsearch.apistore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class APIStoreHandler {
    
    @Autowired
    private APIStoreConfiguration apiStoreConfiguration;
    
    private static final String CUSTOMER_ID_PLACEHOLDER = "{customerId}";
    
    public String fetchAPIStoreWishListGetProductId(Long customerId) {
        return fetchBaseConfigurationWithoutPort()
                .concat(apiStoreConfiguration.getApi().getGetProductId().getPath())
                .replace(CUSTOMER_ID_PLACEHOLDER, String.valueOf(customerId));
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
