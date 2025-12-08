package com.ouat.orderService.exchange.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ouat.orderService.exchange.dto.OrderCancelReturnRecordDto;
import com.ouat.orderService.exchange.dto.OrderDetailDto;
import com.ouat.orderService.exchange.dto.PlaceExchangeOrderDto;
import com.ouat.orderService.exchange.request.PlaceExchangeOrderRequest;

@Repository
public class PlaceExchangeOrderDetailRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public static final Logger LOGGER = LoggerFactory.getLogger(PlaceExchangeOrderDetailRepository.class);

	public OrderDetailDto exchangeOrderDetail(PlaceExchangeOrderRequest exchangeOrderRequest) {

		OrderDetailDto orderDetailDto = null;

		orderDetailDto = jdbcTemplate.queryForObject(SqlConstant.SELECT_QUERY_ORDER_DETAIL, new RowMapper<OrderDetailDto>() {
			@Override
			public OrderDetailDto mapRow(ResultSet rs, int rowNum) throws SQLException {
				OrderDetailDto exchangeOrderDetailDto = OrderDetailDto.builder().orderId(rs.getLong("ooi.order_id"))
						.customerId(rs.getLong("oo.customer_id")).orderStatusId(rs.getInt("oo.order_status_id"))
						.totalAmount(rs.getDouble("oo.total_amount")).orderPayable(rs.getDouble("oo.order_payable"))
						.platformOfferDiscount(rs.getDouble("oo.platform_offered_discount"))
						.promoDiscount(rs.getDouble("oo.promo_discount"))
						.creditApplied(rs.getDouble("oo.credit_applied"))
						.shippingCharges(rs.getDouble("oo.shipping_charges")).orderType(rs.getString("oo.order_type"))
						.paymentMethod(rs.getString("oo.payment_method")).promoCode(rs.getString("oo.promo_code"))
						.platform(rs.getString("oo.platform"))

						.orderItemId(rs.getLong("ooi.id")).sku(rs.getString("ooi.sku")).qty(rs.getInt("ooi.qty"))
						.orderItemTotalAmount(rs.getDouble("ooi.order_item_total_amount"))
						.orderItemPayable(rs.getDouble("ooi.order_item_payable"))
						.orderItemPlatformOfferedDiscount(rs.getDouble("ooi.order_item_platform_offered_discount"))
						.orderItemCreditApplied(rs.getDouble("ooi.order_item_credit_applied"))
						.orderItemPromoDiscount(rs.getDouble("ooi.order_item_promo_discount"))

						.vendorPrice(rs.getDouble("ooi.vendor_price")).isReturnable(rs.getBoolean("ooi.is_returnable"))
						.isExchangable(rs.getBoolean("ooi.is_exchangable"))
						.orderItemShippingCharges(rs.getDouble("ooi.shipping_charges"))
						.ouatMargin(rs.getDouble("ooi.ouat_margin")).addressId(rs.getInt("address_id")).build();
				return exchangeOrderDetailDto;
			}

		}, exchangeOrderRequest.getOrderItemId());
		LOGGER.info("orderDetailDto : {}", orderDetailDto);
		return orderDetailDto;

	}

//	public int insertIntoOrderTable(OrderDto exchangeOrderDto) {
//		KeyHolder holder = new GeneratedKeyHolder();
//		jdbcTemplate.update(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//				PreparedStatement ps = connection.prepareStatement(SqlConstant.INSERT_INTO_ORDER_TABLE,
//						Statement.RETURN_GENERATED_KEYS);
//				ps.setLong(1, exchangeOrderDto.getCustomerId());
//				ps.setInt(2, exchangeOrderDto.getOrderStatusId());
//				ps.setDouble(3, exchangeOrderDto.getTotalAmount());
//				ps.setDouble(4, exchangeOrderDto.getOrderPayable());
//				ps.setDouble(5, exchangeOrderDto.getPlatformOfferedDiscount());
//				ps.setDouble(6, exchangeOrderDto.getPromoDiscount());
//				ps.setDouble(7, exchangeOrderDto.getCreditApplied());
//				ps.setDouble(8, exchangeOrderDto.getShippingCharges());
//				ps.setString(9, exchangeOrderDto.getOrderType());
//				ps.setString(10, exchangeOrderDto.getPaymentMethod());
//				ps.setString(11, exchangeOrderDto.getPromocode());
//				ps.setString(12, exchangeOrderDto.getPlatform());
//				return ps;
//			}
//		}, holder);
//
//		int orderId = holder.getKey().intValue();
//		return orderId;
//	}

