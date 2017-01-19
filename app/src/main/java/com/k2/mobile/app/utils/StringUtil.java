package com.k2.mobile.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * @Title StringUtil.java
 * @Package com.oppo.mo.utils
 * @Description 字符串的处理类工具类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-18 16:59:00
 * @version V1.0
 */
public class StringUtil {
    /**
     * 判断是否为null或空值
     *
     * @param str String
     * @return true or false
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 判断str1和str2是否相同
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equals(String str1, String str2) {
        return str1 == str2 || str1 != null && str1.equals(str2);
    }

    /**
     * 判断str1和str2是否相同(不区分大小写)
     *
     * @param str1 str1
     * @param str2 str2
     * @return true or false
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return str1 != null && str1.equalsIgnoreCase(str2);
    }

    /**
     * 判断字符串str1是否包含字符串str2
     *
     * @param str1 源字符串
     * @param str2 指定字符串
     * @return true源字符串包含指定字符串，false源字符串不包含指定字符串
     */
    public static boolean contains(String str1, String str2) {
        return str1 != null && str1.contains(str2);
    }

    /**
     * 判断字符串是否为空，为空则返回一个空值，不为空则返回原字符串
     *
     * @param str 待判断字符串
     * @return 判断后的字符串
     */
    public static String getString(String str) {
        return str == null ? "" : str;
    }
    /**
     * 判断字符串是否包含数字
     *
     * @param number 待判断字符串
     * @return 判断结果
     */
    public static boolean is_number(String number) {
		if(number==null) 
			return false;
	    return number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?");    
	}
    /**
     * 判断字符串是否包含字母
     *
     * @param alpha 待判断字符串
     * @return 判断结果
     */
	public static boolean is_alpha(String alpha) {
		if(alpha==null) 
			return false;
	    return alpha.matches("[a-zA-Z]+");    
	}
	/**
     * 判断字符串是否包含汉字
     *
     * @param chineseContent 待判断字符串
     * @return 判断结果
     */
	public static boolean is_chinese(String chineseContent) {
		if(chineseContent==null) 
			return false;
		return chineseContent.matches("[\u4e00-\u9fa5]");
	}
    /**
     * 判断字符串是否为8位，第一个字符是否为数字或字母，第二位到第八位是否为数字
     *
     * @param str 校验字符串
     * @return 校验是否正确
     */
    public static boolean checkString(String s) {
    	Pattern p = Pattern.compile("^[a-zA-Z0-9\\s]{1}\\d{7}$");  
		Matcher m = p.matcher(s);  
		return m.matches();  
    }
    /**
     * 判断字符串第一个字符是否为数字或字母，第二位到最后一位是否为数字
     *
     * @param str 校验字符串
     * @return 校验是否正确
     */
    public static boolean checkForm(String s) {
    	Pattern p = Pattern.compile("^[a-zA-Z0-9\\s][0-9]*$");  
		Matcher m = p.matcher(s);  
		return m.matches();  
    }
    /**
     * 判断字符串是否为手机号码
     *
     * @param mobiles 校验字符串
     * @return 校验是否正确
     */
    public static boolean checkMobilePhone(String mobiles) {
    	 String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。  
	    if (TextUtils.isEmpty(mobiles)) 
	    	return false;  
	    else 
	    	return mobiles.matches(telRegex);  
    }
    
    /**
     * 验证手机号码
     * @param mobiles
     * @return
     */
    public static boolean checkMobileNumber(String mobileNumber){
        boolean flag = false;
        try{
                Pattern regex = Pattern.compile("^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
                Matcher matcher = regex.matcher(mobileNumber);
                flag = matcher.matches();
            }catch(Exception e){
                flag = false;
            }
        return flag;
    }
    /**
     * 判断字符串是否为固定电话
     *
     * @param mobiles 校验字符串
     * @return 校验是否正确
     */
    public static boolean checkTelephone(String telephone) {
    	 String telRegex = "^(0[0-9]{2,3}/-)?([2-9][0-9]{6,7})+(/-[0-9]{1,4})?$";//  
	    if (TextUtils.isEmpty(telephone)) 
	    	return false;  
	    else 
	    	return telephone.matches(telRegex);  
    }
    /**
     * 判断字符串是否为邮箱
     *
     * @param mobiles 校验字符串
     * @return 校验是否正确
     */
    public static boolean checkEmail(String email) {
    	 String telRegex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";  
	    if (TextUtils.isEmpty(email)) 
	    	return false;  
	    else 
	    	return email.matches(telRegex);  
    }
}

