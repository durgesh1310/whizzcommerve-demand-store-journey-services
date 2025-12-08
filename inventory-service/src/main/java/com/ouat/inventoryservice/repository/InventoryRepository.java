package com.ouat.inventoryservice.repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ouat.inventoryservice.controller.InventoryController;
import com.ouat.inventoryservice.request.InventoryUpdate;
import com.ouat.inventoryservice.request.SkuAndQty;
import com.ouat.inventoryservice.sqlConstant.SqlConstant;

@Repository
public class InventoryRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryRepository.class);

	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	public int updateInventory(List<SkuAndQty> inventoryUpdateSkuList) {
		 int size = 0;
		     size =  jdbcTemplate.batchUpdate(SqlConstant.UPDATE_INVENTORY, new BatchPreparedStatementSetter() {
		        public void setValues(PreparedStatement ps, int i) throws SQLException {
		           ps.setInt(1, inventoryUpdateSkuList.get(i).getQty());
		           ps.setString(2, "Inventory-management-service");
		           ps.setString(3, inventoryUpdateSkuList.get(i).getSku());    
		        }
		        public int getBatchSize() {
		           return inventoryUpdateSkuList.size();
		        }
		     }).length;
		return size;
		}
	
	
		public int addInventory(List<SkuAndQty> inventoryUpdateSkuList) {
			int size = 0;
			size = jdbcTemplate.batchUpdate(SqlConstant.ADD_INVENTORY, new BatchPreparedStatementSetter() {
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ps.setInt(1, inventoryUpdateSkuList.get(i).getQty());
					ps.setString(2, "Inventory-management-service");
					ps.setString(3, inventoryUpdateSkuList.get(i).getSku());
				}

				public int getBatchSize() {
					return inventoryUpdateSkuList.size();
				}
			}).length;
			return size;
		}
		
		
		public int updateInventoryV1(InventoryUpdate inventoryUpdateRequest) {
			 int size = 0;
			     size =  jdbcTemplate.batchUpdate(SqlConstant.UPDATE_INVENTORY_V1, new BatchPreparedStatementSetter() {
			        public void setValues(PreparedStatement ps, int i) throws SQLException {
			           ps.setInt(1, inventoryUpdateRequest.getSkuAndQtyList().get(i).getQty());
			           ps.setString(2, inventoryUpdateRequest.getUser());
			           ps.setString(3, inventoryUpdateRequest.getSkuAndQtyList().get(i).getSku());    
			        }
			        public int getBatchSize() {
			           return inventoryUpdateRequest.getSkuAndQtyList().size();
			        }
			     }).length;
			return size;
			}
 
		public Map<String, Integer> getInventory(List<String> skus) {
			String skulist = skus.stream().collect(Collectors.joining("','"));
			skulist = "'" + skulist + "'";
			String sql = String.format(SqlConstant.GET_INVENTORY_AND_SKU, skulist);
			LOGGER.info("firing query: {}", sql);
			Map<String, Integer> data = new HashMap<String, Integer>();
			jdbcTemplate.query(sql ,new RowMapper<SkuAndQty>(){  
			    @Override  
			    public SkuAndQty mapRow(ResultSet rs, int rownumber) throws SQLException {  
			    	SkuAndQty skuQty=new SkuAndQty();  
			    	data.put(rs.getString("sku"), rs.getInt("inventory"));
			        return skuQty;  
			    }  
			    });  
			LOGGER.info("data from db : {} and size : {}", data, data.size());
			return data;
		}
}
