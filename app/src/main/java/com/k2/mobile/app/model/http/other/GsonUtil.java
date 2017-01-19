/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.http.other;

import com.google.gson.Gson;
import com.k2.mobile.app.common.exception.DataException;
import com.k2.mobile.app.model.bean.Employee;
import com.k2.mobile.app.model.bean.ErrorMsg;
import com.k2.mobile.app.model.bean.HTTPInput;
import com.k2.mobile.app.model.bean.HTTPOutput;
import com.k2.mobile.app.model.bean.User;
import com.k2.mobile.app.utils.EncryptUtil;
import com.k2.mobile.app.utils.LogUtil;

/**
 * @Title GsonUtil.java
 * @Package com.oppo.mo.model.task
 * @Description Gson解析json数据工具类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class GsonUtil {
	
	/**
	 * @Title: getEntityToJson
	 * @Description: 封装实体转JSON字符串
	 * @param @param entity
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	public static <T> String getEntityToJson(Class<T> entity) {
		String strJsonData = null;
		if (entity != null) {

			// 解析json数组
			Gson gson = new Gson();
			
			try {
				
				strJsonData = gson.toJson(entity);
			}
			catch (Exception e) {
				throw new DataException(e);
				//LogUtil.saveCrashInfo2File(e);
				//e.printStackTrace();
			}
		}
		return strJsonData;
	}
	
	/**
	 * @Title: getJSONToEntity
	 * @Description: 封装JSON字符串映射实体方法
	 * @param @param jsonData
	 * @param @param entity
	 * @param @return    设定文件
	 * @return T    返回类型
	 * @throws
	 */
	public static <T> T getJSONToEntity(String jsonData, Class<T> entity) {
		T infoVO = null;

		try {
			if (!"".equals(jsonData)) {
				
				// 替换 \" 为 "
				jsonData = jsonData.replace("\\\"", "\"");
				// 替换 \\ 为 \
				jsonData = jsonData.replace("\\\\", "\\");
				jsonData = jsonData.substring(1, jsonData.length()-1);
				
				// 解析json数组
				Gson gson = new Gson();
				infoVO = gson.fromJson(jsonData, entity);
			}
		}
		catch (Exception e) {
			throw new DataException(e);
			//LogUtil.saveCrashInfo2File(e);
			//e.printStackTrace();
		}
		
		return infoVO;
	}
	
	/**
	 * 序列化User
	 * @param jsonData
	 * @return User
	 */
	public static User getLoginResult(String jsonData) {
		
		User infoVO = null;

		try {
			if (!"".equals(jsonData)) {
				
				// 替换 \" 为 "
				jsonData = jsonData.replace("\\\"", "\"");
				// 替换 \\ 为 \
				jsonData = jsonData.replace("\\\\", "\\");
				jsonData = jsonData.substring(1, jsonData.length()-1);
				
				// 解析json数组
				Gson gson = new Gson();
				infoVO = gson.fromJson(jsonData, User.class);
			}
		}
		catch (Exception e) {
			throw new DataException(e);
			//LogUtil.saveCrashInfo2File(e);
			//e.printStackTrace();
		}
		
		return infoVO;
	}

	/**
	 * 序列化Employee
	 * @param jsonData
	 * @return Employee
	 */
	public static Employee getGetLastAttendanceResult(String jsonData) {
		
		Employee infoVO = null;

		try {
			if (!"".equals(jsonData)) {
				
				// 替换 \" 为 "
				jsonData = jsonData.replace("\\\"", "\"");
				// 替换 \\ 为 \
				jsonData = jsonData.replace("\\\\", "\\");
				jsonData = jsonData.substring(1, jsonData.length()-1);
				
				// 解析json数组
				Gson gson = new Gson();
				infoVO = gson.fromJson(jsonData, Employee.class);
			}
		}
		catch (Exception e) {
			throw new DataException(e);
			//LogUtil.saveCrashInfo2File(e);
			//e.printStackTrace();
		}
		
		return infoVO;
	}

