/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.checkall.entity;

import com.jeeplus.modules.checkequipment.entity.Equipment;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 点检表Entity
 * @author 张鲁蒙
 * @version 2019-01-07
 */
public class Checkes extends DataEntity<Checkes> {
	
	private static final long serialVersionUID = 1L;
	private String checkNumber;		// 点检标准号
	private Equipment equipment;		// 设备名称
	private String checkMethod;		// 设备检查方法
	private String maintenExplanation;		// 点检设备描述
	private User checkInspector;		// 点检人
	private Date checkDate;		// 点检日期
	private String checkContect;		// 点检内容
	private String checkResult;		// 点检结果
	private String checkState;		// 点检状态
	private User checkUsage;		// 审核人
	private String checkSuggestion;		// 审核意见
	private String searchFlag;

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public Checkes() {
		super();
	}

	public Checkes(String id){
		super(id);
	}

	@ExcelField(title="点检标准号", align=2, sort=6)
	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}
	
	@NotNull(message="设备名称不能为空")
	@ExcelField(title="设备名称", fieldType=Equipment.class, value="equipment.equipment", align=2, sort=7)
	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
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
	
	@ExcelField(title="点检人", fieldType=User.class, value="checkInspector.name", align=2, sort=10)
	public User getCheckInspector() {
		return checkInspector;
	}

	public void setCheckInspector(User checkInspector) {
		this.checkInspector = checkInspector;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message="点检日期不能为空")
	@ExcelField(title="点检日期", align=2, sort=11)
	public Date getCheckDate() {
		return checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@ExcelField(title="点检内容", align=2, sort=12)
	public String getCheckContect() {
		return checkContect;
	}

	public void setCheckContect(String checkContect) {
		this.checkContect = checkContect;
	}
	
	@ExcelField(title="点检结果", dictType="dic_check_result", align=2, sort=13)
	public String getCheckResult() {
		return checkResult;
	}

	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	
	@ExcelField(title="点检状态", align=2, sort=14)
	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	@ExcelField(title="审核人", fieldType=User.class, value="checkUsage.name", align=2, sort=16)
	public User getCheckUsage() {
		return checkUsage;
	}

	public void setCheckUsage(User checkUsage) {
		this.checkUsage = checkUsage;
	}
	
	@ExcelField(title="审核意见", align=2, sort=17)
	public String getCheckSuggestion() {
		return checkSuggestion;
	}

	public void setCheckSuggestion(String checkSuggestion) {
		this.checkSuggestion = checkSuggestion;
	}
	
}