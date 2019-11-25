/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.station.entity;

import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 工作站管理Entity
 * @author 汤进国
 * @version 2019-01-02
 */
public class WorkStation extends DataEntity<WorkStation> {
	
	private static final long serialVersionUID = 1L;
	private String workStation;		// 名称
	private String location;		// 位置
	private String type;		// 类型
	private List<StationDevice> stationDeviceList = Lists.newArrayList();		// 子表列表
	private List<StationWorker> stationWorkerList = Lists.newArrayList();		// 子表列表
	private String userId;    //当前登录人id
	private String deviceNo;   //设备编号
	private String deviceType;   //设备类型
	private String deviceName;   //设备名称
	private String inPassCode;   //工作站放行码
	private String outPassCode;   //工作站放行码
	private String stationIp;  //工作站IP

	@ExcelField(title="工作站IP地址", align=2, sort=9)
	public String getStationIp() {
		return stationIp;
	}

	public void setStationIp(String stationIp) {
		this.stationIp = stationIp;
	}

	@ExcelField(title="进门放行码", align=2, sort=10)
	public String getInPassCode() {
		return inPassCode;
	}

	public void setInPassCode(String inPassCode) {
		this.inPassCode = inPassCode;
	}

	@ExcelField(title="出门放行码", align=2, sort=11)
	public String getOutPassCode() {
		return outPassCode;
	}

	public void setOutPassCode(String outPassCode) {
		this.outPassCode = outPassCode;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public WorkStation() {
		super();
	}

	public WorkStation(String id){
		super(id);
	}

	@ExcelField(title="名称", align=2, sort=6)
	public String getWorkStation() {
		return workStation;
	}

	public void setWorkStation(String workStation) {
		this.workStation = workStation;
	}
	
	@ExcelField(title="位置", align=2, sort=7)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	@ExcelField(title="类型", dictType="station_type", align=2, sort=8)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public List<StationDevice> getStationDeviceList() {
		return stationDeviceList;
	}

	public void setStationDeviceList(List<StationDevice> stationDeviceList) {
		this.stationDeviceList = stationDeviceList;
	}
	public List<StationWorker> getStationWorkerList() {
		return stationWorkerList;
	}

	public void setStationWorkerList(List<StationWorker> stationWorkerList) {
		this.stationWorkerList = stationWorkerList;
	}
}