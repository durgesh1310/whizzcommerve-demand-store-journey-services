package com.ouat.checkout.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utility {

    private static final Logger log = LoggerFactory.getLogger(Utility.class);
    
    private static final char CARRIAGE_RETURN = '\r';
    private static final char LINE_FEED = '\n';
    private static final char SPACE = ' ';

    public static String generateTransactionId() {
        return UUID.randomUUID().toString();
    }

    public static HttpHeaders addHeadersForDownStreamCalls(Enumeration<String> requestHeaders,
            HttpServletRequest httpRequest) {
        HttpHeaders headers = new HttpHeaders();
        if (requestHeaders != null) {
            while (requestHeaders.hasMoreElements()) {
                String headerName = requestHeaders.nextElement();
                headers.add(headerName, convertNewlinesToSpaces(httpRequest.getHeader(headerName)));
            }
        }
        if (!MediaType.APPLICATION_JSON.equals(headers.getContentType())) {
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        return headers;
    }

    public static String convertNewlinesToSpaces(String input) {
        if (input == null) {
            return null;
        }
        String toReturn = input;
        toReturn = toReturn.replace(CARRIAGE_RETURN, SPACE);
        toReturn = toReturn.replace(LINE_FEED, SPACE);
        return toReturn;
    }
    
    public static String getJson(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error("Unable to parse the exception for data = {}", object);
        }
        return null;
    }
    
    public static <T> T convertJsonToObject(String source, Class<T> valueType) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(source, valueType);
        } catch (Exception ex) {
            log.error("error in parsing the source = {}", source);
        }
        return null;
    }
    
    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }
    
    public static Double mapToDouble(Double value) {
        return value == null ? 0.0 : value;
    }
    
    public static String convertToString(int value) {
        return String.valueOf(value);
    }
    
    /**
     * It will generate 6 digit random Number.
     * from 0 to 999999
     * @return String
     * **/
    public static String getSixDigitsRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
