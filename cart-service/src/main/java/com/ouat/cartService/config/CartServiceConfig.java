package com.ouat.cartService.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ouat.cartService.interceptor.DemandStoreLoginInterceptor;
import com.ouat.cartService.interceptor.SessionRequiredInterceptor;
@Configuration
@PropertySource("classpath:/${property.environment}/application.properties")
public class CartServiceConfig implements WebMvcConfigurer {
	
	
	@Bean
    public DemandStoreLoginInterceptor loginInterceptor() {
        return new DemandStoreLoginInterceptor();
    }
	
	@Bean
    public SessionRequiredInterceptor sessionRequired() {
        return new SessionRequiredInterceptor();
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(sessionRequired()).addPathPatterns("/**");
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
