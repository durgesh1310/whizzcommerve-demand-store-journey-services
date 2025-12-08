package com.ouat.inventoryservice.sqlConstant;

public class SqlConstant {
	
	public static final String UPDATE_INVENTORY = "UPDATE products.vendor_inventory SET inventory = (inventory - ?), updated_by=?, updated_at = convert_tz(now(), '+00:00','+05:30')  WHERE SKU =?;";
	
	public static final String ADD_INVENTORY = "UPDATE products.vendor_inventory SET inventory = (inventory + ?), updated_by=?, updated_at = convert_tz(now(), '+00:00','+05:30')  WHERE SKU =?;";
	
	public static final String UPDATE_INVENTORY_V1 = "UPDATE products.vendor_inventory SET inventory =  ?, updated_by=?, updated_at = convert_tz(now(), '+00:00','+05:30')  WHERE SKU =?;";
	
	public static final String GET_INVENTORY_AND_SKU = "SELECT sku, inventory FROM products.vendor_inventory  WHERE expiry > CONVERT_TZ(now(), '+00:00', '+05:30') AND sku IN (%s);";
}
