package com.ouat.myOrders.service;

import java.util.Comparator;

import com.ouat.myOrders.response.OrderListResponse;

class SortByDate implements Comparator<OrderListResponse> {
	 @Override
     public int compare(OrderListResponse a, OrderListResponse b) {
         return a.getOrderDate().compareTo(b.getOrderDate());
     }
}
