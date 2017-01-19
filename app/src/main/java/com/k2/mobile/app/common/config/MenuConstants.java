/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.common.config;

import com.k2.mobile.app.R;

/**
 * @Title LocalConstants.java
 * @Package com.oppo.mo.common.config
 * @Description 常用菜单列表
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-03-27 15:13:00
 * @version V1.0
 */
public class MenuConstants {
	// 菜单名称
	public final static int[] names = {
		R.string.company_news,			R.string.my_address_book,			R.string.my_tasks,		   
		R.string.my_calendar,			R.string.hr_assistant,				R.string.logis_service,         
		R.string.reimbursement,		R.string.consume_query,			R.string.oia_forum,        
		R.string.module_responsible,	R.string.it_service,				R.string.feedback,        
		R.string.troubleshooting,		R.string.ask_for_leave,			R.string.overtime_application, 
		R.string.travel_request,		R.string.access_request,			R.string.leave_application, 
		R.string.benefit_life,			R.string.dormitory_repair,			R.string.dormitory_maintenance,	
		R.string.bus_timetable,		R.string.granducato_hotel,			R.string.sports,
		R.string.beer_and_skittles,	R.string.network_correlation,		R.string.safety_management,
		R.string.certificate_manage,	R.string.process_file,				R.string.air_ticket_reservation,
		R.string.add
	};
	// 菜单图标
	public final static int[] icons = {
		R.drawable.home_news_selector,				R.drawable.home_contacts_selector,		R.drawable.home_task_selector, 
		R.drawable.home_calendar_selector,		R.drawable.home_hr_selector,				R.drawable.home_logistics_selector, 
		R.drawable.home_reim_selector,				R.drawable.home_consu_selector,			R.drawable.home_oia_selector, 
		R.drawable.home_module_leader_selector,	R.drawable.home_it_selector,				R.drawable.home_feedback_selector, 
		R.drawable.home_troubleshooting_selector,	R.drawable.home_ask_leave_selector,		R.drawable.home_overtime_selector, 
		R.drawable.home_business_trip_selector,	R.drawable.home_competence_selector,		R.drawable.home_turnover_selector, 
		R.drawable.home_benefit_life_selector,	R.drawable.home_repair_selector,			R.drawable.home_maintenance_selector,
		R.drawable.home_add_selector,				R.drawable.home_add_selector,				R.drawable.home_add_selector,
		R.drawable.home_add_selector,				R.drawable.home_add_selector,				R.drawable.home_add_selector,
		R.drawable.home_add_selector,				R.drawable.home_approval_file_selector,	R.drawable.home_plane_ticket_selector,
		R.drawable.home_add_selector
	};
	// 菜单编号
	public final static String[] menuCode = {
		HttpConstants.COMPANY_NEW, 			HttpConstants.MY_CONTACTS,				HttpConstants.MY_TASK, 
		HttpConstants.MY_CALENDAR, 			HttpConstants.HR_HELPER,				HttpConstants.LOGSVC, 
		HttpConstants.MY_REIMBURSEMENT, 	HttpConstants.MY_RECORDS_CONSUMPTION,	HttpConstants.OIA_FORUM, 
		HttpConstants.MODULE_RESPONSIBLE,	HttpConstants.IT,						HttpConstants.FEEDBACK,  
		HttpConstants.HANDLING_PROBLE,		HttpConstants.LEAVE_FLOW,				HttpConstants.WORK_OVERTIME_FLOW, 
		HttpConstants.BUSINESS_TRAVEL_FLOW, HttpConstants.ACCESS_FLOW,				HttpConstants.QUIT_FLOW, 
		HttpConstants.BENEFIT_LIFE,			HttpConstants.DREPAIR,					HttpConstants.DMAINTENANCE,
		HttpConstants.BUS_TIMETABLE,		HttpConstants.GHOTEL,					HttpConstants.SPORTS,
		HttpConstants.BANDS,				HttpConstants.NETWORK,					HttpConstants.SAFETY,
		HttpConstants.CERTIFICATE,			HttpConstants.APPROVAL_DOCUMENTS,		HttpConstants.PLANE_TICKET_RESERVE,
		HttpConstants.ADD
	};
}
