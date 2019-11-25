/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.devicemanage.entity;

import com.jeeplus.modules.station.entity.WorkStation;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 设备状态管理Entity
 * @author 汤进国
 * @version 2019-01-02
 */
public class DeviceManage extends DataEntity<DeviceManage> {
	
	private static final long serialVersionUID = 1L;
	private WorkStation station;		// 工作站
	private String deviceDeviceNo;		// 设备编码
	private String deviceName;		// 设备名称
	private String deviceFlag;		// 设备状态
	private String deviceType;		// 设备种类
	private String searchFlag;

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public DeviceManage() {
		super();
	}

	public DeviceManage(String id){
		super(id);
	}

	@ExcelField(title="工作站", fieldType=WorkStation.class, value="station.name", align=2, sort=6)
	public WorkStation getStation() {
		return station;
	}

	public void setStation(WorkStation station) {
		this.station = station;
	}
	
	@ExcelField(title="设备编码", align=2, sort=7)
	public String getDeviceDeviceNo() {
		return deviceDeviceNo;
	}

	public void setDeviceDeviceNo(String deviceDeviceNo) {
		this.deviceDeviceNo = deviceDeviceNo;
	}
	
	@ExcelField(title="设备名称", align=2, sort=8)
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@ExcelField(title="设备状态", dictType="device_flag", align=2, sort=9)
	public String getDeviceFlag() {
		return deviceFlag;
	}

	public void setDeviceFlag(String deviceFlag) {
		this.deviceFlag = deviceFlag;
	}
	
	@ExcelField(title="设备种类", dictType="device_type", align=2, sort=10)
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
}