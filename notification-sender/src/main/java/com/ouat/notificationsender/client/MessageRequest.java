package com.ouat.notificationsender.client;

import java.io.Serializable;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class MessageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mobileNumber;
    private String content;
    private DLTTemplateType dltTemplateType;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

	public DLTTemplateType getDltTemplateType() {
		return dltTemplateType;
	}

	public void setDltTemplateType(DLTTemplateType dltTemplateType) {
		this.dltTemplateType = dltTemplateType;
	}

    
}