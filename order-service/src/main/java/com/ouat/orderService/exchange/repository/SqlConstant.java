package com.ouat.orderService.exchange.repository;

public class SqlConstant {
	
//	public static String SELECT_QUERY_TO_CHECK_INVENTORY = "SELECT pi.sku, vi.inventory FROM products.product_item pi JOIN products.vendor_inventory vi ON (vi.sku = pi.sku) WHERE pi.sku = ?";
	
	public static String SELECT_QUERY_ORDER_DETAIL = "SELECT oo.customer_id, oo.order_status_id, oo.total_amount, oo.order_payable, oo.platform_offered_discount, oo.promo_discount, oo.credit_applied, oo.shipping_charges, oo.order_date, oo.order_type, oo.payment_method, oo.promo_code, oo.platform, ooi.id, ooi.order_id, ooi.sku, ooi.qty, ooi.order_item_total_amount, ooi.order_item_payable, ooi.order_item_platform_offered_discount, ooi.order_item_promo_discount, ooi.order_item_credit_applied, ooi.vendor_price, ooi.ouat_margin, ooi.order_status_id, ooi.is_returnable, ooi.is_exchangable, ooi.shipping_charges, osd.address_id FROM orders.order oo JOIN orders.order_item ooi ON (ooi.order_id = oo.id) JOIN orders.order_shipment_detail osd ON (osd.order_id = ooi.order_id) WHERE ooi.id = ? AND ooi.order_status_id=5 AND ooi.is_exchangable = 1";
	
//	public static String INSERT_INTO_ORDER_TABLE = "INSERT INTO orders.order(customer_id, order_status_id, total_amount, order_payable, platform_offered_discount, promo_discount, credit_applied, shipping_charges, order_date, order_type,cart_created_date, payment_method, promo_code,platform) VALUES (?,?,?,?,?,?,?,?,convert_tz(now(),'+00:00','+05:30') ,?,convert_tz(now(),'+00:00','+05:30'),? ,?,?)";
	 
//	public static String INSERT_INTO_ORDER_SHIPMENT_TABLE = "INSERT INTO orders.order_shipment_detail(order_id, address_id, created_by, created_at) Values (?,?, 'OrderService', convert_tz(now(),'+00:00','+05:30'))";
	 
//	public static String INSERT_INTO_ORDER_ITEM_TABLE = "INSERT INTO orders.order_item (order_id, sku, qty, order_item_total_amount, order_item_payable, order_item_platform_offered_discount,order_item_promo_discount, order_item_credit_applied,vendor_price, ouat_margin, order_status_id,  created_at ,created_by, updated_at,updated_by,is_returnable, is_exchangable,estimated_shipped_date, estimated_delivery_date, shipping_charges) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, convert_tz(now(),'+00:00','+05:30') ,? ,convert_tz(now(),'+00:00','+05:30') , ?, ?, ?, ?, ?, ?);";
	 
	public static String INSERT_INTO_ORDER_CANCEL_RETURN_RECORD ="INSERT INTO orders.order_cancel_return_record (order_id, order_item_id, qty, requested_date, record_type, reason_id, created_by, created_at) VALUES (?,?,?, convert_tz(now(),'+00:00','+05:30') ,'EXCHANGE',?,?, convert_tz(now(),'+00:00','+05:30'))";

    public static String INSERT_INTO_EXCHANGE_ORDER_DETAIL = "INSERT INTO orders.exchange_order (order_item_id, new_sku, created_by, created_at) VALUES (?,?,'OrderService', convert_tz(now(),'+00:00','+05:30'))";
    
    
}
