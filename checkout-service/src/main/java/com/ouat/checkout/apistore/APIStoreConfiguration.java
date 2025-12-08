package com.ouat.checkout.apistore;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@PropertySource(value = "classpath:${property.environment}/apiconfig.properties")
@ConfigurationProperties(prefix = "apistore")
@Getter
@Setter
public class APIStoreConfiguration {
    @NotNull
    private String protocol;
    @NotNull
    private String host;
    @NotNull
    private String port;
    @NotNull
    private String basePath;
    @NotNull
    private APIStoreConfiguration.API api;
    @NotNull
    private APIStoreConfiguration.DefaultHttpConfigs defaultHttpConfigs;

    @Getter
    @Setter
	public static class API {
        @NotNull
        private APIStoreConfiguration.ApiDetail customerCredit;
        @NotNull
        private APIStoreConfiguration.ApiDetail customerAddress;
        @NotNull
        private APIStoreConfiguration.ApiDetail customerShoppingCartDetails;
        @NotNull
        private APIStoreConfiguration.ApiDetail customerPromoCodeDetails;
        @NotNull
        private APIStoreConfiguration.ApiDetail skusInventoryCodDetails;
    }

    @Getter
    @Setter
    public static class DefaultHttpConfigs {
        @NotNull
        @Min(1)
        private int validateAfterInactivityTimeoutInMs;
        @NotNull
        private int keepAliveTimeoutInMs;
        @NotNull
        private int maxConnections;
        @NotNull
        private int maxConnectionPerRoute;
    }
 
    @Getter
    @Setter
    public static class ApiDetail {
        @NotEmpty
        @NotNull
        private String path;
        @NotNull
        private int connectTimeout;
        @NotNull
        private int readTimeout;
    }

}
