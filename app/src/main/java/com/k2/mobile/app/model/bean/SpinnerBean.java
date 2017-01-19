package com.k2.mobile.app.model.bean;    

import java.io.Serializable;
  
public class SpinnerBean implements Serializable{
	
	private String value = "";
    private String text = "";

    public SpinnerBean(String _value, String _text) {
        value = _value;
        text = _text;
    }

	@Override
    public String toString() {

        return text;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
 