package com.ouat.suggestionandsearch.util;

import java.util.List;
import java.util.UUID;

import org.apache.commons.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {

	private static final Logger log = LoggerFactory.getLogger(Utility.class);

	public static String getTrackingId() {
		return UUID.randomUUID().toString();
	}

	public static String toLowerCase(String object) {
		return object == null ? null : object.toLowerCase();
	}

	@Bean
	public static <T> T convertJsonToObject(String source, Class<T> valueType) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			return mapper.readValue(source, valueType);
		} catch (Exception ex) {
			log.info("error in parsing the source = {}", source);
		}
		return null;
	}
	
	public static String getJson(Object object) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	public static String[] convertArrayIntoSeprateStrings(final List<String> lst) {
		return lst.stream().toArray(String[]::new);
	}

	public static String toCamelCase(String string) {
		return StringUtils.hasText(string) == true ? WordUtils.capitalizeFully(string, ' ', '_') : null;
	}

}
