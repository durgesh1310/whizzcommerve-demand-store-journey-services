package com.ouat.notificationsender.config;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@PropertySource("classpath:/${property.environment}/application.properties")
public class NotificationSenderConfig {
	
	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder mapperBuilder) {
      return mapperBuilder.build().setSerializationInclusion(Include.NON_NULL);
    }
    
    @Bean
	VelocityEngine velocityEngine() {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		velocityEngine.init();
		return velocityEngine;
	}
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
       return propertySourcesPlaceholderConfigurer;
    }
}
