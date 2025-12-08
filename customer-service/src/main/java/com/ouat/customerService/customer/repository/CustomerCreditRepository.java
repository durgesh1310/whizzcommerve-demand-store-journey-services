package com.ouat.customerService.customer.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ouat.customerService.customer.request.CustomerCreditHistory;
import com.ouat.customerService.customer.request.TotalCreditUsedAndType;
import com.ouat.customerService.dto.CustomerCredit;

@Repository
public class CustomerCreditRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	JDBCTemplateWrapper jdbcTemplateWrapper;
	
	public Double creditByCustomerId(Integer customerId) {
		try {
			return jdbcTemplate.queryForObject(
					"SELECT amount FROM customers.customer_credit where customer_id = ?", Double.class,
					customerId);
		} catch (DataAccessException e) {
		}
		return 0.0;
	}
	
	
	public List<CustomerCredit> creditsByCustomerId(Integer customerId) {
		return jdbcTemplateWrapper.find(
				"SELECT amount, credit_type, expiry FROM customers.customer_credit where customer_id = ?",
				new RowMapper<CustomerCredit>() {
					@Override
					public CustomerCredit mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new CustomerCredit(rs.getDouble("amount"), rs.getString("credit_type"), rs.getDate("expiry"));
					}
				}, customerId);
	}

	public int[] addInHistory(CustomerCreditHistory customerCreditHistory) {
		String query = "insert into customers.customer_credit_history (customer_id, order_id, order_item_id, credit_amount, action, remark, created_by, credit_type) values (?, ?, ?, ?, ?, ?, ?, ?)";
		return jdbcTemplate.batchUpdate(query, 
		 new BatchPreparedStatementSetter() {
			public int getBatchSize() {
				return customerCreditHistory.getOrderItemCreditDetail().size();
			}
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setLong(1, customerCreditHistory.getCustomerId());
				ps.setLong(2, customerCreditHistory.getOrderId());
				ps.setLong(3, customerCreditHistory.getOrderItemCreditDetail().get(i).getOrderItemId());
				ps.setDouble(4, customerCreditHistory.getOrderItemCreditDetail().get(i).getAmount());
				ps.setString(5, "REMOVED");
				ps.setString(6, String.format("Credits %s spend on %s order number", customerCreditHistory.getOrderItemCreditDetail().get(i).getAmount(), customerCreditHistory.getOrderId()));
				ps.setString(7, "customer-service");
				ps.setString(8,  customerCreditHistory.getType());

			}
		});
	}

    
	public int updateCustomerCredit(CustomerCreditHistory customerCreditHistory,  String remark) {
		Long customerId = customerCreditHistory.getCustomerId();
			if(customerCreditHistory.getType().equals("M")) {
				jdbcTemplateWrapper.execute("update customers.customer_credit set amount = amount - ?, remark = ? where customer_id = ?  AND credit_type= ?", customerCreditHistory.getTotalCreditUsed(), remark, customerId, customerCreditHistory.getType());

			}else if (customerCreditHistory.getType().equals("L")) {
				jdbcTemplateWrapper.execute("update customers.customer_credit set amount = amount - ?, remark = ? where customer_id = ? AND credit_type= ?", customerCreditHistory.getTotalCreditUsed(), remark, customerId, customerCreditHistory.getType());
			}
		return 1;
	}
	
	public int insertIntoCustomerCredit(Long customerId, double credit, String remark, String creditType) {
		return jdbcTemplateWrapper.execute("insert into customers.customer_credit (customer_id , amount, credit_type, remark, created_by, created_at) values (?, ?, ?, ?, ?, convert_tz(now(),'+00:00','+05:30')) ON DUPLICATE KEY UPDATE  amount = amount + ?, updated_by = ?, updated_at = convert_tz(now(),'+00:00','+05:30')", customerId, credit, creditType, remark, "customer-service", credit, "customer-service");
	}
	
	public int addInHistorySingle(Long customerId, int orderId, int orderItemId, Double credit, String action, String remark) {
		String query = "insert into customers.customer_credit_history (customer_id, order_id, order_item_id, credit_amount, action, remark, created_by, created_at) values (?, ?, ?, ?, ?, ?, ?, convert_tz(now(),'+00:00','+05:30'))";
		return jdbcTemplate.update(query, customerId, orderId, orderItemId, credit, action, remark, "customer-service");
	}
	
	

}
