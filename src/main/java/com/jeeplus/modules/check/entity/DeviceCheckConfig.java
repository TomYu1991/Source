/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * 设备点检配置Entity
 * @author 汤进国
 * @version 2019-03-16
 */
public class DeviceCheckConfig extends DataEntity<DeviceCheckConfig> {

	private static final long serialVersionUID = 1L;
	private String workingGroup;		// 班组
	private String groupLeader;		//班组长
	private String groupWorker;		//在岗工人
	private String workingArea;		// 作业区域
	private String checkCycle;	// 点检周期
	private String checkDate;		//点检时间
	private String isEnable;	//是否启用
	private Date beginCheckDate;		// 开始 点检日期
	private Date endCheckDate;		// 结束 点检日期
	private String searchFlag;
	private List<DeviceCheckItem> deviceCheckItemList = Lists.newArrayList();		// 子表列表

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckCycle() {
		return checkCycle;
	}

	public void setCheckCycle(String checkCycle) {
		this.checkCycle = checkCycle;
	}


	public String getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}



	public List<DeviceCheckItem> getDeviceCheckItemList() {
		return deviceCheckItemList;
	}

	public void setDeviceCheckItemList(List<DeviceCheckItem> deviceCheckItemList) {
		this.deviceCheckItemList = deviceCheckItemList;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginCheckDate() {
		return beginCheckDate;
	}

	public void setBeginCheckDate(Date beginCheckDate) {
		this.beginCheckDate = beginCheckDate;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndCheckDate() {
		return endCheckDate;
	}

	public void setEndCheckDate(Date endCheckDate) {
		this.endCheckDate = endCheckDate;
	}

	public DeviceCheckConfig() {
		super();
	}

	public DeviceCheckConfig(String id){
		super(id);
	}

	public String getWorkingGroup() {
		return workingGroup;
	}

	public void setWorkingGroup(String workingGroup) {
		this.workingGroup = workingGroup;
	}

	public String getWorkingArea() {
		return workingArea;
	}

	public void setWorkingArea(String workingArea) {
		this.workingArea = workingArea;
	}

	public String getGroupLeader() {
		return groupLeader;
	}

	public void setGroupLeader(String groupLeader) {
		this.groupLeader = groupLeader;
	}

	public String getGroupWorker() {
		return groupWorker;
	}

	public void setGroupWorker(String groupWorker) {
		this.groupWorker = groupWorker;
	}
}