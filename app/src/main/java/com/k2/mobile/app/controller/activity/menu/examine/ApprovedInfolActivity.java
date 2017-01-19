package com.k2.mobile.app.controller.activity.menu.examine;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.BizInfoBean;
import com.k2.mobile.app.model.bean.GroupBean;
import com.k2.mobile.app.model.bean.ItemsBean;
import com.k2.mobile.app.model.bean.ProcBaseInfoBean;
import com.k2.mobile.app.model.bean.ProcLogInfoBean;
import com.k2.mobile.app.model.bean.PublicRequestBean;
import com.k2.mobile.app.model.bean.PublicResultBean;
import com.k2.mobile.app.model.bean.ReqBodyBean;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.bean.RowsBean;
import com.k2.mobile.app.model.bean.TaskItemBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.model.other.ReceiveResponse;
import com.k2.mobile.app.utils.BitmapUtil;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.PublicResHeaderUtils;
import com.k2.mobile.app.utils.Utils;
import com.k2.mobile.app.utils.Utils.OnAlertSelectId;
import com.k2.mobile.app.view.dynamicgrid.MoreListView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

/**
 * 
 * @ClassName: ApprovedInfolActivity
 * @Description: 审批/申请详情
 * @author linqijun
 * @date 2015-4-27 20:12:46
 *
 */
public class ApprovedInfolActivity extends BaseActivity implements OnClickListener {
	
	private Button btn_back;
	private TextView tv_title;
	private TextView tv_b_page;	
	private TextView tv_n_page;
	
	private String sn;
	private String destination;
	private String folio;
	
	private TaskItemBean taskItemBean;
	private ProgressDialog dialog;
	
