package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
  
public class CategoryBean implements Serializable{
	
    private String id;
    private String questionCategoryName;

     
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuestionCategoryName() {
		return questionCategoryName;
	}

	public void setQuestionCategoryName(String questionCategoryName) {
		this.questionCategoryName = questionCategoryName;
	}
 
}
 