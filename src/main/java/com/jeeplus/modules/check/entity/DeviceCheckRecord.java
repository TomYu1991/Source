/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.entity;

import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.core.persistence.DataEntity;

import java.util.Date;
import java.util.List;

/**
 * 员工通行记录Entity
 * @author
 * @version
 */
public class DeviceCheckRecord extends DataEntity<DeviceCheckRecord> {

	private static final long serialVersionUID = 1L;

	private String id;		// id
	private String workingGroup;		// 班组
	private String workingArea;		// 作业区域
	private String groupLeader;		// 班组长
	private String groupWorker;		// 记录人
	private String checkCycle;		// 点检周期
	private String checkDate;		// 点检时间
	private String configId;		// 配置表id
	private String isFinished;		//是否点检完成
	private Date startTime;			//起始时间
	private Date endTime;			//结束时间
	private String isAborted;		//是否取消
	private String isExpired;		//是否过期
    private String searchFlag;
	private List<DeviceCheckRitem> deviceCheckRitemList;

	public List<DeviceCheckRitem> getDeviceCheckRitemList() {
		return deviceCheckRitemList;
	}

	public void setDeviceCheckRitemList(List<DeviceCheckRitem> deviceCheckRitemList) {
		this.deviceCheckRitemList = deviceCheckRitemList;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public void setGroupWorker(String groupWorker) {
        this.groupWorker = groupWorker;
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

	public DeviceCheckRecord() {
		super();
	}

	public DeviceCheckRecord(String id){
		super(id);
	}

	@ExcelField(title="班组", align=2, sort=7)
	public String getWorkingGroup() {
		return this.workingGroup;
	}
	public void setWorkingGroup(String workingGroup) {
		this.workingGroup = workingGroup;
	}

	@ExcelField(title="作业区域", align=2, sort=8)
	public String getWorkingArea() {
		return this.workingArea;
	}
	public void setWorkingArea(String workingArea) {
		this.workingArea = workingArea;
	}

	@ExcelField(title="班组长", align=2, sort=9)
	public String getGroupLeader() {
		return this.groupLeader;
	}
	public void setGroupLeader(String groupLeader) {
		this.groupLeader = groupLeader;
	}

	@ExcelField(title="记录人", align=2, sort=10)
	public String getGroupWorker() {
		return this.groupWorker;
	}
	public void setGoupWorker(String groupWorker) {
		this.groupWorker = groupWorker;
	}

	@ExcelField(title="点检周期", align=2, sort=11)
	public String getCheckCycle() {
		return this.checkCycle;
	}
	public void setCheckCycle(String checkCycle) {
		this.checkCycle = checkCycle;
	}

	@ExcelField(title="点检时间", align=2, sort=12)
	public String getCheckDate() {
		return this.checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}


	public String getConfigId() {
		return this.configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}

	public String getIsFinished() {
		return isFinished;
	}

	public void setIsFinished(String isFinished) {
		this.isFinished = isFinished;
	}

	public String getIsAborted() {
		return isAborted;
	}

	public void setIsAborted(String isAborted) {
		this.isAborted = isAborted;
	}

	public String getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(String isExpired) {
		this.isExpired = isExpired;
	}
}