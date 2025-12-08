package com.ouat.notificationsender.repository;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import java.util.stream.Collectors;

 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.jdbc.core.JdbcTemplate;
 import org.springframework.jdbc.core.RowMapper;
 import org.springframework.stereotype.Repository;

@Repository
public class NotificationSendorRepository {
	public static final Logger LOGGER = LoggerFactory.getLogger(NotificationSendorRepository.class);

	public static String GET_VENDOR_DETAIL = "Select  pi.sku,vc.email , vd.company, oi.id FROM products.product_item pi JOIN products.product p ON (p.id=pi.product_id) JOIN supplychain.vendor_details vd ON (vd.id=p.vendor_id) JOIN supplychain.vendor_contact vc \n"
			+ "			ON (vc.vendor_id= vd.id) JOIN orders.order_item oi ON (oi.sku=pi.sku) \n"
			+ "            JOIN orders.order oo ON (oi.order_id = oo.id)\n"
			+ "            where pi.sku IN (%s) AND oo.order_number = ? group by pi.sku;";
	@Autowired
	JdbcTemplate jdbcTemplate;

	RowMapper<VendorDetailDto> vendorDetailRowMapper = (rs, rowNum) -> {
		return new VendorDetailDto(
				rs.getString("vc.email"),
				rs.getString("pi.sku"),
				rs.getString( "vd.company"),
				rs.getString("id")
		);
 	};
	public Map<String, VendorDetailDto> getVendorDetailFromDb(List<String> skuList, String orderId) {
        LOGGER.info( "hitting the dp with  parameter : {}", skuList);
		String queryParameter =  "'" + skuList.stream().collect(Collectors.joining("','"))  + "'";
		Map<String, VendorDetailDto> vendorDetailMap= new HashMap<String, VendorDetailDto>();
		List<VendorDetailDto> vendorDetailList = jdbcTemplate.query(String.format(GET_VENDOR_DETAIL, queryParameter),vendorDetailRowMapper, orderId);
        LOGGER.info( "query is successflu executed with response : {} ", vendorDetailList);
		for(VendorDetailDto it : vendorDetailList) {
			vendorDetailMap.put(it.getSku(),it);
		}
        LOGGER.info( "returning the map : {} ", vendorDetailMap);
		return vendorDetailMap;
 	}
}
