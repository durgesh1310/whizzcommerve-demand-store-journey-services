package com.ouat.suggestionandsearch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsUtility {
	
	private static final Logger log = LoggerFactory.getLogger(MetricsUtility.class);
	
	public static void addDownStreamMetrics(String downStreamName, Double totalTime, String requestType,
			String trackingId) {
		log.info("DownStreamName = {}, TOTAL_LATENCY = {}, RequestType = {}, TrackingId = {}", downStreamName,
				totalTime, requestType, trackingId);
	}
	
	public static void addMethodMetrics(String serviceName, Double totalTime, String requestType, String trackingId) {
		log.info("MethodName = {}, TOTAL_LATENCY = {}, RequestType = {}, TrackingId = {}", serviceName,
				totalTime, requestType, trackingId);
	}
	
}
