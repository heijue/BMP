package com.k2.mobile.app.controller.activity.menu.examine;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.k2.mobile.app.BuildConfig;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.menu.hr.HRStaticLinkListActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.adapter.FlowAdapter;
import com.k2.mobile.app.model.bean.FlowBean;
import com.k2.mobile.app.model.bean.PublicRequestBean;
import com.k2.mobile.app.model.bean.PublicResultBean;
import com.k2.mobile.app.model.bean.ReqBodyBean;
import com.k2.mobile.app.model.bean.ResPublicBean;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.PublicResHeaderUtils;
import com.k2.mobile.app.view.widget.AddAndSubEditText;
import com.k2.mobile.app.view.widget.XListView;
import com.k2.mobile.app.view.widget.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author linqijun
 * @version V1.0
 * @Title PendingFragment.java
 * @Package com.oppo.mo.controller.activity.menu.workbench;
 * @Description 我审批的
 * @Company K2
 * @date 2015-3-13 下午3:22:19
 */
@SuppressLint("NewApi")
public class ApprovedFragment extends Fragment implements OnClickListener {

    private View view = null;
    private Activity     mActivity;
    private LinearLayout ll_start, ll_approval;
    private TextView tv_not_approval, tv_pproved;
    private XListView lv_work_show;
    // 页码
    private int     pageIndex   = 1;
    // 每页行数
    private int     pageSize    = 10;
    // 操作类别 1,拟制中 2,审核中 3,已通过 4,未审批 5,已审批
    private int     operClass   = 4;
    private int     flag        = 1;
    private boolean commitDel   = false;
    private String  examination = null;
    private String workListItemCode;
    // 适配器
    private FlowAdapter      fAdapter  = null;
    private List<FlowBean>   fList     = null;
    // 未审批临时集合
    private List<FlowBean>   nApList   = null;
    // 已审批临时集合
    private List<FlowBean>   apList    = null;
    // 广播
    private IncomingReceiver iReceiver = null;
    private int              positions = 0;
    private String           opinion   = null;
    ProgersssDialog dialog = null;
    // 跳转
    private Intent mIntent  = null;
    // 我审批的
    private String tipsAp   = null;
    // 我已审批的
    private String tipsAped = null;

    private int apCount = 0;
    private int x       = 687;
    private int y       = 400;

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    String json = (String) msg.obj;
                    if (null != json && !"-1".equals(json)) {
                        List<FlowBean> afList = JSON.parseArray(json, FlowBean.class);
                        if (null != afList) {
                            // 设置操作类型
                            if (BuildConfig.DEBUG)
                                for (int i = 0; i < afList.size(); i++) {
                                    afList.get(i).setClassType(operClass);
                                }

                            if (3 != flag) {
                                fList.clear();
                            }

                            fList.addAll(afList);
                            fAdapter.notifyDataSetChanged();

                            if (4 == operClass) {
                                nApList.clear();
                                nApList.addAll(fList);
                            } else if (5 == operClass) {
                                apList.clear();
                                apList.addAll(fList);
                            }
                        }

                        String tmpTips = null;

                        if ((null == afList || 1 > afList.size()) && 1 > fList.size()) {                // 暂无数据
                            tmpTips = getString(R.string.no_data);
                        } else if (null != afList && afList.size() < 10) {            // 所有数据加载完
                            tmpTips = getString(R.string.all_data_loaded);
                        } else {                                                    // 查看更多
                            tmpTips = getString(R.string.xlistview_footer_hint_normal);
                        }

                        lv_work_show.setTips(tmpTips);

                        if (4 == operClass) {
                            tipsAp = tmpTips;
                        } else if (5 == operClass) {
                            tipsAped = tmpTips;
                        }
                    }

                    if (4 == operClass) {
                        Intent mIntent = new Intent(BroadcastNotice.APPROVAL_COUNT);
                        getActivity().sendBroadcast(mIntent);
                    }

