package com.k2.mobile.app.utils;    

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * @Title DataEraseUtils.java
 * @Package com.oppo.mo.utils.DataEraseUtils
 * @Description 数据擦除类
 * @Company  K2
 * 
 * @author lin
 * @date 2015-03-10 09:06:22
 * @version V1.0
 */  
public class DataEraseUtils {

	private static String PACKAFENAME = "com.oppo.mo";
	
	public DataEraseUtils(){
		
	}
	/**
	 * 方法名: eraseDatabase() 
	 * 功 能 : 擦除数据库 
	 * 参 数 : void 
	 * 返回值: int 是否擦除成功
	 */
	public static int eraseDatabase(){
		
		return 0;
	}
	
	/**
	 * 方法名: eraselocal() 
	 * 功 能 : 擦除本地数据 
	 * 参 数 : void 
	 * 返回值: int 是否擦除成功
	 */
	public static int eraseLocal(){
		
		return 0;
	}
	
	/**
	 * 方法名: eraselocal() 
	 * 功 能 : 擦除缓存数据 
	 * 参 数 : void 
	 * 返回值: int 是否擦除成功
	 */
	public static int eraseCache(){
		
		return 0;
	}
	
	/**
	 * 方法名: eraselocal() 
	 * 功 能 : 程序自动退出 
	 * 参 数 : void 
	 * 返回值: int 是否擦除成功
	 */
	public static void appClose(){
		System.exit(0);
//		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	/**
	 * 方法名: eraselocal() 
	 * 功 能 : 程序卸载 
	 * 参 数 : void 
	 * 返回值: void
	 */
	public static void appUninstall(Context context){
	    Uri packageUri = Uri.parse("package:"+ context.getPackageName());	// 获取包名
	    Intent intent = new Intent(Intent.ACTION_DELETE,packageUri);		// 发送包名广播进行删除
	    context.startActivity(intent);  
	}
	
	/* 
	 * m命令可以通过adb在shell中执行，同样，我们可以通过代码来执行 
	 */  
	public static String execCommand(String ...command)  {  
		Process process=null;  
		InputStream errIs=null;  
		InputStream inIs=null;  
		String result="";  

		try {  
			process=new ProcessBuilder().command(command).start();  
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			int read = -1;  
			errIs=process.getErrorStream();           
			while((read=errIs.read()) != -1){  
				baos.write(read);  
			}  
			inIs=process.getInputStream();  
			while((read=inIs.read()) != -1){  
				baos.write(read);  
			}  
			result=new String(baos.toByteArray());  
			if(inIs!=null)  
				inIs.close();  
			if(errIs!=null)  
				errIs.close();  
			process.destroy();  
		} catch (IOException e) {  
			result = e.getMessage();  
		}  
		return result;  
	}  
}
 