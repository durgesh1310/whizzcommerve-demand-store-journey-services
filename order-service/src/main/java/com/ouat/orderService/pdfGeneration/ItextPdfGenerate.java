
package com.ouat.orderService.pdfGeneration;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.itextpdf.barcodes.Barcode128;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.ouat.orderService.repository.GetLabelDto; 
@Service
public class ItextPdfGenerate {
	 
	  
	  public void createPdf(String pdfFilePAth, GetLabelDto labelDto) throws FileNotFoundException {
	      PdfWriter writer = new PdfWriter(pdfFilePAth);                  
	      PdfDocument pdfDoc = new PdfDocument(writer);                      
	      Document doc = new Document(pdfDoc);                            
	      doc.add(buildTable(pdfDoc, labelDto));                   
	      doc.close();  
	  }

	private Table buildTable(PdfDocument pdfDoc, GetLabelDto labelDto) throws FileNotFoundException {
		float [] pointColumnWidths1 = {600f};       
	      Table table = new Table(pointColumnWidths1);                             
	      Cell cell1 = new Cell();  
	      cell1.add(new Paragraph("Shipping Label").setFontSize(15).setBold().setTextAlignment(TextAlignment.CENTER));
	      table.addCell(cell1);            
	      
	      Cell cell2 = new Cell();       
	      cell2.add( builTable1(pdfDoc, labelDto));       
	      table.addCell(cell2);  
	      
	      Cell cell3 = new Cell();       
	      cell3.add( buildOrderDetail(labelDto));       
	      table.addCell(cell3);  
	      
	      Cell cell4 = new Cell();       
	      cell4.add( buildTable5(labelDto));       
	      table.addCell(cell4); 
	    
	      Cell cell5 = new Cell();       
	      cell5.add( buildTable4());       
	      table.addCell(cell5);
		return table;
	}

	private Table builTable1(PdfDocument pdfDoc, GetLabelDto labelDto) throws FileNotFoundException {
		Table table1 = buildShippingDetail(labelDto);
	    Cell cell = new Cell(); 
	    cell.add(buildAmountDetail(pdfDoc, labelDto));
	    table1.addCell(cell);
		return table1;
	}

	private Table buildTable5(GetLabelDto labelDto) {
		float [] nestedpointColumnwidth5 = {600f};       
	    Table table5 = new Table(nestedpointColumnwidth5);
	      
	    Cell cell1 = new Cell();       
	    cell1.add( buildProductDetail(labelDto));     
	    table5.addCell(cell1);
	      
	    Cell cell2 = new Cell();       
	    cell2.add( buildPriceDetails(labelDto));     
	    table5.addCell(cell2);
		return table5;
	}

	private Table buildTable4() {
		float [] nestedpointColumnwidth4 = {600f};       
	    Table table4 = new Table(nestedpointColumnwidth4); 
	      
	    Cell cell1 = new Cell();  
	    cell1.add(new Paragraph().add("Price are inclusive of all applicable taxes").setFontSize(8));
	    table4.addCell(cell1);
	    Cell cell2 = new Cell();  
	    cell2.add(new Paragraph().add("Sold By:").setFontSize(11).setBold());
	    cell2.add(new Paragraph().add(buildSoldBy()).setFontSize(10));
	    table4.addCell(cell2);
		return table4;
	}


