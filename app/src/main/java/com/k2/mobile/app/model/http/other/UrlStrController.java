/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.other;

import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.DataException;

/**
 * @Title UrlStrController.java
 * @Package com.oppo.mo.model.http
 * @Description 接口地址封装类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class UrlStrController {
	/**
	 * @Title: formatUrlStr
	 * @Description: char类型转byte类型
	 * @param code 错误码
	 * @return int 对应String的ID  
	 * @throws
	 */
	public static String formatUrlStr(String url, int mode) {
		// 返回结果
		String result = null;
		try {
			// 获取已设置的URL
			String serverip = url;
			if(null == serverip){
				serverip = "";
			}
			switch (mode) {
				// 登陆
				case LocalConstants.LOGIN_SERVICE: 
					result = serverip + "/mo/login.do";
					break;
				// 绑定用户
				case LocalConstants.BIND_USER_SERVICE: 
					result = serverip + "/mo/getEmailOrPhone.do";
					break;
				// 发送验证码
				case LocalConstants.SMS_CHECK_SERVICE: 
					result = serverip + "/mo/checkCode.do";
					break;
				// 注销用户
				case LocalConstants.LOGOUT_SERVICE: 
					result = serverip + "/mo/logout.do";
					break;
				// 数据擦除
				case LocalConstants.DATA_WIPE_SERVER: 
					result = serverip + "/mo/wipe.do";
					break;
				// 擦除信息反馈
				case LocalConstants.WIPE_FEED_SERVER: 
					result = serverip + "/mo/wipeFeedback.do";
					break;
				// 个人消费
				case LocalConstants.MY_CONSUME_SERVER: 
					result = serverip + "/mo/myConsume.do";
					break;
				// 我的任务及待办 - 查询
				case LocalConstants.MY_TASK_QUERY_SERVER: 
					result = serverip + "/mo/myTask.do";
					break;
				// 我的任务及待办 - 查询执行人
				case LocalConstants.QUERY_EXECUTE_SERVER: 
					result = serverip + "/mo/myTaskUserList.do";
					break;
				// 我的任务及待办 - 新增
				case LocalConstants.ADD_TASK_SERVER: 
					result = serverip + "/mo/myTaskAdd.do";
					break;
				// 我的任务及待办 - 修改状态
				case LocalConstants.UPDATE_TASK_STATE_SERVER: 
					result = serverip + "/mo/myTaskEdit.do";
					break;
			    // 我的日历 - 查询
				case LocalConstants.MY_CALENDAR_QUERY_SERVER: 
					result = serverip + "/mo/myCalendar.do";
					break;
				// 我的日历 - 列表查询
				case LocalConstants.MY_CALENDAR_LIST_SERVER: 
					result = serverip + "/mo/myCalendarList.do";
					break;
				// 我的日历 - 删除
				case LocalConstants.MY_CALENDAR_DELETE_SERVER: 
					result = serverip + "/mo/delCalendar.do";
					break;
				// 我的日历 - 修改
				case LocalConstants.MY_CALENDAR_UPDATE_SERVER: 
					result = serverip + "/mo/updateCalendar.do";
					break;
				// 我的日历 - 增加
				case LocalConstants.MY_CALENDAR_ADD_SERVER: 
					result = serverip + "/mo/addCalendar.do";
					break;
				// 我的流程查询 - 增加
				case LocalConstants.MY_PROCESS_QUERY_SERVER: 
					result = serverip + "/mo/myFlow.do";
					break;
				// 我的流程 - 提示信息查询
				case LocalConstants.MY_PROCESS_PROMPT_MSG_SERVER: 
					result = serverip + "";
					break;
				// 我的流程 - 需要我审批 - 未审批
				case LocalConstants.MY_PROCESS_NOT_APPROVAL_SERVER: 
					result = serverip + "/mo/getMyFlowList.do";
					break;
				// 我的流程 - 需要我审批 － 我已审批
				case LocalConstants.MY_PROCESS_APPROVAL_SERVER: 
					result = serverip + "/mo/getK2WorkItemLogs.do";
					break;
				// 我的流程 - 我发起的流程 － 拟制中
				case LocalConstants.MY_PROCESS_FICTION_SERVER: 
					result = serverip + "/mo/fictionMyFlow.do";
					break;
				// 我的流程 - 我发起的流程 － 审核中
				case LocalConstants.MY_PROCESS_AUDIT_SERVER: 
					result = serverip + "/mo/getAuditintProcessInstances.do";
					break;
				// 我的流程 - 我发起的流程 － 已通过
				case LocalConstants.MY_PROCESS_PASSED_SERVER: 
					result = serverip + "/mo/getApprovedProcessInstances.do";
					break;
				// 我的流程 - 我发起的流程 － 同意
				case LocalConstants.MY_PROCESS_AGREE_SERVER: 
					result = serverip + "/mo/agreeMyFlow.do";
					break;
				// 我的流程 - 需要我审批 - 退回环节信息
				case LocalConstants.MY_PROCESS_RETUEN_NODE_SERVER: 
					result = serverip + "/mo/disagreeMyFlow.do";
					break;
				// 我的流程 - 需要我审批 - 不同意
				case LocalConstants.MY_PROCESS_DELETE_APPROVAL_SERVER: 
					result = serverip + "/mo/disagreeMyFlow.do";
					break;
				// 我的流程 - 我发起的 - 删除
				case LocalConstants.DELETE_START_FLOW_SERVER: 
					result = serverip + "/mo/delMyFlow.do";
					break;	
				// 我的流程 - 我发起的 - 送审
				case LocalConstants.TRIAL_START_FLOW_SERVER: 
					result = serverip + "/mo/submittalMyFlow.do";
					break;		
				// 我的流程查询 - 修改工作状态
				case LocalConstants.MY_PROCESS_UP_STATE_SERVER: 
					result = serverip + "/mo/updateMyFlowState.do";
					break;
				// 通讯录 - 员工通讯列表
				case LocalConstants.USER_COMMUNICATION_LIST_SERVER:
					result = serverip + "/mo/myAddressBookList.do";
					break;
				// 通讯录 - 员工通讯详情
				case LocalConstants.USER_COMMUNICATION_DETAIL_SERVER:
					result = serverip + "/mo/myAddressBookDetail.do";
					break;
				// 获取常用菜单
				case LocalConstants.GET_COMMON_MENU_SERVER:
					result = serverip + "/mo/allChildMenus.do";
					break;
				case LocalConstants.USER_VAILDATA_SERVER:
					result = serverip + "/mo/vaildata.do";
					break;
				case LocalConstants.EXPENSE_SCHEDULE_SERVER:
					result = serverip + "/mo/myExpenseSchedule.do";
					break;
				case LocalConstants.SEARCH_SERVER:
					result = serverip + "/mo/search.do";
					break;
				case LocalConstants.QUERY_TASK_INFO_SERVER:
					result = serverip + "/mo/myTaskDetail.do";
					break;
				// 我的邮件 - 收件箱 -内部邮件
				case LocalConstants.INBOX_INTERNAL_EMAIL_SERVER:
//					result = serverip + "/mo/";
					break;
				// 个人中心 - 详细资料
				case LocalConstants.PERSONAL_CENTER_DETAILS_SERVER:
					result = serverip + "/mo/userInfo.do";
					break;
				// 个人中心 - 修改资料
				case LocalConstants.PERSONAL_CENTER_MODIFY_DATA_SERVER:
					result = serverip + "/mo/updateUserInfo.do";
					break;
				// 个人中心 - 修改头像
				case LocalConstants.PERSONAL_CENTER_MODIFY_AVATAR_SERVER:
					result = serverip + "/mo/headPortrait.do";
					break;
				// 我的邮件 - 首页菜单
				case LocalConstants.EMAIL_HOME_SERVER:
					result = serverip + "/mo/internalMailFolder.do";
					break;
				// 我的邮件 - 收邮件和自定义邮件
				case LocalConstants.EMAIL_RECEIVE_SERVER:
					result = serverip + "/mo/internalMail.do";
					break;
				// 我的邮件 - 转为任务
				case LocalConstants.EMAIL_ADD_TASK_SERVER:
					result = serverip + "/mo/internalMailToTask.do";
					break;
				// 我的邮件 - 收邮件和自定义邮件
				case LocalConstants.EMAIL_CONTACTS_SERVER:
					result = serverip + "/mo/internalMailContacts.do";
					break;
				case LocalConstants.EMAIL_SEND_EMAIL_SERVER:
					result = serverip + "/mo/internalMailAdd.do";
					break;
				case LocalConstants.EMAIL_EMAIL_INFO_SERVER:
					result = serverip + "/mo/internalMailDetail.do";
					break;
				case LocalConstants.EMAIL_DEL_SERVER:
					result = serverip + "/mo/internalMailBatch.do";
					break;
				case LocalConstants.EMAIL_ISREAD_SERVER:
					result = serverip + "/mo/internalMailBatchRead.do";
					break;
				// 我的邮件 - 邮件查询
				case LocalConstants.EMAIL_SEARCH_SERVER:
					result = serverip + "/mo/searchAll.do";
					break;
				// 公司新闻 - 列表查询
				case LocalConstants.NEW_LIST_SERVER:
					result = serverip + "/mo/companyNewsList.do";
					break;
				// 公司新闻 - 详情 
				case LocalConstants.NEW_INFO_SERVER:
					result = serverip + "/mo/processFormInit.do";
					break;
				// 公司新闻 - 新闻详情 
				case LocalConstants.NEW_ONLY_INFO_SERVER:
					result = serverip + "/mo/newsDetailInit.do";
					break;
				// 公司新闻 - 流程文件
				case LocalConstants.NEW_PROCESS_SERVER:
					result = serverip + "/mo/processNewsList.do";
					break;
				// 查询常用菜单列表
				case LocalConstants.MUNE_COMMONLY_LIST_SERVER: 
					result = serverip + "/mo/customMenus.do";
					break;
				// 添加常用菜单列表
				case LocalConstants.MUNE_COMMONLY_ADD_SERVER: 
					result = serverip + "/mo/customMenuAdd.do";
					break;
				// 删除常用菜单列表
				case LocalConstants.MUNE_COMMONLY_DEL_SERVER: 
					result = serverip + "/mo/customMenuDel.do";
					break;
				// 查询用户所有菜单列表
				case LocalConstants.MUNE_ALL_LIST_SERVER: 
					result = serverip + "/mo/menu.do";
					break;
				// 查询更多页面所有菜单列表
				case LocalConstants.MUNE_MORE_ALL_LIST_SERVER: 
					result = serverip + "/mo/allCustomMenus.do";
					break;
				// 查询子菜单列表
				case LocalConstants.MUNE_SUB_LIST_SERVER: 
					result = serverip + "/mo/menu.do";
					break;
				// 程序自动更新
				case LocalConstants.GET_APP_VERSION_SERVER: 
					result = serverip + "/mo/appVersion.do";
					break;
				// 问题反馈-列表
				case LocalConstants.FEEDBACK_LIST_SERVER:
					result = serverip + "/mo/questionFeedbacks.do";
					break;
				// 问题反馈-类别
				case LocalConstants.FEEDBACK_CLASS_SERVER:
					result = serverip + "/mo/questionCategories.do";
					break;
				// 问题反馈-问题类型
				case LocalConstants.FEEDBACK_TYPE_SERVER:
					result = serverip + "/mo/questionClasses.do";
					break;
				// 问题反馈-新建和编辑时保存、提交
				case LocalConstants.FEEDBACK_OPER_SERVER:
					result = serverip + "/mo/questionFeedbackCreate.do";
					break;
				// 问题反馈-问题详情
				case LocalConstants.FEEDBACK_INFO_SERVER:
					result = serverip + "/mo/questionFeedbackDetail.do";
					break;
				// 问题反馈-删除和关闭
				case LocalConstants.FEEDBACK_DEL_SERVER:
					result = serverip + "/mo/questionFeedbackDel.do";
					break;
				// 问题反馈-评价
				case LocalConstants.FEEDBACK_EVALUATION_SERVER:
					result = serverip + "/mo/questionFeedbackAppraise.do";
					break;
				// 问题反馈-预处理问题反馈
				case LocalConstants.FEEDBACK_PRETREATMENT_SERVER:
					result = serverip + "/mo/questionFeedbackPreHandle.do";
					break;
				// 问题反馈-处理问题反馈
				case LocalConstants.FEEDBACK_PROCESS_SERVER:
					result = serverip + "/mo/questionFeedbackHandle.do";
					break;
				// 获取工作待办数量
				case LocalConstants.WORK_COUNT_SERVER:
					result = serverip + "/mo/getWorkNum.do";
					break;
				// 宿舍报修
				case LocalConstants.DORMITORY_REPAIR_SERVER:
					result = serverip + "/mo/dormRepair.do";
					break;
				// 宿舍报修详情
				case LocalConstants.DORMITORY_REPAIR_DETAIL_SERVER:
					result = serverip + "/mo/dormRepairDetail.do";
					break;
				// 添加宿舍报修
				case LocalConstants.DORMITORY_REPAIR_ADD_SERVER:
					result = serverip + "/mo/dormRepairAdd.do";
					break;
				// 宿舍报修删除
				case LocalConstants.DORMITORY_REPAIR_DEL_SERVER:
					result = serverip + "/mo/dormRepairDel.do";
					break;
				// 宿舍报修关闭
				case LocalConstants.DORMITORY_REPAIR_CLOSE_SERVER:
					result = serverip + "/mo/dormRepairClose.do";
					break;
				// 评价宿舍报修
				case LocalConstants.DORMITORY_REPAIR_EVALUATION_SERVER:
					result = serverip + "/mo/dormRepairToRated.do";
					break;	
				default: 
					result = "";
					break;
			}
		}
		catch (Exception e) {
			throw new DataException(e);
		}

		return result;
	}
}
