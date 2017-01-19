package com.k2.mobile.app.controller.activity.login;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.igexin.sdk.PushManager;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.common.config.HttpConstants;
import com.k2.mobile.app.common.config.LocalConstants;
import com.k2.mobile.app.common.exception.HttpException;
import com.k2.mobile.app.controller.activity.MainActivity;
import com.k2.mobile.app.controller.core.BaseActivity;
import com.k2.mobile.app.controller.core.BaseApp;
import com.k2.mobile.app.model.bean.PublicRequestBean;
import com.k2.mobile.app.model.bean.PublicResultBean;
import com.k2.mobile.app.model.bean.ReqBodyBean;
import com.k2.mobile.app.model.bean.ResLoginBean;
import com.k2.mobile.app.model.bean.User;
import com.k2.mobile.app.model.http.ResponseInfo;
import com.k2.mobile.app.model.http.callback.RequestCallBack;
import com.k2.mobile.app.model.http.other.SendRequest;
import com.k2.mobile.app.utils.DialogUtil;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.ErrorCodeContrast;
import com.k2.mobile.app.utils.LogUtil;
import com.k2.mobile.app.utils.NetWorkUtil;
import com.k2.mobile.app.utils.PublicResHeaderUtils;
import com.k2.mobile.app.utils.StringUtil;
import com.k2.mobile.app.utils.ToastUtil;
import com.sangfor.ssl.IVpnDelegate;
import com.sangfor.ssl.SFException;
import com.sangfor.ssl.SangforAuth;
import com.sangfor.ssl.common.VpnCommon;
import com.sangfor.ssl.service.setting.SystemConfiguration;

import java.io.FileNotFoundException;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * @author liangzy
 * @ClassName: LoginActivity
 * @Description: 登陆页面
 * @date 2015-3-6 上午10:20:48
 */
@SuppressLint("NewApi")
public class LoginActivity extends BaseActivity implements OnClickListener, IVpnDelegate {
    private static final String TAG = "SFSDK_" + MainActivity.class.getSimpleName();
    private static boolean VPN_STATE = false;
    private Button btn_login;            // 登录按钮
    private EditText et_userName;        // 用户名
    private EditText et_password;        // 密码
    private CheckBox cb_userName;        // 记住账号
    //	private TextView tv_forgetPsd;		// 忘记密码
    private TextView tv_setting;        // 设置
    //	private LinearLayout ll_logo;
    private TextView tv_logo;
    private TextView tv_user_del, tv_pwd_del;
    private String VPN_IP = "";
    private String USER = "";
    private String PASSWD = "";
    private InetAddress m_iAddr = null;
    private static final int VPN_PORT = 443; // vpn设备端口号，一般为443
    private SharedPreferences sharedPreferences;    // 存放用户信息
    private String userName;                        // 需要存储的用户名
    private String userPwd = null;
    // 广播
    private IncomingReceiver iReceiver = null;
    private SharedPreferences.Editor editor;

    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            DialogUtil.closeDialog();
            switch (msg.what) {
                case 1:
                    // 保存用户账号
                    editor = sharedPreferences.edit();
                    if (cb_userName.isChecked()) {
                        editor.putString("userName", userName);
                    } else {
                        editor.putString("userName", "");
                    }
                    editor.commit();

                    String resJson = (String) msg.obj;
                    if (null == resJson || "".equals(resJson.trim())) {
                        LogUtil.promptInfo(LoginActivity.this, ErrorCodeContrast.getErrorCode("1202", res));
                        break;
                    } else {
                        try {
                            ResLoginBean resBean = JSON.parseObject(resJson, ResLoginBean.class);
                            if (null != resBean) {
                                if (null != resBean.getTokenID()) {
                                    BaseApp.token = resBean.getTokenID();
                                }

                                if (null != resBean.getMask()) {
                                    BaseApp.mask = resBean.getMask();
                                }

                                User user = new User();
                                user.setUserAccount(resBean.getUserAccount());
                                user.setUserId(resBean.getUserAccount());
                                user.setUserName(resBean.getUserChsName());
                                user.setOrgName(resBean.getDepartmentName());
                                user.setRealityOrgName(resBean.getDepartmentName());
                                user.setJobDesc(resBean.getPositionName());
                                BaseApp.user = user;
                                // 将登陆后用户的信息保存到本地
                                startActivity(MainActivity.class);
                                LoginActivity.this.finish();
                            } else {
                                LogUtil.promptInfo(LoginActivity.this, ErrorCodeContrast.getErrorCode("1202", LoginActivity.this.res));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtil.promptInfo(LoginActivity.this, ErrorCodeContrast.getErrorCode("1202", LoginActivity.this.res));
                        }
                    }
                    break;
            }
        }

