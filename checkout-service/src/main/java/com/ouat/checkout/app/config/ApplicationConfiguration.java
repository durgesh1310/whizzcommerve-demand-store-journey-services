package com.ouat.checkout.app.config;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ouat.checkout.interceptor.DemandStoreLoginInterceptor;

@Configuration
@PropertySource("classpath:/${property.environment}/application.properties")
public class ApplicationConfiguration  implements WebMvcConfigurer {
	
    @Autowired
    ThreadPoolExecutorConfiguration threadPoolExecutorConfiguration;
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter());
        return restTemplate;
    }
    
    @Bean(name =  "razorPayRestTemplate")
    public RestTemplate razorPayRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        restTemplate.getMessageConverters().add(mappingJackson2HttpMessageConverter());
        return restTemplate;
    }
    
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(
                Arrays.asList(MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED));
        return mappingJackson2HttpMessageConverter;
    }
	
	@Bean
    public DemandStoreLoginInterceptor loginInterceptor() {
        return new DemandStoreLoginInterceptor();
    }
	
	@Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(loginInterceptor()).addPathPatterns("/**");
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
    
    @Bean("threadPoolExecutor")
    public ThreadPoolTaskExecutor getThreadPoolExecutor() {
        return getThreadPoolTaskExecutor(threadPoolExecutorConfiguration.getCorePoolSize(),
                threadPoolExecutorConfiguration.getMaxPoolSize(),
                threadPoolExecutorConfiguration.getQueueCapacity(),
                threadPoolExecutorConfiguration.getThreadNamePrefix());
    }

    private ThreadPoolTaskExecutor getThreadPoolTaskExecutor(int corePoolSize, int maxPoolSize,
                                                             int queueCapacity, String threadNamePrefix) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        executor.getThreadPoolExecutor().prestartAllCoreThreads();
        return executor;
    }
    
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
       return propertySourcesPlaceholderConfigurer;
    }    

}
