package com.ouat.myOrders.sqlCommonConstant;

public class SqlCommonConstant {
	public static final String ORDER_DETAIL = "SELECT oo.order_type, oo.id AS order_id, oo.order_number AS order_number, oo.customer_id, oo.order_date, oo.promo_code AS promocode_applied, oo.payment_method, "
			+ "oo.total_amount AS order_total_amount, oo.order_payable, "
			+ "oo.platform_offered_discount,  oo.promo_discount, oo.credit_applied, oo.shipping_charges, ooi.is_returnable, ooi.is_exchangable, "
			+ "ooi.created_at, ooi.order_item_credit_applied, ooi.order_item_promo_discount, ooi.order_item_platform_offered_discount, ooi.order_item_payable, "
			+ "ooi.order_item_total_amount, ooi.sku, ooi.qty, ooi.id AS order_item_id, oocrr.requested_date AS cancel_return_requested_date, "
			+ "oocrr.is_refund_required, oocrr.is_refunded,ifnull(oocrr.record_type, 'Others') as record_type, oocrr.refunded_date, "
			+ "ifnull(oocrr.refund_type, 'Others') as refund_type, oocrr.refund_amount, oos.status AS order_item_status, "
			+ "ootd.tracking_url, ootd.shipped_date, ootd.delivered_date, ootd.awb, ootd.courrier_partner , "
			+ "osd.address_id as shipping_address_id, ootd.rto_date,ootd.rto_delivered_date,ootd.rto_tracking_no FROM orders.order oo JOIN orders.order_item ooi ON (oo.id = ooi.order_id) "
			+ "JOIN orders.order_status oos ON (ooi.order_status_id = oos.id) JOIN orders.order_shipment_detail osd ON (osd.order_id = oo.id)  "
			+ "LEFT JOIN orders.order_cancel_return_record oocrr ON  "
			+ "(oocrr.order_item_id = ooi.id AND oo.id = oocrr.order_id) LEFT JOIN "
			+ "orders.order_tracking_detail ootd ON (ootd.order_item_id = ooi.id)  "
			+ "WHERE  (oo.id = ? OR oo.order_number=?) AND oo.customer_id = ? order by ooi.id asc; ";
	
	public final static String ORDER_LIST_DETAIL = "SELECT oo.order_number, oo.id, ooi.id as order_item_id , oo.order_payable, ooi.sku,  "
			+ "ooi.order_item_payable,  oos.status, otd.delivered_date,  otd.shipped_date,  "
			+ " oocrr.requested_date,  oocrr.refunded_date,  oo.order_date,  ooi.created_at , "
			+ " oocrr.is_refund_required, oocrr.is_refunded , ifnull(oocrr.record_type, 'Others') "
			+ " as record_type, ooi.qty, oo.order_type, otd.rto_tracking_no, otd.rto_date,otd.rto_delivered_date FROM orders.order oo JOIN orders.order_item ooi ON "
			+ " (oo.id = ooi.order_id)  JOIN  orders.order_status oos ON (ooi.order_status_id = oos.id) LEFT JOIN  "
			+ " orders.order_tracking_detail otd ON(ooi.id = otd.order_item_id) LEFT JOIN orders.order_cancel_return_record oocrr  "
			+ " ON(oocrr.order_item_id = ooi.id) where customer_id = ? AND  "
			+ "oo.order_status_id <> 1 group by ooi.id order by oo.id desc LIMIT 10 OFFSET ?;";

}
 