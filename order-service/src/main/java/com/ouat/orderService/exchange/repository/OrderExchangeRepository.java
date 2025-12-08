package com.ouat.orderService.exchange.repository;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ouat.orderService.interceptor.RequestHeaders;
import com.ouat.orderService.response.CustomerDetailVO;

@Repository
public class OrderExchangeRepository {

	private static final Logger log = LoggerFactory.getLogger(OrderExchangeRepository.class);

	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final String FETCH_ORDERED_SKU_QUERY = "Select sku from orders.order_item oi JOIN orders.order o ON (o.id=oi.order_id) JOIN orders.order_tracking_detail otd ON (otd.order_item_id=oi.id) where (oi.id= %s) and (oi.is_exchangable=1) AND (oi.order_status_id=5) AND (o.customer_id= %s) AND (otd.delivered_date<= DATE_ADD(convert_tz(NOW(), '+00:00','+5:30') , INTERVAL 7 DAY))";
	
	public String getSku(Long orderItemId, Long customerId) {
		return jdbcTemplate.queryForObject(String.format(FETCH_ORDERED_SKU_QUERY, orderItemId, customerId), String.class);		
	}
}
