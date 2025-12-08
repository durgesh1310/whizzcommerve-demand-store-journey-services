package com.ouat.orderService.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ouat.orderService.client.CourierResponse;
import com.ouat.orderService.exception.BusinessProcessException;
import com.ouat.orderService.exception.DownStreamException;
import com.ouat.orderService.pdfGeneration.ItextPdfGenerate;
import com.ouat.orderService.repository.GetLabelDto;
import com.ouat.orderService.repository.InvoiceDetailsRepository;
import com.ouat.orderService.repository.OrderRepository;
import com.ouat.orderService.repository.dto.AwbAndCourierPartner;
import com.ouat.orderService.request.OrderItemsDetail;
import com.ouat.orderService.request.ShipmentDetailsRequest;
import com.ouat.orderService.response.OrderItemIdAndErrorMessage;
import com.ouat.orderService.response.PostShippmentDetailResponse;

import ch.qos.logback.classic.Logger;

@Service
public class Label {
	public Logger LOGGER = (Logger) LoggerFactory.getLogger(Label.class);
 
	@Autowired
	InvoiceDetailsRepository invoiceDetailsRepository;
	
	@Autowired
	OrderRepository orderRepository;
	
	String PDF_PATH = "/tmp/source.pdf"; 
	
	@Autowired
	ItextPdfGenerate pdfgenerate;
	@Autowired
	private AllocateShipperService service;
	
	public String getLabels(String IntegerOrderId) throws FileNotFoundException {
				return generateBase64String(IntegerOrderId);
	}
	
	public PostShippmentDetailResponse postShipmentDetail(ShipmentDetailsRequest shipmentDetailsRequest) throws BusinessProcessException{
		       invoiceDetailsRepository.insertIntoOrderInvoiceDetail(shipmentDetailsRequest.getOrderItems());
			   return buildResponsePostShipmentDetail(shipmentDetailsRequest);
	}

	private PostShippmentDetailResponse buildResponsePostShipmentDetail(ShipmentDetailsRequest shipmentDetailsRequest) {
		List<OrderItemIdAndErrorMessage> orderItemResponse = new ArrayList<>();
		for(OrderItemsDetail it : shipmentDetailsRequest.getOrderItems()) {
			OrderItemIdAndErrorMessage orderItem = new OrderItemIdAndErrorMessage();
			orderItem.setErrorMessage("Order Items Not Inserted in Database");
			orderItem.setOrderItemId(it.getOrderItemId());
			orderItemResponse.add(orderItem);
		}
		PostShippmentDetailResponse response = new PostShippmentDetailResponse();
		response.setOrderItems(orderItemResponse);
		response.setStatus("SUCCESS");
	       LOGGER.info("post shipment detail reponse: {}",response);
    
		return response;
	}
	
	
	private String generateBase64String(String orderItemIds) throws FileNotFoundException {
		GetLabelDto labelDto = orderRepository.getOrderDetail(orderItemIds);
        pdfgenerate.createPdf(PDF_PATH,labelDto);
		String b64 = "";
	       try {
	    	      File file = new File(PDF_PATH);
	    	      byte [] bytes = Files.readAllBytes(file.toPath());
	    	       b64 = Base64.getEncoder().encodeToString(bytes);
	    	    } catch (Exception e) {
	    	      e.printStackTrace();
	    	    } 
	       return  b64 ;
	}

	public CourierResponse courierDetails(String orderItemId) throws JsonProcessingException, DownStreamException, ParseException {
		AwbAndCourierPartner awbCourier = orderRepository.getAwbAndCourierCode(orderItemId);
		if(null != awbCourier && null != awbCourier.getAwb()  && null!= awbCourier.getCourier()) {
			return service.buildCourierDetailResponse(awbCourier.getAwb(), awbCourier.getCourier());
		}
		return service.allocateOrderToBestShipperWorkFlow(orderItemId);
	}

}
