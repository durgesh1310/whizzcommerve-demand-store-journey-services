package com.ouat.orderService.returnExchangeCancelAlert.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReturnExchangeCancelAlertRepository {
	
 
	private String ORDER_ALERT_ATTRIBUTE = "SELECT  pp.name,   ooi.sku, o.order_number, ooi.order_id, ooi.qty,    oocrc.record_type,    oocrr.reason,   pid.image_url FROM  orders.order_item ooi  join orders.order o on o.id=ooi.order_id  JOIN "
			+ "    orders.order_cancel_return_record oocrc ON (oocrc.order_item_id = ooi.id)   JOIN   orders.order_cancel_return_reason oocrr ON (oocrr.id = oocrc.reason_id)     JOIN "
			+ "    products.product_item ppi ON (ppi.sku = ooi.sku)  JOIN  products.product pp ON (pp.id = ppi.product_id) "
			+ "    JOIN products.product_img ppimg ON (ppimg.product_id = ppi.product_id)  JOIN  products.image_dictionary pid ON (ppimg.image_id = pid.image_id) "
			+ "WHERE  ooi.sku = ?  AND ooi.id = ? AND oocrc.record_type=? AND ppimg.is_default=1 group by ooi.sku;";
 
	@Autowired
	JdbcTemplate jdbcTemplate;
 
	RowMapper<ReturnExchangeCancelAlertDto> getorderAlertAttribute = (rs, rowNum) -> {
		return new ReturnExchangeCancelAlertDto(
				rs.getString("pp.name"),
    			rs.getString("ooi.sku"),
    			rs.getString("pid.image_url"),
    			rs.getInt("ooi.qty"),
    			rs.getString("oocrr.reason"),
		RecordType.valueOf(rs.getString("record_type")),
				rs.getString("order_number"));
 	};
	public ReturnExchangeCancelAlertDto  getAlertAttribute(String sku, Long orderItemId, String recordType) {
		return jdbcTemplate.queryForObject(ORDER_ALERT_ATTRIBUTE,getorderAlertAttribute,  sku, orderItemId, recordType);
 	}

}
