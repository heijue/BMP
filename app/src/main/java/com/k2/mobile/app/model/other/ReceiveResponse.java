package com.k2.mobile.app.model.other;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.k2.mobile.app.model.bean.ActionsBean;
import com.k2.mobile.app.model.bean.BaseInfoBean;
import com.k2.mobile.app.model.bean.BizInfoBean;
import com.k2.mobile.app.model.bean.DataBean;
import com.k2.mobile.app.model.bean.ExtendInfoBean;
import com.k2.mobile.app.model.bean.GroupBean;
import com.k2.mobile.app.model.bean.HeaderBean;
import com.k2.mobile.app.model.bean.ItemsBean;
import com.k2.mobile.app.model.bean.MoreBean;
import com.k2.mobile.app.model.bean.ProcBaseInfoBean;
import com.k2.mobile.app.model.bean.ProcLogInfoBean;
import com.k2.mobile.app.model.bean.ResultBean;
import com.k2.mobile.app.model.bean.RowsBean;
import com.k2.mobile.app.model.bean.TaskItemBean;
import com.k2.mobile.app.model.bean.TaskListBean;
import com.k2.mobile.app.model.bean.TaskListItem;

import android.util.Log;
/**
 * Get Service Data
 * 
 * @author ZYB2
 * 
 */
