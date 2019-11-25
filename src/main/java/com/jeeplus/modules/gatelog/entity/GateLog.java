/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.gatelog.entity;

import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 门岗日志Entity
 * @author jeeplus
 * @version 2018-12-22
 */
public class GateLog extends DataEntity<GateLog> {
	
	private static final long serialVersionUID = 1L;
	private String operation;		// 操作
	private User u;		// 操作人员
	private String officeId;		// 所属部门
	private String companyId;		// 所属公司
	private String gateNum;		// 门岗
	private String deviceNum;		// 设备号
	private String consignId;		// 委托/预约单号
	private String passCode;		// 放行码
	private String vehicleNo;		// 车牌号
	private Date beginDate;
	private Date endDate;
	private Date date;		// 操作日期
	private String userIP;		// 操作者ip
	private String exception;		// 异常信息
	private String dataType;   //0正常，1未同步，2已同步
	private String weighNo;		// 磅单号
	private String seatNum;		// 集控室坐席编号
    private String printNum;   //打印次数
	private String searchFlag;
    private String remarks;
    private Date ceateDate;
	private String workName;
	private String icNumber;

	public Date getCeateDate() {
		return ceateDate;
	}

	public void setCeateDate(Date ceateDate) {
		this.ceateDate = ceateDate;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public GateLog() {
		super();
	}

	public GateLog(String id){
		super(id);
	}

	@ExcelField(title="操作", align=2, sort=6)
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	@ExcelField(title="操作人员", fieldType=User.class, value="u.name", align=2, sort=7)
	public User getU() {
		return u;
	}

	public void setU(User u) {
		this.u = u;
	}

	@ExcelField(title="门岗",dictType = "vehicle_pass",align=2, sort=10)
	public String getGateNum() {
		return gateNum;
	}

	public void setGateNum(String gateNum) {
		this.gateNum = gateNum;
	}

	@ExcelField(title="姓名", align=2, sort=11)
	public String getWorkName() {
		return workName;
	}

	@ExcelField(title="工号", align=2, sort=12)
	@Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@ExcelField(title="卡号", align=2, sort=13)
	public String getIcNumber() {
		return icNumber;
	}

	public void setIcNumber(String icNumber) {
		this.icNumber = icNumber;
	}

	public void setWorkName(String workName) {
		this.workName = workName;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getDeviceNum() {
		return deviceNum;
	}

	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	@ExcelField(title="车牌号", align=2, sort=14)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	@ExcelField(title="委托/预约单号", align=2, sort=15)
	public String getConsignId() {
		return consignId;
	}

	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}
	
	@ExcelField(title="出门条号", align=2, sort=16)
	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}

	@ExcelField(title="磅单号", align=2, sort=17)
	public String getWeighNo() { return weighNo; }

	public void setWeighNo(String weighNo) { this.weighNo = weighNo; }

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="操作日期", align=2, sort=18)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	@ExcelField(title="备注", align=2, sort=19)
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}