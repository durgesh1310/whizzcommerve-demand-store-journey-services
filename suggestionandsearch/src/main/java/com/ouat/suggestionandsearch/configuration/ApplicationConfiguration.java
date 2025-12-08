package com.ouat.suggestionandsearch.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.ouat.suggestionandsearch.interceptor.SessionRequiredInterceptor;

@Configuration
@PropertySource("classpath:/${property.environment}/application.properties")
public class ApplicationConfiguration implements WebMvcConfigurer {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
       return propertySourcesPlaceholderConfigurer;
    }
    
    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    public SessionRequiredInterceptor sessionInterceptor() {
        return new SessionRequiredInterceptor();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(sessionInterceptor()).addPathPatterns("/**");
    }
    
}
