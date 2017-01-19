/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */ 
package com.k2.mobile.app.utils;    

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;

import com.k2.mobile.app.common.exception.DataException;
  
/**
 * @Title EncryptUtil.java
 * @Package com.oppo.mo.utilse
 * @Description 数据加密&解密类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class EncryptUtil {
	
	/** 缓存区 */
	private static final int BUFFER_SIZE = 1024;

	/** 编码字符集UTF-8 */
	private static final String CHARESET_UTF8 = "UTF-8";
	
	/** 加密算法MD5 */
	private static final String ALGORITHM_MD5 = "MD5";

	/** 字符匹配常量 */
	private static final String COMPARE_STRING = "0123456789abcdef";

	/** 算法DES-偏移向量 */
	private static byte[] BYTE_IV = {1,2,3,4,5,6,7,8};//"12345678".getBytes();

	/** 加密算法DES */
	private static final String ALGORITHM_DES = "DES";

	/** 加密算法DES */
	private static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";//DES/CBC/PKCS5Padding
	
	/** Base64编码合法字符集 */
	private static final char[] LEGAL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	
	/**
	 * @Title: encryptDES
	 * @Description: DES加密算法
	 * @param encryptString 待加密的数据
	 * @param encryptKey    加密的KEY
	 * @return String 返回类型
	 * @throws
	 */
	public static String encryptDES(String encryptString, String encryptKey) {
		
		String strEncrypt = null;
		try {
			IvParameterSpec zeroIv = new IvParameterSpec(BYTE_IV);
			SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), ALGORITHM_DES);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
			byte[] encryptedData = cipher.doFinal(encryptString.getBytes(CHARESET_UTF8));
			strEncrypt =  EncryptUtil.encodeBase64(encryptedData);//new String(bytesToHexString(encryptedData));//new String(Base64.encode(encryptedData, Base64.DEFAULT)); //new String(Base64.encode(encryptedData, 0, encryptedData.length, Base64.DEFAULT), CHARESET_UTF8);//new String(encryptedData);		
		} catch(Exception e) {
			throw new DataException(e);
		}
		return strEncrypt;
	}
	
	/**
	 * @Title: decryptDES
	 * @Description: DES解密算法
	 * @param decryptString 待解密的数据
	 * @param decryptKey    解密的KEY
	 * @return   
	 * @throws
	 */
	public static String decryptDES(String decryptString, String decryptKey) {
		String strDecrypt = null;
		if (null == decryptKey) {
			return null;
		}
		try {
			byte[] byteMi = EncryptUtil.decodeBase64(decryptString);//byte[] byteMi = hexStringToBytes(decryptString);//decryptString.getBytes();//new Base64.encode(encode, 0, encode.length, Base64.DEFAULT);//new Base64.decode(decryptString);
			IvParameterSpec zeroIv = new IvParameterSpec(BYTE_IV);
			SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), ALGORITHM_DES);
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
			byte decryptedData[] = cipher.doFinal(byteMi);
			strDecrypt = new String(decryptedData);//new String(bytesToHexString(decryptedData));//new String(decryptedData, Base64.DEFAULT); //new String(Base64.encode(decryptedData, 0, decryptedData.length, Base64.DEFAULT), CHARESET_UTF8);//new String(decryptedData);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return strDecrypt;
	}
	
