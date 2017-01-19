/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.common.config;

import android.os.Environment;

/**
 * @Title LocalConstants.java
 * @Package com.oppo.mo.common.config
 * @Description 数据字典类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class LocalConstants {

	private final static String TAG = "LocalConstants";

	public final static String CRASH_FILE_PATH = "/sdcard/oppo/crash/";

	public final static String DOWNLOAD_FILE_PATH = "/sdcard/oppo/download";
	// 本地文件存放路径
	public final static String LOCAL_FILE_PATH = Environment.getExternalStorageDirectory() + "/oppo-mo/poto";
	// 没有SD卡时存放路径
	public final static String LOCAL_FILE_PATH_T = Environment.getDataDirectory() + "/oppo-mo/poto";

	public final static String DEFAULT_SPLIT_LINE = "------------------------";
	
	public static final String SHARED_PREFERENCE_LOGIN = "LOGIN_MOMENT_MESSAGE";

	public static final String SHARED_PREFERENCE_SETTING = "SETTING_MOMENT_MESSAGE";

	public static final int VERSION_CODES_KITKAT = 19;

	public static final String API_KEY = "806ba6b85e3eeec87cf911f79f175f7e";

	public static final String SHARED_PREFERENCE_NAME = "SP_SHAKE_MOMENT";

	public static final String MAIN_FOLDER_NAME = "ShakeMoment";

	public static final String CAMERA_FOLDER_NAME = "Camera";

	public static final String IMG_FOLDER_NAME = "Images";

	public static final String LOG_FOLDER_NAME = "Log";

	public static final int COMMON_REFRESH_DELAY_TIME = 1000;

	// Resource
	public static boolean isSDCardAvailable = false;

	public static String USER_NAME = "";

	public static String USER_PASSWORD = "";

	public static String TOKEN = "";

	public static String MASK = "";

	public static String USER_PRO_NAME = "";

	public static String DESTINATION_FOLDER_PATH = "destinationFolderPath";

	public static boolean isOffline = false;

	public static boolean isRefresh = false;

	public static boolean CMCU_WAP_PROXY = false;

	public static boolean CT_WAP = false;

	public static boolean isProxy = false;

	public static boolean IS_USE_GZIP = true;

	// other
	public static boolean IS_OPEN_FILE = false;

	public final static String DEFAULT_CAMERA_PATH = "/Camera";

	public static final String SP_USER_PASSWORD = "SP_USER_PASSWORD";

	public static final String SP_USER_LNAME = "SP_USER_LNAME";

	public static final String SP_USER_ID = "SP_USER_ID";

	public static final String SP_GOLE_COUNT = "SP_GOLE_COUNT";

	public static final String SP_USER_NICK_NAME = "SP_USER_NICK_NAME";

	public static final String SP_USER_AVATAR = "SP_USER_AVATAR";

	public static final String SP_USER_NAME = "SP_USER_NAME";

	public static final String SP_USER_PASS = "SP_USER_PASS";

	public static final String SP_TO_REGISTER = "SP_TO_REGISTER";

	// public static final int SEARCH_SUCCESS = 1001;
	//
	// public static final int SEARCH_FAILED = 1002;
	//
	// public static final int SEARCH_STOP = 1003;

	public static final String SP_SERVER_URL = "SP_SERVER_URL";

	public static final String SP_SERVER_URL_VALUE = "SP_SERVER_URL_VALUE";

	public static final String SP_FIRST_TAG = "SP_FIRST_TAG";

	public static final String SP_IS_FIRST_VISIT = "SP_IS_FIRST_VISIT";

	public static final String SP_LANGUAGE_TYPE = "SP_LANGUAGE_TYPE";

	public static final String SP_LANGUAGE_TYPE_VALUE = "SP_LANGUAGE_TYPE_VALUE";

	public static final String SP_DEFAULT_LOCATION = "SP_DEFAULT_LOCATION";

	public static final String SP_DEFAULT_LOCATION_ID = "SP_DEFAULT_LOCATION_ID";

	public static final String SP_DEFAULT_LOCATION_NAME = "SP_DEFAULT_LOCATION_NAME";

	public static final String SP_CAN_MANAGER = "SP_CAN_MANAGER";

	public static final String SP_CAN_MANAGER_VALUE = "SP_CAN_MANAGER_VALUE";

	public static final String SP_USER = "SP_USER";

	public static final String SP_USER_ID_VALUE = "SP_USER_ID_VALUE";

	public static final String SP_USER_NAME_VALUE = "SP_USER_NAME_VALUE";

	public static final String SP_USER_ADDRESS_VALUE = "SP_USER_ADDRESS_VALUE";

	public static final String SP_USER_EMAIL_VALUE = "SP_USER_EMAIL_VALUE";

	public static final String SP_USER_FIRST_NAME_VALUE = "SP_USER_FIRST_NAME_VALUE";

	public static final String SP_USER_LAST_NAME_VALUE = "SP_USER_LAST_NAME_VALUE";

	public static final String SP_USER_PHONE_VALUE = "SP_USER_PHONE_VALUE";

	public static final String SP_USER_REMARK_VALUE = "SP_USER_REMARK_VALUE";

	public static final String SP_USER_SEX_VALUE = "SP_USER_SEX_VALUE";

	public static final String SP_USER_STATUS_VALUE = "SP_USER_STATUS_VALUE";

	public static final String SP_EMPLOYEE = "SP_EMPLOYEE";

	public static final String SP_EMPLOYEE_ID_VALUE = "SP_EMPLOYEE_ID_VALUE";

	public static final String SP_EMPLOYEE_LOGIN_ID_VALUE = "SP_EMPLOYEE_LOGIN_ID_VALUE";

	public static final String SP_EMPLOYEE_FULL_NAME_VALUE = "SP_EMPLOYEE_FULL_NAME_VALUE";

	public static final String SP_EMPLOYEE_GENDER_VALUE = "SP_EMPLOYEE_GENDER_VALUE";

	public static final String SP_EMPLOYEE_IS_SUPERVISOR_VALUE = "SP_EMPLOYEE_IS_SUPERVISOR_VALUE";

	public static final String SP_EMPLOYEE_LANGUAGE_VALUE = "SP_EMPLOYEE_LANGUAGE_VALUE";

	public static final String SP_EMPLOYEE_LAST_STATUS_VALUE = "SP_EMPLOYEE_LAST_STATUS_VALUE";

	public static final String SP_EMPLOYEE_PROJECT_ID_VALUE = "SP_EMPLOYEE_PROJECT_ID_VALUE";

	public static final String SP_EMPLOYEE_PROJECT_NAME_VALUE = "SP_EMPLOYEE_PROJECT_NAME_VALUE";

	public static final String SP_EMPLOYEE_CURPROJECT_ID_VALUE = "SP_EMPLOYEE_CURPROJECT_ID_VALUE";

	public static final String SP_EMPLOYEE_CURPROJECT_NAME_VALUE = "SP_EMPLOYEE_CURPROJECT_NAME_VALUE";

	public static final String SP_EMPLOYEE_PROJECT_ID_SELECTED_VALUE = "SP_EMPLOYEE_PROJECT_ID_SELECTED_VALUE";

	public static final String SP_EMPLOYEE_PROJECT_NAME_SELECTED_VALUE = "SP_EMPLOYEE_PROJECT_NAME_SELECTED_VALUE";

	public static final String SP_EMPLOYEE_CAN_MANAGER_VALUE = "SP_EMPLOYEE_CAN_MANAGER_VALUE";

	public static final int HTTP_TIMEOUT_CONNECTION = 10000;

	public static final int HTTP_TIMEOUT_SOCKET = 10000;

	public static final int COMMON_UPLOAD_SERVICE = 2000;
	
	/** 数据接口方法代码 */
	// 用户登陆
	public static final int LOGIN_SERVICE = 1001;     
	// 用户绑定
	public static final int BIND_USER_SERVICE = 1002;
	// 短信验证
	public static final int SMS_CHECK_SERVICE = 1003;
	// 用户注销
	public static final int LOGOUT_SERVICE = 1004;
	// 数据擦除
	public static final int DATA_WIPE_SERVER = 1005;
	// 数据擦除结果反馈
	public static final int WIPE_FEED_SERVER = 1006;
	// 查询个人消费
	public static final int MY_CONSUME_SERVER = 1007;
	// 我的任务及待办 - 查询
	public static final int MY_TASK_QUERY_SERVER = 1009;
	// 我的任务及待办 - 查询执行人
	public static final int QUERY_EXECUTE_SERVER = 1010;
	// 我的任务及待办 - 新增
	public static final int ADD_TASK_SERVER = 1011;
	// 我的任务及待办 - 修改状态
	public static final int UPDATE_TASK_STATE_SERVER = 1012;
	// 我的日历 - 查询
	public static final int MY_CALENDAR_QUERY_SERVER = 1013;
	// 我的日历 - 列表查询
	public static final int MY_CALENDAR_LIST_SERVER = 1014;
	// 我的日历 - 删除
	public static final int MY_CALENDAR_DELETE_SERVER = 1015;
	// 我的日历 - 修改
	public static final int MY_CALENDAR_UPDATE_SERVER = 1016;
	// 我的日历 - 增加
	public static final int MY_CALENDAR_ADD_SERVER = 1017;
	// 我的流程 - 提示信息查询
	public static final int MY_PROCESS_PROMPT_MSG_SERVER = 1018;
	// 我的流程 - 需要我审批 - 未审批
	public static final int MY_PROCESS_NOT_APPROVAL_SERVER = 1401;
	// 我的流程 - 需要我审批 － 我已审批
	public static final int MY_PROCESS_APPROVAL_SERVER = 1402;
	// 我的流程 - 我发起的流程 － 拟制中
	public static final int MY_PROCESS_FICTION_SERVER = 1403;
	// 我的流程 - 我发起的流程 － 审核中
	public static final int MY_PROCESS_AUDIT_SERVER = 1404;
	// 我的流程 - 我发起的流程 － 已通过
    public static final int MY_PROCESS_PASSED_SERVER = 1405;
	// 我的流程 - 我发起的流程 － 同意
	public static final int MY_PROCESS_AGREE_SERVER = 1406;
	// 我的流程 - 需要我审批 - 退回环节信息
	public static final int MY_PROCESS_RETUEN_NODE_SERVER = 1407;
	// 我的流程 - 需要我审批 - 不同意
	public static final int MY_PROCESS_DELETE_APPROVAL_SERVER = 1408;
	// 我的流程 - 我发起的 - 删除
	public static final int DELETE_START_FLOW_SERVER = 1409;
	// 我的流程 - 我发起的 - 送审
	public static final int TRIAL_START_FLOW_SERVER = 1410;
	// 我的流程 - 查询
	public static final int MY_PROCESS_QUERY_SERVER = 1024;
	// 我的流程 - 修改工作状态
	public static final int MY_PROCESS_UP_STATE_SERVER = 1025;
	// 通讯录 - 员工通讯列表
	public static final int USER_COMMUNICATION_LIST_SERVER = 1026;
	// 通讯录 - 员工通讯详情
	public static final int USER_COMMUNICATION_DETAIL_SERVER = 1027;
    // 用户绑定
	public static final int USER_VAILDATA_SERVER = 1028;
	// 查询个人消费
	public static final int EXPENSE_SCHEDULE_SERVER = 1029;
	// 搜索
	public static final int SEARCH_SERVER = 1030;
	// 搜索
	public static final int QUERY_TASK_INFO_SERVER = 1031;
	// 个人中心 - 详细资料
	public static final int PERSONAL_CENTER_DETAILS_SERVER = 1032;
	// 个人中心 - 修改资料
	public static final int PERSONAL_CENTER_MODIFY_DATA_SERVER = 1033;
	// 个人中心 - 修改头像
	public static final int PERSONAL_CENTER_MODIFY_AVATAR_SERVER = 1034;
	// 我的邮件 - 收件箱 - 内部邮件
	public static final int INBOX_INTERNAL_EMAIL_SERVER = 1100;
	// 我的邮件 - 首页菜单
	public static final int EMAIL_HOME_SERVER = 1101;
	// 我的邮件 - 收邮件和自定义邮件
	public static final int EMAIL_RECEIVE_SERVER = 1102;
	// 我的邮件 - 获取通信录
	public static final int EMAIL_CONTACTS_SERVER = 1103;
	// 我的邮件 - 发送邮件
	public static final int EMAIL_SEND_EMAIL_SERVER = 1104;
	// 我的邮件 -邮件详情
	public static final int EMAIL_EMAIL_INFO_SERVER = 1105;
	// 我的邮件 -批量编辑
	public static final int EMAIL_DEL_SERVER = 1106;
	// 我的邮件 - 标志已读
	public static final int EMAIL_ISREAD_SERVER = 1107;
	// 我的邮件 - 转为任务
	public static final int EMAIL_ADD_TASK_SERVER = 1108;
	// 我的邮件 - 邮件查询
	public static final int EMAIL_SEARCH_SERVER = 1109;
	// 获取常用菜单
	public static final int GET_COMMON_MENU_SERVER = 1110;
	// 公司新闻 - 列表查询
	public static final int NEW_LIST_SERVER = 1200;
	// 公司新闻 - 详情
	public static final int NEW_INFO_SERVER = 1201;
	// 公司新闻 - 流程文件
	public static final int NEW_PROCESS_SERVER = 1202;
	// 公司新闻 - 新闻详情
	public static final int NEW_ONLY_INFO_SERVER = 1203;
	// 查询常用菜单列表
	public static final int MUNE_COMMONLY_LIST_SERVER = 1300;
	// 添加常用菜单列表
	public static final int MUNE_COMMONLY_ADD_SERVER = 1301;
	// 删除常用菜单列表
	public static final int MUNE_COMMONLY_DEL_SERVER = 1302;
	// 菜单所有列表
	public static final int MUNE_ALL_LIST_SERVER = 1304;
	// 菜单更多页面所有列表
	public static final int MUNE_MORE_ALL_LIST_SERVER = 1305;
	// 子菜单列表
	public static final int MUNE_SUB_LIST_SERVER = 1306;
	// 程序自动更新
	public static final int GET_APP_VERSION_SERVER = 1400;
	// 问题反馈-列表
	public static final int FEEDBACK_LIST_SERVER = 1500;
	// 问题反馈-类别
	public static final int FEEDBACK_CLASS_SERVER = 1501;
	// 问题反馈-问题类型
	public static final int FEEDBACK_TYPE_SERVER = 1502;
	// 问题反馈-新建和编辑时保存、提交
	public static final int FEEDBACK_OPER_SERVER = 1503;
	// 问题反馈-问题详情
	public static final int FEEDBACK_INFO_SERVER = 1504;
	// 问题反馈-删除和关闭
	public static final int FEEDBACK_DEL_SERVER = 1505;
	// 问题反馈-评价
	public static final int FEEDBACK_EVALUATION_SERVER = 1506;
	// 问题反馈-预处理问题反馈
	public static final int FEEDBACK_PRETREATMENT_SERVER = 1507;
	// 问题反馈-处理问题反馈
	public static final int FEEDBACK_PROCESS_SERVER = 1508;
	//获取工作待办数量
	public static final int WORK_COUNT_SERVER = 1509; 
	// 宿舍报修
	public static final int DORMITORY_REPAIR_SERVER = 1600;
	// 宿舍报修详情
	public static final int DORMITORY_REPAIR_DETAIL_SERVER = 1601;
	// 添加宿舍报修
	public static final int DORMITORY_REPAIR_ADD_SERVER = 1602;
	// 宿舍报修删除
	public static final int DORMITORY_REPAIR_DEL_SERVER = 1603;
	// 宿舍报修关闭
	public static final int DORMITORY_REPAIR_CLOSE_SERVER = 1604;
	// 宿舍报修评价
	public static final int DORMITORY_REPAIR_EVALUATION_SERVER = 1605;
		
	public static final int GET_SUBMIT_SERVICE = 2000;
}
