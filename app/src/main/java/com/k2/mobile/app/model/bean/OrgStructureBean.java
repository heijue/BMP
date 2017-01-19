package com.k2.mobile.app.model.bean;    

import com.k2.mobile.app.utils.TreeNodeId;
import com.k2.mobile.app.utils.TreeNodeLabel;
import com.k2.mobile.app.utils.TreeNodePid;

public class OrgStructureBean {

	@TreeNodeId
	private int _id;
	@TreeNodePid
	private int parentId;
	@TreeNodeLabel
	private String name;
	private long length;
	private String desc;

	public OrgStructureBean(int _id, int parentId, String name)
	{
		super();
		this._id = _id;
		this.parentId = parentId;
		this.name = name;
	}
}
 