//	/**
//	 * @Title: encryptBASE64
//	 * @Description: BASE64 加密
//	 * @param strTmp
//	 * @return   
//	 * @throws
//	 */
//	public static String encryptBASE64(String strTmp) {
//		if (strTmp == null || strTmp.length() == 0) {
//			return null;
//		}
//		try {
//			byte[] encode = strTmp.getBytes(CHARESET_UTF8);
//			// base64 加密
//			return new String(Base64.encode(encode, 0, encode.length, Base64.DEFAULT), CHARESET_UTF8);
//
//		} catch (UnsupportedEncodingException e) {
//			throw new DataException(e);
//			//e.printStackTrace();
//		}
//
//		return null;
//	}
//
//	/**
//	 * @Title: decryptBASE64
//	 * @Description: BASE64 解密
//	 * @param strTmp
//	 * @return   
//	 * @throws
//	 */
//	public static String decryptBASE64(String strTmp) {
//		if (strTmp == null || strTmp.length() == 0) {
//			return null;
//		}
//		try {
//			byte[] encode = strTmp.getBytes(CHARESET_UTF8);
//			// base64 解密
//			return new String(Base64.decode(encode, 0, encode.length, Base64.DEFAULT), CHARESET_UTF8);
//
//		} catch (UnsupportedEncodingException e) {
//			throw new DataException(e);
//			//e.printStackTrace();
//		}
//
//		return null;
//	}
	
	/**
	 * @Title: encodeBase64
	 * @Description: 对byte数组进行Base64编码
	 * @param data
	 * @return   
	 * @throws
	 */
    public static String encodeBase64(byte[] data) {
        int start = 0;
        int len = data.length;
        StringBuffer buf = new StringBuffer(data.length * 3 / 2);

        try {
			
        	int end = len - 3;
            int i = start;
            int n = 0;

            while (i <= end) {
                int d = (((data[i]) & 0x0ff) << 16)
                        | (((data[i + 1]) & 0x0ff) << 8)
                        | ((data[i + 2]) & 0x0ff);

                buf.append(LEGAL_CHARS[(d >> 18) & 63]);
                buf.append(LEGAL_CHARS[(d >> 12) & 63]);
                buf.append(LEGAL_CHARS[(d >> 6) & 63]);
                buf.append(LEGAL_CHARS[d & 63]);

                i += 3;

                if (n++ >= 14) {
                    n = 0;
                    buf.append(" ");
                }
            }

            if (i == start + len - 2) {
                int d = (((data[i]) & 0x0ff) << 16)
                        | (((data[i + 1]) & 255) << 8);

                buf.append(LEGAL_CHARS[(d >> 18) & 63]);
                buf.append(LEGAL_CHARS[(d >> 12) & 63]);
                buf.append(LEGAL_CHARS[(d >> 6) & 63]);
                buf.append("=");
            } else if (i == start + len - 1) {
                int d = ((data[i]) & 0x0ff) << 16;

                buf.append(LEGAL_CHARS[(d >> 18) & 63]);
                buf.append(LEGAL_CHARS[(d >> 12) & 63]);
                buf.append("==");
            }
        } catch (Exception e) {
			throw new DataException(e);
		}

        return buf.toString();
    }

    /**
     * @Title: decodeBase64
     * @Description: 字符转ASCII编码
     * @param c
     * @return   
     * @throws
     */
    private static int decodeBase64(char c) {
        if (c >= 'A' && c <= 'Z')
            return (c) - 65;
        else if (c >= 'a' && c <= 'z')
            return (c) - 97 + 26;
        else if (c >= '0' && c <= '9')
            return (c) - 48 + 26 + 26;
        else
            switch (c) {
            case '+':
                return 62;
            case '/':
                return 63;
            case '=':
                return 0;
            default:
                //throw new RuntimeException("unexpected code: " + c);
    			throw new DataException(""+c);
            }
    }

    /**
     * @Title: decodeBase64
     * @Description: Base64编码字符串转byte数组
     * @param s
     * @return   
     * @throws
     */
    public static byte[] decodeBase64(String s) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            decodeBase64(s, bos);
        } catch (IOException e) {
            //throw new RuntimeException();
        	throw new DataException(e);
        }
        byte[] decodedBytes = bos.toByteArray();
        try {
            bos.close();
            bos = null;
        } catch (IOException ex) {
            //System.err.println("Error while decoding BASE64: " + ex.toString());
//        	LogUtils.i("TAG: Error while decoding BASE64: " + ex.getMessage());
        	throw new DataException(ex);
        }
        return decodedBytes;
    }

    /**
     * @Title decodeBase64
     * @Description: Base64编码字符串转输出流
     * @param s
     * @param os
     * @throws IOException   
     * @throws
     */
    private static void decodeBase64(String s, OutputStream os) throws IOException {
        int i = 0;

        int len = s.length();

        while (true) {
            while (i < len && s.charAt(i) <= ' ')
                i++;

            if (i == len)
                break;

            int tri = (decodeBase64(s.charAt(i)) << 18)
                    + (decodeBase64(s.charAt(i + 1)) << 12)
                    + (decodeBase64(s.charAt(i + 2)) << 6)
                    + (decodeBase64(s.charAt(i + 3)));

            os.write((tri >> 16) & 255);
            if (s.charAt(i + 2) == '=')
                break;
            os.write((tri >> 8) & 255);
            if (s.charAt(i + 3) == '=')
                break;
            os.write(tri & 255);

            i += 4;
        }
    }
	
	/**
	 * @Title: hexStringToBytes
	 * @Description: 十六进制字符串 转换为 byte[]
	 * @param hexString
	 * @return   
	 * @throws
	 */
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	/**
	 * @Title: charToByte
	 * @Description: char类型转byte类型
	 * @param c
	 * @return   
	 * @throws
	 */
	private static byte charToByte(char c) {
		return (byte) COMPARE_STRING.indexOf(c);
		// return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * @Title: bytesToHexString
	 * @Description: byte[] 转换为 十六进制字符串
	 * @param srcBytes
	 * @return   
	 * @throws
	 */
	public static String bytesToHexString(byte[] srcBytes) {
		
		StringBuilder stringBuilder = new StringBuilder("");

		if (srcBytes == null || srcBytes.length <= 0) {
			return null;
		}

		for (int i = 0; i < srcBytes.length; i++) {
			int v = srcBytes[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		
		return stringBuilder.toString();
	}
	
	/**
	 * @Title: stringToMD5
	 * @Description: 将字符串转成32位MD5值
	 * @param string
	 * @return   
	 * @throws
	 */
	public static String stringToMD5(String string) {
		byte[] hash;

		try {
			hash = MessageDigest.getInstance(ALGORITHM_MD5).digest(string.getBytes(CHARESET_UTF8));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}

		return hex.toString();
	}

	
	/**
	 * @Title: encryptGZIP
	 * @Description: GZIP加密
	 * @param strTmp
	 * @return   
	 * @throws
	 */
	public static byte[] encryptGZIP(String strTmp) {
		byte[] encode;
		if (strTmp == null || strTmp.length() == 0) {
			return null;
		}

		try {
			// gzip压缩
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(baos);
			gzip.write(strTmp.getBytes(CHARESET_UTF8));

			gzip.close();

			encode = baos.toByteArray();

			baos.flush();
			baos.close();

			// base64 加密
			//return encode;
//			return new String(encode, "UTF-8");

		} catch (UnsupportedEncodingException e) {
			throw new DataException(e);
			//e.printStackTrace();
		} catch (IOException e) {
			throw new DataException(e);
			//e.printStackTrace();
		}

		return encode;
	}
	
	/**
	 * @Title: decryptGZIP
	 * @Description: GZIP解密
	 * @param strTmp
	 * @return   
	 * @throws
	 */
	public static String decryptGZIP(String strTmp) {
		if (strTmp == null || strTmp.length() == 0) {
			return null;
		}

		try {
			
			byte[] decode = strTmp.getBytes(CHARESET_UTF8);
			
			//gzip 解压缩
			ByteArrayInputStream bais = new ByteArrayInputStream(decode);
			GZIPInputStream gzip = new GZIPInputStream(bais);

			byte[] buf = new byte[BUFFER_SIZE];
			int len = 0;
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			while((len=gzip.read(buf, 0, BUFFER_SIZE))!=-1){
				 baos.write(buf, 0, len);
			}
			gzip.close();
			baos.flush();
			
			decode = baos.toByteArray();
			
			baos.close();

			return new String(decode, CHARESET_UTF8);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * @Title: getJsonStringFromGZIP
	 * @Description: GZIP解压缩，转成JSON字符串
	 * @param response
	 * @return String    返回类型
	 * @throws
	 */
	public static String getJsonStringFromGZIP(HttpResponse response) {
        String jsonString = null;
        try {
            InputStream is = response.getEntity().getContent();
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            // Gzip 流 的前两个字节是 0x1f8b
            if (result != -1 && headerData == 0x1f8b) {
//                LogUtils.i("TAG:  use GZIPInputStream  ");
                is = new GZIPInputStream(bis);
            } else {
//                LogUtils.i("TAG:  not use GZIPInputStream");
                is = bis;
            }
            InputStreamReader reader = new InputStreamReader(is, "utf-8");
            char[] data = new char[100];
            int readSize;
            StringBuffer sb = new StringBuffer();
            while ((readSize = reader.read(data)) > 0) {
                sb.append(data, 0, readSize);
            }
            jsonString = sb.toString();
            bis.close();
            reader.close();
        } catch (Exception e) {
//            LogUtil.e("TAG", e.toString(),e);
            throw new DataException(e);
        }
 
//        LogUtils.i("TAG: getJsonStringFromGZIP net output : " + jsonString );
        return jsonString;
    }
 
	/**
	 * @Title: getShort
	 * @Description: 字节转int类型
	 * @param @param data
	 * @param @return    设定文件
	 * @return int    返回类型
	 * @throws
	 */
    private static int getShort(byte[] data) {
        return (data[0]<<8) | data[1]&0xFF;
    }
    
    /**
	 * @Title: getEncodeData
	 * @Description: 		将数据加密
	 * @param json  	  	JOSN数据
	 * @param url    	  	方法的URL
	 * @param encryptKey 	加密钥
	 * @return String    	返回加密过后的数据
	 * @throws
	 */
    public static String getEncodeData(String json, String encryptKey){
		if(null == json || null == encryptKey){			 
			return null;
		}
		// MD5数据加密
		String md5 = EncryptUtil.stringToMD5(json);	
		// DES数据加密
		String des = EncryptUtil.encryptDES(md5 + json, encryptKey);	
		// Base64转码
//		String base64 = EncryptUtil.encodeBase64(des.getBytes());		
		return des;
	}
    
    /**
	 * @Title: getEncodeData
	 * @Description: 		将数据解密码
	 * @param json  	  	JOSN数据
	 * @param encryptKey 	解密钥
	 * @return String    	返回解密过后的数据
	 * @throws
	 */
    public static String getDecodeData(String json, String encryptKey){
		if(null == json || null == encryptKey){
			return null;
		}
		// decodeBase64解密
		byte[] ebase64 = null;
		// DES解密
		String des = null;
		
//		ebase64 = EncryptUtil.decodeBase64(json);
//		des = EncryptUtil.decryptDES(new String(ebase64), encryptKey);	
		des = EncryptUtil.decryptDES(json, encryptKey);	
		if(des != null){
			// 将数据拆分，获取前32位校验码
			String checkCode = des.substring(0, 32);	
			// 将数据拆分，获取后数据信息
			String data = des.substring(32, des.length());    		
			// 将数据通过MD5生成校验码
			String md5 = EncryptUtil.stringToMD5(data);								
			// 如果校验通过，返回数据
			if(checkCode.equals(md5)){												
				return data;
			}else{
				return null;
			}
		} else {
			return null;
		}
	}
}
 