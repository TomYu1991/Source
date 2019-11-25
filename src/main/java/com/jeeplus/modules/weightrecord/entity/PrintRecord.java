/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightrecord.entity;

import com.jeeplus.modules.weightrecord.entity.WeightRecord;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 打印记录Entity
 * @author 汤进国
 * @version 2019-04-18
 */
public class PrintRecord extends DataEntity<PrintRecord> {
	
	private static final long serialVersionUID = 1L;
	private String operation;		// 操作
	private String stationIp;		// 打印位置
	private String vehicleNo;		// 车牌号
	private WeightRecord weight;		// 磅单号 父类
	private String ladelNo;		// 铁水罐号

	public PrintRecord() {
		super();
	}

	public PrintRecord(String id){
		super(id);
	}

	public PrintRecord(WeightRecord weight){
		this.weight = weight;
	}

	@ExcelField(title="操作", align=2, sort=7)
	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	@ExcelField(title="打印位置", align=2, sort=8)
	public String getStationIp() {
		return stationIp;
	}

	public void setStationIp(String stationIp) {
		this.stationIp = stationIp;
	}
	
	@ExcelField(title="车牌号", align=2, sort=9)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	public WeightRecord getWeight() {
		return weight;
	}

	public void setWeight(WeightRecord weight) {
		this.weight = weight;
	}
	
	@ExcelField(title="铁水罐号", align=2, sort=11)
	public String getLadelNo() {
		return ladelNo;
	}

	public void setLadelNo(String ladelNo) {
		this.ladelNo = ladelNo;
	}
	
}