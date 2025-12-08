package com.ouat.customerService.customer.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ouat.customerService.constants.LoginType;
import com.ouat.customerService.customer.request.CustomerAddressRequest;
import com.ouat.customerService.customer.request.CustomerRequest;
import com.ouat.customerService.customer.request.Gender;
import com.ouat.customerService.customer.response.AddressDetailResponse;
import com.ouat.customerService.customer.response.CustomerDetailResponse;
import com.ouat.customerService.response.CustomerDetailVO;

@Repository
public class CustomerRepository {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	JDBCTemplateWrapper jdbcTemplateWrapper;
	
	
	public boolean isCustomerAlreadyExistsWithMobile(String mobile) {
		Integer flag = jdbcTemplateWrapper.findInteger("Select count(1) FROM customers.customer where mobile = ?", mobile);
		return flag != null && flag == 1; 
	}
	
	public boolean isCustomerAlreadyExistsWithEmail(String email) {
		Integer flag = jdbcTemplateWrapper.findInteger("Select count(1) FROM customers.customer where email = ?", email);
		return flag != null && flag == 1;
	}
	
	public int addCustomer(CustomerRequest customerRequest) {
		return jdbcTemplateWrapper.execute("insert into customers.customer (first_name, last_name, mobile, email, gender, source, medium, campaign, created_by) values (?, ?, ?, ?, ?, ?, ?, ?, ?)", customerRequest.getFirstName(), customerRequest.getLastName(), customerRequest.getMobile(), customerRequest.getEmailId(), customerRequest.getGender().toString(), customerRequest.getSource(), customerRequest.getMedium(), customerRequest.getCampaign(), "customer-service");
	}
	
	public int signup(CustomerRequest customerRequest) {
		jdbcTemplateWrapper.execute("insert into customers.customer (first_name, last_name, mobile, email, gender, source, medium, campaign, created_by, created_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?, CONVERT_TZ(now(), '+00:00', '+05:30'))", customerRequest.getFirstName(), customerRequest.getLastName(), customerRequest.getMobile(), customerRequest.getEmailId(), customerRequest.getGender().toString(), customerRequest.getSource(), customerRequest.getMedium(), customerRequest.getCampaign(), "customer-service");
		return findCustomerDetailByMobile(customerRequest.getMobile()).getCustomerId();
	}


