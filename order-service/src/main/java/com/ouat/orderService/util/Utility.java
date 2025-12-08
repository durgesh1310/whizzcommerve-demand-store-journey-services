package com.ouat.orderService.util;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {

	private static final Logger log = LoggerFactory.getLogger(Utility.class);

	public static String getJson(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	public static String prepareStringFromListOfIntegersForInClause(List<Integer> list) {
		return list.stream().map(String::valueOf).collect(Collectors.joining(","));
	}

	public static String prepareStringFromListOfStringsForInClause(List<String> list) {
		return list.stream().collect(Collectors.joining("','", "'", "'"));
	}

	 @Bean
	    public static <T> T convertJsonToObject(String source, Class<T> valueType) {
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            return mapper.readValue(source, valueType);
	        } catch (Exception ex) {
	            log.info("error in parsing the source = {}", ExceptionUtils.getStackTrace(ex));
	        }
	        return null;
	    }

}