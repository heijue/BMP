package com.k2.mobile.app.controller.core;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.fastjson.JSON;
import com.igexin.getuiext.data.Consts;
import com.igexin.sdk.PushConsts;
import com.k2.mobile.app.R;
import com.k2.mobile.app.common.config.BroadcastNotice;
import com.k2.mobile.app.controller.activity.menu.examine.ApprovedInfolActivity;
import com.k2.mobile.app.model.bean.PushBean;

import java.util.List;

public class IReceiver extends BroadcastReceiver{

	private String pageName = "com.oppo.mo";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		 if(intent.getAction().equals(BroadcastNotice.IM_MO)){  
//             isTopActivy(context);
         }else if(intent.getAction().equals(BroadcastNotice.IGEXIN)){
        	 Bundle bundle = intent.getExtras();
        	 String data = null;
        	 switch (bundle.getInt(Consts.CMD_ACTION)) {
				case Consts.GET_MSG_DATA:
					// 获取透传（payload）数据
					byte[] payload = bundle.getByteArray("payload");
					if (payload != null){
						data = new String(payload);
						System.out.println("GexinSdkDemo Got Payload:" + data);
					}
					
					if(null != data){
						PushBean bean = JSON.parseObject(data, PushBean.class);
						//定义NotificationManager
				        String ns = Context.NOTIFICATION_SERVICE;
				        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);
				        //定义通知栏展现的内容信息
				        int icon = R.drawable.ic_ico;
				        CharSequence tickerText = bean.getTitle(); 
				        long when = System.currentTimeMillis();
				        Notification notification = new Notification(icon, tickerText, when);
				        notification.flags = Notification.FLAG_AUTO_CANCEL;
				        //定义下拉通知栏时要展现的内容信息
				        CharSequence contentTitle = bean.getTitle();
				        CharSequence contentText = bean.getFolio();
				        Intent notificationIntent = new Intent(context, ApprovedInfolActivity.class);
				        notificationIntent.putExtra("SN", bean.getSN());
				        notificationIntent.putExtra("destination", bean.getDestination());
				        notificationIntent.putExtra("folio", bean.getFolio());
						
				        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
				        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
				        // 用mNotificationManager的notify方法通知用户生成标题栏消息通知
				        mNotificationManager.notify(1, notification);
					}
					
					break;
				case PushConsts.GET_CLIENTID:
					// 获取ClientID(CID)
					BaseApp.clientid = bundle.getString("clientid");
					System.out.println("IReceiver BaseApp.clientid:" + BaseApp.clientid);
					// TODO:
					/* 第三方应用需要将ClientID上传到第三方服务器，并且将当前用户帐号和ClientID进行关联，
					以便以后通过用户帐号查找ClientID进行消息推送。有些情况下ClientID可能会发生变化，为保证获取最新的ClientID，
					请应用程序在每次获取ClientID广播后，都能进行一次关联绑定 */
					break;
				default:
					break;
			}
         } 
	}
	
	// 保持后台当前切换状态
	public void isTopActivy(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(300);
        for (ActivityManager.RunningTaskInfo info : runningTaskInfos) {
            if (info.topActivity.getPackageName().equals(pageName) || info.baseActivity.getPackageName().equals(pageName)) {
	        	Intent dialogIntent = new Intent(); 
	        	ComponentName cn = new ComponentName(pageName, info.topActivity.getClassName());
	        	dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
	        	dialogIntent.setComponent(cn);
	        	context.startActivity(dialogIntent); 
            }
        }
    }
}
 