package com.ouat.wishlist.config;

import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ouat.wishlist.interceptor.DemandStoreLoginInterceptor;
import com.ouat.wishlist.interceptor.SessionRequiredInterceptor;

@Configuration
@PropertySource("classpath:/${property.environment}/application.properties")
public class AppConfig implements WebMvcConfigurer {

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}
	
	@Bean
    public DemandStoreLoginInterceptor loginInterceptor() {
        return new DemandStoreLoginInterceptor();
    }
	
	@Bean
    public SessionRequiredInterceptor sessionInterceptor() {
        return new SessionRequiredInterceptor();
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(sessionInterceptor()).addPathPatterns("/**");
    }

	private static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Bean
    public static Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat(dateTimeFormat);
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(dateTimeFormat)));
        };
    }
	
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder mapperBuilder) {
      return mapperBuilder.build().setSerializationInclusion(Include.NON_NULL);
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
       return propertySourcesPlaceholderConfigurer;
    }
}
