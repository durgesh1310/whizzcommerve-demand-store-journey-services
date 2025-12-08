package com.ouat.checkout.placeOrder.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.ouat.checkout.placeOrder.DTO.OrderDTO;
import com.ouat.checkout.placeOrder.DTO.OrderItemDTO;
import com.ouat.checkout.placeOrder.DTO.OrderShippmentDetailDTO;
import com.ouat.checkout.placeOrder.client.OrderItemCreditDetail;
import com.ouat.checkout.placeOrder.commonConstant.SqlConstant;
import com.ouat.checkout.placeOrder.request.PaymentGatewayDetailRequest;

@Repository
public class PlaceOrderRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PlaceOrderRepository.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
    public int insertIntoOrderTable(OrderDTO orderDTO) {
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
                        SqlConstant.INSERT_INTO_ORDER_TABLE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, orderDTO.getOrderNumber());
                ps.setInt(2, orderDTO.getCustomerId());
                ps.setInt(3, orderDTO.getOrderStatusId());
                ps.setDouble(4, Math.round(orderDTO.getTotalAmount()));
                ps.setDouble(5, orderDTO.getOrderPayable() == null ? 0.0
                        : Math.round(orderDTO.getOrderPayable()));
                ps.setDouble(6, orderDTO.getPlatformOfferedDiscount() == null ? 0.0
                        : Math.round(orderDTO.getPlatformOfferedDiscount()));
                ps.setDouble(7, orderDTO.getPromoDiscount() == null ? 0.0
                        : Math.round(orderDTO.getPromoDiscount()));
                ps.setDouble(8, orderDTO.getCreditApplied() == null ? 0.0
                        : Math.round(orderDTO.getCreditApplied()));
                ps.setDouble(9, orderDTO.getShippingCharges() == null ? 0.0
                        : Math.round(orderDTO.getShippingCharges()));
                ps.setString(10, orderDTO.getOrderType());
                ps.setString(11, orderDTO.getPaymentMethod());
                ps.setString(12, orderDTO.getPromocode());
                ps.setString(13, orderDTO.getPlatform().toString() == null ? ""
                        : orderDTO.getPlatform().toString());
                ps.setString(14, orderDTO.getUtmSource());
                ps.setString(15, orderDTO.getUtmCampaign());
                ps.setString(16, orderDTO.getUtmMedium());
                return ps;
            }
        }, holder);

        int orderId = holder.getKey().intValue();
        return orderId;
    }
    
	public int insertIntoOrderItemTable(List<OrderItemDTO> orderItemDTOList) {
 			  int size = 0;
				      size =  jdbcTemplate.batchUpdate(SqlConstant.INSERT_INTRO_ORDER_ITEM_TABLE, new BatchPreparedStatementSetter() {
				         public void setValues(PreparedStatement ps, int i) throws SQLException {
				            ps.setInt(1, orderItemDTOList.get(i).getOrderId());
				            ps.setString(2, orderItemDTOList.get(i).getSku());
				            ps.setInt(3, orderItemDTOList.get(i).getQty());
				            ps.setDouble(4, Math.round(orderItemDTOList.get(i).getOrderItemTotalAmount()));
				            ps.setDouble(5, Math.round(orderItemDTOList.get(i).getOrderItemPayable()));
				            ps.setDouble(6,Math.round( orderItemDTOList.get(i).getOrderItemPlatformOfferedDiscount()));
				            ps.setDouble(7,Math.round( orderItemDTOList.get(i).getOrderItemPromoDiscount()));
				            ps.setDouble(8, Math.round(orderItemDTOList.get(i).getOrderItemCreditApplied()));
				            ps.setDouble(9, orderItemDTOList.get(i).getVendorPrice());
				            ps.setDouble(10, orderItemDTOList.get(i).getOuatPrice());
				            ps.setInt(11, orderItemDTOList.get(i).getOrderStatusId());
 				            ps.setString(12, orderItemDTOList.get(i).getOrderCreatedBy());
 				            ps.setString(13, orderItemDTOList.get(i).getUpdatedBy());
				            ps.setBoolean(14, orderItemDTOList.get(i).getIsReturnable());
				            ps.setBoolean(15, orderItemDTOList.get(i).getIsExchangable());
				            ps.setDate(16, orderItemDTOList.get(i).getEstimatedShippedDate());
				            ps.setDate(17, orderItemDTOList.get(i).getEstimatedDeliveryDate());
				            ps.setDouble(18, Math.round(orderItemDTOList.get(i).getOrderItemShippingCharges()));
				      //    ps.setInt(16, orderItemDTOList.get(i).getParentOrderItemId()); 
				         }
				         public int getBatchSize() {
				            return orderItemDTOList.size();
				         }
				      }).length;
					return size;
		}
		public int insertIntoOrderShippmentDetail(OrderShippmentDetailDTO orderShippmentDetailDTO) {
 		        KeyHolder holder = new GeneratedKeyHolder();
		        jdbcTemplate.update(new PreparedStatementCreator(){
		            @Override
		            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		                PreparedStatement ps = connection.prepareStatement( SqlConstant.INSERT_INTO_ORDER_SHIPMENT_TABLE, Statement.RETURN_GENERATED_KEYS);
		                ps.setInt(1, orderShippmentDetailDTO.getOrderId());
		                ps.setInt(2, orderShippmentDetailDTO.getAddressId());
		                return ps;
		            }
		        }, holder);
		        int  orderShipmentId = holder.getKey().intValue();
		        return orderShipmentId;
		}
		/**
		 * Method takes orderId and orderStatus and then change status of all orderItem
		 * @return 
		 */
		public int orderStatus(Integer orderId, Integer orderStatusId, Integer customerId) {
			String query = "update orders.order_item as oi JOIN orders.order o ON (o.id = oi.order_id) set oi.order_status_id = ?, oi.updated_at = CONVERT_TZ(now(), '+00:00', '+05:30'), o.order_status_id = ?, oi.updated_by='order-status-api'  where oi.order_id = ? AND o.customer_id = ?";
			return jdbcTemplate.update(query, orderStatusId, orderStatusId, orderId, customerId);
		}  
	 
		
		/**
		 * Method takes orderId and orderStatus and then change status of all orderItem
		 * @return 
		 */
		public int orderStatusV1(Integer orderId, Integer orderStatusId, Integer customerId) {
			String query = "update orders.order_item as ooi JOIN orders.order oo ON (oo.id = ooi.order_id) set oo.order_status_id = ?, "
					+ "ooi.updated_at = CONVERT_TZ(now(), '+00:00', '+05:30'), ooi.order_status_id = ?,  "
					+ "ooi.updated_by='order-status-api'  where ooi.order_id = ? AND oo.customer_id = ? "
					+ "AND oo.order_status_id = 1 AND payment_method = 'ONLINE';";
			return jdbcTemplate.update(query, orderStatusId, orderStatusId, orderId, customerId);
		}  
	 
		public Integer getCustomer(Integer orderId) {
			String query = "SELECT customer_id FROM orders.order where id = ?";
			try {
				return jdbcTemplate.queryForObject(query,  Integer.class, orderId);
			}catch(DataAccessException e) {
				LOGGER.info("exception occure while getting customer : {}",  e.getMessage());
			}
			return null;
		}  
	 
		
		
		
		RowMapper<OrderItemCreditDetail> orderItemDetailMaper = (rs, rowNum) -> {
			return new OrderItemCreditDetail(rs.getLong("id"), rs.getDouble("order_item_credit_applied"));
		};
		public  List<OrderItemCreditDetail> getOrderItemDetail(List<String >skuList, Integer orderId) {
			String query= "SELECT id,order_item_credit_applied FROM orders.order_item WHERE order_id = ? AND sku IN(%s)";
			String skus = skuList.stream().collect(Collectors.joining("','"));
			skus = "'" + skus + "'";
 			List<OrderItemCreditDetail> orderItemCreditDetail = new ArrayList<>();
			try {
				orderItemCreditDetail = jdbcTemplate.query(String.format(query, skus), orderItemDetailMaper, orderId);
			} catch (Exception e) {
				LOGGER.error("Error while fetching order items : {} ", e.getMessage(), e);
			}
 
			return orderItemCreditDetail;
		}

		public void savePaymentGatewayResponse(Integer orderId, PaymentGatewayDetailRequest paymentGatewayDetailRequest) {
			String query = "insert into orders.order_payment_gateway_detail (order_id, pg_payment_id, pg_order_id, pg_signature, created_by, created_at) values (?, ?, ?, ?, 'order-status-api', CONVERT_TZ(now(), '+00:00', '+05:30'))";
			jdbcTemplate.update(query, orderId, paymentGatewayDetailRequest.getRazorpayPaymentId(), paymentGatewayDetailRequest.getRazorpayOrderId(), paymentGatewayDetailRequest.getRazorpaySignature());
		}

        public Integer fetchOrderIdFromOrderNumber(String orderNumber) {
            String query =
                    new StringBuilder().append("select id from orders.order where order_number=")
                            .append(orderNumber).toString();
            return jdbcTemplate.queryForObject(query, Integer.class);
        }
        
        public String fetchOrderNumberFromOrderId(Integer orderId) {
            String query =
                    new StringBuilder().append("select order_number from orders.order where id=")
                            .append(orderId).toString();
            return jdbcTemplate.queryForObject(query, String.class);
        }
        
		public void savePaymentGatewayResponse(Long taggdId,  String razorpayId, String transactionId) {
			String query = "insert into orders.order_payment_gateway_customer (customer_id, razorpay_customer_id, transaction_id, created_by, created_at) values (?, ?, ?, 'payment-gateway-customer', CONVERT_TZ(now(), '+00:00', '+05:30'))";
			jdbcTemplate.update(query, taggdId, razorpayId, transactionId);
		}

		public String fetchRazorpayCustomerId(Long customerId) {
			String razorpayCustomerId = null;
			String query = "SELECT razorpay_customer_id FROM orders.order_payment_gateway_customer WHERE customer_id = ?";
			try {
				razorpayCustomerId = jdbcTemplate.queryForObject(query, String.class, customerId);
			} catch (DataAccessException e) {

			}

			return razorpayCustomerId;
		}
		
		public String fetchUnattemptedRazorpayOrders(Integer orderId) {
			try {
				return jdbcTemplate.queryForObject(
						"Select opg.pg_order_id FROM orders.order_payment_gateway_detail opg JOIN orders.order o ON (opg.order_id=o.id) where o.order_status_id IN (1,2) AND opg.re_verified<1 AND opg.pg_order_id != '' AND  o.id = ?",
						String.class, orderId);
			} catch (DataAccessException e) {
				// TODO: handle exception
			}
			return null;
		}
		
		public String fetchRazorpayOrders(String pgOrderId) {
			try {
				return jdbcTemplate.queryForObject(
						"Select order_id from orders.order_payment_gateway_detail where pg_order_id=? limit 1",
						String.class, pgOrderId);
			} catch (DataAccessException e) {
				// TODO: handle exception
			}
			return null;
		}
     
}

