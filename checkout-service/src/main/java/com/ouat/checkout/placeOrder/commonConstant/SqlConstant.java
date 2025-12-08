package com.ouat.checkout.placeOrder.commonConstant;

public class SqlConstant {
	 public static String INSERT_INTO_ORDER_TABLE= "INSERT INTO orders.order(order_number, customer_id, order_status_id, total_amount, order_payable, platform_offered_discount, promo_discount, credit_applied, shipping_charges, order_date, order_type,cart_created_date, payment_method, promo_code,platform, utm_source, utm_campaign, utm_medium) VALUES (?,?,?,?,?,?,?,?,?,convert_tz(now(),'+00:00','+05:30') ,?,convert_tz(now(),'+00:00','+05:30'),? ,?,?,?,?,?)";
	 
	 public static String INSERT_INTO_ORDER_SHIPMENT_TABLE = "INSERT INTO orders.order_shipment_detail(order_id, address_id) Values (?,?)";
	 
	 public static String INSERT_INTRO_ORDER_ITEM_TABLE = "INSERT INTO orders.order_item(order_id, sku, qty, order_item_total_amount, order_item_payable, order_item_platform_offered_discount,order_item_promo_discount, order_item_credit_applied,vendor_price, ouat_margin, order_status_id,  created_at ,created_by, updated_at,updated_by,is_returnable, is_exchangable,estimated_shipped_date, estimated_delivery_date, shipping_charges) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, convert_tz(now(),'+00:00','+05:30') ,? ,convert_tz(now(),'+00:00','+05:30') , ?, ?, ?, ?, ?, ?);";
}