//	/**
//	 * 序列化SyncInfo
//	 * @param jsonData
//	 * @return SyncInfo
//	 */
//	public static SyncInfo getGetSyncInfoResult(String jsonData) {
//		
//		SyncInfo infoVO = null;
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			try {
//				
//				infoVO = gson.fromJson(jsonData, SyncInfo.class);
//			}
//			catch (Exception e) {
//				LogUtil.saveCrashInfo2File(e);
//				e.printStackTrace();
//			}
//		}
//		
//		return infoVO;
//	}
//
//	/**
//	 * 序列化TimeAttendance
//	 * @param jsonData
//	 * @return TimeAttendance
//	 */
//	public static TimeAttendance getAddOrUpdateResult(String jsonData) {
//		
//		TimeAttendance infoVO = null;
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			try {
//				
//				infoVO = gson.fromJson(jsonData, TimeAttendance.class);
//			}
//			catch (Exception e) {
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return infoVO;
//	}

	/**
	 * 序列化ErrorMsg
	 * @param jsonData
	 * @return ErrorMsg
	 */
	public static ErrorMsg getErrorResult(String jsonData) {
		
		ErrorMsg infoVO = null;
		try {
			if (!"".equals(jsonData)) {
				
				// 替换 \" 为 "
				jsonData = jsonData.replace("\\\"", "\"");
				// 替换 \\ 为 \
				jsonData = jsonData.replace("\\\\", "\\");
				jsonData = jsonData.substring(1, jsonData.length()-1);
				
				// 解析json数组
				Gson gson = new Gson();
				
				try {
					
					infoVO = gson.fromJson(jsonData, ErrorMsg.class);
				}
				catch (Exception e) {
					
					e.printStackTrace();
				}
			}
		}
		catch (Exception e) {
			throw new DataException(e);
			//LogUtil.saveCrashInfo2File(e);
			//e.printStackTrace();
		}
		
		return infoVO;
	}
	
//	/**
//	 * 序列化List<Project>
//	 * @param jsonData
//	 * @return List<Project>
//	 */
//	public static List<Project> getProjectInfoVOs(String jsonData) {
//		
//		List<Project> taskInfoVOs = new ArrayList<Project>();
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			Type listType = new TypeToken<List<Project>>(){}.getType();
//			
//			try {
//				
//				taskInfoVOs = gson.fromJson(jsonData, listType);
//			}
//			catch (Exception e) {
//				
//				taskInfoVOs = null;
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return taskInfoVOs;
//	}
//	
//	/**
//	 * 序列化List<EmployeeLastAttendance>
//	 * @param jsonData
//	 * @return List<EmployeeLastAttendance>
//	 */
//	public static List<EmployeeLastAttendance> getEmployeeInfoVOs(String jsonData) {
//		
//		List<EmployeeLastAttendance> taskInfoVOs = new ArrayList<EmployeeLastAttendance>();
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			Type listType = new TypeToken<List<EmployeeLastAttendance>>(){}.getType();
//			
//			try {
//				
//				taskInfoVOs = gson.fromJson(jsonData, listType);
//				for(int i=0; i<taskInfoVOs.size(); i++) {
//					String time = taskInfoVOs.get(i).getTime();
//					time = (time != null?time:"2014-01-01 00:00:00");
//					taskInfoVOs.get(i).setLocal_Time(DateUtil.chageServerTime(DateUtil.stringToDateTimeFormat(time)));
//				}
//			}
//			catch (Exception e) {
//				
//				taskInfoVOs = null;
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return taskInfoVOs;
//	}
//	
//	/**
//	 * 序列化List<TimeAttendance>
//	 * @param jsonData
//	 * @return List<TimeAttendance>
//	 */
//	public static List<TimeAttendance> getTimeAttendanceVOs(String jsonData) {
//		
//		List<TimeAttendance> taskInfoVOs = new ArrayList<TimeAttendance>();
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			Type listType = new TypeToken<List<TimeAttendance>>(){}.getType();
//			
//			try {
//				
//				taskInfoVOs = gson.fromJson(jsonData, listType);
//				for(int i=0; i<taskInfoVOs.size(); i++) {
//					String time = taskInfoVOs.get(i).getTime();
//					time = (time != null?time:"2014-01-01 00:00:00");
//					taskInfoVOs.get(i).setLocal_Time(DateUtil.chageServerTime(DateUtil.stringToDateTimeFormat(time)));
//				}
//			}
//			catch (Exception e) {
//				
//				taskInfoVOs = null;
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return taskInfoVOs;
//	}
	
//	/**
//	 * 序列化List<TaskInfoVO>
//	 * @param jsonData
//	 * @return List<TaskInfoVO>
//	 */
//	public static List<TaskInfoVO> getTaskInfoVOs(String jsonData) {
//		
//		List<TaskInfoVO> taskInfoVOs = new ArrayList<TaskInfoVO>();
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			Type listType = new TypeToken<List<TaskInfoVO>>(){}.getType();
//			
//			try {
//				
//				taskInfoVOs = gson.fromJson(jsonData, listType);
//			}
//			catch (Exception e) {
//				
//				taskInfoVOs = null;
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return taskInfoVOs;
//	}
//	
//	/**
//	 * 序列化TaskInfo
//	 * @param jsonData
//	 * @return TaskInfo
//	 */
//	public static TaskInfo getTaskInfo(String jsonData) {
//		
//		TaskInfo infoVO = null;
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			try {
//				
//				infoVO = gson.fromJson(jsonData, TaskInfo.class);
//			}
//			catch (Exception e) {
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return infoVO;
//	}
//	
//	/**
//	 * 序列化MemberInfo
//	 * @param jsonData
//	 * @return MemberInfo
//	 */
//	public static MemberInfo getMemberInfo(String jsonData) {
//		
//		MemberInfo memberInfo = null;
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			try {
//				
//				memberInfo = gson.fromJson(jsonData, MemberInfo.class);
//			}
//			catch (Exception e) {
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return memberInfo;
//	}
//	
//	/**
//	 * 序列化List<MemberInfo>
//	 * @param jsonData
//	 * @return List<MemberInfo>
//	 */
//	public static List<MemberInfo> getMemberInfos(String jsonData) {
//		
//		List<MemberInfo> MemberInfos = new ArrayList<MemberInfo>();
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			Type listType = new TypeToken<List<MemberInfo>>(){}.getType();
//			
//			try {
//				
//				MemberInfos = gson.fromJson(jsonData, listType);
//			}
//			catch (Exception e) {
//				
//				MemberInfos = null;
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return MemberInfos;
//	}
//	
//	/**
//	 * 序列化ProjectInfo
//	 * @param jsonData
//	 * @return ProjectInfo
//	 */
//	public static ProjectInfo getProjectInfo(String jsonData) {
//		
//		ProjectInfo projectInfo = null;
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			try {
//				
//				projectInfo = gson.fromJson(jsonData, ProjectInfo.class);
//			}
//			catch (Exception e) {
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return projectInfo;
//	}
//	
//	/**
//	 * 序列化List<ProjectInfo>
//	 * @param jsonData
//	 * @return List<ProjectInfo>
//	 */
//	public static List<ProjectInfo> getProjectInfos(String jsonData) {
//		
//		List<ProjectInfo> projectInfos = null;
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			Type listType = new TypeToken<List<ProjectInfo>>(){}.getType();
//			
//			try {
//				
//				projectInfos = gson.fromJson(jsonData, listType);
//			}
//			catch (Exception e) {
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return projectInfos;
//	}
//	
//	/**
//	 * 序列化List<ProjectInfo>
//	 * @param jsonData
//	 * @return List<ProjectInfo>
//	 */
//	public static List<TaskComment> getTaskComments(String jsonData) {
//		
//		List<TaskComment> taskComments = new ArrayList<TaskComment>();
//		
//		if (!"".equals(jsonData)) {
//			
//			// 替换 \" 为 "
//			jsonData = jsonData.replace("\\\"", "\"");
//			// 替换 \\ 为 \
//			jsonData = jsonData.replace("\\\\", "\\");
//			jsonData = jsonData.substring(1, jsonData.length()-1);
//			
//			// 解析json数组
//			Gson gson = new Gson();
//			
//			Type listType = new TypeToken<List<TaskComment>>(){}.getType();
//			
//			try {
//				
//				taskComments = gson.fromJson(jsonData, listType);
//			}
//			catch (Exception e) {
//				
//				taskComments = null;
//				
//				e.printStackTrace();
//			}
//		}
//		
//		return taskComments;
//	}
//	
	

	
	/**
	 * @Title: httpClientRequest
	 * @Description: 客户端数据加密
	 * @param strMsgBody
	 * @param strEncryptKey
	 * @return   
	 * @throws
	 */
	public static String httpClientRequest(String strMsgBody, String strEncryptKey) {
		try {
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");
			long tsClientRequestBegin = System.nanoTime();
			LogUtil.i("TAG: 客户端请求开始时间戳: "+tsClientRequestBegin);
		
			// MD5加密后数据
			String strMD5Encrypt = EncryptUtil.stringToMD5(strMsgBody);
			LogUtil.i("TAG: app端MD5加密后数据: "+strMD5Encrypt);
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");
	
			// MD5加密数据拼接消息体
			String strMD5EncryptTmp = strMD5Encrypt+strMsgBody;
			LogUtil.i("TAG: app端MD5加密数据拼接消息体: "+strMD5EncryptTmp);
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");
	
			// DES加密后数据
			String strDESEncrypt = EncryptUtil.encryptDES(strMD5EncryptTmp, strEncryptKey);
			LogUtil.i("TAG: app端DES加密后数据: "+strDESEncrypt);
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");

	        long tsClientRequestEnd = System.nanoTime();
			LogUtil.i("TAG: 客户端请求时间戳: "+tsClientRequestEnd);
			LogUtil.i("TAG: 请求总耗时: " + (tsClientRequestEnd - tsClientRequestBegin));
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");

			// JSON格式输出
			String strHTTPOutput = "{\\\"ReqBody\\\":\\\""+strDESEncrypt+"\\\",\\\"ReqLang\\\":\\\"zh-CN\\\"}";
			LogUtil.i("TAG: app端strHTTPOutput: "+strHTTPOutput);
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");
		
			return strHTTPOutput;
		} catch(Exception e) {
			return null;
		}
	}
	
	/**
	 * @Title: httpClientResponse
	 * @Description: 客户端数据解密
	 * @param strJsonEncrypt
	 * @param strEncryptKey
	 * @return   
	 * @throws
	 */
	public static String httpClientResponse(String strHTTPInput, String strEncryptKey) {
		String strTmp = null;
		try {
			// GSON解析
			HTTPInput json = GsonUtil.getHTTPInputResult(strHTTPInput);//.getHTTPOutputResult(strHTTPInput);
			
			// 获取加密数据
			String strJsonEncrypt = json.getResBody();
			LogUtil.i("TAG: 中间件获取到的加密数据: "+strJsonEncrypt);
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");
			
//			LogUtils.i("TAG: ----------------------------------------------------------------------------------------------------------");
			long tsClientResponseBegin = System.nanoTime();
			LogUtil.i("TAG: 客户端响应开始时间戳: "+tsClientResponseBegin);
			
			// DES解密后数据
			String strDESDecrypt = EncryptUtil.decryptDES(strJsonEncrypt, strEncryptKey);
			LogUtil.i("TAG: 中间件DES解密后数据: "+strDESDecrypt);
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");
			
			// MD5加密数据分离
			String strMD5EncryptSplit = strDESDecrypt.substring(0, 32);
			LogUtil.i("TAG: 中间件获取的MD5密文: "+strMD5EncryptSplit);
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");
			
			// 消息体
			String strMsgBodySplit = strDESDecrypt.substring(32, strDESDecrypt.length());
			LogUtil.i("TAG: 中间件获取的消息体: "+strMsgBodySplit);
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");

			// MD5验证消息体
			if(strMD5EncryptSplit != null && strMD5EncryptSplit.equals(EncryptUtil.stringToMD5(strMsgBodySplit))) {
				LogUtil.i("TAG: MD5验证通过！");
				LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");
				strTmp = strMsgBodySplit;
			} else {
				LogUtil.i("TAG: MD5不验证通过！");
				LogUtil.i("TAG: 加密前消息体："+strMsgBodySplit);
				LogUtil.i("TAG: 加密后消息体："+strMD5EncryptSplit);
				LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");
			}

	        long tsClientResponseEnd = System.nanoTime();
			LogUtil.i("TAG: 客户端响应时间戳: "+tsClientResponseEnd);
			LogUtil.i("TAG: 响应总耗时: " + (tsClientResponseEnd - tsClientResponseBegin));
			LogUtil.i("TAG: ----------------------------------------------------------------------------------------------------------");

		} catch(Exception e) {
			e.printStackTrace();
		}

		return strTmp;
		
	}
	
	/**
	 * @Title: getHTTPOutputResult
	 * @Description: 序列化HTTPOutput对象
	 * @param strJsonData
	 * @return   
	 * @throws
	 */
	public static HTTPOutput getHTTPOutputResult(String strJsonData) {
		
		HTTPOutput infoVO = null;
		
		if (!"".equals(strJsonData)) {
			LogUtil.i("TAG: strJsonData: "+strJsonData);
			// 替换 \" 为 "
			strJsonData = strJsonData.replace("\\\"", "\"");
			// 替换 \\ 为 \
			strJsonData = strJsonData.replace("\\\\", "\\");
			//strJsonData = strJsonData.substring(1, strJsonData.length()-1);
			
			// 解析json数组
			Gson gson = new Gson();
			
			try {
				
				infoVO = gson.fromJson(strJsonData, HTTPOutput.class);
			}
			catch (Exception e) {
				//LogUtil.saveCrashInfo2File(e);
				e.printStackTrace();
			}
		}
		
		return infoVO;
	}

	/**
	 * @Title: getHTTPInputResult
	 * @Description: 序列化HTTPOutput对象
	 * @param strJsonData
	 * @return   
	 * @throws
	 */
	public static HTTPInput getHTTPInputResult(String strJsonData) {
		
		HTTPInput infoVO = null;
		
		if (!"".equals(strJsonData)) {
			LogUtil.i("TAG: strJsonData: "+strJsonData);
			// 替换 \" 为 "
			strJsonData = strJsonData.replace("\\\"", "\"");
			// 替换 \\ 为 \
			strJsonData = strJsonData.replace("\\\\", "\\");
			//strJsonData = strJsonData.substring(1, strJsonData.length()-1);
			
			// 解析json数组
			Gson gson = new Gson();
			
			try {
				
				infoVO = gson.fromJson(strJsonData, HTTPInput.class);
			}
			catch (Exception e) {
				//LogUtil.saveCrashInfo2File(e);
				e.printStackTrace();
			}
		}
		
		return infoVO;
	}
	
	/**
	 * 去除引号
	 * @return String
	 */
	public static String replaceQuotationMark(String jsonData) {
		String strTmp = null;
		try {
			strTmp = jsonData.replace("\"", "").replace("\\", "");
		}
		catch (Exception e) {
			throw new DataException(e);
			//LogUtil.saveCrashInfo2File(e);
			//e.printStackTrace();
		}
		return strTmp;
	}
}
