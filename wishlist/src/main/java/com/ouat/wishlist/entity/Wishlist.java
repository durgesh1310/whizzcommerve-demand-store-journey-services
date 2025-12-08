package com.ouat.wishlist.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ouat.wishlist.request.PlatformType;
import com.ouat.wishlist.request.SourceType;
import com.sun.istack.NotNull;

@Entity
@Table(name="wishlist_item")
@Access(AccessType.FIELD)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Wishlist  implements Serializable {
	
	private static final long serialVersionUID = -8774106036027053323L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	private Long customerId;
	
	@NotNull
	private Long productId;

	private Double fromPrice;

	private Double toPrice;
	
	@Enumerated(EnumType.STRING)
	private SourceType source;
	
	private Long sourceId;
	
	@Column
	@Enumerated(EnumType.STRING)
	private PlatformType platform;
	
	private String utmCampaign;
	
	private String utmMedium;
	
	private String utmSource;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String updatedBy;
	
	private Date updatedDate;


	public Double getFromPrice() {
		return fromPrice;
	}

	public void setFromPrice(Double fromPrice) {
		this.fromPrice = fromPrice;
	}

	public Double getToPrice() {
		return toPrice;
	}

	public void setToPrice(Double toPrice) {
		this.toPrice = toPrice;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public SourceType getSource() {
		return source;
	}

	public void setSource(SourceType source) {
		this.source = source;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	
	public PlatformType getPlatform() {
		return platform;
	}

	public void setPlatform(PlatformType platform) {
		this.platform = platform;
	}

	public String getUtmCampaign() {
		return utmCampaign;
	}

	public void setUtmCampaign(String utmCampaign) {
		this.utmCampaign = utmCampaign;
	}

	public String getUtmMedium() {
		return utmMedium;
	}

	public void setUtmMedium(String utmMedium) {
		this.utmMedium = utmMedium;
	}

	public String getUtmSource() {
		return utmSource;
	}

	public void setUtmSource(String utmSource) {
		this.utmSource = utmSource;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Wishlist(Long id, Long customerId, Long productId, Double fromPrice, Double toPrice, SourceType source,
			Long sourceId, PlatformType platform, String utmCampaign, String utmMedium, String utmSource,
			String createdBy, Date createdDate, String updatedBy, Date updatedDate) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.productId = productId;
		this.fromPrice = fromPrice;
		this.toPrice = toPrice;
		this.source = source;
		this.sourceId = sourceId;
		this.platform = platform;
		this.utmCampaign = utmCampaign;
		this.utmMedium = utmMedium;
		this.utmSource = utmSource;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.updatedBy = updatedBy;
		this.updatedDate = updatedDate;
	}

	public Wishlist() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String toString() {
		String serialized = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			serialized = objectMapper.writeValueAsString(this);
		} catch (JsonProcessingException jpe) {
			jpe.printStackTrace();
		}
		return serialized;
	}
	
	

}