public class ReceiveResponse {
	/**
	 * 判断json数据格式是否正确
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isJSONNull(String object) {
		if (object == null || "".equals(object) || "[]".equals(object)
				|| "{}".equals(object) || "null".equals(object)) {
			return true;
		}
		return false;
	}

	/**
	 * GetTaskList
	 * 
	 * @param jsonResult
	 * @return object
	 * @throws JSONException
	 * 
	 */
	public static List<TaskListBean> getTaskList(String jsonResult) {

		BaseInfoBean bBean = null;

		ExtendInfoBean eBean = null;

		TaskListBean tlBean = null;

		List<TaskListBean> tlBeanList;

		List<ItemsBean> itemBeanList;

		try {
			if (isJSONNull(jsonResult)) {
				return null;
			} else {
                 Log.i("version", jsonResult);
				tlBeanList = new ArrayList<TaskListBean>();
				JSONArray JSONArrtaskList = new JSONArray(jsonResult);
 
				for (int i = 0; i < JSONArrtaskList.length(); i++) {
					JSONObject jsonObjBase = (JSONObject) JSONArrtaskList
							.get(i);

					JSONObject jsonObjBaseInfo = jsonObjBase
							.getJSONObject(BaseInfoBean.BASEINFO);

					JSONObject jsonObjExtendInfo = jsonObjBase
							.getJSONObject(ExtendInfoBean.EXTENDINFO);

					JSONArray jsonArryBaseInfo = jsonObjBaseInfo
							.getJSONArray(ItemsBean.ITEMS);

					JSONArray jsonArryExtendInfo = jsonObjExtendInfo
							.getJSONArray(ItemsBean.ITEMS);

					tlBean = new TaskListBean();

					itemBeanList = new ArrayList<ItemsBean>();

					if (jsonArryBaseInfo != null) {
						bBean = new BaseInfoBean();

						for (int j = 0; j < jsonArryBaseInfo.length(); j++) {
							JSONObject jsonObj = (JSONObject) jsonArryBaseInfo
									.get(j);
							itemBeanList.add(getItemBean(jsonObj));
						}
						bBean.setItemList(itemBeanList);
						tlBean.setBaseInfoBean(bBean);
					}
					if (jsonArryExtendInfo != null) {
						eBean = new ExtendInfoBean();
						for (int j = 0; j < jsonArryExtendInfo.length(); j++) {
							JSONObject jsonObj = (JSONObject) jsonArryExtendInfo
									.get(j);
							itemBeanList.add(getItemBean(jsonObj));
						}
						eBean.setItemList(itemBeanList);

						tlBean.setExtendInfoBean(eBean);
					}

					tlBeanList.add(tlBean);

				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tlBeanList;
	}

	/**
	 * GetTaskListFormat
	 * 
	 * @param taskList
	 * @return
	 */
	public static List<TaskListItem> forMatTaskList(List<TaskListBean> taskList) {

		BaseInfoBean baseBean = null;

		ExtendInfoBean extendBean = null;

		TaskListItem listItem = null;

		List<TaskListItem> taskListItem = new ArrayList<TaskListItem>();
		taskList = taskList == null ? new ArrayList<TaskListBean>() : taskList;

		for (int i = 0; i < taskList.size(); i++) {
				baseBean = taskList.get(i).getBaseInfoBean();

				extendBean = taskList.get(i).getExtendInfoBean();

				listItem = new TaskListItem();

				if (baseBean != null) {
					List<ItemsBean> beanList = baseBean.getItemList();
					for (int j = 0; j < beanList.size(); j++) {
						ItemsBean item = beanList.get(j);
						if (item.getName().equals("ProcessName")) {
							listItem.setProcessName(item.getValue());
						} else if (item.getName().equals("Folio")) {
							listItem.setFolio(item.getValue());
						} else if (item.getName().equals("StartDate")) {
							listItem.setStartDate(item.getValue());
						} else if (item.getName().equals("SN")) {
							listItem.setsN(item.getValue());
						} else if (item.getName().equals("Destination")) {
							listItem.setDestination(item.getValue());
						} else if (item.getName().equals("DisplayName")) {
							listItem.setDisplayName(item.getValue());
						}else if(item.getName().equals("FormId")){
							listItem.setFormId(item.getValue());
						}else if(item.getName().equals("ActivityId")){
							listItem.setActivityId(item.getValue());
						}
					}
				}
				if (extendBean != null) {
					List<ItemsBean> beanList = extendBean.getItemList();
					for (int j = 0; j < beanList.size(); j++) {
						ItemsBean item = beanList.get(j);
						if (item.getName().equals("DisplayName")) {
							listItem.setDisplayName(item.getValue());
						} else if (item.getName().equals("StaffIcon")) {
							listItem.setStaffIcon(item.getValue());
						}
					}
				}
				taskListItem.add(listItem);
		}

		return taskListItem;
	}

	/**
	 * GetTaskInfo
	 * 
	 * @param jsonResult
	 * @return
	 */
	public static TaskItemBean getTaskInfo(String jsonResult) {

		TaskItemBean taskItemBean = null;
		ProcBaseInfoBean procBaseInfo;
		ProcLogInfoBean procLogInfo;
		BizInfoBean bizInfo;
		List<GroupBean> groupBeanList;
		ActionsBean action;
		try {
			if (isJSONNull(jsonResult)) {
				return null;
			} else {
				taskItemBean = new TaskItemBean();
				procBaseInfo = new ProcBaseInfoBean();
				procLogInfo = new ProcLogInfoBean();
				bizInfo = new BizInfoBean();
				action = new ActionsBean();
				groupBeanList = new ArrayList<GroupBean>();
				JSONObject JSONTaskItemBean = new JSONObject(jsonResult);

				JSONObject JSONProcBaseInfo = JSONTaskItemBean.getJSONObject(ProcBaseInfoBean.PROCBASEINFO);// Procbaseinfo

				JSONObject JSONGroup1 = JSONProcBaseInfo.getJSONObject(GroupBean.GROUP);

				procBaseInfo.setGroupBean(getGroupBean(JSONGroup1));

				JSONObject JSONBizInfo = JSONTaskItemBean.getJSONObject(BizInfoBean.BIZINFO);// BizInfo

				JSONArray JSONArrGroups = JSONBizInfo.getJSONArray(GroupBean.GROUPS);

				for (int i = 0; i < JSONArrGroups.length(); i++) {
					JSONObject JSONGroup = JSONArrGroups.getJSONObject(i);
					groupBeanList.add(getGroupBean(JSONGroup));
				}

				bizInfo.setGroupBeanList(groupBeanList);

				JSONObject JSONProcLogInfo = JSONTaskItemBean.getJSONObject(ProcLogInfoBean.PROCLOGINFO);// ProcLogInfo

				JSONObject JSONGroup2 = JSONProcLogInfo.getJSONObject(GroupBean.GROUP);

				procLogInfo.setGroupBean(getGroupBean(JSONGroup2));

				JSONObject JSONActions = JSONTaskItemBean.getJSONObject(ActionsBean.ACTIONS);// Actions

				action.setItemList(getItemBeanList(JSONActions));

				taskItemBean.setProcBaseInfo(procBaseInfo);
				taskItemBean.setBizInfo(bizInfo);
				taskItemBean.setProcLogInfo(procLogInfo);
				taskItemBean.setAction(action);

			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return taskItemBean;
	}

	/**
	 * GetItemBean
	 * 
	 * @param jsonObj
	 * @return
	 * @throws JSONException
	 */
	public static ItemsBean getItemBean(JSONObject jsonObj) throws JSONException {
		ItemsBean itemBean = new ItemsBean();
		itemBean.setName(jsonObj.getString(ItemsBean.NAME));
		itemBean.setValue(jsonObj.getString(ItemsBean.VALUE));
		itemBean.setLabel(jsonObj.getString(ItemsBean.LABEL));
		itemBean.setVisible(jsonObj.getBoolean(ItemsBean.VISIBLE));
		itemBean.setEditable(jsonObj.getBoolean(ItemsBean.EDITABLE));
		itemBean.setDetailsUrl(jsonObj.getString(ItemsBean.DETAILSURL));
		itemBean.setFormat(jsonObj.getString(ItemsBean.FORMAT));
		return itemBean;
	}

	/**
	 * GetGroupBean
	 * 
	 * @param JSONGroup
	 * @return
	 */
	public static GroupBean getGroupBean(JSONObject JSONGroup)
			throws JSONException {
		GroupBean group = null;
		HeaderBean header;

		String type = "";
		List<RowsBean> rowsBeanList = new ArrayList<RowsBean>();
		if (JSONGroup != null) {
			group = new GroupBean();
			header = new HeaderBean();

			type = JSONGroup.getString(GroupBean.TYPE);
			group.setLabel(JSONGroup.getString(GroupBean.LABEL));
			group.setCollapsed(JSONGroup.getBoolean(GroupBean.COLLAPSED));
			if (type.equals("Single")) {
				group.setType(type);
				group.setItemList(getItemBeanList(JSONGroup));
				group.setHeaderBean(null);
				group.setRowsBeanList(null);
			} else if (type.equals("Table")) {
				group.setType(type);
				group.setItemList(null);
				JSONObject JSONHeader = JSONGroup
						.getJSONObject(HeaderBean.HEADER);
				header.setItemList(getItemBeanList(JSONHeader));
				group.setHeaderBean(header);
				JSONArray JSONArrRows = JSONGroup.getJSONArray(RowsBean.ROWS);
				for (int i = 0; i < JSONArrRows.length(); i++) {
					RowsBean rows = new RowsBean();
					DataBean data = new DataBean();
					MoreBean more = new MoreBean();
					JSONObject JSONObj = JSONArrRows.getJSONObject(i);
					JSONObject JSONData = JSONObj.getJSONObject(DataBean.DATA);
					JSONObject JSONMore = JSONObj.getJSONObject(MoreBean.MORE);
					data.setItemList(getItemBeanList(JSONData));
					more.setItemList(getItemBeanList(JSONMore));
					rows.setDataBean(data);
					rows.setMoreBean(more);
					rowsBeanList.add(rows);
				}

				group.setRowsBeanList(rowsBeanList);
			}
			return group;
		}
		return group;
	}

	/**
	 * GetItemBeanList
	 * 
	 * @param JSONObj
	 * @return
	 */
	public static List<ItemsBean> getItemBeanList(JSONObject JSONObj)
			throws JSONException {
		if (JSONObj != null) {
			List<ItemsBean> itemsBeanList = new ArrayList<ItemsBean>();
			JSONArray JSONArrTaskItem = JSONObj.getJSONArray(ItemsBean.ITEMS);
			ItemsBean item = null;
			for (int i = 0; i < JSONArrTaskItem.length(); i++) {
				JSONObject jsonObj = JSONArrTaskItem.getJSONObject(i);
				item = getItemBean(jsonObj);
				itemsBeanList.add(item);
			}
			return itemsBeanList;
		}
		return null;
	}

	/**
	 * GetResultBean
	 * 
	 * @param jsonResult
	 * @return object
	 * @throws JSONException
	 * 
	 */
	public static ResultBean getResultBean(String jsonResult) {
		ResultBean resultBean = null;
		try {
			if (isJSONNull(jsonResult)) {
				return null;
			} else {
				JSONObject JSONreslut = new JSONObject(jsonResult);
				Log.i("jsonResult", jsonResult);
				resultBean = new ResultBean();
				resultBean.setResult(JSONreslut.getString(ResultBean.RESULT));
				resultBean.setMessage(JSONreslut.getString(ResultBean.MESSAGE));
				resultBean.setMessageDetails(JSONreslut
						.getString(ResultBean.MESSAGEDETAILS));
				return resultBean;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

}