	RowMapper<CustomerDetailResponse> customerDetailResponse = (rs, rowNum) -> {
		return new CustomerDetailResponse(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"), null , rs.getString("mobile"), rs.getString("email"), rs.getInt("is_active"), rs.getString("user_type"), rs.getInt("is_created_today"));
	};
	
	
	
	public CustomerDetailResponse findCustomerDetailByEmail(String email) {
		try {
		return jdbcTemplateWrapper.findOne("Select id, first_name, last_name, mobile, email, is_active, user_type, IF(CURDATE() = date(created_date) , 1 , 0) as is_created_today FROM customers.customer where email = ?", customerDetailResponse,
				email);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public CustomerDetailResponse findCustomerDetailByMobile(String mobile) {
		try {
		return jdbcTemplateWrapper.findOne("Select id, first_name, last_name, mobile, email, is_active, user_type, IF(CURDATE() = date(created_date) , 1 , 0) as is_created_today FROM customers.customer where mobile = ?", customerDetailResponse,
				mobile);
		}catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
	
	public boolean isCustomerExists(Integer id) {
		Integer flag = jdbcTemplateWrapper.findInteger("Select count(1) FROM customers.customer where id = ? and is_active = 1", id);
		return flag != null && flag == 1;
	}

	public int addCustomerAddress(CustomerAddressRequest customerAddressRequest) {
		return jdbcTemplateWrapper.execute("insert into customers.customer_addresses (house_owner_name, pincode, address, landmark, city, state, mobile_number, customer_id, created_by) values (?, ?, ?, ?, ?, ?, ?, ?, ?)", customerAddressRequest.getFullName(), customerAddressRequest.getPincode(), customerAddressRequest.getAddress(), customerAddressRequest.getLandmark(), customerAddressRequest.getCity(), customerAddressRequest.getState(), customerAddressRequest.getMobile(), customerAddressRequest.getCustomerId(), "customer-service");
		
	}

	public int updateAddress(CustomerAddressRequest customerAddressRequest) {
		return jdbcTemplateWrapper.execute("update customers.customer_addresses set house_owner_name = ?, pincode = ?, address = ?, landmark = ?, city = ?, state = ?, mobile_number = ?, updated_by = ?, updated_date = convert_tz(now(),'+00:00','+05:30') where id = ? AND customer_id = ?", customerAddressRequest.getFullName(), customerAddressRequest.getPincode(), customerAddressRequest.getAddress(), customerAddressRequest.getLandmark(), customerAddressRequest.getCity(), customerAddressRequest.getState(), customerAddressRequest.getMobile(), "customer-service", customerAddressRequest.getAddressId(), customerAddressRequest.getCustomerId());
		
	}

	RowMapper<AddressDetailResponse> addressDetailRowMapper = (rs, rowNum) -> {
		return new AddressDetailResponse(rs.getString("house_owner_name"), rs.getInt("pincode"), rs.getString("address") , rs.getString("landmark"), rs.getString("city"), rs.getString("state"), rs.getString("mobile_number"), rs.getInt("id"));
	};

	public List<AddressDetailResponse> findAddress(Integer customerId) {
		return jdbcTemplateWrapper.find("SELECT * FROM customers.customer_addresses where customer_id = ? order by updated_date desc", addressDetailRowMapper, customerId);
	}
	
	public AddressDetailResponse findAddress(Integer customerId, Integer addressId) {
		return jdbcTemplateWrapper.findOne("SELECT * FROM customers.customer_addresses where id = ? AND customer_id = ?", addressDetailRowMapper, addressId , customerId);
	}

	public boolean isCustomerAddressExists(Long customerId, Integer addressId) {
		Integer flag = jdbcTemplateWrapper.findInteger("Select count(1) FROM customers.customer_addresses where id = ? and customer_id = ?", addressId, customerId);
		return flag != null && flag == 1;
	}

	public int deleteAddress(Long customerId, Integer addressId) {
		return jdbcTemplateWrapper.execute("DELETE FROM customers.customer_addresses where id = ? and customer_id = ? ", addressId, customerId);
	}

	public boolean hasCustomerAvailedAppInstallCredits(Long customerId) {
		Integer flag = jdbcTemplateWrapper.findInteger("Select count(1) FROM customers.app_install where customer_id = ?", customerId);
		return flag != null && flag == 1;
	}

	public void addAppInstallCredits(Long customerId, String platform) {
		jdbcTemplateWrapper.execute("insert into customers.app_install (customer_id, platform, created_at, created_by) values (?, ?, convert_tz(now(),'+00:00','+05:30'), 'customer-service') ", customerId, platform);
	}
	
	public void userSignupCredits(Long customerId, String platform) {
		jdbcTemplateWrapper.execute("insert into customers.signup_credit (customer_id, platform, created_at, created_by) values (?, ?, convert_tz(now(),'+00:00','+05:30'), 'customer-service') ", customerId, platform);
	}
	
	public boolean hasCustomerAvailedSignupCredits(Long customerId) {
		Integer flag = jdbcTemplateWrapper.findInteger("Select count(1) FROM customers.signup_credit where customer_id = ?", customerId);
		return flag != null && flag == 1;
	}

	
	public int addGuestCustomer(String signInId, LoginType loginType) {
		String mobile = null, email = null;
		if(loginType == LoginType.MOBILE) {
			mobile = signInId;
		} else {
			email = signInId;
		}
		return jdbcTemplateWrapper.execute("insert into customers.customer (first_name, mobile, email, created_by, user_type) values (?, ?, ?, ?, ?)" , "", mobile, email, "customer-service", "GUEST");
	}

	public void updateCustomerAndAddress(CustomerDetailVO customerDetailVO, CustomerAddressRequest customerAddressRequest) {
		customerAddressRequest.setCustomerId(customerDetailVO.getCustomerId().intValue());
		jdbcTemplateWrapper.execute("update customers.customer set first_name = ?, mobile = ?, email = ?, updated_by = ?, user_type = ?, updated_date = CONVERT_TZ(now(), '+00:00', '+05:30') where id = ? ", customerAddressRequest.getFullName(), customerAddressRequest.getMobile(), customerAddressRequest.getEmail(), "customer-service", "USER", customerDetailVO.getCustomerId());
		if(null != customerAddressRequest.getPincode() && customerAddressRequest.getPincode()>0) {
			addCustomerAddress(customerAddressRequest);
		}
	}
	
	public void updateCustomerName(String name, Integer customerId) {
		jdbcTemplateWrapper.execute("update customers.customer set first_name = ?, updated_by = ?, user_type = ?, updated_date = CONVERT_TZ(now(), '+00:00', '+05:30') where id = ? ", name, "customer-service", "USER", customerId);
	}
	
	//query for guest checkout
	/*public String getCustomerEmailFromPhone(String phone) {
 		String query = "select email from customers.customer where mobile = ?";
 		try {
  			return jdbcTemplateWrapper.queryForSingleColumn(query, phone);
  		}catch(DataAccessException e) {
  			return null;
  		}
	}

	public String getCustomerPhoneAndFromEmail(String email) {
  		String query = "select mobile from customers.customer where email = ?";
  		try {
  			return jdbcTemplateWrapper.queryForSingleColumn(query, email);
  		}catch(DataAccessException e) {
  			return null;
  		}
 	}

	public Integer updateEmail(String email,String updatedEmail, String  mobile ) {
		String query = "UPDATE customers.customer set email = ? where email = ? and mobile = ?;";
		return jdbcTemplateWrapper.updateEmail(query, email, updatedEmail, mobile);
	}
*/
	public Integer getCustomerId (String  mobile) {
		String queryForCustomerId = "Select id from customers.customer where mobile = ?;";
		return jdbcTemplateWrapper.getCustomerId(queryForCustomerId, mobile);
	}
	public Integer getAddressId(String updatedEmail, String  mobile ,  Integer customerId) {
		String queryForCustomerId = "Select  id from customers.customer_addresses  where  customer_id = ? ORDER BY id DESC LIMIT 1;";
		return jdbcTemplateWrapper.getAddressId(queryForCustomerId, customerId);
	}

	public Integer updateEmailByMobile(String mobile, String email) {
		String queryToUpdateEmailByPhone = "update customers.customer set email = ?, user_type = 'USER', updated_by = 'updateEmailByMobile', updated_date = CONVERT_TZ(now(), '+00:00', '+05:30') where mobile = ?";
		return  jdbcTemplateWrapper.updateEmailByMobile(queryToUpdateEmailByPhone,email,mobile );
  	}
	public Integer updateMobileByEmail(String mobile, String email) {
		String queryToUpdateEmailByPhone = "update customers.customer set mobile = ?, user_type = 'USER', updated_by = 'updateMobileByEmail', updated_date = CONVERT_TZ(now(), '+00:00', '+05:30') where email = ?";
		return  jdbcTemplateWrapper.updateMobileByEmail(queryToUpdateEmailByPhone,email,mobile );
  	}

	public int updateCustomerEmail(Long customerId, String email) {
		String queryToUpdateEmail = "update customers.customer set email = ?, user_type = 'USER', updated_by = 'updateCustomerEmail', updated_date = CONVERT_TZ(now(), '+00:00', '+05:30') where id = ?";
		return  jdbcTemplateWrapper.execute(queryToUpdateEmail, email, customerId);
	}

	public CustomerDetailResponse findCustomerDetailByCustomerId(Long customerId) {
		try {
			return jdbcTemplateWrapper.findOne("Select id, first_name, last_name, mobile, email, is_active, user_type, IF(CURDATE() = date(created_date) , 1 , 0) as is_created_today FROM customers.customer where id = ?", customerDetailResponse,
					customerId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		
	}
	
	public Long getParticipantRank(Long customerId) {
		Long str = null;
		String query = "SELECT cpr.ranking FROM customers.participant_ranking cpr WHERE cpr.customer_id= ?  AND cpr.is_active=1 order by cpr.id desc";
		try {
			Long ranking = jdbcTemplate.queryForObject(query, Long.class, customerId);
			str = ranking;
		} catch (EmptyResultDataAccessException e) {

		}
		return str;

	}
}
