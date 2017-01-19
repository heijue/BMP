/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.k2.mobile.app.common.exception.DbException;

/**
 * @Title SQLiteHelper.java
 * @Package com.oppo.mo.model.db
 * @Description SQLite帮助类
 * @Company  K2
 * 
 * @author Jason.Wu
 * @date 2015-01-27 15:13:00
 * @version V1.0
 */
public class CopyOfDBHelper extends SQLiteOpenHelper {
	
	/** 数据库名称 */
	private final static String DB_NAME = "oppo.db";
	
	/** 数据库版本号 */
	private final static int DB_VERSION = 1;

	/** 图片信息表 */
	public final static String TB_PICTURE_INFO = "tb_pricture_info";
	
	/** 数据库操作对象 */
	private static CopyOfDBHelper mdbHelper;

	/** 数据库底层操作对象 */
	private SQLiteDatabase db;
	
	/**
	 * 创建SQLiteHelper
	 * 
	 * @param context
	 * @return
	 */
	public static CopyOfDBHelper create(Context context) {
		return getInstance(context);
	}
	
	/**
	 * 获取SQLiteHelper单例
	 * 
	 * @param context
	 * @return
	 */
	private synchronized static CopyOfDBHelper getInstance(Context context) {
		if (mdbHelper == null) {
			mdbHelper = new CopyOfDBHelper(context);
		}
		return mdbHelper;
	}

//	public SQLiteHelper(Context context) {
//		// TODO Auto-generated constructor stub
//		this.db = FinalDb.create(context, DB_NAME, LogUtil.isDebug);
//	}

	public CopyOfDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		this.db = db;
//		LogUtil.i("DBHelper", "# Create table");

		try {
		/* PictureInfo */
		db.execSQL("create table "+TB_PICTURE_INFO+"(_id integer PRIMARY KEY AUTOINCREMENT,f_name char, f_id char, f_path char, f_totalSize Long, f_status integer, f_createdDate char, f_employeeId char, f_projectId char)");
		} catch(Exception e) {
			throw new DbException(e);
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
	}

	public void closeDb() {
		try {
			if (db != null) {
				db.close();
				db = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new DbException(e);
		}
	}
}
