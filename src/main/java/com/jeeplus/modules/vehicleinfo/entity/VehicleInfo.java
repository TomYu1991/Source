/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.vehicleinfo.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 车辆信息表Entity
 * @author 汤进国
 * @version 2019-01-17
 */
public class VehicleInfo extends DataEntity<VehicleInfo> {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String archiveFlag;		// 归档标记
	private String companyCode;		// 公司代码
	private String companyCname;		// 公司中文名称
	private String typeCode;		// 类别代码
	private String typeCodeName;  //类别名称
	private String vehicleNo;		// 车牌号
	private String groupCode;		// 组批代码
	private String passCode;		// 放行码
	private String rfidNo;		// RFID卡号
	private String srfidNo;		// RFID卡号
	private String groupCompanyName;		// 集团公司名称
	private String deptCode;		// 部门代码
	private String carryCompanyName;		// 承运公司名称
	private String opDeptCode;		// 业务部门代码
	private String useUserId;		// 使用人工号
	private String transContactPerson;		// 运输联系人
	private String transContactPersonTel;		// 运输联系人电话
	private String wagonType;		// 局车类型
	private String qty;		// 数量
	private Date dealTime;		// 处置时间
	private Date approveTime;		// 审批时间
	private String approvePersonNo;		// 审批人工号
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String searchFlag;
    private Date createDate;

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getTypeCodeName() {
		return typeCodeName;
	}

	public void setTypeCodeName(String typeCodeName) {
		this.typeCodeName = typeCodeName;
	}

	public VehicleInfo() {
		super();
	}

	public VehicleInfo(String id){
		super(id);
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	@ExcelField(title="公司中文名称", align=2, sort=8)
	public String getCompanyCname() {
		return companyCname;
	}

	public void setCompanyCname(String companyCname) {
		this.companyCname = companyCname;
	}

	@ExcelField(title="车辆类别",dictType = "vehicle_type",align=2, sort=9)
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	@ExcelField(title="车牌号", align=2, sort=10)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="组批代码", align=2, sort=11)
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	@ExcelField(title="放行码", align=2, sort=12)
	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
	
	@ExcelField(title="RFID卡号1", align=2, sort=13)
	public String getRfidNo() {
		return rfidNo;
	}

	public void setRfidNo(String rfidNo) {
		this.rfidNo = rfidNo;
	}

	@ExcelField(title="RFID卡号2", align=2, sort=14)
	public String getSrfidNo() {
		return srfidNo;
	}

	public void setSrfidNo(String srfidNo) {
		this.srfidNo = srfidNo;
	}
	
	@ExcelField(title="集团公司名称", align=2, sort=15)
	public String getGroupCompanyName() {
		return groupCompanyName;
	}

	public void setGroupCompanyName(String groupCompanyName) {
		this.groupCompanyName = groupCompanyName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	@ExcelField(title="承运公司名称", align=2, sort=17)
	public String getCarryCompanyName() {
		return carryCompanyName;
	}

	public void setCarryCompanyName(String carryCompanyName) {
		this.carryCompanyName = carryCompanyName;
	}

	public String getOpDeptCode() {
		return opDeptCode;
	}

	public void setOpDeptCode(String opDeptCode) {
		this.opDeptCode = opDeptCode;
	}
	
	@ExcelField(title="使用人工号", align=2, sort=19)
	public String getUseUserId() {
		return useUserId;
	}

	public void setUseUserId(String useUserId) {
		this.useUserId = useUserId;
	}
	
	@ExcelField(title="运输联系人", align=2, sort=20)
	public String getTransContactPerson() {
		return transContactPerson;
	}

	public void setTransContactPerson(String transContactPerson) {
		this.transContactPerson = transContactPerson;
	}
	
	@ExcelField(title="运输联系人电话", align=2, sort=21)
	public String getTransContactPersonTel() {
		return transContactPersonTel;
	}

	public void setTransContactPersonTel(String transContactPersonTel) {
		this.transContactPersonTel = transContactPersonTel;
	}
	
	@ExcelField(title="局车类型", align=2, sort=22)
	public String getWagonType() {
		return wagonType;
	}

	public void setWagonType(String wagonType) {
		this.wagonType = wagonType;
	}
	
	@ExcelField(title="数量", align=2, sort=23)
	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="处置时间", align=2, sort=24)
	public Date getDealTime() {
		return dealTime;
	}

	public void setDealTime(Date dealTime) {
		this.dealTime = dealTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="审批时间", align=2, sort=25)
	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}
	
	@ExcelField(title="审批人工号", align=2, sort=26)
	public String getApprovePersonNo() {
		return approvePersonNo;
	}

	public void setApprovePersonNo(String approvePersonNo) {
		this.approvePersonNo = approvePersonNo;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="开始时间", align=2, sort=27)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="结束时间", align=2, sort=28)
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getArchiveFlag() {
		return archiveFlag;
	}

	public void setArchiveFlag(String archiveFlag) {
		this.archiveFlag = archiveFlag;
	}
}