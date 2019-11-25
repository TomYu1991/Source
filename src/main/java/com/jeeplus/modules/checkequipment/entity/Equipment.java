/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.checkequipment.entity;

import com.jeeplus.modules.station.entity.WorkStation;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 点检设备表Entity
 * @author 张鲁蒙
 * @version 2019-01-04
 */
public class Equipment extends DataEntity<Equipment> {
	
	private static final long serialVersionUID = 1L;
	private WorkStation station;		// 工作站
	private String equipment;		// 设备名称
	private String checkMethod;		// 设备检查方法
	private String maintenExplanation;		// 点检设备描述
	private String equipmentType;		// 设备类型
	private String searchFlag;

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public Equipment() {
		super();
	}

	public Equipment(String id){
		super(id);
	}

	@ExcelField(title="工作站", fieldType=WorkStation.class, value="station.workStation", align=2, sort=6)
	public WorkStation getStation() {
		return station;
	}

	public void setStation(WorkStation station) {
		this.station = station;
	}
	
	@ExcelField(title="设备名称", align=2, sort=7)
	public String getEquipment() {
		return equipment;
	}

	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	
	@ExcelField(title="设备检查方法", align=2, sort=8)
	public String getCheckMethod() {
		return checkMethod;
	}

	public void setCheckMethod(String checkMethod) {
		this.checkMethod = checkMethod;
	}
	
	@ExcelField(title="点检设备描述", align=2, sort=9)
	public String getMaintenExplanation() {
		return maintenExplanation;
	}

	public void setMaintenExplanation(String maintenExplanation) {
		this.maintenExplanation = maintenExplanation;
	}
	
	@ExcelField(title="设备类型", dictType="dic_equipment_type", align=2, sort=10)
	public String getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(String equipmentType) {
		this.equipmentType = equipmentType;
	}
	
}