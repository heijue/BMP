package com.k2.mobile.app.utils;    

import android.content.res.Resources;

import com.k2.mobile.app.R;

/**
 * @Title ErrorCodeContrast.java
 * @Package com.oppo.mo.utilse
 * @Description 错误码对照表
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-12 12:00:00
 * @version V1.0
 */  
public class ErrorCodeContrast {
	/**
	 * @Title: getErrorCode
	 * @Description: 根据错误码返回提示语
	 * @param code 错误码
	 * @param res 资源
	 * @return 提示语 
	 * @throws
	 */
	public static String getErrorCode(String code, Resources res){
		if (code == null || "".equals(code.trim())) {
			return null;
		}
		String iCode = null;
		int iCodes = Integer.parseInt(code);
		switch (iCodes) {
			case 0:
				iCode = res.getString(R.string.unknown_exception);
				break;
			case 3:
				iCode = res.getString(R.string.unknown_version);
				break;
			case 101:
				iCode = res.getString(R.string.user_is_null);
				break;
			case 102:
				iCode = res.getString(R.string.user_name_format_error);
				break;
			case 103:
				iCode = res.getString(R.string.global_password_empty);
				break;
			case 104:
				iCode = res.getString(R.string.password_format_error);
				break;
			case 105:
				iCode = res.getString(R.string.password_length_error);
				break;
			case 106:
				iCode = res.getString(R.string.repeat_input_password);
				break;
			case 107:
				iCode = res.getString(R.string.password_not_match);
				break;
			case 108:
				iCode = res.getString(R.string.iphone_number_not_null);
				break;
			case 109:
				iCode = res.getString(R.string.iphone_number_11);
				break;
			case 110:
				iCode = res.getString(R.string.email_not_null);
				break;
			case 111:
				iCode = res.getString(R.string.email_format_error);
				break;
			case 112:
				iCode = res.getString(R.string.check_code_error);
				break;
			case 113:
				iCode = res.getString(R.string.old_passworf_not_null);
				break;
			case 114:
				iCode = res.getString(R.string.new_passworf_not_null);
				break;
			case 115:
				iCode = res.getString(R.string.repeat_input_new_password);
				break;
			case 116:
				iCode = res.getString(R.string.old_new_password_error);
				break;	
			case 117:
				iCode = res.getString(R.string.username_password_error);
				break;
			case 118:
				iCode = res.getString(R.string.user_not_exist);
				break;
			case 201:
				iCode = res.getString(R.string.client_sign_failure);
				break;
			case 202:
				iCode = res.getString(R.string.client_sign_check_failure);
				break;
			case 203:
				iCode = res.getString(R.string.client_encrypt_failure);
				break;
			case 204:
				iCode = res.getString(R.string.client_decrypt_failure);
				break;
			case 205:
				iCode = res.getString(R.string.server_sign_failure);
				break;
			case 206:
				iCode = res.getString(R.string.server_sign_check_failure);
				break;
			case 207:
				iCode = res.getString(R.string.server_encrypt_failure);
				break;
			case 208:
				iCode = res.getString(R.string.server_dencrypt_failure);
				break;
			case 209:
				iCode = res.getString(R.string.imei_get_failed);
				break;
			case 210:
				iCode = res.getString(R.string.token_failuren);
				break;
			case 211:
				iCode = res.getString(R.string.code_overdue);
				break;
			case 1101:
				iCode = res.getString(R.string.mac_not_exist);
				break;
			case 1102:
				iCode = res.getString(R.string.mac_illegal);
				break;
			case 1103:
				iCode = res.getString(R.string.roken_illegal);
				break;
			case 1104:
				iCode = res.getString(R.string.token_overdue);
				break;
			case 1105:
				iCode = res.getString(R.string.authorization_failed);
				break;
			case 1106:
				iCode = res.getString(R.string.username_not_exist);
				break;	
			case 1107:
						iCode = res.getString(R.string.user_name_format_error);
				break;
			case 1108:
				iCode = res.getString(R.string.user_freezing);
				break;
			case 1109:
				iCode = res.getString(R.string.user_illegal);
				break;
			case 1110:
				iCode = res.getString(R.string.user_exception);
				break;
			case 1111:
				iCode = res.getString(R.string.password_error);
				break;
			case 1112:
				iCode = res.getString(R.string.verification_code_sending_failed);
				break;
			case 1113:
				iCode = res.getString(R.string.number_illegal_not_exist);
				break;
			case 1114:
				iCode = res.getString(R.string.number_bind_other);
				break;
			case 1115:
				iCode = res.getString(R.string.number_bind_not_this);
				break;
			case 1116:
				iCode = res.getString(R.string.old_password_error);
				break;
			case 1117:
				iCode = res.getString(R.string.user_erase);
				break;
			case 1118:
				iCode = res.getString(R.string.user_repeat_bind);
				break;
			case 1119:
				iCode = res.getString(R.string.system_busy);
				break;
			case 1120:
				iCode = res.getString(R.string.other_exception);
				break;
			case 1201:
				iCode = res.getString(R.string.system_data_exception);
				break;	
			case 1202:
				iCode = res.getString(R.string.message_parsing_error);
				break;
			case 1203:
				iCode = res.getString(R.string.msg_check_fail);
				break;
			case 1204:
				iCode = res.getString(R.string.msg_LOGIN_fail);
				break;
			case 1205:
				iCode = res.getString(R.string.msg_param_null);
				break;
			case 1206:
				iCode = res.getString(R.string.server_err);
				break;
			case 1207:
				iCode = res.getString(R.string.frequent_operation);
				break;
			case 1208:
				iCode = res.getString(R.string.incorrect_signature);
				break;
			case 1209:
				iCode = res.getString(R.string.erase_instruction_failure);
				break;
			case 1210:
				iCode = res.getString(R.string.server_data_erasure);
				break;
			case 9999:
				iCode = res.getString(R.string.binding_of_success);
				break;
		}
		return iCode;
	}
}
 