package com.ouat.orderService.client;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AllocateShipperResponse {

	@JsonProperty("udaan_parent_shipment_id")
	private String udaanParentShipmentId;

	private String err;
	private Boolean success;

	@JsonProperty("order_id")
	private String orderItemId;

	@JsonProperty("track_url")
	private String trackUrl;

	private String courier;

	@JsonProperty("dispatch_mode")
	private String dispatchMode;

	@JsonProperty("client_order_id")
	private String clientOrderId;

	@JsonProperty("ip_string")
	private String ipString;

	@JsonProperty("courier_id")
	private String courierId;

	@JsonProperty("tracking_id")
	private String trackingId;

	@JsonProperty("manifest_img_link_v2")
	private String manifestImgLinkV2;

	@JsonProperty("manifest_link_pdf")
	private String manifestLinkPdf;

	@JsonProperty("edd_stamp")
	private String eddStamp;

	@JsonProperty("routing_code")
	private String routingCode;

	@JsonProperty("manifest_img_link")
	private String manifestImgLink;

	@JsonProperty("order_pk")
	private String orderPk;

	@JsonProperty("manifest_link")
	private String manifestLink;

	@JsonProperty("courier_allocation")
	private Courier_allocation courierAllocation;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	@ToString
	public static class Courier_allocation {
		private String selected_type;
		private String rule;
	}

}