	private Table buildPriceDetails(GetLabelDto labelDto) {  //buildTable7
		float [] nestedpointColumnwidth7= {520f,80f};       
	      Table table7 = new Table(nestedpointColumnwidth7);
	   
	      Cell cell1 = new Cell();       
	      cell1.add(new Paragraph().add("Shipping Charges(INR)").setFontSize(11).setBold().setTextAlignment(TextAlignment.RIGHT));       
	      table7.addCell(cell1);
	      Cell cell2 = new Cell();       
	      cell2.add(new Paragraph().add(convertToString(labelDto.getOrderItemShippingCharges())).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));       
	      table7.addCell(cell2);
	      Cell cell3 = new Cell();       
	      cell3.add(new Paragraph().add("Store Credit(INR)").setFontSize(11).setBold().setTextAlignment(TextAlignment.RIGHT));       
	      table7.addCell(cell3);
	      Cell cell4 = new Cell();       
	      cell4.add(new Paragraph().add(convertToString(labelDto.getOrderItemCreditApplied())).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));       
	      table7.addCell(cell4);
	      
	      
	      Cell cell5 = new Cell();       
	      cell5.add(new Paragraph().add("Promo Amount(INR)").setFontSize(11).setBold().setTextAlignment(TextAlignment.RIGHT));       
	      table7.addCell(cell5);
	      Cell cell6 = new Cell();       
	      cell6.add(new Paragraph().add(convertToString(labelDto.getOrderItemPromoDiscount())).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));       
	      table7.addCell(cell6);
	      Cell cell7 = new Cell();       
	      cell7.add(new Paragraph().add("Total(INR)").setFontSize(11).setBold().setTextAlignment(TextAlignment.RIGHT));       
	      table7.addCell(cell7);
	      Cell cell8 = new Cell();       
	      cell8.add(new Paragraph().add(convertToString(labelDto.getOrderItemPayable())).setFontSize(10).setBold().setTextAlignment(TextAlignment.RIGHT));       
	      table7.addCell(cell8);
		return table7;
	}


	private Table buildProductDetail(GetLabelDto labelDto) { //buildTable6
		float [] nestedpointColumnwidth6= {10f,1000f,115f,115f,115f,115f,115f,115f};       
	      Table table6 = new Table(nestedpointColumnwidth6);
	      
	      Cell cell1 = new Cell();  
	      cell1.add(new Paragraph().add("SNo.").setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER));       
	      table6.addCell(cell1);

	      Cell cell2 = new Cell();       
	      cell2.add(new Paragraph().add("Item Description").setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER));       
	      table6.addCell(cell2);
	      
	      Cell cell3 = new Cell();       
	      cell3.add(new Paragraph().add("Size").setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER));       
	      table6.addCell(cell3);

	      Cell cell4 = new Cell();       
	      cell4.add(new Paragraph().add("Qty").setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER));       
	      table6.addCell(cell4);
	      Cell cell5 = new Cell();     
	      cell5.add(new Paragraph().add("MRP(INR)").setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER));       

	      table6.addCell(cell5);

	      Cell cell6 = new Cell();  
	      cell6.add(new Paragraph().add("Rate(INR)").setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER));       
	      table6.addCell(cell6);
	      Cell cell7 = new Cell();       
	      cell7.add(new Paragraph().add("Product Discount(INR)").setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER));       
	      table6.addCell(cell7);

	      Cell cell8 = new Cell();       
	      cell8.add(new Paragraph().add("Amount(INR)").setFontSize(11).setBold().setTextAlignment(TextAlignment.CENTER));       
	      table6.addCell(cell8);
	      
	      Cell cell9 = new Cell();       
	      cell9.add(new Paragraph().add("1").setFontSize(10).setTextAlignment(TextAlignment.CENTER));       
	      table6.addCell(cell9);

	      Cell cell10 = new Cell();     
	      cell10.add(new Paragraph().add(convertToString(buildProductDescription(labelDto))).setFontSize(10).setTextAlignment(TextAlignment.CENTER));       
	      table6.addCell(cell10);
	      Cell cell11 = new Cell();       
	      cell11.add(new Paragraph().add(convertToString(labelDto.getSize())).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));       
	      table6.addCell(cell11);

	      Cell cell12 = new Cell();       
	      cell12.add(new Paragraph().add(convertToString(labelDto.getQty())).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));       
	      table6.addCell(cell12);
	      Cell cell13 = new Cell();       
	      cell13.add(new Paragraph().add(convertToString(labelDto.getRegularPrice())).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));       
	      table6.addCell(cell13);

	      Cell cell14 = new Cell();       
	      cell14.add(new Paragraph().add(convertToString(labelDto.getSellingPrice())).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));       
	      table6.addCell(cell14);
	      Cell cell15 = new Cell();       
	      cell15.add(new Paragraph().add(convertToString(labelDto.getOrderItemPlatformOfferedDiscount())).setFontSize(10)).setTextAlignment(TextAlignment.RIGHT);       
	      table6.addCell(cell15);

	      Cell cell16 = new Cell();       
	      cell16.add(new Paragraph().add(convertToString(labelDto.getOrderItemTotal())).setFontSize(10).setTextAlignment(TextAlignment.RIGHT));       
	      table6.addCell(cell16);
		return table6;
	}
	private String convertToString(Object obj) {
		if(null==obj)return "";
		return obj.toString();
	}

	private Table buildOrderDetail(GetLabelDto getLabelDto) {  //buildTable3
		float [] nestedpointColumnwidth3 = {300f,300f};       
	      Table table3 = new Table(nestedpointColumnwidth3); 
	      table3.setBorder(Border.NO_BORDER);
	      Cell cell1 = new Cell();  
	      cell1.add(new Paragraph().add("ORDER #:            ").add(new Paragraph(convertToString(getLabelDto.getOrderItemId())).setBold()));
	      cell1.add(new Paragraph().add("ORDER DATE:         ").add(new Paragraph(formatDate(getLabelDto)).setBold()));
	      table3.addCell(cell1);
	      
	      Cell cell2 = new Cell();  
	      cell2.add(new Paragraph().add("Dimensions: 10*10*10").setFontSize(8));
	      cell2.add(new Paragraph().add("0.1Kgs").setFontSize(8));
	      table3.addCell(cell2);
		return table3;
	}

	private String formatDate(GetLabelDto getLabelDto) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
		if(null== getLabelDto.getOrderDate()) {
			return "";
		}
		Date  date = new Date(getLabelDto.getOrderDate().getTime());
	    return formatter.format(date);
	}


	private Table buildShippingDetail(GetLabelDto getLabelDto) { //buildTable1
		float [] nestedpointColumnwidth2 = {200f,200f,200f};       
	      Table table1 = new Table(nestedpointColumnwidth2); 
	      Cell cell1 = new Cell();
	      cell1.add(new Paragraph().add("DELIVER TO :").setFontSize(11).setBold().setMarginLeft(5));
	      cell1.add(new Paragraph().add(buildDeliverTo(getLabelDto)).setFontSize(10).setMarginLeft(5));
	      table1.addCell(cell1);
	      
	      Cell cell2 = new Cell();  
	      cell2.add(new Paragraph().add("IF UNDELIVERED, PLEASE RETURN TO").setFontSize(11).setBold().setMarginLeft(5));
	      cell2.add(new Paragraph().add(buildReturnTo(getLabelDto)).setFontSize(10).setMarginLeft(5));
	      table1.addCell(cell2);
		return table1;
	}


	private Table buildAmountDetail(PdfDocument pdfDoc, GetLabelDto labelDto ) throws FileNotFoundException {  //buildTable2
		float [] nestedpointColumnwidth3 = {200f};       
	      Table table2 = new Table(nestedpointColumnwidth3); 
	      Cell cell1 = new Cell();  
	      if(labelDto.getPaymentMethod().equals("COD")) {
		      cell1.add(new Paragraph().add("COD").setFontSize(14) .setBold().setMarginTop(10).setMarginLeft(5));
		      cell1.add(new Paragraph().add(convertToString(labelDto.getRoutingCode())).setFontSize(8).setMarginLeft(100));
		      cell1.add(new Paragraph().add("AMOUNT TO BE COLLECTED (COD)").setFontSize(8).setBold().setBold().setMarginTop(10).setMarginLeft(5));
		      table2.addCell(cell1);

		      Cell cell2 = new Cell(); 
		      String total = String.format("TOTAL COLLECTABLE:   %s", convertToString(labelDto.getOrderItemPayable()));
		      cell2.add(new Paragraph().add(total).setFontSize(10).setMarginTop(20).setBold().setTextAlignment(TextAlignment.CENTER)); 
		      cell2.add(new Paragraph().add(convertToString(labelDto.getCourierPartner())).setBold().setTextAlignment(TextAlignment.CENTER));
		      cell2.add(buildBarCode(pdfDoc,labelDto.getAwb()).setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginBottom(50)).getPaddingTop(); 
		      table2.addCell(cell2);
	      }else {
	    	  cell1.add(new Paragraph().add("PREPAID SHIPMENT").setFontSize(14) .setBold().setMarginTop(10).setMarginLeft(5));
		      cell1.add(new Paragraph().add(convertToString(labelDto.getRoutingCode())).setFontSize(8).setMarginLeft(100));
		      cell1.add(new Paragraph().add("DO NOT COLLECT CASH").setFontSize(9).setBold().setBold().setMarginTop(10).setMarginLeft(5));
		      table2.addCell(cell1);
		      
		      Cell cell2 = new Cell(); 
		      cell2.add(new Paragraph().add("TOTAL COLLECTABLE:   NIL").setFontSize(10).setMarginTop(20).setBold().setTextAlignment(TextAlignment.CENTER)); 
		      cell2.add(new Paragraph().add(convertToString(labelDto.getCourierPartner())).setBold().setTextAlignment(TextAlignment.CENTER));
		      cell2.add(buildBarCode(pdfDoc,convertToString(labelDto.getAwb())).setHorizontalAlignment(HorizontalAlignment.CENTER).setMarginBottom(50)).getPaddingTop(); 
		      table2.addCell(cell2);
	      }
	      
	     
		return table2;
	}


	private Image buildBarCode(PdfDocument pdfDoc, String code) {
		Barcode128 code128 = new Barcode128(pdfDoc);
		code128.setBaseline(-1);
		code128.setSize(10);
		code128.setCode(convertToString(code));
		code128.setCodeType(Barcode128.CODE128);
		Image code128Image = new Image(code128.createFormXObject(pdfDoc));
		return code128Image;
	}
	 
		
		private String buildDeliverTo(GetLabelDto getLabelDto) {
			String  deliverTo = convertToString(getLabelDto.getCustomerName())
					   .concat(",\n")
					   .concat(convertToString(getLabelDto.getCustomerAddress()))
					   .concat("\n")
					   .concat(convertToString(getLabelDto.getCustomerLandMark()))
					   .concat("\n")
					   .concat(convertToString(getLabelDto.getCustomerCity()))
					   .concat(",")
					   .concat(convertToString(getLabelDto.getCustomerState()))
					   .concat("\n")
					   .concat(convertToString(getLabelDto.getCustomerPincode()))
					   .concat(",\n")
					   .concat(convertToString(getLabelDto.getCustomerMobile()));
			return deliverTo;
		}
		private String buildReturnTo(GetLabelDto getLabelDto) {
			String returnTo =   convertToString(getLabelDto.getVendorName())
					   .concat("\n")
					   .concat(convertToString(getLabelDto.getVendorAddressLine1()))
					   .concat("\n")
					   .concat(convertToString(getLabelDto.getVendorAddressLine2()))
					   .concat("\n")
					   .concat(convertToString(getLabelDto.getVendorCity()))
					   .concat("\n")
					   .concat(convertToString(getLabelDto.getVendorState()))
					   .concat("\n")
					   .concat(convertToString(getLabelDto.getVendorCoutry()))
					   .concat("\n")
					   .concat(convertToString(getLabelDto.getVendorPincode()));
			return returnTo;
		}
		private String buildSoldBy() {
			String soldBy = "Once Upon a Trunk Pvt Ltd Khasra no 354, 2nd Floor,\n"
			   		+ "Jagat Complex, Ghitorni , New Delhi\n"
			   		+ "New Delhi\n"
			   		+ "Delhi\n"
			   		+ "India-110030\n"
			   		+ "GST No: 07AACCO1562G1ZN";
			return soldBy;
		}
		private String buildProductDescription(GetLabelDto labelDto) {
	    	 return labelDto.getSku().concat("-").concat(labelDto.getProductName().concat("VendorSKUCode : ").concat((null == labelDto.getVendorSku())?"":labelDto.getVendorSku()));
	     }
	

}
