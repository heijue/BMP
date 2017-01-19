package com.k2.mobile.app.model.bean;    

import java.util.List;

/**
 * @Title: ResNewsBean.java
 * @Package com.oppo.mo.model.bean
 * @Description: 新闻实体类
 * @Company:广东欧泊移动通讯有限公司
 * 
 * @author linqijun	
 * @date 2015-04-27 15:57:00
 * @version V1.0
 */ 
public class ResNewsBean {
	// 置顶新闻集合
	private List<TopNewsBean> newsTopRspEntities ;
	// 普通新闻集合
	private List<CommonNewsBean> newsListRspEntities;
	
	public List<TopNewsBean> getNewsTopRspEntities() {
		return newsTopRspEntities;
	}
	public void setNewsTopRspEntities(List<TopNewsBean> newsTopRspEntities) {
		this.newsTopRspEntities = newsTopRspEntities;
	}
	public List<CommonNewsBean> getNewsListRspEntities() {
		return newsListRspEntities;
	}
	public void setNewsListRspEntities(List<CommonNewsBean> newsListRspEntities) {
		this.newsListRspEntities = newsListRspEntities;
	}
}
 