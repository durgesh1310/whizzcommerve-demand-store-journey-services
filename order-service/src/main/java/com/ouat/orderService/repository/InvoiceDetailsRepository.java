package com.ouat.orderService.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ouat.orderService.request.OrderItemsDetail;

@Repository
public class InvoiceDetailsRepository {
    
	@Autowired
    private JdbcTemplate jdbcTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(InvoiceDetailsRepository.class);
    
 
  	public static String INSERT_INTO_ORDER_TABLE = "INSERT INTO orders.invoice_detail(order_item_id, invoice_number,  created_by, created_at, updated_by, updated_at) VALUES (?,?,?, Convert_tz(Now(), '+00:00', '+05:30'),?, Convert_tz(Now(), '+00:00', '+05:30'))"
  			+ " ON DUPLICATE KEY UPDATE order_item_id = ? ";
    
    public int insertIntoOrderInvoiceDetail(List<OrderItemsDetail> orderItemDTOList) {
		  int size = 0;
			      size =  jdbcTemplate.batchUpdate(INSERT_INTO_ORDER_TABLE, new BatchPreparedStatementSetter() {
			         public void setValues(PreparedStatement ps, int i) throws SQLException {
			        	ps.setString(1,  orderItemDTOList.get(i).orderItemId);
			            ps.setString(2, orderItemDTOList.get(i).invoiceNumber);
			            ps.setString(3, "uniware");
			            ps.setString(4, "uniware");
			            ps.setString(5, orderItemDTOList.get(i).orderItemId );
			         }
			         public int getBatchSize() {
			            return orderItemDTOList.size();
			         }
			      }).length;
				return size;
	}
}   
