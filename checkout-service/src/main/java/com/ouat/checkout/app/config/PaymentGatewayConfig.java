package com.ouat.checkout.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import lombok.Getter;
import lombok.Setter;

@Configuration
@PropertySource(value = "classpath:/${property.environment}/paymentgateway.properties")
@ConfigurationProperties(prefix = "payment")
@Getter
@Setter
public class PaymentGatewayConfig {
    private PaymentGatewayDetail razorPay;
    
    @Getter
    @Setter
    public static class PaymentGatewayDetail{
        private String url;
        private String secretKey;
        private String accessKey;
        private String customer;
    }
}


