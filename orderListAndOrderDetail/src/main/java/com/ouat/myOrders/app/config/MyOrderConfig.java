package com.ouat.myOrders.app.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ouat.myOrders.interceptor.DemandStoreLoginInterceptor;

 
@Configuration
@PropertySource("classpath:/${property.environment}/application.properties")
public class MyOrderConfig implements WebMvcConfigurer {
	
	
	@Bean
    public DemandStoreLoginInterceptor loginInterceptor() {
        return new DemandStoreLoginInterceptor();
    }
	
	
	@Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
    }
	
	 @Bean
	 public RestTemplate restTemplate(RestTemplateBuilder builder) {
	    return builder.build();
	 }
	 
     @Bean
     public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
         PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer =
                 new PropertySourcesPlaceholderConfigurer();
         return propertySourcesPlaceholderConfigurer;
     }
}
