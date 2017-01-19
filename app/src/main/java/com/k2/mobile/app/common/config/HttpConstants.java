/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.common.config;

/**
 * @Title HttpConstants.java
 * @Package com.oppo.mo.common.config
 * @Description 接口配置类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class HttpConstants {
//  测试服务器地址
//	public static final String WEBSERVICE_URL = "http://172.16.106.207:8090/MobileService/InvokeAction";
	// 域名
	public static String DOMAIN_NAME = null;
	// 端口
	public static String PROT = null;
	// 站点
	public static String SITE = null;
	// 方法路径
	public static String METHOD = "/MobileService/InvokeAction";
	// 新闻
	public static String NEWSURL = "/Admin/News/NewContents/Detail";
	// 新闻
	public static String POPULAR = "/Admin/PopularInfo/PopularDetail/ContentDetail";
	// 请求地址
	public static String WEBSERVICE_URL = null ;
	//	文件服务器
	public static final String FILE_URL = "http://motest.myoas.com:10088/home/upfiles";
	//	惠生活
	public static final String STATIC_LINK_URL = "http://motest.myoppo.com:8888/mo/huiLife.do?module=";
	//	HR助手
	public static final String HR_STATIC_LINK_URL = "http://motest.myoppo.com:8888/mo/hrQuery.do?module=";
	//	忘记密码
	public static final String FORGET_PSD_LINK_URL = "http://motest.myoppo.com:8888/mo/pwdTip.do";
	
	// 固定密钥
	public static final String STRENCRYPTKEY = "KSTAR@K2"; 
	// 
	public static final String SECRECT_KEY = "343325d4b1ee3e09f79a72da31bbcaf3";
	
	public static double SYNCINFO_COUNTDOWN = 0;
	
	public static int CLOCK_COUNTDOWN = 60;

	public static final String PARAMETER_TOKEN = "◎";

	/*我的通讯录**/
	public static String MY_CONTACTS = "f7787c1a7be94a42ae9eacf7299a45ec";
	/* 公司新闻菜单 */
	public static String COMPANY_NEW = "8ddf37f226714332a81f24ce8f958523"; 
	/*热门咨询 */
	public static String POPULAR_NEW = "5481db9c8139494ea47b633fa2c08aff";   

	 
	/* 我的申请 */
	public static String MY_PROCESS = "856b60a9474846549d2a41bb466d479c";
	/* 我的审批 */
	public static String PORTAL = "4d13dffa5f19488aaad22ab55c2cdb76";	 
	
	/* 问题反馈 */
	public static String FEEDBACK = "80844c30da36421e8e0570857e045656"; 
	/* HR助手 */
	public static String HR_HELPER = "5481db9c8139494ea47b633fa2c08aff";//"7051355848e848b6b507b096b5e91964";
	/* 添加 */
	public static String ADD = "o93ljb0er30743ea9d7d255490c7we3r";
	/* 请假流程 */
	public static String LEAVE_FLOW = "73296c4955744116a67a825705d8a5e9"; 
	/* 出差流程 */
	public static String BUSINESS_TRAVEL_FLOW = "0bc80ec4512e494ba1b48c14e78b2f99"; 
	/* 报销申请 */
	public static String ACCESS_FLOW = "575cd33b0e694c45b4b49df9d8ddf231";
	/* 个人办公 */
	public static String PERSONAL_OFFICE = "eb71a7a55bdf4dd789de1636f920d555";
	/* 服务支持 */
	public static String SERVICE_SUPPORT = "6264ac41b0844935839a5f93b362243e";
	/* 发起申请 */
	public static String PROCESS_ENTRANCE = "a0340febc456438ca4c90952a34203cf";
	/* 其它 */
	public static String OTHER_MENU = "f2b7906b7b004248b05c2484481a863a";
	
	/*我的日历**/
	public static String MY_CALENDAR = "93d264cb52384baabc6e3e943aa4a63d";
	/*我的邮件**/
	public static String MY_EMAIL = "3bb264d0314c4673bb0170cbaae99612";
	/*我的消费记录**/
	public static String MY_RECORDS_CONSUMPTION = "a80b164e1a164f5289cb38f187109ca1";//"741ac066df8f4074a33287619dcd5890";
	/* 费用报销 */
	public static String MY_REIMBURSEMENT = "491bfad6a0cb42628a2908ed43f04ca2";
	/* 我的任务 */
	public static String MY_TASK = "e33ac94dd2444b60a6e9b20ce69aaa7e";
	/* 审批文件 */
	public static String APPROVAL_DOCUMENTS = "abd067f541d54ccda1dea122d15edb94";
	/* 应用汇总菜单 */
	public static String COLLECT = "928d82dfbdf4c94b80b21df8f3da3cb6";
	/* OIA论坛菜单 */
	public static String OIA_FORUM = "12571ae1481c4666ae6500135a964374";
	/* 帮助菜单 */
	public static String HELP = "b7e6292d8baa4dee846fd68516ee7943";
	/* 处理问题 */
	public static String HANDLING_PROBLE = "fef5d0036e0f4c069c1397869461aaeb";
	/* 后勤服务 */
	public static String LOGSVC = "7fb79c1102a94c89b0dd4e0f2d24e62c"; 
	/* 宿舍报修 */
	public static String DREPAIR = "26f8a9fbd48d4d20be9b41136540787c"; 
	/* 宿舍维修 */
	public static String DMAINTENANCE = "030569d967b4475a898f7f847fc68f57"; 
	/* 惠生活 */
	public static String BENEFIT_LIFE = "da80a8384ce54699adde0d931b0bea4d"; 
	/* 住宿旅馆 */
	public static String GHOTEL = "0a591c9ccec14bcebc2f436a094aa34e"; 
	/* 吃喝玩乐 */
	public static String BANDS = "714f8977629f46b6ab5b06671d4db8a5"; 
	/* 安全管理 */
	public static String SAFETY = "ce6d3402cde343f5a3cb3b5e84e15053"; 
	/* 证件办理 */
	public static String CERTIFICATE = "0ff3adec755d4a6cb05227c1b148d3be"; 
	/* 班车时刻表 */
	public static String BUS_TIMETABLE = "b6e7290991da4df6897f880ca24c53d0"; 
	/* 体育运动 */
	public static String SPORTS = "ab06b840d5504d1fa38496ffd86b4730"; 
	/* 网络相关 */
	public static String NETWORK = "5336fd3575e54734a5ff3f6ad677b2c0"; 
	/* HR服务 */
	public static String HR = "b1a33ff12e6d4e468a9dc93c9176ac62";
	/* IT服务 */
	public static String IT = "6aae5fab230b49338df4fc281a981b90";
	/* 模块负责人 */
	public static String MODULE_RESPONSIBLE = "19639b04470743ea9d7d255490c7490f";
	/* 加班流程 */
	public static String WORK_OVERTIME_FLOW = "ce4984ed7aaa4f79b8357aa4a64bee8f";
	/* 离职申请 */
	public static String QUIT_FLOW = "668cb494a88d4e37b1d9eae8286f7990";
	/* 机票预定 */
	public static String PLANE_TICKET_RESERVE = "a94c454b229f4a6abc04e7f658769780";
}