        ;
    };
    private CheckBox cb_vpn;
    private boolean isVPNChecked = false;
    private boolean vpn_logout = false;
    private boolean vpn_login = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler());
        try {
            com.sangfor.ssl.service.utils.logger.Log.init(getApplicationContext());
            com.sangfor.ssl.service.utils.logger.Log.LEVEL = com.sangfor.ssl.service.utils.logger.Log.DEBUG;
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        setContentView(R.layout.activity_login);
        SangforAuth sfAuth = SangforAuth.getInstance();
        try {
            // SDK模式初始化，easyapp 模式或者是l3vpn模式，两种模式区别请参考文档。
            //		    sfAuth.init(this, this, SangforAuth.AUTH_MODULE_EASYAPP);
            sfAuth.init(this, this, SangforAuth.AUTH_MODULE_L3VPN);
            sfAuth.setLoginParam(AUTH_CONNECT_TIME_OUT, String.valueOf(5));
        } catch (SFException e) {
            e.printStackTrace();
        }
        isVPNChecked = loginSharedPreference.getBoolean("isOpen", false);
        VPN_IP = settingSharedPreference.getString("vpn_ip", "");
        USER = settingSharedPreference.getString("vpn_user", "");
        PASSWD = settingSharedPreference.getString("vpn_passwd", "");
        if (isVPNChecked && !VPN_IP.equals("") && !USER.equals("") && !PASSWD.equals("")) {
            //启动vpn
            initSslVpn();
        }
        initView();                        // 初始化UI
        initListener();                    // 初始化监听器
        createFilter();

    }

    @Override
    protected void onStart() {
        super.onStart();
        VPN_IP = settingSharedPreference.getString("vpn_ip", "");
        USER = settingSharedPreference.getString("vpn_user", "");
        PASSWD = settingSharedPreference.getString("vpn_passwd", "");
        checkLang();                    // 判断语言
    }

    /**
     * 方法名: initView()
     * 功 能 : 初始化
     * 参 数 : void
     * 返回值: void
     */
    private void initView() {
        btn_login = (Button) findViewById(R.id.login_btn);
        et_userName = (EditText) findViewById(R.id.et_login_username);
        et_password = (EditText) findViewById(R.id.et_login_password);
        cb_userName = (CheckBox) findViewById(R.id.cb_userName);
        cb_vpn = (CheckBox) findViewById(R.id.cb_vpn);
        cb_vpn.setChecked(isVPNChecked);
        tv_logo = (TextView) findViewById(R.id.tv_logo);
        tv_user_del = (TextView) findViewById(R.id.tv_user_del);
        tv_pwd_del = (TextView) findViewById(R.id.tv_pwd_del);
        /**
         * 给cb_vpn设置点击回调事件
         */
        cb_vpn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (VPN_IP.equals("") && USER.equals("") && PASSWD.equals("")) {
                    cb_vpn.setChecked(false);
                    ToastUtil.showToast(LoginActivity.this,R.string.set_up_the_VPN);
                    return;
                }

                if (isChecked) {
                        vpn_login = initSslVpn();
                } else {
                     SangforAuth.getInstance().vpnLogout();
                }
                loginSharedPreference.edit().putBoolean("isOpen",isChecked).commit();
            }
        });
        //tv_forgetPsd = (TextView) findViewById(R.id.tv_forgetPsd);
        tv_setting = (TextView) findViewById(R.id.tv_setting);

        // 读取用户账号信息
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_WORLD_READABLE);
        userName = sharedPreferences.getString("userName", null);
        if (null != userName && !"".equals(userName)) {
            et_userName.setText(userName);
            tv_user_del.setVisibility(View.VISIBLE);
        }

        if (null == BaseApp.clientid || "".equals(BaseApp.clientid)) {
            PushManager.getInstance().initialize(getApplicationContext());
            BaseApp.clientid = PushManager.getInstance().getClientid(getApplicationContext());
        }

    }


    /**
     * 方法名: initListener()
     * 功 能 : 初始化监听器
     * 参 数 : void
     * 返回值: void
     */
    private void initListener() {
        et_userName.addTextChangedListener(userTextWatcher);
        et_password.addTextChangedListener(userTextWatcher);
        btn_login.setOnClickListener(this);
        //tv_forgetPsd.setOnClickListener(this);
        tv_user_del.setOnClickListener(this);
        tv_pwd_del.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
    }

    /**
     * @param void
     * @return void
     * @throws
     * @Title: createFilter
     * @Description: 创建IntentFilter
     */
    private void createFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BroadcastNotice.AUTON_LOGIN);
        iReceiver = new IncomingReceiver();
        // 注册广播
        registerReceiver(iReceiver, filter);
    }

    TextWatcher userTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String userName = et_userName.getText().toString();
            String userpwd = et_password.getText().toString();

            if (null == userName || "".equals(userName.trim())) {
                tv_user_del.setVisibility(View.GONE);
            } else {
                tv_user_del.setVisibility(View.VISIBLE);
            }

            if (null == userpwd || "".equals(userpwd.trim())) {
                tv_pwd_del.setVisibility(View.GONE);
            } else {
                tv_pwd_del.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onDestroy() {
        unregisterReceiver(iReceiver);
        super.onDestroy();
    }

    /**
     * 接收广播
     */
    private class IncomingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadcastNotice.AUTON_LOGIN)) {
                // 判断网络是否连接
                if (!NetWorkUtil.isNetworkAvailable(LoginActivity.this)) {
                    DialogUtil.showLongToast(LoginActivity.this, R.string.global_network_disconnected);
                } else {
                    // 获取用户信息
                    String info = getLoginInfo();
                    if (null != info) {
                        // 验证登陆信息
                        SendRequest.submitRequest(LoginActivity.this, info, submitCallBack);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                if (StringUtil.isNullOrEmpty(HttpConstants.DOMAIN_NAME)) {
                    DialogUtil.showLongToast(LoginActivity.this, "请设置服务器地址");
                    break;
                }

                if (StringUtil.isNullOrEmpty(HttpConstants.PROT)) {
                    DialogUtil.showLongToast(LoginActivity.this, "请设置端口");
                    break;
                }
                // 判断网络是否连接
                if (!NetWorkUtil.isNetworkAvailable(LoginActivity.this)) {
                    DialogUtil.showLongToast(LoginActivity.this, R.string.global_network_disconnected);
                    break;
                }
                // 获取用户信息
                String info = getLoginInfo();
                if (null != info) {
                    // 验证登陆信息
                    SendRequest.submitRequest(LoginActivity.this, info, submitCallBack);
                }
                System.out.println(info+"登陆详情");
                break;
            //忘记密码
/*			case R.id.tv_forgetPsd:
                Intent intent = new Intent(this,StaticLinkListActivity.class);
				intent.putExtra(IntentConstant.FORGET_PSD_TYPE, 8);
				startActivity(intent);
				break;*/
            case R.id.tv_user_del:
                et_userName.setText("");
                tv_user_del.setVisibility(View.GONE);
                break;
            case R.id.tv_pwd_del:
                et_password.setText("");
                tv_pwd_del.setVisibility(View.GONE);
                break;
            case R.id.tv_setting:
                Intent mIntent = new Intent(this, SettingActivity.class);
                startActivity(mIntent);
                break;
        }
    }

    /**
     * @param void
     * @return String 返回的数据
     * @throws
     * @Title: getLoginInfo
     * @Description: 获取登陆信息
     */
    private String getLoginInfo() {
        // 取得用户名
        userName = et_userName.getText().toString();
        // 取得密码
        userPwd = et_password.getText().toString();
        // 判别用户名是否为空
        if (null == userName || "".equals(userName.trim())) {
            LogUtil.promptInfo(LoginActivity.this, res.getString(R.string.user_is_null));
            return null;
            // 判别密码是否为空
        } else if (null == userPwd || "".equals(userPwd.trim())) {
            LogUtil.promptInfo(LoginActivity.this, res.getString(R.string.pwd_is_null));
            return null;
        }

        ReqBodyBean bBean = new ReqBodyBean();
        bBean.setInvokeFunctionCode("F10000001");
        bBean.setInvokeParameter("[\"" + userName + "\",\"" + userPwd + "\"]");

        PublicRequestBean bean = new PublicRequestBean();
        bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
        bean.setReqBody(bBean);

        return JSON.toJSONString(bean);
    }

    /**
     * http请求回调
     */
    RequestCallBack<String> submitCallBack = new RequestCallBack<String>() {

        @Override
        public void onStart() {
            DialogUtil.showWithCancelProgressDialog(LoginActivity.this, null, res.getString(R.string.global_prompt_message), null);
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

                if ("1126".equals(resMsg.getResHeader().getStateCode())) {  // 擦除
                    editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    // 判断网络是否连接
                    if (NetWorkUtil.isNetworkAvailable(LoginActivity.this)) {
                        String wipe = userLoginOut();
                        if (null != wipe) {
                            SendRequest.submitRequest(LoginActivity.this, wipe, submitCallBack);
                        }
                    }

                    return;
                }
                // 判断返回标识状态是否为空
                if (null == resMsg || null == resMsg.getResHeader().getStateCode() || "".equals(resMsg.getResHeader().getStateCode())) {
                    LogUtil.promptInfo(LoginActivity.this, ErrorCodeContrast.getErrorCode("0", res));
                    return;
                    // 如果用新手机登陆
                } else if ("1116".equals(resMsg.getResHeader().getStateCode())) {
                    Intent mIntent = new Intent(LoginActivity.this, UserCheckActivity.class);
                    mIntent.putExtra("userName", userName);
                    mIntent.putExtra("userPwd", userPwd);
                    startActivity(mIntent);

                    return;
                    // 验证不合法
                } else if (!"1".equals(resMsg.getResHeader().getStateCode())) {
                    LogUtil.promptInfo(LoginActivity.this, resMsg.getResHeader().getReturnMsg());
                    return;
                }
                // 判断消息体是否为空
                if (null == resMsg.getResBody().getResultString() || "".equals(resMsg.getResBody().getResultString().trim())) {
                    LogUtil.promptInfo(LoginActivity.this, ErrorCodeContrast.getErrorCode("0", res));
                } else {
                    if (null != resMsg.getResHeader().getSectetKey() && !"".equals(resMsg.getResHeader().getSectetKey().trim())) {
                        BaseApp.key = resMsg.getResHeader().getSectetKey();
                    }
                    // 获取解密后并校验后的值
                    Message msgs = new Message();
                    msgs.what = 1;
                    msgs.obj = resMsg.getResBody().getResultString();
                    mHandler.sendMessage(msgs);
                }
            } else {
                LogUtil.promptInfo(LoginActivity.this, ErrorCodeContrast.getErrorCode("0", res));
            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            DialogUtil.closeDialog();
            if (null != msg && msg.equals(LocalConstants.API_KEY)) {
                LogUtil.promptInfo(LoginActivity.this, ErrorCodeContrast.getErrorCode("1207", res));
            } else {
                LogUtil.promptInfo(LoginActivity.this, ErrorCodeContrast.getErrorCode("0", res));
            }
        }
    };

    /**
     * @param void
     * @return String 返回的数据
     * @throws
     * @Title: userMenuInfo
     * @Description: 获取当前用户菜单权限
     */
    private String userLoginOut() {

        ReqBodyBean bBean = new ReqBodyBean();
        bBean.setInvokeFunctionCode("F10000006");

        PublicRequestBean bean = new PublicRequestBean();
        bean.setReqHeader(PublicResHeaderUtils.getReqHeader());
        bean.setReqBody(bBean);

        return JSON.toJSONString(bean);
    }

    private boolean initSslVpn() {
        SangforAuth sfAuth = SangforAuth.getInstance();

        m_iAddr = null;
        final String ip = VPN_IP;

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    m_iAddr = InetAddress.getByName(ip);
                    Log.i(TAG, "ip Addr is : " + m_iAddr.getHostAddress());
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (m_iAddr == null || m_iAddr.getHostAddress() == null) {
            Log.d(TAG, "vpn host error");
            return false;
        }
        long host = VpnCommon.ipToLong(m_iAddr.getHostAddress());
        int port = VPN_PORT;

        if (sfAuth.vpnInit(host, port) == false) {
            Log.d(TAG, "vpn init fail, errno is " + sfAuth.vpnGeterr());
            return false;
        }

        return true;
    }

    @Override
    public void vpnCallback(int vpnResult, int authType) {
        SangforAuth sfAuth = SangforAuth.getInstance();

        switch (vpnResult) {
            case IVpnDelegate.RESULT_VPN_INIT_FAIL:
                /**
                 * 初始化vpn失败
                 */
                Log.i(TAG, "RESULT_VPN_INIT_FAIL, error is " + sfAuth.vpnGeterr());
                displayToast("RESULT_VPN_INIT_FAIL, error is " + sfAuth.vpnGeterr());
                break;

            case IVpnDelegate.RESULT_VPN_INIT_SUCCESS:
                /**
                 * 初始化vpn成功，接下来就需要开始认证工作了
                 */
                Log.i(TAG, "RESULT_VPN_INIT_SUCCESS, current vpn status is " + sfAuth.vpnQueryStatus());
                //                 displayToast("RESULT_VPN_INIT_SUCCESS, current vpn status is "
                //                        + sfAuth.vpnQueryStatus());
                Log.i(TAG, "vpnResult============" + vpnResult + "\nauthType ============" + authType);

                //设置后台不自动登陆,true为off,即取消自动登陆.默认为false,后台自动登陆.
                sfAuth.setLoginParam(AUTO_LOGIN_OFF_KEY, "true");
                // 初始化成功，进行认证操作　（此处采用“用户名密码”认证）
                doVpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);
                break;

            case IVpnDelegate.RESULT_VPN_AUTH_FAIL:
                /**
                 * 认证失败，有可能是传入参数有误，具体信息可通过sfAuth.vpnGeterr()获取
                 */
                String errString = sfAuth.vpnGeterr();
                Log.i(TAG, "RESULT_VPN_AUTH_FAIL, error is " + errString);
                displayToast("RESULT_VPN_AUTH_FAIL, error is " + errString);
                break;

            case IVpnDelegate.RESULT_VPN_AUTH_SUCCESS:
                /**
                 * 认证成功，认证成功有两种情况，一种是认证通过，可以使用sslvpn功能了，
                 *
                 * 另一种是 前一个认证（如：用户名密码认证）通过，但需要继续认证（如：需要继续证书认证）
                 */
                if (authType == IVpnDelegate.AUTH_TYPE_NONE) {
                    Log.i(TAG, "welcome to sangfor sslvpn!");
                    //    displayToast("welcome to sangfor sslvpn!");

                    // 若为L3vpn流程，认证成功后会自动开启l3vpn服务，需等l3vpn服务开启完成后再访问资源
                    if (SangforAuth.getInstance().getModuleUsed() == SangforAuth.AUTH_MODULE_EASYAPP) {
                        // EasyApp流程，认证流程结束，可访问资源。

                    }

                } else if (authType == IVpnDelegate.VPN_TUNNEL_OK) {
                    // l3vpn流程，l3vpn服务通道建立成功，可访问资源


                } else {
                    Log.i(TAG, "auth success, and need next auth, next auth type is " + authType);
                    displayToast("auth success, and need next auth, next auth type is " + authType);

                    if (authType == IVpnDelegate.AUTH_TYPE_SMS) {
                        // 下一次认证为短信认证，获取相关的信息
                        String phoneNum = SangforAuth.getInstance().getSmsPhoneNum();
                        String countDown = SangforAuth.getInstance().getSmsCountDown();
                        String toastStrsg = "sms code send to [" + phoneNum + "]\n"
                                + "reget code count down [" + countDown + "]\n";
                        // Toast.makeText(this, toastStrsg, Toast.LENGTH_LONG).show();
                    } else if (authType == IVpnDelegate.AUTH_TYPE_RADIUS) {
                        // Toast.makeText(this, "start radius challenge auth", Toast.LENGTH_LONG).show();
                    } else {
                        doVpnLogin(authType);
                    }
                }
                break;
            case IVpnDelegate.RESULT_VPN_AUTH_CANCEL:
                Log.i(TAG, "RESULT_VPN_AUTH_CANCEL");
                displayToast("RESULT_VPN_AUTH_CANCEL");
                break;
            case IVpnDelegate.RESULT_VPN_AUTH_LOGOUT:
                /**
                 * 主动注销（自己主动调用logout接口）
                 */
                Log.i(TAG, "RESULT_VPN_AUTH_LOGOUT");
                //displayToast("");
                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_FAIL:
                /**
                 * L3vpn启动失败，有可能是没有l3vpn资源，具体信息可通过sfAuth.vpnGeterr()获取
                 */
                Log.i(TAG, "RESULT_VPN_L3VPN_FAIL, error is " + sfAuth.vpnGeterr());
                displayToast("VPN 启动失败：" + sfAuth.vpnGeterr());
                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_SUCCESS:
                /**
                 * L3vpn启动成功
                 */
                Log.i(TAG, "RESULT_VPN_L3VPN_SUCCESS ===== "
                        + SystemConfiguration.getInstance().getSessionId());
                ToastUtil.showToast(LoginActivity.this, R.string.vpn_state);
                VPN_STATE = true;

                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_RELOGIN:
                /**
                 *  L3vpn服务端注销虚拟IP,一般是私有帐号在其他设备同时登录造成的
                 */
                Log.i(TAG, "relogin now");
                displayToast("relogin now");
                break;
            case IVpnDelegate.VPN_STATUS_ONLINE:
                /**
                 * 与设备连接建立
                 */
                Log.i(TAG, "online");
                displayToast("online");
                break;
            case IVpnDelegate.VPN_STATUS_OFFLINE:
                /**
                 * 与设备连接断开
                 */
                Log.i(TAG, "offline");
                displayToast("offline");
                break;
            default:
                /**
                 * 其它情况，不会发生，如果到该分支说明代码逻辑有误
                 */
                Log.i(TAG, "default result, vpn result is " + vpnResult);
                displayToast("default result, vpn result is " + vpnResult);
                break;
        }
    }

    @Override
    public void reloginCallback(int status, int result) {
        switch (status) {

            case IVpnDelegate.VPN_START_RELOGIN:
                Log.e(TAG, "relogin callback start relogin start ...");
                break;
            case IVpnDelegate.VPN_END_RELOGIN:
                Log.e(TAG, "relogin callback end relogin ...");

                if (result == IVpnDelegate.VPN_RELOGIN_SUCCESS) {
                    Log.e(TAG, "relogin callback, relogin success!");
                    //  displayToast("relogin callback, relogin success! " );
                } else {
                    Log.e(TAG, "relogin callback, relogin failed");
                    //   displayToast("relogin callback, relogin failed");

                }
                break;
        }
    }

    @Override
    public void vpnRndCodeCallback(byte[] data) {
        Log.d(TAG, "vpnRndCodeCallback data: " + Boolean.toString(data == null));
        if (data != null) {
            Log.i(TAG, "vpnRndCodeCallback RndCo we not support RndCode now");
        }
    }

    public static class CrashHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread thread, Throwable ex) {

            // 打印未捕获的异常堆栈
            Log.d(TAG, "UnHandledException: ");
            ex.printStackTrace();
        }
    }

    private void displayToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    private void doVpnLogin(int authType) {
        Log.d(TAG, "doVpnLogin authType " + authType);

        boolean ret = false;
        SangforAuth sfAuth = SangforAuth.getInstance();

        switch (authType) {
            case IVpnDelegate.AUTH_TYPE_CERTIFICATE:
                String certPasswd = "";
                String certName = "";
                sfAuth.setLoginParam(IVpnDelegate.CERT_PASSWORD, certPasswd);
                sfAuth.setLoginParam(IVpnDelegate.CERT_P12_FILE_NAME, certName);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_CERTIFICATE);
                break;
            case IVpnDelegate.AUTH_TYPE_PASSWORD:
                String user = USER;
                String passwd = PASSWD;
                sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_USERNAME, user);
                sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_PASSWORD, passwd);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);
                break;
            case IVpnDelegate.AUTH_TYPE_SMS:
                // 进行短信认证
                String smsCode = "";
                sfAuth.setLoginParam(IVpnDelegate.SMS_AUTH_CODE, smsCode);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS);
                break;
            case IVpnDelegate.AUTH_TYPE_SMS1:
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS1);
                break;

            case IVpnDelegate.AUTH_TYPE_TOKEN:
                String token = "123321";
                sfAuth.setLoginParam(IVpnDelegate.TOKEN_AUTH_CODE, token);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_TOKEN);
                break;

            case IVpnDelegate.AUTH_TYPE_RADIUS:
                //进行挑战认证
                String challenge = "";
                sfAuth.setLoginParam(IVpnDelegate.CHALLENGE_AUTH_REPLY, challenge);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_RADIUS);
                break;
            default:
                Log.w(TAG, "default authType " + authType);
                break;
        }

        if (ret == true) {
            Log.i(TAG, "success to call login method");
        } else {
            Log.i(TAG, "fail to call login method");
        }

    }

    @Override
    /*
	 * l3vpn模式（SangforAuth.AUTH_MODULE_L3VPN）必须重写这个函数：
	 * 注意：当前Activity的launchMode不能设置为 singleInstance，否则L3VPN服务启动会失败。
	 * 原因：
	 * L3VPN模式需要通过startActivityForResult向系统申请使用L3VPN权限，{@link VpnService#prepare}
	 * 但startActivityForResult有限制：
	 * You cannot use startActivityForResult() if the activity being started is not running
	 * in the same task as the activity that starts it.
	 * This means that neither of the activities can have launchMode="singleInstance"
	 * 也就是说当前Activity的launchMode不能设置为 singleInstance
	 *
	 *
	 *EASYAPP模式 (SangforAuth.AUTH_MODULE_EASYAPP）： 请忽略。
	 */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SangforAuth.getInstance().onActivityResult(requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
