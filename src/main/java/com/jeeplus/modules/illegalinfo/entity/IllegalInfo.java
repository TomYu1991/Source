/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.illegalinfo.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 非法闯入信息记录Entity
 * @author illegalinfo
 * @version 2019-04-06
 */
public class IllegalInfo extends DataEntity<IllegalInfo> {
	
	private static final long serialVersionUID = 1L;
	private String monitorPosition;		// 监控点位置
	private String vehicleNo;		// 车牌号
	private String supplierName;		// 发货方
	private String consigneUser;		// 收货方
	private Date passTime;		// 通行时间
	private String takePhoto;		// 抓拍图片
	private String warningInfo;		// 违法信息
	private Date begiPassTime;//开始通行时间
	private Date endPassTime;//结束通行时间
	private String searchFlag;

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public Date getBegiPassTime() {
		return begiPassTime;
	}

	public void setBegiPassTime(Date begiPassTime) {
		this.begiPassTime = begiPassTime;
	}

	public Date getEndPassTime() {
		return endPassTime;
	}

	public void setEndPassTime(Date endPassTime) {
		this.endPassTime = endPassTime;
	}

	public IllegalInfo() {
		super();
	}

	public IllegalInfo(String id){
		super(id);
	}

	@ExcelField(title="监控点位置", align=2, sort=6)
	public String getMonitorPosition() {
		return monitorPosition;
	}

	public void setMonitorPosition(String monitorPosition) {
		this.monitorPosition = monitorPosition;
	}
	
	@ExcelField(title="车牌号", align=2, sort=7)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="发货方", align=2, sort=8)
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	@ExcelField(title="收货方", align=2, sort=9)
	public String getConsigneUser() {
		return consigneUser;
	}

	public void setConsigneUser(String consigneUser) {
		this.consigneUser = consigneUser;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="通行时间", align=2, sort=10)
	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}
	
	public String getTakePhoto() {
		return takePhoto;
	}

	public void setTakePhoto(String takePhoto) {
		this.takePhoto = takePhoto;
	}
	
	@ExcelField(title="违法信息", align=2, sort=12)
	public String getWarningInfo() {
		return warningInfo;
	}

	public void setWarningInfo(String warningInfo) {
		this.warningInfo = warningInfo;
	}
	
}