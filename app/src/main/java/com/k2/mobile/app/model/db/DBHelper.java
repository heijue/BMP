/*
 * Copyright (c) 2015. OPPO Co., Ltd.
 */
package com.k2.mobile.app.model.db;

import java.util.List;

import android.content.Context;

import com.k2.mobile.app.model.db.SQLiteHelper.DaoConfig;
import com.k2.mobile.app.model.db.sqlite.Selector;
import com.k2.mobile.app.model.db.sqlite.WhereBuilder;

/**
 * @Title DBHelper.java
 * @Package com.oppo.mo.model.db
 * @Description SQLite帮助类
 * @Company  K2
 * 
 * @author linqijun
 * @date 2015-04-7 10:17:00
 * @version V1.0
 */
public class DBHelper{
	
	/** 数据库名称 */
	private final static String DB_NAME = "oppo.db";
	
	/** 数据库版本号 */
	private final static int DB_VERSION = 1;
	
	/** 数据库操作对象 */
	private static SQLiteHelper db;

	public DBHelper(Context context){
		DaoConfig config = new DaoConfig(context);
		config.setDbName(DB_NAME);				//	数据库名
		config.setDbVersion(DB_VERSION); 		//	db版本
		// 创建数据库  
	    db = SQLiteHelper.create(config);	
	}
	/**
	 * 创建表
	 */
	public void createTable(Class<?> entity){
		db.createTableIfNotExist(entity);
	}
	/**
	 * 删除表
	 */
	public void dateleTable(Class<?> entity){
		db.dropTable(entity);
	}
	/**
	 * 保存数据
	 * @param <T>
	 */
	public <T> void save(T entity){
		db.save(entity);
	}
	/**
	 * 根据列名删除数据
	 */
	public void delByColumnName(Class<?> entity, String columnName, String value){
		db.delete(entity, WhereBuilder.b(columnName, "=", value));
	}
	/**
	 * 查询所有数据
	 * @param <T>
	 */
	public List<?> query(Class<?> entity){
		return db.findAll(entity); //通过类型查找
	}
	
	/**
	 * 根据条件查询数据
	 * @param <T>
	 * @param column 列名
	 * @param value 值
	 * return <T>
	 */
	public <T> T queryColumn(Class<?> entity, String column, String value){
		return db.findFirst(Selector.from(entity).where(column,"=",value)); 
	}
	
	/**
	 * 根据条件查询数据
	 * @param <T>
	 * @param <T>
	 * @param column 列名
	 * @param value 值
	 * return <T>
	 */
	public <T> List<T> customQuery(Class<?> entity, String column, String value ){
		return db.findAll(Selector.from(entity).where(WhereBuilder.b(column, "=", value))); 
	}
	
	/**
	 * 根据条件查询数据
	 * @param <T>
	 * @param <T>
	 * @param column 列名
	 * @param value 值
	 * @param columns 列名
	 * @param values 值
	 * return <T>
	 */
	public <T> List<T> customQuery(Class<?> entity, String column, String value, String columns, String values){
		return db.findAll(Selector.from(entity).where(WhereBuilder.b(column, "=", value)).and(WhereBuilder.b(columns, "=", values))); 
	}
	
	/**
	 * 根据条件查询数据
	 * @param <T>
	 * @param <T>
	 * @param column 列名
	 * @param value 值
	 * @param columns 列名
	 * @param values 值
	 * return <T>
	 */
	public <T> List<T> customQuery(Class<?> entity, String column1, String value1, String column2, String value2, String column3, String value3){
		return db.findAll(Selector.from(entity).where(WhereBuilder.b(column1, "=", value1)).and(WhereBuilder.b(column2, "=", value2)).and(WhereBuilder.b(column3, "=", value3))); 
	}
	/**
	 * 关闭连接数据库
	 * @param void
	 * return void
	 */
	public void closeDB(){
		db.close();
	}
}
