package com.ouat.orderService.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ouat.orderService.client.AllocateShipperResponse;
import com.ouat.orderService.constants.SqlConstant;
import com.ouat.orderService.dto.OrderItemDetailsDto;
import com.ouat.orderService.repository.dto.AwbAndCourierPartner;


@Repository
public class OrderRepository {
	private static Logger LOGGER = LoggerFactory.getLogger(OrderRepository.class);
	@Autowired
	ObjectMapper mapper ;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static String GET_LABELS_DETAIL = "SELECT ooi.id as order_item_id, ooi.sku, sav.sku_attribute_value as size,ooi.qty, \n"
			+ "if(ppi.sale_end_date>CONVERT_TZ(now(), '+00:00', '+05:30'), ppi.sale_price, ppi.retail_price ) as selling_price,\n"
			+ "ppi.regular_price, ooi.order_item_total_amount, ooi.order_item_payable, ooi.order_item_platform_offered_discount, ooi.order_item_promo_discount, ooi.shipping_charges,\n"
			+ "ooi.order_item_credit_applied,  ooi.created_at as order_date,oo.payment_method, pp.name, ppi.vendor_sku, svc.name as vendor_name,sva.address_line_1 as vendor_address_Line_1, sva.address_line_1 as vendor_address_Line_2 , sva.city as vendor_city,\n"
			+ "sva.pincode as vendor_pincode, sva.state as vendor_state, sva.country as vendor_country, sva.address_type as vendor_address_type, sva.vendor_id, ca.house_owner_name as customer_name , ca.address as customer_address, ca.pincode as customer_pincode , ca.landmark as customer_landmark,\n"
			+ "ca.city as customer_city, ca.state as customer_state, ca.mobile_number as customer_mobile , awbmd.awb , awbmd.courrier_partner,awbmd.routing_code\n"
			+ "FROM orders.order_item ooi JOIN products.product_item ppi ON (ppi.sku = ooi.sku) JOIN products.product pp ON (pp.id = ppi.product_id) \n"
			+ "JOIN supplychain.vendor_address sva ON (sva.vendor_id = pp.vendor_id) LEFT JOIN products.sku_attribute_value sav ON (sav.sku = ooi.sku AND product_attribute_id = 319)\n"
			+ "LEFT JOIN supplychain.vendor_contact svc ON (svc.id = pp.vendor_id) LEFT JOIN orders.awb_meta_data awbmd ON (awbmd.order_item_id = ooi.id) JOIN\n"
			+ "orders.order oo ON (oo.id = ooi.order_id) JOIN orders.order_shipment_detail osd ON (osd.order_id=oo.id) \n"
			+ "JOIN customers.customer_addresses ca ON (osd.address_id = ca.id ) where ooi.id IN (%s) AND sva.address_type = 'SHIPPING_ADDRESS'";
	
	public GetLabelDto getOrderDetail(String orderItemId) {
		String query = String.format(GET_LABELS_DETAIL, orderItemId);
	    LOGGER.info("query : {}",  query);
		GetLabelDto labelDto = null;
			labelDto =  jdbcTemplate.queryForObject(query,  new RowMapper<GetLabelDto>(){
				public GetLabelDto mapRow(ResultSet rs, int rowNum) throws SQLException{
					GetLabelDto orderDetail =  GetLabelDto.builder()
							.orderItemId(rs.getLong("order_item_id"))
							.sku(rs.getString("ooi.sku"))
							.size(rs.getString("size"))
							.qty(rs.getInt("ooi.qty"))
							.sellingPrice(rs.getDouble("selling_price"))
							.regularPrice(rs.getDouble("ppi.regular_price"))
							.orderItemTotal(rs.getDouble("ooi.order_item_total_amount"))
							.orderItemPayable(rs.getDouble("ooi.order_item_payable"))
							.orderItemPlatformOfferedDiscount(rs.getDouble("ooi.order_item_platform_offered_discount"))
							.orderItemPromoDiscount(rs.getDouble("ooi.order_item_promo_discount"))
							.orderItemShippingCharges(rs.getDouble("ooi.shipping_charges"))
							.orderItemCreditApplied(rs.getDouble("ooi.order_item_credit_applied"))
						    .orderDate(rs.getDate("order_date"))
						    .paymentMethod(rs.getString("oo.payment_method"))
						    .productName(rs.getString("pp.name"))
						    .vendorName(rs.getString("vendor_name"))
						    .vendorSku(rs.getString("ppi.vendor_sku"))
						    .vendorAddressLine1(rs.getString("vendor_address_Line_1"))
						    .vendorAddressLine2(rs.getString("vendor_address_Line_2"))
						    .vendorCity(rs.getString("vendor_city"))
						    .vendorPincode(rs.getInt("vendor_pincode"))
						    .vendorState(rs.getString("vendor_state"))
						    .vendorCoutry(rs.getString("vendor_country"))
						    .vendorAddressType(rs.getString("vendor_address_type"))
						    .vendorId(rs.getInt("sva.vendor_id"))
						    .customerName(rs.getString("customer_name"))
						    .customerAddress(rs.getString("customer_address"))
						    .customerPincode(rs.getInt("customer_pincode"))
						    .customerLandMark(rs.getString("customer_landmark"))
						    .customerCity(rs.getString("customer_city"))
						    .customerState(rs.getString("customer_state"))
						    .customerMobile(rs.getString("customer_mobile"))
						    .awb(rs.getString("awbmd.awb"))
						    .courierPartner(rs.getString("awbmd.courrier_partner"))
						    .routingCode(rs.getString("awbmd.routing_code"))
							.build();
			    	return orderDetail;
			    }
			});	
		
		 LOGGER.info("labelDto : {}",labelDto);
		return labelDto;
	}
	
	
	
