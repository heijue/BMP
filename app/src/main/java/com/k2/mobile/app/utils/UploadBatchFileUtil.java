package com.k2.mobile.app.utils;    
  
import java.io.File;  
import java.io.IOException;  
import java.util.ArrayList;  
import java.util.Map;  

import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.entity.mime.HttpMultipartMode;  
import org.apache.http.entity.mime.MultipartEntityBuilder;  
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;  
  
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.os.Handler;  
import android.os.Message;
   
/** 
 * 采用HttpClient上传文件,支持多文件上传 
 */  
public class UploadBatchFileUtil {  
	
    public static final int UPLOAD_SUCCESS = 0x123;  
    public static final int UPLOAD_FAIL = 0x124;  
    private Handler handler;  
    
    public UploadBatchFileUtil(Handler handler) {  
        this.handler = handler;  
    }     
    /** 
     * @param params 请求参数，包括请求的的方法参数method如：“upload”， 
     * 请求上传的文件类型fileTypes如：“.jpg.png.docx” 
     * @param files 要上传的文件集合 
     */  
    public void uploadFileToServer(final String url, final Map<String, String> params, final ArrayList<File> files) {  
       if(null == files || files.size() < 1){
    	   Message msg = new Message();
           msg.what = 999;
           handler.sendMessage(msg);// 通知主线程数据发送成功  
           return;
        }
    	new Thread(new Runnable() {           
            @Override  
            public void run() {  
            	Message msg = new Message();
                try {  
                	String res = uploadFiles(url,params,files);
                     if (null != res) {  
                     	msg.obj = res;
                    }
                } catch (Exception e) {  
                    e.printStackTrace();  
                } 
                msg.what = 999;
                handler.sendMessage(msg);	// 通知主线程数据发送成功  
            }  
        }).start();  
    }  
    /** 
     * @param url servlet的地址 
     * @param params 要传递的参数 
     * @param files 要上传的文件 
     * @return true if upload success else false 
     * @throws ClientProtocolException 
     * @throws IOException 
     */  
    private String uploadFiles(String url, Map<String, String> params, ArrayList<File> files) throws ClientProtocolException, IOException {  
       
    	HttpPost post = new HttpPost(url);									// 创建 HTTP POST 请求
    	HttpClient client = new DefaultHttpClient();						// 开启一个客户端 HTTP 请求   
    	HttpParams param = client.getParams();  
    	HttpConnectionParams.setConnectionTimeout(param, 5000);  			// 设置连接超时
        HttpConnectionParams.setSoTimeout(param, 8000);  					// 设置请求超时
        
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();  
//      builder.setCharset(Charset.forName("uft-8"));						// 设置请求的编码格式  
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);				// 设置浏览器兼容模式  
        int count = 0;  
        for (File file:files) {  
          FileBody fileBody = new FileBody(file);							// 把文件转换成流对象FileBody  
          builder.addPart("files["+count+"]", fileBody);   					// files[] 是服务器参数名称
//            builder.addBinaryBody("files["+count+"]", file);  
            count++;  
        }  
        
        builder.addTextBody("method", params.get("method"));				// 设置请求参数  
        builder.addTextBody("fileTypes", params.get("fileTypes"));			// 设置请求参数 
        
        HttpEntity entity = builder.build();								// 生成 HTTP POST 实体        
        post.setEntity(entity);												// 设置请求参数  
        HttpResponse response = client.execute(post);						// 发起请求 并返回请求的响应  
        
        if (response.getStatusLine().getStatusCode() == 200) {
        	String res = EntityUtils.toString(response.getEntity());
        	System.out.println(res);
            return res;  
        } 
        
        return null;         
    }  
}  