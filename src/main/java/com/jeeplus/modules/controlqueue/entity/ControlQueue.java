/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.controlqueue.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 集控室排队Entity
 * @author 汤进国
 * @version 2019-01-10
 */
public class ControlQueue extends DataEntity<ControlQueue> {
	
	private static final long serialVersionUID = 1L;
	private String stationId;		// 工作站id
	private String seatNum;		// 坐席编号
	private String state;		// 排队状态
	private String content;		// 内容
	private String weightId;		// 磅单id
	private String queueNum;     //排队人数
	private String stationName;   // 工作站名称
	private String stateName;    // 排队状态
	private String consignId;    // 委托单id
	private String rfidNo;			//RFID卡号
	private String vehicleNo;		// 车牌号
	private String billPic;       //票据截图


	public String getBillPic() {
		return billPic;
	}

	public void setBillPic(String billPic) {
		this.billPic = billPic;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getConsignId() {
		return consignId;
	}

	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}

	public String getRfidNo() {
		return rfidNo;
	}

	public void setRfidNo(String rfidNo) {
		this.rfidNo = rfidNo;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getQueueNum() {
		return queueNum;
	}

	public void setQueueNum(String queueNum) {
		this.queueNum = queueNum;
	}

	public ControlQueue() {
		super();
	}

	public ControlQueue(String id){
		super(id);
	}

	@ExcelField(title="工作站id", align=2, sort=6)
	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	
	@ExcelField(title="坐席编号", align=2, sort=7)
	public String getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}
	
	@ExcelField(title="排队状态", align=2, sort=8)
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@ExcelField(title="内容", align=2, sort=9)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@ExcelField(title="磅单id", align=2, sort=10)
	public String getWeightId() {
		return weightId;
	}

	public void setWeightId(String weightId) {
		this.weightId = weightId;
	}
	
}