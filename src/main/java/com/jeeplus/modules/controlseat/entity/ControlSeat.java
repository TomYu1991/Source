/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.controlseat.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 集控室坐席表Entity
 * @author 汤进国
 * @version 2019-01-14
 */
public class ControlSeat extends DataEntity<ControlSeat> {
	
	private static final long serialVersionUID = 1L;
	private String seatName;		// 坐席名称
	private String seatNum;		// 坐席编号
	private String seatState;		// 坐席状态
	private String seatIp;
	@ExcelField(title="坐席IP", align=2, sort=9)
	public String getSeatIp() {
		return seatIp;
	}

	public void setSeatIp(String seatIp) {
		this.seatIp = seatIp;
	}

	public ControlSeat() {
		super();
	}

	public ControlSeat(String id){
		super(id);
	}

	@ExcelField(title="坐席名称", align=2, sort=7)
	public String getSeatName() {
		return seatName;
	}

	public void setSeatName(String seatName) {
		this.seatName = seatName;
	}
	
	@ExcelField(title="坐席编号", align=2, sort=8)
	public String getSeatNum() {
		return seatNum;
	}

	public void setSeatNum(String seatNum) {
		this.seatNum = seatNum;
	}
	
	@ExcelField(title="坐席状态",dictType = "seat_status",align=2, sort=10)
	public String getSeatState() {
		return seatState;
	}

	public void setSeatState(String seatState) {
		this.seatState = seatState;
	}
	
}