	public OrderItemDetailsDto queryForOrderItemsDetails(String orderItemIds) {
		String query = String.format(SqlConstant.ORDER_ITEM_SHIPMENT_DETAILS_QUERY, orderItemIds);
		return jdbcTemplate.queryForObject(query,
				new RowMapper<OrderItemDetailsDto>() {
					@Override
					public OrderItemDetailsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
						OrderItemDetailsDto orderItemDetailsDto = new OrderItemDetailsDto();
						orderItemDetailsDto.setOrderId(rs.getString("order_id"));
						orderItemDetailsDto.setItemName(rs.getString("item_name"));
						orderItemDetailsDto.setClientOrderId(rs.getString("order_id"));
						orderItemDetailsDto.setInvoiceNumber(rs.getString("order_id"));
						orderItemDetailsDto.setQuantity(rs.getInt("quantity"));
						orderItemDetailsDto.setInvoiceValue(rs.getDouble("invoice_value"));
						orderItemDetailsDto.setCodAmount(rs.getDouble("cod_amount"));
						orderItemDetailsDto.setTotalDiscount(rs.getDouble("total_discount"));
						orderItemDetailsDto.setShippingCharge(rs.getDouble("shipping_charge"));
						orderItemDetailsDto.setPaymentMethod(rs.getString("payment_method"));
						orderItemDetailsDto.setPrice(rs.getDouble("price"));
						orderItemDetailsDto.setItemName(rs.getString("item_name"));
						orderItemDetailsDto.setQuantity(rs.getInt("quantity"));
						orderItemDetailsDto.setSku(rs.getString("sku"));
						orderItemDetailsDto.setFromName(rs.getString("from_name"));
						orderItemDetailsDto.setFromPhoneNumber(rs.getString("from_phone_number"));
						orderItemDetailsDto.setFromAddress(rs.getString("from_address"));
						orderItemDetailsDto.setFromPincode(rs.getString("from_pincode"));
						orderItemDetailsDto.setPickupGstin(rs.getString("pickup_gstin"));
						orderItemDetailsDto.setToName(rs.getString("to_name"));
						orderItemDetailsDto.setToEmail(rs.getString("to_email"));
						orderItemDetailsDto.setToPhoneNumber(rs.getString("to_phone_number"));
						orderItemDetailsDto.setToPincode(rs.getString("to_pincode"));
						orderItemDetailsDto.setToAddress(rs.getString("to_address"));
						return orderItemDetailsDto;

					}

				});
	}

	public int updateBestShipperOrderStatus(AllocateShipperResponse response) {
		LOGGER.info("updateBestShipperOrderStatus : {} ", response.toString());
		jdbcTemplate.update(SqlConstant.UPSERT_STATUS_OF_ORDER_SHIPMENT_DETAILS, 
				(null == response.getClientOrderId()) ? "" : response.getClientOrderId(),
				(null == response.getTrackingId()) ? "" : response.getTrackingId(),
				(null == response.getCourier()) ? "" : response.getCourier(), 
				(null == response.getRoutingCode()) ? "" : response.getRoutingCode(),
				(null == response.getTrackingId()) ? "" : response.getTrackingId(), 
				(null == response.getCourier()) ? "" : response.getCourier(),
				(null == response.getRoutingCode()) ? "" : response.getRoutingCode());
		return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
	}
	
	public int updateShipmentDetailStatus (AllocateShipperResponse response) {
		LOGGER.info("updateShipmentDetailStatus : {} ", response.toString());
		jdbcTemplate.update(SqlConstant.UPSERT_STATUS_OF_ORDER_SHIPMENT_QUERY, 
				(null == response.getClientOrderId()) ? "" : response.getClientOrderId(),
				(null == response.getTrackingId()) ? "" : response.getTrackingId(), 
				(null == response.getCourier()) ? "" :  response.getCourier(),
				(null == response.getTrackingId()) ? "" : response.getTrackingId(),
				(null == response.getCourier())? "" : response.getCourier());
		return jdbcTemplate.queryForObject("select last_insert_id()", Integer.class);
	}
	
	public int updateForOrderItem(String orderItemIds) {
		return jdbcTemplate.update(SqlConstant.UPDATE_STATUS_OF_ORDER_ITEMS_QUERY,orderItemIds);
	}
  
	private String AWB_COURIER_PARTNER = "SELECT awb, courrier_partner FROM orders.awb_meta_data WHERE order_item_id IN ( %s );";

	public AwbAndCourierPartner getAwbAndCourierCode(Long orderItemId) throws JsonMappingException, JsonProcessingException {
		String value = jdbcTemplate.queryForObject(AWB_COURIER_PARTNER, String.class);
		LOGGER.info(value);
		AwbAndCourierPartner ss = mapper.readValue(value, AwbAndCourierPartner.class);
		return ss;
		// TODO Auto-generated method stub
		
	}
	
	public AwbAndCourierPartner getAwbAndCourierCode(String orderItemId) {
		String query = String.format(AWB_COURIER_PARTNER, orderItemId);
	    LOGGER.info("query : {}",  query);
		AwbAndCourierPartner awb = null;
		try {
			awb =  jdbcTemplate.queryForObject(query,  new RowMapper<AwbAndCourierPartner>(){
				public AwbAndCourierPartner mapRow(ResultSet rs, int rowNum) throws SQLException{
					AwbAndCourierPartner orderDetail =  AwbAndCourierPartner.builder()
							.awb(rs.getString("awb"))
							.courier(rs.getString("courrier_partner"))
							 .build();
			    	return orderDetail;
			    }
			});	
		}catch (Exception e) {
			LOGGER.info(e.getMessage()
	    );	
		} 
		
		LOGGER.info("labelDto : {}",awb);
		return awb;
		
		
		 
	}
	

}