//	public int insertIntoOrderItemTable(OrderItemDTO orderItemDTO) {
//		KeyHolder holder = new GeneratedKeyHolder();
//		jdbcTemplate.update(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//				PreparedStatement ps = connection.prepareStatement(SqlConstant.INSERT_INTO_ORDER_ITEM_TABLE,
//						Statement.RETURN_GENERATED_KEYS);
//				ps.setLong(1, orderItemDTO.getOrderId());
//				ps.setString(2, orderItemDTO.getSku());
//				ps.setInt(3, orderItemDTO.getQty());
//				ps.setDouble(4, orderItemDTO.getOrderItemTotalAmount());
//				ps.setDouble(5, orderItemDTO.getOrderItemPayable());
//				ps.setDouble(6, orderItemDTO.getOrderItemPlatformOfferedDiscount());
//				ps.setDouble(7, orderItemDTO.getOrderItemPromoDiscount());
//				ps.setDouble(8, orderItemDTO.getOrderItemCreditApplied());
//				ps.setDouble(9, orderItemDTO.getVendorPrice());
//				ps.setDouble(10, orderItemDTO.getOuatPrice());
//				ps.setInt(11, orderItemDTO.getOrderStatusId());
//				ps.setString(12, orderItemDTO.getOrderCreatedBy());
//				ps.setString(13, orderItemDTO.getUpdatedBy());
//				ps.setBoolean(14, orderItemDTO.getIsReturnable());
//				ps.setBoolean(15, orderItemDTO.getIsExchangable());
//				ps.setDate(16, orderItemDTO.getEstimatedShippedDate());
//				ps.setDate(17, orderItemDTO.getEstimatedDeliveryDate());
//				ps.setDouble(18, orderItemDTO.getOrderItemShippingCharges());
//				return ps;
//			}
//
//		}, holder);
//		int orderItemId = holder.getKey().intValue();
//		return orderItemId;
//
//	}

//	public int insertIntoOrderShippmentDetail(OrderShippmentDetailDTO exchangeOrderShippmentDetailDTO) {
//		KeyHolder holder = new GeneratedKeyHolder();
//		jdbcTemplate.update(new PreparedStatementCreator() {
//			@Override
//			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//				PreparedStatement ps = connection.prepareStatement(SqlConstant.INSERT_INTO_ORDER_SHIPMENT_TABLE,
//						Statement.RETURN_GENERATED_KEYS);
//				ps.setLong(1, exchangeOrderShippmentDetailDTO.getOrderId());
//				ps.setInt(2, exchangeOrderShippmentDetailDTO.getAddressId());
//				return ps;
//			}
//		}, holder);
//		int orderShipmentId = holder.getKey().intValue();
//		return orderShipmentId;
//	}

	public int insertIntoOrderCancelRecord(OrderCancelReturnRecordDto orderCancelReturnRecordDto) {
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(SqlConstant.INSERT_INTO_ORDER_CANCEL_RETURN_RECORD,
						Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, orderCancelReturnRecordDto.getOrderId());
				ps.setLong(2, orderCancelReturnRecordDto.getOrderItemId());
				ps.setInt(3, orderCancelReturnRecordDto.getQty());
				ps.setInt(4, orderCancelReturnRecordDto.getReasonId());
				ps.setString(5, orderCancelReturnRecordDto.getCreatedBy());
				return ps;
			}

		}, holder);
		int orderCancelId = holder.getKey().intValue();
		return orderCancelId;
	}

	public int inserIntoExchangeOrderDetail(PlaceExchangeOrderDto exchangeOrderDto) {
		return jdbcTemplate.update(SqlConstant.INSERT_INTO_EXCHANGE_ORDER_DETAIL,  exchangeOrderDto.getOrderItemId(), exchangeOrderDto.getSku());
	}
	
	public int updateOrderItemStatus(Long orderItemId, Integer orderStatusId) {
		String query = "update orders.order_item set order_status_id = ?, updated_by = ?, updated_at = CONVERT_TZ(now(), '+00:00', '+05:30') where id = ?";
		return jdbcTemplate.update(query, orderStatusId, "order-service-cancel-api", orderItemId);
	}


	/*
	 * public SkuAndQtyRequest getInventory(PlaceExchangeOrderRequest
	 * exchangeOrderRequest) {
	 * 
	 * String query = String.format(SqlConstant.SELECT_QUERY_TO_CHECK_INVENTORY,
	 * exchangeOrderRequest); LOGGER.info("query : {}", query); return
	 * jdbcTemplate.queryForObject(query, new RowMapper<SkuAndQtyRequest>() { public
	 * SkuAndQtyRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
	 * SkuAndQtyRequest skuAndQtyRequest = new SkuAndQtyRequest();
	 * skuAndQtyRequest.setSku(rs.getString("pi.sku"));
	 * skuAndQtyRequest.setQty(rs.getInt("vi.inventory")); return skuAndQtyRequest;
	 * 
	 * } }, exchangeOrderRequest.getSku());
	 * 
	 * }
	 */}
