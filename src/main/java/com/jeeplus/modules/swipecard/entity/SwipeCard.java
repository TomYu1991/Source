/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.swipecard.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.sys.entity.User;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 刷卡人员权限Entity
 * @author zhanglumeng 
 * @version 2019-02-13
 */
public class SwipeCard extends DataEntity<SwipeCard> {
	
	private static final long serialVersionUID = 1L;
	private String id;  //ID
	private String icnumber;		// 进出门卡卡号
	private WorkStation station;		// 工作站id
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private User user;		// 人员id
	private String jobNumber;		// 工号
	private String officeId;		// 所属部门
	private String idcard;		// 身份证号
	private String workerId;		// 人员管理id
    private String remarks;
    private String workName;
    private String telephone;

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public SwipeCard() {
		super();
	}

	public SwipeCard(String id){
		super(id);
	}

	@ExcelField(title="进出门卡卡号", align=2, sort=7)
	public String getIcnumber() {
		return icnumber;
	}

	public void setIcnumber(String icnumber) {
		this.icnumber = icnumber;
	}
	
	@ExcelField(title="工作站id", align=2, sort=8)
	public WorkStation getStation() {
		return station;
	}

	public void setStation(WorkStation station) {
		this.station = station;
	}



	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始时间", align=2, sort=9)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=10)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@ExcelField(title="人员id", align=2, sort=11)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@ExcelField(title="工号", align=2, sort=12)
	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}
	
	@ExcelField(title="所属部门", align=2, sort=13)
	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}
	
	@ExcelField(title="身份证号", align=2, sort=14)
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	
	@ExcelField(title="人员管理id", align=2, sort=15)
	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}
	
}