	private ExpandableListView expandableListView;
	private String[] parentViewStr; // 组列表
	private List<String> group; // 组列表
	private ProcBaseInfoBean procBaseInfoBean;
	private BizInfoBean bizInfoBean;
	private ProcLogInfoBean procLogInfoBean;
	private int index = -1;
	private String values = "";
	private TaskInfoAdapter taskInfoAdapter;
	private int lastClick = -1;// 上一次点击的group的position
	private int server = 1;
	private int checkServer = 1;
	private String _actionName = null; 
	private String _actionValue = null; 
	
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			switch (msg.what) {
				case 3:
					
				case 1:
					String result = (String) msg.obj;
					if(null != result){
						taskItemBean = ReceiveResponse.getTaskInfo(result);
						if(null != taskItemBean){
							getParentView();
//							autoExpandGroup();
							if (taskInfoAdapter == null) {
								taskInfoAdapter = new TaskInfoAdapter();
							}  
							
							expandableListView.setAdapter(taskInfoAdapter);
						}
					}
					break;
				case 2:
					String res = (String) msg.obj;
					System.out.println("res = "+res);
					if(null != res && !"".equals(res.trim())){
						ResPublicBean rBean = JSON.parseObject(res, ResPublicBean.class);
						if(null != rBean && null != rBean.getResult() && ("S".equals(rBean.getResult().trim()) || "s".equals(rBean.getResult().trim()))){
							LogUtil.promptInfo(ApprovedInfolActivity.this, "审批成功");	
							sendBroadcast(new Intent(BroadcastNotice.NOT_APPROVAL));
							finish();
							break;
						}
					}
					
					LogUtil.promptInfo(ApprovedInfolActivity.this, "审批失败");	
					
					break;
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去除头部
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_approved_info);
		initView();
		initListener();
		logic();
		BaseApp.addActivity(this);
	}

	/**
	 * 方法名: initView() 
	 * 功 能 : 初始化 
	 * 参 数 : void 
	 * 返回值: void
	 */
	@SuppressLint("NewApi")
	private void initView() {
		btn_back = (Button) findViewById(R.id.btn_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_b_page = (TextView) findViewById(R.id.tv_b_page);	
		tv_n_page = (TextView) findViewById(R.id.tv_n_page);
		
		expandableListView = (ExpandableListView) findViewById(R.id.task_item_expandable);
		
		dialog = new ProgressDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("正在加载数据....");
		dialog.setCancelable(false);
		group = new ArrayList<String>();
			 
	}
	
	/**
	 * 方法名: initListener() 
	 * 功 能 : 初始化 监听器 
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void initListener() {
		btn_back.setOnClickListener(this);
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) { 
				 
				if(lastClick==-1){
					expandableListView.expandGroup(groupPosition);
				}else if (lastClick != -1 && lastClick != groupPosition){
					//expandableListView.collapseGroup(lastClick);
					expandableListView.expandGroup(groupPosition);
				} else if (lastClick != -1 && lastClick == groupPosition) { 
					if (expandableListView.isGroupExpanded(groupPosition)){
						expandableListView.collapseGroup(groupPosition);
					} 
					else{
						expandableListView.expandGroup(groupPosition);
					} 	
				} 
				lastClick = groupPosition;
				 
				 
				return true;
			}
		});
	}
	/**
	 * 方法名: logic() 
	 * 功 能 : 逻辑处理
	 * 参 数 : void 
	 * 返回值: void
	 */
	private void logic(){
		sn = getIntent().getStringExtra("SN");
		destination = getIntent().getStringExtra("destination");
		folio = getIntent().getStringExtra("folio");
		checkServer = getIntent().getIntExtra("checkServer", 1);
		tv_title.setText(folio);
		
		tv_b_page.setVisibility(View.GONE);
		tv_n_page.setVisibility(View.GONE);
				
		System.out.println("checkServer = "+checkServer);
		requestServer(checkServer);
	}
	
	private void getParentView() {

		procBaseInfoBean = taskItemBean.getProcBaseInfo();
		bizInfoBean = taskItemBean.getBizInfo();
		procLogInfoBean = taskItemBean.getProcLogInfo(); 
		if(1 == checkServer){
			parentViewStr = new String[bizInfoBean.getGroupBeanList().size() + 3];
		}else{
			parentViewStr = new String[bizInfoBean.getGroupBeanList().size() + 2];
		}
		
		parentViewStr[0] = procBaseInfoBean.getGroupBean().getLabel();

		List<ItemsBean> itemBeanL = procBaseInfoBean.getGroupBean().getItemList();
		for (int i = 0; i < itemBeanL.size(); i++) {
			ItemsBean ite = itemBeanL.get(i);
			if (ite.getName().equals("SN") || ite.getName().equals("Folio")
					|| ite.getName().equals("ProcessName")) {
				 itemBeanL.remove(ite);
			}
		} 
 
		for (int i = 0; i < bizInfoBean.getGroupBeanList().size(); i++) {
			parentViewStr[i + 1] = bizInfoBean.getGroupBeanList().get(i).getLabel();
		}

		parentViewStr[bizInfoBean.getGroupBeanList().size() + 1] = procLogInfoBean.getGroupBean().getLabel(); 
		if(1 == checkServer){
			parentViewStr[bizInfoBean.getGroupBeanList().size() + 2] = "操作"; 
		}
		
		for (int i = 0; i < parentViewStr.length; i++) {
			group.add(parentViewStr[i]);
		}
	}
	
	private void autoExpandGroup(){
		//展开
		if(procBaseInfoBean.getGroupBean().isCollapsed()==false){
			expandableListView.expandGroup(0);
		}

		for (int i = 0; i < bizInfoBean.getGroupBeanList().size(); i++) { 
			//展开
			if( bizInfoBean.getGroupBeanList().get(i).isCollapsed()==false){
				expandableListView.expandGroup(i + 1);
			}
		}
		//展开
		if( procLogInfoBean.getGroupBean().isCollapsed()==false){
			expandableListView.expandGroup(bizInfoBean.getGroupBeanList().size() + 1);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_back:
	            finish();
				break;
		}		
	}
	
   /**
	* @Title: getQuestData
	* @Description: 请求报文
	* @param void
	* @return String 返回的数据 
	* @throws
	*/
	private String getQuestData(int serverType){
		
		//System.out.print("serverType:"+serverType);
		
		String code = null;
		String parameter = null;
		
		ReqBodyBean bBean = new ReqBodyBean();
		if(1 == serverType){
			code = "F60000008";
			parameter = "[\""+sn+"\",\""+destination+"\"]";
		}else if(3 == serverType){
			code = "F60000015";
			parameter = "[\""+sn+"\",\""+destination+"\"]";
		}else{
			code = "F60000014";
			parameter = "[\""+sn+"\",\""+_actionValue+"\",\""+values+"\",\""+destination+"\"]";
		}
		
		bBean.setInvokeFunctionCode(code);
		bBean.setInvokeParameter(parameter);
				
		PublicRequestBean bean = new PublicRequestBean();
		bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
		bean.setReqBody(bBean);
		
		return JSON.toJSONString(bean);
	}
	
	/**
	* @Title: requestServer
	* @Description: 发送请求报文
	* @param void
	* @return void
	* @throws 13924381869  13824881960
	*/
	private void requestServer(int serverType){
		server = serverType;
		// 判断网络是否连接
		if (!NetWorkUtil.isNetworkAvailable(ApprovedInfolActivity.this)) {
			DialogUtil.showLongToast(ApprovedInfolActivity.this, R.string.global_network_disconnected);
		}else{			
			String info = getQuestData(serverType);
			SendRequest.submitRequest(ApprovedInfolActivity.this, info, submitCallBack);
	}
  }
	
	/**
	 * http请求回调
	 */
	RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

		@Override
		public void onStart() {
			DialogUtil.showWithCancelProgressDialog(ApprovedInfolActivity.this, null, res.getString(R.string.global_prompt_message), null);
		}

		@Override
		public void onLoading(long total, long current, boolean isUploading) {

		}
		
		@Override
		public void onSuccess(ResponseInfo<String> responseInfo) {     
			DialogUtil.closeDialog();
			
			String result = responseInfo.result.toString();
			byte[] ebase64 = EncryptUtil.decodeBase64(result);
			if (null != result && !"".equals(result.trim())) {
				PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
				// 判断返回标识状态是否为空
				if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {				
					LogUtil.promptInfo(ApprovedInfolActivity.this, ErrorCodeContrast.getErrorCode("0",  getResources()));
					return;
				}else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
					LogUtil.promptInfo(ApprovedInfolActivity.this, resMsg.getResHeader().getReturnMsg());	
					return;
				}
				// 判断消息体是否为空
				if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {		 
					LogUtil.promptInfo(ApprovedInfolActivity.this, ErrorCodeContrast.getErrorCode("0",  getResources()));
				} else {
					
					if(null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())){
						BaseApp.key = resMsg.getResHeader().getSectetKey();
					}
					// 获取解密后并校验后的值
					Message msgs = new Message();
					msgs.obj = resMsg.getResBody().getResultString();
					msgs.what = server;
					mHandler.sendMessage(msgs);
				}
			} else {
				LogUtil.promptInfo(ApprovedInfolActivity.this, ErrorCodeContrast.getErrorCode("0", getResources()));
			}	
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			DialogUtil.closeDialog();
			if(null != msg && msg.equals(LocalConstants.API_KEY)){
				LogUtil.promptInfo(ApprovedInfolActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
			}else{
				LogUtil.promptInfo(ApprovedInfolActivity.this, ErrorCodeContrast.getErrorCode("0", res));
			}
		}
	};
	 
	class TaskInfoAdapter extends BaseExpandableListAdapter {
		  
		List<ItemsBean> actionsList;//操作选项集合 
		
	    public TaskInfoAdapter(){
	    	actionsList = taskItemBean.getAction().getItemList(); 
	    }
	    
		// -----------------Child----------------//
		@Override
		public Object getChild(int groupPosition, int childPosition) {

			return null; 
		}

		@Override
		public int getChildrenCount(int childPosition) { 
			GroupBean groupBean = GetCurrentGroupBean(childPosition); 
			return GetGroupCount(groupBean);
		} 
		
		@Override
		public long getChildId(int groupPosition, int childPosition) {

			return childPosition; 
		}
      
		//获取当前groupPosition 对应的组
		private GroupBean GetCurrentGroupBean(int groupPosition){
			
			if (groupPosition == 0) {
				return  procBaseInfoBean.getGroupBean();
			}
			if (groupPosition > 0 && groupPosition < bizInfoBean.getGroupBeanList().size() + 1) {
				return bizInfoBean.getGroupBeanList().get(groupPosition - 1);
			
			}
			if (groupPosition == bizInfoBean.getGroupBeanList().size() + 1) {
				return procLogInfoBean.getGroupBean();
			}
			if (groupPosition == bizInfoBean.getGroupBeanList().size() + 2) {
				return null;
			}
			return  null;
		}
		
		//获取GroupBean 在列表中有多少列
		private int GetGroupCount(GroupBean groupBean){
			if(groupBean==null){
				return 1;
			}
			
			if (groupBean.getType().equals("Single")) { 
				return   groupBean.getItemList().size();
			} else if (groupBean.getType().equals("Table")) {
				List<ItemsBean> headerBeanItem = groupBean.getHeaderBean().getItemList();
				List<RowsBean> rowsBean = groupBean.getRowsBeanList(); 
				int count=0;
				if (headerBeanItem != null) {
					count++;
				}
				if (rowsBean != null) {
					count+=rowsBean.size();
				} 
				return  count; 
			}
			return 0;
		}
		
 
		@Override
		public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) { 
			 GroupBean currentGroupBean=GetCurrentGroupBean(groupPosition);//当前group数据
			 
			 if (currentGroupBean != null) {
				TextView foTOne, foTTwo;
				final LinearLayout listViewpPanl;
				final MoreListView listView;
				if (currentGroupBean.getType().equals("Single")) {
					if (childPosition == 0) {
						View fifthView = View.inflate(ApprovedInfolActivity.this, R.layout.activity_task_item_five, null);
						foTOne = (TextView) fifthView.findViewById(R.id.item_four_content_one);
						foTTwo = (TextView) fifthView.findViewById(R.id.item_four_content_two);
						ItemsBean item = currentGroupBean.getItemList().get(0);
						singleDataFormat(item, foTOne, foTTwo);
						fifthView.setPadding(20, 0, 0, 0);
						return fifthView;
					} else { 
						View fourthView = View.inflate(ApprovedInfolActivity.this, R.layout.activity_task_item_four, null);
						foTOne = (TextView) fourthView.findViewById(R.id.item_four_content_one);
						foTTwo = (TextView) fourthView.findViewById(R.id.item_four_content_two);
//						foTTwo.setMovementMethod(ScrollingMovementMethod
//  								.getInstance()); 
// 						foTTwo.setHorizontallyScrolling(true); 
// 						foTTwo.setMaxWidth(10);
						ItemsBean item = currentGroupBean.getItemList().get(childPosition);
						singleDataFormat(item, foTOne, foTTwo); 
						fourthView.setPadding(20, 0, 0, 0);
						  
						return fourthView;
					}
				}
				if (currentGroupBean.getType().equals("Table")) {
					LinearLayout.LayoutParams paramsT = new LinearLayout.LayoutParams(
							android.view.ViewGroup.LayoutParams.MATCH_PARENT,
							android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
					if (childPosition == 0) { 
						View secondView = View.inflate(ApprovedInfolActivity.this,
								R.layout.activity_task_item_two, null);

						LinearLayout tableHeader = (LinearLayout) secondView
								.findViewById(R.id.task_info_table_header);

						secondView.setPadding(20, 0, 0, 0);

						List<ItemsBean> headerL = currentGroupBean.getHeaderBean()
								.getItemList();

						for (int i = 0; i < headerL.size(); i++) {
							ItemsBean headerItem = headerL.get(i);
							TextView textV = new TextView(ApprovedInfolActivity.this);
							textV.setTextColor(res.getColor(R.color.item_color));
							textV.setText(headerItem.getLabel());
							textV.setLayoutParams(paramsT);
							tableHeader.addView(textV);
						}
						return secondView;

					} else {
						paramsT.setMargins(0, 0, 5, 0);
						final View thirdView = View.inflate(ApprovedInfolActivity.this, R.layout.activity_task_item_three, null); 
						
						 RowsBean rows= currentGroupBean.getRowsBeanList().get(childPosition-1);
						 
						 List<ItemsBean> moreBeanList = rows.getMoreBean().getItemList();
						  
						 final TableListViewAdapter tableV = new TableListViewAdapter(moreBeanList);

						listViewpPanl = (LinearLayout) thirdView.findViewById(R.id.list_view_panl);
						listView = (MoreListView) thirdView.findViewById(R.id.table_list_view);	
								 

						LinearLayout tableHeader = (LinearLayout) thirdView.findViewById(R.id.task_item_panl_two);
						 
						for (int i = 0; i < rows.getDataBean().getItemList().size(); i++) {
							ItemsBean headerItem = rows.getDataBean().getItemList().get(i);
							TextView textV = new TextView(ApprovedInfolActivity.this);
							textV.setMovementMethod(ScrollingMovementMethod.getInstance());
							textV.setMaxLines(10);
							textV.setHorizontallyScrolling(true);
							textV.setLayoutParams(paramsT);
							textV.setText(headerItem.getValue());
							tableHeader.addView(textV);
						}

						thirdView.setPadding(20, 0, 0, 0);

						 final int index = childPosition;
						thirdView.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								if (index != 0) {
									if (listViewpPanl.getVisibility() == View.GONE) {
										listViewpPanl.setVisibility(View.VISIBLE);
										 // tableV.itemL = moreBeanL;
										 //tableV.notifyDataSetChanged();
									} else if (listViewpPanl.getVisibility() == View.VISIBLE) {
										 //tableV.itemL = null;
										listViewpPanl.setVisibility(View.GONE);
									}
								}
							}
						}); 
						 
						int totalHeight = 0;
						for (int i = 0; i < tableV.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
							View listItem = tableV.getView(i, null, listView);
							listItem.measure(0,0); // 计算子项View 的宽高
							totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
						}
						 ViewGroup.LayoutParams params = listView.getLayoutParams();
						params.height = totalHeight + (listView.getDividerHeight() * (tableV.getCount() - 1));
						//params.height=LayoutParams.WRAP_CONTENT;
						listView.setLayoutParams(params);
						listView.setAdapter(tableV); 
						 
						return thirdView; 
					}
				}
			} else {
				final View saveView = View.inflate(ApprovedInfolActivity.this, R.layout.activity_task_item_save, null);
				Button operate = (Button) saveView.findViewById(R.id.task_operate_choose);
				Button clear = (Button) saveView.findViewById(R.id.task_operate_clear);

				final EditText value = (EditText) saveView.findViewById(R.id.task_operate_value);

				value.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_UP) {
							index = childPosition;
						}
						return false;
					}
				});

				value.addTextChangedListener(new TextWatcher() {

					@Override
					public void onTextChanged(CharSequence s, int start,
							int before, int count) {

					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {

					}

					@Override
					public void afterTextChanged(Editable s) {

						values = value.getText().toString();
						saveView.setBackgroundColor(res.getColor(R.color.white));
					}
				});

				value.setText(values);

				value.clearFocus();

				if (index != -1 && index == childPosition) {
					value.requestFocus();
				}

				clear.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!value.getText().toString().equals("")) {
							value.setText("");
						}
					}
				});

				operate.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Utils.showDialog(ApprovedInfolActivity.this,
								new OnAlertSelectId() {
									@Override
									public void onClick(String whichName) {
										for (int i = 0; i < actionsList.size(); i++) {
											if (actionsList.get(i).getName().equals(whichName)) {
												alertOKOrCancel(whichName,actionsList.get(i).getValue());
											}
										}
									}
								}, actionsList);
					}
				});
				saveView.setPadding(20, 0, 0, 0);
				return saveView;
			}
			if (isLastChild) {
				return null;
			}
			return null;

		}
 
		// ----------------Group----------------//
		@Override
		public Object getGroup(int groupPosition) {
			return group.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return group.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

			View firstView = View.inflate(ApprovedInfolActivity.this, R.layout.activity_task_item_one, null);
			TextView textView = (TextView) firstView.findViewById(R.id.task_item_title);
			ImageView taskItemIcon = (ImageView) firstView.findViewById(R.id.task_item_icon);
			ImageView taskItemArrow = (ImageView) firstView.findViewById(R.id.task_item_arrow);
			RelativeLayout taskItemPanl = (RelativeLayout) firstView.findViewById(R.id.task_item_panl);
			textView.setText(getGroup(groupPosition).toString());

			if (isExpanded) {
				taskItemArrow.setImageBitmap(BitmapUtil.readBitMap(ApprovedInfolActivity.this, R.drawable.item_down));
				taskItemIcon.setImageBitmap(BitmapUtil.readBitMap(ApprovedInfolActivity.this, R.drawable.item_location_title));
				taskItemPanl.setBackgroundResource(R.drawable.item_banner);
			} else {
				taskItemArrow.setImageBitmap(BitmapUtil.readBitMap(ApprovedInfolActivity.this, R.drawable.item_up));
				taskItemIcon.setImageBitmap(BitmapUtil.readBitMap(ApprovedInfolActivity.this, R.drawable.item_location_title_active));
				taskItemPanl.setBackgroundResource(R.drawable.item_banner_active);
			}

			return firstView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

		private void singleDataFormat(ItemsBean item, TextView foTOne,
				TextView foTTwo) {
//			if (!item.getFormat().equals("null")) {
//				Calendar ca = DateUtil.formatrDate(item.getValue());
//				SimpleDateFormat format = new SimpleDateFormat(item.getFormat());
//				Date d = ca.getTime();
//				foTOne.setText(item.getLabel());
//				foTTwo.setText(format.format(d));
//			} else {
				foTOne.setText(item.getLabel());
				foTTwo.setText(item.getValue()); 
			//}
		}

		//
		private void alertOKOrCancel(final String whichName,final String whichValue) {
			_actionName = whichName;
			_actionValue = whichValue;
			AlertDialog.Builder builder = new AlertDialog.Builder(ApprovedInfolActivity.this);
			builder.setTitle("提示");
			builder.setMessage(whichName + "  sure  ?");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogs, int which) {
							requestServer(2);
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialogs, int which) {
							dialogs.dismiss();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		}

	}

	// TableListAdapter
	class TableListViewAdapter extends BaseAdapter {

		public List<ItemsBean> itemL;

		public TableListViewAdapter(List<ItemsBean> itemL) {
			this.itemL = itemL;
		}

		@Override
		public int getCount() {

			return itemL.size();
		}

		@Override
		public Object getItem(int position) {

			return itemL.get(position);
		}

		@Override
		public long getItemId(int position) {

			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
         
			TextView t1 = new TextView(ApprovedInfolActivity.this);
			ItemsBean item = itemL.get(position);
			t1.setText(item.getLabel() + ":  " + item.getValue()); 
			return t1;
		}

	}
}