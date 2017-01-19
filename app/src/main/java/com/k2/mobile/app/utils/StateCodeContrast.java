package com.k2.mobile.app.utils;    

import android.content.res.Resources;

import com.k2.mobile.app.R;

/**
 * @Title StateCodeContrast.java
 * @Package com.oppo.mo.utils
 * @Description 状态信息对照表
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-31 17:00:00
 * @version V1.0
 */  
public class StateCodeContrast {
	/**
	 * @Title: getStartCode
	 * @Description: 根据状态值获取提示语
	 * @param code 状态码
	 * @param res 资源
	 * @return 提示语 
	 * @throws
	 */
	public static String getStartCode(String code, Resources res){
		if (code == null || "".equals(code.trim())) {
			return null;
		}
		String iCode = null;
		if ("CANCELLED".equals(code)) {
			iCode = res.getString(R.string.cancelled);
		}else if ("EMPAPPR".equals(code)) {
			iCode = res.getString(R.string.empappr);
		}else if ("HOLD_PENDING_RECEIPTS".equals(code)) {
			iCode = res.getString(R.string.hold_pending_receipts);
		}else if ("INPROGRESS".equals(code)) {
			iCode = res.getString(R.string.inprogress);
		}else if ("INVOICED".equals(code)) {
			iCode = res.getString(R.string.invoiced);
		}else if ("IN_PARENT_PACKET".equals(code)) {
			iCode = res.getString(R.string.in_parent_packet);
		}else if ("MGRPAYAPPR".equals(code)) {
			iCode = res.getString(R.string.mgrpayappr);
		}else if ("PAID".equals(code)) {
			iCode = res.getString(R.string.paid);
		}else if ("PARPAID".equals(code)) {
			iCode = res.getString(R.string.parpaid);
		}else if ("PAYAPPR".equals(code)) {
			iCode = res.getString(R.string.payappr);
		}else if ("PENDING_IMAGE_SUBMISSION".equals(code)) {
			iCode = res.getString(R.string.pending_image_submission);
		}else if ("PEND_HOLDS_CLEARANCE".equals(code)) {
			iCode = res.getString(R.string.pend_holds_clearance);
		}else if ("REJECTED".equals(code)) {
			iCode = res.getString(R.string.rejected);
		}else if ("RESOLUTN".equals(code)) {
			iCode = res.getString(R.string.resolutn);
		}else if ("RETURNED".equals(code)) {
			iCode = res.getString(R.string.returned);
		}else if ("SAVED".equals(code)) {
			iCode = res.getString(R.string.saved);
		}else if ("SUBMITTED".equals(code)) {
			iCode = res.getString(R.string.submitted);
		}else if ("UNUSED".equals(code)) {
			iCode = res.getString(R.string.unused);
		}else if ("WITHDRAWN".equals(code)) {
			iCode = res.getString(R.string.withdrawn);
		}else if ("PENDMGR".equals(code)) {
			iCode = res.getString(R.string.pendmgr);
		}else if ("ERROR".equals(code)) {
			iCode = res.getString(R.string.error);
		}else if ("MGRAPPR".equals(code)) {
			iCode = res.getString(R.string.mgrappr);
		} 
		
		return iCode;
	}
}
 