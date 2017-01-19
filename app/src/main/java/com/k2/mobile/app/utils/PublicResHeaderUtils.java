package com.k2.mobile.app.utils;    

import android.content.Context;
import android.content.SharedPreferences;

import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.ReqHeaderBean;
  
public class PublicResHeaderUtils {

	public static ReqHeaderBean getReqHeader(){
		ReqHeaderBean hBean = new ReqHeaderBean();
		if(null != BaseApp.user){
			hBean.setUserAccount(BaseApp.user.getUserId());
		}
		hBean.setTokenID(BaseApp.token);
		hBean.setMac(BaseApp.macAddr);
		hBean.setSectetKey(HttpConstants.STRENCRYPTKEY);
		hBean.setSign("");
		hBean.setDeviceType("2");
		hBean.setClientIP(BaseApp.clientid);
		hBean.setOperatingSystemType("2");
		hBean.setOperatingSystemVer("1.0");
		hBean.setMask(BaseApp.mask);


		BaseApp app =  BaseApp.getInstance();
		SharedPreferences sp = app.getSP(LocalConstants.SHARED_PREFERENCE_SETTING, Context.MODE_PRIVATE);
		int lang = sp.getInt("locale",0);
		String lc = "en-US";
		if ( lang < 3){
			lc = BaseApp.localeValues[lang];
		}

		hBean.setLocale(lc);
		
		return hBean;
	}
}
 