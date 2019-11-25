/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.entity;

import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 设备状态管理Entity
 * @author 汤进国
 * @version 2019-01-02
 */
public class DeviceCheckRitem extends DataEntity<DeviceCheckRitem> {

	private static final long serialVersionUID = 1L;

	private String id;		// id
	private String recordId;		// 主表id
	private String deviceName;		// 设备名称
	private String checkItem;		// 点检项目
	private String checkContent;		// 点检内容
	private String checkReference;		// 点检标准
	private String checkMethod;		// 点检方式
	private String deviceState;		//设备状态
	private String checkResult;		// 点检结果
	private String searchFlag;

	public String getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(String deviceState) {
		this.deviceState = deviceState;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public DeviceCheckRitem() {
		super();
	}

	public DeviceCheckRitem(String id){
		super(id);
	}

	@ExcelField(title="主表id", align=2, sort=7)
	public String getRecordId() {
		return this.recordId;
	}
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}

	@ExcelField(title="设备名称", align=2, sort=7)
	public String getDeviceName() {
		return this.deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@ExcelField(title="点检项目", align=2, sort=7)
	public String getCheckItem() {
		return this.checkItem;
	}
	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}

	@ExcelField(title="点检内容", align=2, sort=7)
	public String getCheckContent() {
		return this.checkContent;
	}
	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}

	@ExcelField(title="点检标准", align=2, sort=7)
	public String getCheckReference() {
		return this.checkReference;
	}
	public void setCheckReference(String checkReference) {
		this.checkReference = checkReference;
	}

	@ExcelField(title="点检方式", align=2, sort=7)
	public String getCheckMethod() {
		return this.checkMethod;
	}
	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}

	@ExcelField(title="点检结果", align=2, sort=7)
	public String getCheckResult() {
		return this.checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}



	
}