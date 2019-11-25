/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.entity;

import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

/**
 * 设备点检配置子表Entity
 * @author yuzhongtong
 * @version 2019-06-05
 */
public class DeviceCheckItem extends DataEntity<DeviceCheckItem> {
	
	private static final long serialVersionUID = 1L;
	private DeviceCheckConfig deviceCheckConfig;
	private DeviceCheckRecord deviceCheckRecord;
	private String configId;	//配置表id
	private String recordId;	//记录表id
	private String deviceName;		// 设备名称
	private String checkItem;		// 点检项目
	private String checkContent;		// 点检内容
	private String checkReference;		// 点检标准
	private String checkResult;		// 点检结果
    private String checkMethod;     //点检方式
	private String deviceState;		//设备状态
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

	public String getCheckMethod() {
        return checkMethod;
    }

    public void setCheckMethod(String checkMethod) {
        this.checkMethod = checkMethod;
    }

    public DeviceCheckItem() {
		super();
	}
	public DeviceCheckItem(String id){
		super(id);
	}

	@ExcelField(title="设备名称", align=2, sort=2)
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@ExcelField(title="点检项目", align=2, sort=3)
	public String getCheckItem() {
		return checkItem;
	}

	public void setCheckItem(String checkItem) {
		this.checkItem = checkItem;
	}
	
	@ExcelField(title="点检内容", align=2, sort=4)
	public String getCheckContent() {
		return checkContent;
	}

	public void setCheckContent(String checkContent) {
		this.checkContent = checkContent;
	}
	
	@ExcelField(title="点检标准", align=2, sort=5)
	public String getCheckReference() {
		return checkReference;
	}

	public void setCheckReference(String checkReference) {
		this.checkReference = checkReference;
	}
	
	@ExcelField(title="点检结果", dictType="check_result", align=2, sort=6)
	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}

	public DeviceCheckConfig getDeviceCheckConfig() {
		return deviceCheckConfig;
	}

	public void setDeviceCheckConfig(DeviceCheckConfig deviceCheckConfig) {
		this.deviceCheckConfig = deviceCheckConfig;
	}

	public DeviceCheckRecord getDeviceCheckRecord() {
		return deviceCheckRecord;
	}

	public void setDeviceCheckRecord(DeviceCheckRecord deviceCheckRecord) {
		this.deviceCheckRecord = deviceCheckRecord;
	}

	public String getConfigId() {
		return configId;
	}

	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getRecordId() {
		return recordId;
	}

	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
}