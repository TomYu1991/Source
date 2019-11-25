/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.station.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 设备管理Entity
 * @author 汤进国
 * @version 2019-01-02
 */
public class StationDevice extends DataEntity<StationDevice> {
	
	private static final long serialVersionUID = 1L;
	private String deviceName;		// 设备名称
	private String deviceNum;		// 设备编号
	private String deviceType;		// 设备类型
	private WorkStation station;		// 工作站id 父类
	
	public StationDevice() {
		super();
	}

	public StationDevice(String id){
		super(id);
	}

	public StationDevice(WorkStation station){
		this.station = station;
	}

	@ExcelField(title="设备名称", align=2, sort=6)
	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	
	@ExcelField(title="设备编号", align=2, sort=7)
	public String getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	
	@ExcelField(title="设备类型", dictType="device_type", align=2, sort=8)
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	public WorkStation getStation() {
		return station;
	}

	public void setStation(WorkStation station) {
		this.station = station;
	}
	
}