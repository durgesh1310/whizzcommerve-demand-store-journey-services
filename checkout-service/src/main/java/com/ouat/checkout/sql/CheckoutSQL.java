package com.ouat.checkout.sql;

public class CheckoutSQL {
    public static final String LAST_SHIPPED_ADDRESS_QUERY = "SELECT osd.address_id FROM orders.order o JOIN  orders.order_shipment_detail osd ON o.id = osd.order_id WHERE o.customer_id = ? ORDER BY o.order_date DESC LIMIT 1";

    public static final String SHIPPING_CHARGES_QUERY = "SELECT IF(cart_value < ? , shipping_charge, 0) AS shipping_charge FROM cart.shipping_charges sc WHERE platform = ?";
    
    public static final String GET_SHIPPING_CHARGES = "SELECT * FROM cart.shipping_charges where platform = ?;";


}