                    break;
                case 2:
                    commitDel = false;
                    String delResult = (String) msg.obj;
                    if (null != delResult) {
                        ResPublicBean rBean = JSON.parseObject(delResult, ResPublicBean.class);
                        if (null != rBean) {
                            if ("1".equals(rBean.getSuccess())) {
                                pageIndex = 1;
                                flag = 2;
                                fList.remove(positions);
                                fAdapter.notifyDataSetChanged();

                                if (4 == operClass) {
                                    Intent mIntent = new Intent(BroadcastNotice.APPROVAL_COUNT);
                                    getActivity().sendBroadcast(mIntent);
                                }
                            } else {
                                LogUtil.promptInfo(getActivity(), getResources().getString(R.string.approval_failure));
                            }
                        }
                    }
                    break;
                case 7:
                    lv_work_show.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0));
                    break;
                case 8:
                    lv_work_show.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(),
                            SystemClock.uptimeMillis(), MotionEvent.ACTION_MOVE, x, y, 0));
                    y += 110;
                    break;
            }
        }

        ;
    };
    private AddAndSubEditText mSearch;
    private List<FlowBean>    mSearchList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_launch_approval, null);
        mActivity = getActivity();
        initView();
        initListener();
        initAdapter(4);
        createFilter();

        operClass = 4;
        requestServer();
        return view;
    }

    /*
     * 方法名: initView()
     * 功 能 : 初始化布局控件
     * 参 数 : void
     * 返回值: void
     */
    private void initView() {
        ll_start = (LinearLayout) view.findViewById(R.id.ll_start);
        ll_approval = (LinearLayout) view.findViewById(R.id.ll_approval);
        tv_not_approval = (TextView) view.findViewById(R.id.tv_not_approval);
        tv_pproved = (TextView) view.findViewById(R.id.tv_pproved);
        mSearch = (AddAndSubEditText) view.findViewById(R.id.search);
        ll_start.setVisibility(View.GONE);
        ll_approval.setVisibility(View.VISIBLE);

        lv_work_show = (XListView) view.findViewById(R.id.lv_work_show);
        lv_work_show.setPullRefreshEnable(true);    // 设置下拉更新
        lv_work_show.setPullLoadEnable(false);        // 设置让它上拉更新  ljw 2016-01-16 禁用上拉更新功能

        fList = new ArrayList<FlowBean>();
        nApList = new ArrayList<FlowBean>();
        apList = new ArrayList<FlowBean>();
        // 广播
        switchBg(1);
    }

    /**
     * 方法名: initAdapter()
     * 功 能 : 初始化适配器
     * 参 数 : void
     * 返回值: void
     */

    //ljw 2016-06-17 修改适配器显示
    private void initAdapter(int openType) {
        fAdapter = new FlowAdapter(getActivity(), fList, openType);
        lv_work_show.setAdapter(fAdapter);
    }

    /*
     * 方法名: initListener()
     * 功 能 : 初始化事件
     * 参 数 : void
     * 返回值: void
     */


    private void initListener() {

        tv_not_approval.setOnClickListener(this);
        tv_pproved.setOnClickListener(this);
        lv_work_show.setXListViewListener(xListener);
        lv_work_show.setOnItemClickListener(itemListener);

        //搜索匹配
        mSearch.setOnDrawableRightListener(new AddAndSubEditText.OnDrawableRightListener() {
            @Override
            public void onDrawableRightClick(View view) {
                String searchString = mSearch.getText().toString().toLowerCase();
                if (pageFlag == 1) {
                    if (searchString.equals("")) {
                        fList.clear();
                        fList.addAll(nApList);
                        fAdapter.notifyDataSetChanged();
                    } else {
                        fList.clear();
                        for (FlowBean flowBean : nApList) {
                            String activityName = flowBean.getActivityName();
                            String displayName = flowBean.getDisplayName();
                            String folio = flowBean.getFolio();
                            if (!activityName.isEmpty() && activityName.toLowerCase().contains(searchString)) {
                                fList.add(flowBean);
                            } else if (!displayName.isEmpty()&&displayName.toLowerCase().contains(searchString)) {
                                fList.add(flowBean);
                            } else if (!folio.isEmpty()&&folio.toLowerCase().contains(searchString)) {
                                fList.add(flowBean);
                            }
                        }
                        fAdapter.notifyDataSetChanged();
                    }
                } else if (pageFlag == 2) {
                    if (searchString.equals("")) {
                        fList.clear();
                        fList.addAll(apList);
                        fAdapter.notifyDataSetChanged();
                    } else {
                            fList.clear();
                        for (FlowBean flowBean : apList) {
                            String activityName = flowBean.getActivityName();
                            String displayName = flowBean.getDisplayName();
                            String folio = flowBean.getFolio();
                            if (!activityName.isEmpty() && activityName.toLowerCase().contains(searchString)) {
                                fList.add(flowBean);
                            } else if (!displayName.isEmpty()&&displayName.toLowerCase().contains(searchString)) {
                                fList.add(flowBean);
                            } else if (!folio.isEmpty()&&folio.toLowerCase().contains(searchString)) {
                                fList.add(flowBean);
                            }
                        }
                        fAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    /**
     * @return void
     * @throws
     * @Title: createFilter
     * @Description: 创建IntentFilter
     */
    private void createFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastNotice.NOT_APPROVAL);
        filter.addAction(BroadcastNotice.APPROVAL_DELETE_TIPS);
        filter.addAction(BroadcastNotice.APPROVAL_COUNT_SUB);
        iReceiver = new IncomingReceiver();
        // 注册广播
        getActivity().registerReceiver(iReceiver, filter);
    }

    /**
     * 接收广播
     */
    private class IncomingReceiver extends BroadcastReceiver {
        @SuppressLint("Recycle")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastNotice.NOT_APPROVAL)) {
                pageIndex = 1;
                flag = 2;
                requestServer();
            } else if (action.equals(BroadcastNotice.APPROVAL_DELETE_TIPS)) {
                workListItemCode = intent.getStringExtra("workListItemCode");
                examination = intent.getStringExtra("examination");
                positions = intent.getIntExtra("positions", -1);
                opinion = intent.getStringExtra("opinion");

                if (null != dialog && dialog.isShowing()) {
                    return;
                }

                dialog = new ProgersssDialog(getActivity());
                WindowManager windowManager = getActivity().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
                lp.width = (int) (display.getWidth() * 0.9); //设置宽度
                lp.height = (int) (display.getHeight() * 0.45); //设置宽度
                dialog.getWindow().setAttributes(lp);

                dialog.show();
            } else if (action.equals(BroadcastNotice.APPROVAL_COUNT_SUB)) {
                requestServer();
            }
        }
    }

    /**
     * @return void
     * @throws
     * @Title: doubleClickRefresh
     * @Description: 双击刷新
     */
    public void doubleClickRefresh() {
        x = 687;
        y = 600;
        lv_work_show.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0));
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 6; i++) {
                    Message msg = new Message();
                    msg.what = 8;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Message msg = new Message();
                msg.what = 7;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    // 上拉分页加载，下拉刷新
    IXListViewListener xListener = new IXListViewListener() {

        @Override
        public void onRefresh() {
            pageIndex = 1;
            flag = 2;
            requestServer();

        }

        @Override
        public void onLoadMore() {
            flag = 3;
            pageIndex++;
            requestServer();
        }
    };

    OnItemClickListener itemListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            positions = position - 1;
            if (fList.get(positions).getOpentype() != null && fList.get(positions).getOpentype() != "") {
                int _type = Integer.parseInt(fList.get(positions).getOpentype());
                switch (_type) {
                    case 1:
                        OpenNativeIntent();
                        break;
                    case 2:
                        break;
                    case 3:
                        OpenHtml5Intent(fList.get(positions));
                        break;
                    default:
                        OpenNativeIntent();
                        break;
                }
            } else {
                //默认打开原生Native
                OpenNativeIntent();
            }
        }
    };

    //打开Native的详情界面
    private void OpenNativeIntent() {

        Intent mIntent = new Intent(getActivity(), ApprovedInfolActivity.class);
        mIntent.putExtra("SN", fList.get(positions).getSN());
        mIntent.putExtra("folio", fList.get(positions).getFolio());
        int checkServer = 1;
        if (4 == operClass) {
            checkServer = 1;
        } else if (5 == operClass) {
            checkServer = 3;
        }
        mIntent.putExtra("checkServer", checkServer);
        mIntent.putExtra("destination", fList.get(positions).getDestination());
        mIntent.putExtra("processFullName", fList.get(positions).getProcessFullName());
        mIntent.putExtra("bsCode", fList.get(positions).getBsCode());
        startActivity(mIntent);
    }

    //打开HTML5的详情界面
    private void OpenHtml5Intent(FlowBean Model) {
        Intent mIntent = new Intent(getActivity(), HRStaticLinkListActivity.class);
        mIntent.putExtra("title", Model.getFolio());
        mIntent.putExtra("url", Model.getLinkurl());
        startActivity(mIntent);
        return;
    }


    /**
     * @return String 返回的数据
     * @throws
     * @Title: onLoad
     * @Description: 停止加载进度条
     */
    private void onLoad() {
        lv_work_show.stopRefresh();
        lv_work_show.stopLoadMore();
        lv_work_show.setRefreshTime(getResources().getString(R.string.just));
    }

    /**
     * @return String 返回的数据
     * @throws
     * @Title: getQuestData
     * @Description: 请求报文
     */
    public String getQuestData(String code) {

        ReqBodyBean bBean = new ReqBodyBean();
        bBean.setInvokeFunctionCode(code);
        bBean.setInvokeParameter("[\"" + pageSize + "\",\"" + pageIndex + "\"]");

        PublicRequestBean bean = new PublicRequestBean();
        bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
        bean.setReqBody(bBean);

        return JSON.toJSONString(bean);
    }

    /**
     * @return void
     * @throws
     * @Title: requestServer
     * @Description: 发送请求报文
     */
    public void requestServer() {

        if (!NetWorkUtil.isNetworkAvailable(getActivity())) {
            DialogUtil.showLongToast(getActivity(), R.string.global_network_disconnected);
        } else {
            String info = null;
            if (commitDel) {
                info = getQuestData("F60000001");
            } else {
                switch (operClass) {
                    case 4:
                        info = getQuestData("F60000001");
                        break;
                    case 5:
                        info = getQuestData("F60000002");
                        break;
                }
            }
            SendRequest.submitRequest(getActivity(), info, submitCallBack);
        }
    }


    /**
     * http请求回调
     */
    RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

        @Override
        public void onStart() {
            if (1 == flag) {
                DialogUtil.showWithCancelProgressDialog(getActivity(), null, getResources().getString(R.string.global_prompt_message), null);
            } else {
                onLoad();
            }
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {

        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            if (1 == flag) {
                DialogUtil.closeDialog();
            } else {
                onLoad();
            }
            String result = responseInfo.result.toString();
            byte[] ebase64 = EncryptUtil.decodeBase64(result);
            System.out.println("ebase64 = " + new String(ebase64));
            if (null != result && !"".equals(result.trim())) {
                PublicResultBean resMsg = JSON.parseObject(new String(ebase64), PublicResultBean.class);
                // 判断返回标识状态是否为空
                if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {
                    LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
                    return;
                } else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
                    LogUtil.promptInfo(getActivity(), resMsg.getResHeader().getReturnMsg());
                    return;
                }
                // 判断消息体是否为空
                if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {
                    LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
                } else {

                    if (null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())) {
                        BaseApp.key = resMsg.getResHeader().getSectetKey();
                    }
                    // 获取解密后并校验后的值
                    Message msgs = new Message();
                    msgs.obj = resMsg.getResBody().getResultString();
                    if (commitDel) {
                        if (null != dialog) {
                            dialog.dismiss();
                        }
                        msgs.what = 2;
                    } else {
                        msgs.what = 1;
                    }
                    mHandler.sendMessage(msgs);
                }
            } else {
                LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
            }
        }


        @Override
        public void onFailure(HttpException error, String msg) {
            if (1 == flag) {
                DialogUtil.closeDialog();
            } else {
                onLoad();
            }
            commitDel = false;
            if (msg.equals(LocalConstants.API_KEY)) {
                return;
            } else {
                LogUtil.promptInfo(getActivity(), ErrorCodeContrast.getErrorCode("0", getResources()));
            }
        }
    };

    /**
     * 方法名: MyOnPageChangeListener
     * 功 能 : 为选项卡绑定监听器
     * 参 数 : void
     * 返回值: void
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(final int position) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }


    @Override
    public void onDestroy() {
        if (null != iReceiver)
            getActivity().unregisterReceiver(iReceiver);
        super.onDestroy();
    }

    ;

    public class ProgersssDialog extends Dialog implements android.view.View.OnClickListener {
        private TextView tv_examination;
        private EditText ed_opinion;
        private TextView tv_commit;
        private TextView tv_close;
        private View    dView   = null;
        private Context context = null;

        public ProgersssDialog(Context context) {
            super(context, R.style.actibity_dialog);
            this.context = context;
            initDialog();

            tv_examination.setText(examination);

            tv_commit.setOnClickListener(this);
            tv_close.setOnClickListener(this);
            //dialog添加视图
            setContentView(dView);
        }

        private void initDialog() {
            //加载布局文件
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dView = inflater.inflate(R.layout.dialog_not_approval, null);
            tv_examination = (TextView) dView.findViewById(R.id.tv_examination);
            ed_opinion = (EditText) dView.findViewById(R.id.ed_opinion);
            tv_commit = (TextView) dView.findViewById(R.id.tv_commit);
            tv_close = (TextView) dView.findViewById(R.id.tv_close);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_close:
                    dismiss();
                    break;
                case R.id.tv_commit:
                    commitDel = true;
                    flag = 1;
                    opinion = ed_opinion.getText().toString();
                    if (opinion == null || "".equals(opinion.trim())) {
                        DialogUtil.showLongToast(getContext(), R.string.disagree_reason_tips);
                        break;
                    }
                    requestServer();
                    break;
            }
        }
    }

    private int pageFlag = 1;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_not_approval:
                switchBg(1);
                operClass = 4;
                pageIndex = 1;
                flag = 1;
                pageFlag = 1;
                mSearch.setText("");
                lv_work_show.setTips(tipsAp);
                fList.clear();
                fList.addAll(nApList);
                initAdapter(4);
                break;
            case R.id.tv_pproved:
                switchBg(2);
                operClass = 5;
                pageIndex = 1;
                pageFlag = 2;
                mSearch.setText("");
                flag = 1;
                lv_work_show.setTips(tipsAped);
                if (1 > apCount) {
                    apCount++;
                    //					String infos = getQuestData();
                    //					requestServer(infos);
                    doubleClickRefresh();
                } else {
                    fList.clear();
                    fList.addAll(apList);
                    initAdapter(5);
                }
                break;
        }
    }

    /**
     * 方法名: switchBg()
     * 功 能 : 设置背景
     * 参 数 : val 被选中的控件编号
     * 返回值: void
     */
    private void switchBg(int val) {
        switch (val) {
            case 1:
                if (Build.VERSION.SDK_INT < 16) {
                    tv_not_approval.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bg));
                    tv_pproved.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                } else {
                    tv_not_approval.setBackground(getResources().getDrawable(R.drawable.tab_bg));
                    tv_pproved.setBackground(getResources().getDrawable(R.color.white));
                }
                tv_not_approval.setTextColor(getResources().getColor(R.color.white));
                tv_pproved.setTextColor(getResources().getColor(R.color.black));
                break;
            case 2:
                if (Build.VERSION.SDK_INT < 16) {
                    tv_not_approval.setBackgroundDrawable(getResources().getDrawable(R.color.white));
                    tv_pproved.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_bg));
                } else {
                    tv_not_approval.setBackground(getResources().getDrawable(R.color.white));
                    tv_pproved.setBackground(getResources().getDrawable(R.drawable.tab_bg));
                }
                tv_pproved.setTextColor(getResources().getColor(R.color.white));
                tv_not_approval.setTextColor(getResources().getColor(R.color.black));
                break;
        }
    }
}