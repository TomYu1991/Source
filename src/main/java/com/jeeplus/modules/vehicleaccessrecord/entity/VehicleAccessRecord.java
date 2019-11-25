/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.vehicleaccessrecord.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 车辆进出记录Entity
 * @author 汤进国
 * @version 2019-01-18
 */
public class VehicleAccessRecord extends DataEntity<VehicleAccessRecord> {

	private static final long serialVersionUID = 1L;
	private String id;
	private String vehicleNo;		// 车牌号
	private String transContactPerson;		// 司机名称
	private String idcard;		// 身份证号
	private String transContactPersonTel;		// 司机联系方式
	private String rfidNo;		// RFID卡号
	private Date intoTime;		// 进厂时间
	private Date outTime;		// 出厂时间
	private String consignType;		// 委托单类型
	private String state;		// 车辆状态
	private String queueNum;   	//	排队车辆数
	private String peccancy;		//违章标记
    private String takePhotos;    //出门视频抓拍
	private Date beginintoTime;		// 进厂开始时间
	private Date endintoTime;		// 进厂结束时间
	private Date beginoutTime;		// 出厂时间
	private Date endoutTime;		// 出厂时间
    private String remarks;     //通行岗亭
	private String reason;
	private String searchFlag;
	private String pic;

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getBeginintoTime() {
		return beginintoTime;
	}

	public void setBeginintoTime(Date beginintoTime) {
		this.beginintoTime = beginintoTime;
	}

	public Date getEndintoTime() {
		return endintoTime;
	}

	public void setEndintoTime(Date endintoTime) {
		this.endintoTime = endintoTime;
	}

	public Date getBeginoutTime() {
		return beginoutTime;
	}

	public void setBeginoutTime(Date beginoutTime) {
		this.beginoutTime = beginoutTime;
	}

	public Date getEndoutTime() {
		return endoutTime;
	}

	public void setEndoutTime(Date endoutTime) {
		this.endoutTime = endoutTime;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	public String getTakePhotos() {
		return takePhotos;
	}

	public void setTakePhotos(String takePhotos) {
		this.takePhotos = takePhotos;
	}

	public VehicleAccessRecord() {
		super();
	}

	public VehicleAccessRecord(String id){
		super(id);
	}

	@ExcelField(title="车牌号", align=2, sort=6)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="司机名称", align=2, sort=7)
	public String getTransContactPerson() {
		return transContactPerson;
	}

	public void setTransContactPerson(String transContactPerson) {
		this.transContactPerson = transContactPerson;
	}

	@ExcelField(title="身份证号", align=2, sort=9)
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@ExcelField(title="司机联系方式", align=2, sort=9)
	public String getTransContactPersonTel() {
		return transContactPersonTel;
	}

	public void setTransContactPersonTel(String transContactPersonTel) {
		this.transContactPersonTel = transContactPersonTel;
	}
	
	@ExcelField(title="放行类型",dictType="open_type", align=2, sort=10)
	public String getRfidNo() {
		return rfidNo;
	}

	public void setRfidNo(String rfidNo) {
		this.rfidNo = rfidNo;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="进厂时间", align=2, sort=11)
	public Date getIntoTime() {
		return intoTime;
	}

	public void setIntoTime(Date intoTime) {
		this.intoTime = intoTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="出厂时间", align=2, sort=12)
	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	@ExcelField(title="委托单类型",dictType="consign_type",align=2, sort=13)
	public String getConsignType() {
		return consignType;
	}

	public void setConsignType(String consignType) {
		this.consignType = consignType;
	}

	public String getQueueNum() {
		return queueNum;
	}

	public void setQueueNum(String queueNum) {
		this.queueNum = queueNum;
	}

	@ExcelField(title="车辆状态", dictType="vehicle_state", align=2, sort=14)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@ExcelField(title="有无违章",dictType="peccancy", align=2, sort=14)
	public String getPeccancy() {
		return peccancy;
	}

	public void setPeccancy(String peccancy) {
		this.peccancy = peccancy;
	}

}