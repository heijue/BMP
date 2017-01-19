package com.k2.mobile.app.model.bean;

import java.io.Serializable;

/**
 * Result's Object
 * 
 * @author ZYB2
 * 
 */
public class ResultBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// items
	public final static String RESULT = "Result";// Result

	public final static String MESSAGE = "Message";// Message

	public final static String MESSAGEDETAILS = "MessageDetails";// MessageDetails

	// values
	private String result;

	private String message;

	private String messageDetails;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(String messageDetails) {
		this.messageDetails = messageDetails;
	}

}
