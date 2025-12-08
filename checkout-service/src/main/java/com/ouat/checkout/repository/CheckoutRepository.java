package com.ouat.checkout.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ouat.checkout.controller.CheckoutController;
import com.ouat.checkout.placeOrder.DTO.ShippingChargesDetailDTO;
import com.ouat.checkout.sql.CheckoutSQL;

@Repository
public class CheckoutRepository {
	
    private static final Logger log = LoggerFactory.getLogger(CheckoutController.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Integer queryForFindingLastShippedAddress(Integer customerId) {
        List<Integer> result = jdbcTemplate.query(CheckoutSQL.LAST_SHIPPED_ADDRESS_QUERY,
                new RowMapper<Integer>() {
                    @Override
                    public Integer mapRow(ResultSet rs, int numRows) throws SQLException {
                        return rs.getInt("address_id");
                    }
                }, customerId);
        if (result == null || result.isEmpty())
            return null;
        return result.get(0);
    }

    public Double queryForShippingCharges(String platform, Double cartValue) {
    	log.info("gettting shipping charges from db for platform : {} ad cart value : {}", platform, cartValue);
        List<Double> result =
                jdbcTemplate.query(CheckoutSQL.SHIPPING_CHARGES_QUERY, new RowMapper<Double>() {
                    @Override
                    public Double mapRow(ResultSet rs, int numRows) throws SQLException {
                        return rs.getDouble("shipping_charge");
                    }
                }, cartValue, platform);
        if (result == null || result.isEmpty())
            return null;
        return result.get(0);
    }
    
	RowMapper<ShippingChargesDetailDTO> shippingChargesVORowMapper = (rs, rowNum) -> {
		return new ShippingChargesDetailDTO(rs.getDouble("shipping_charge"), rs.getDouble("cart_value"));
	};

	public ShippingChargesDetailDTO getshippingCharge(String platform) {
		ShippingChargesDetailDTO shippingChargesDetailDTO = null;
		try {
			shippingChargesDetailDTO = jdbcTemplate.queryForObject(CheckoutSQL.GET_SHIPPING_CHARGES,
					shippingChargesVORowMapper, platform);
		} catch (Exception e) {
		}
		return shippingChargesDetailDTO;
	}
}
