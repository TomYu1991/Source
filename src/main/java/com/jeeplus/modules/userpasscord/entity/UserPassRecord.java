/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.userpasscord.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 员工通行记录Entity
 * @author 汤进国
 * @version 2019-03-16
 */
public class UserPassRecord extends DataEntity<UserPassRecord> {
	
	private static final long serialVersionUID = 1L;
	private String personnelId;		// 工号
	private String name;		// 姓名
	private String sex;		// 性别
	private String state;		// 状态
	private String cardSerial;		// 识别卡号
	private String deptName;		// 部门名称
	private Date passTime;		// 通行时间
	private String passStation;		// 通行门岗
	private String passType;		// 通行类型
	private Date beginpassTime;		// 开始 通行时间
	private Date endpassTime;		// 结束 通行时间
	private String searchFlag;


	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getBeginpassTime() {
		return beginpassTime;
	}

	public void setBeginpassTime(Date beginpassTime) {
		this.beginpassTime = beginpassTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndpassTime() {
		return endpassTime;
	}

	public void setEndpassTime(Date endpassTime) {
		this.endpassTime = endpassTime;
	}

	public UserPassRecord() {
		super();
	}

	public UserPassRecord(String id){
		super(id);
	}

	@ExcelField(title="工号", align=2, sort=7)
	public String getPersonnelId() {
		return personnelId;
	}

	public void setPersonnelId(String personnelId) {
		this.personnelId = personnelId;
	}
	
	@ExcelField(title="姓名", align=2, sort=8)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@ExcelField(title="性别", align=2, sort=9)
	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	@ExcelField(title="识别卡号", align=2, sort=10)
	public String getCardSerial() {
		return cardSerial;
	}

	public void setCardSerial(String cardSerial) {
		this.cardSerial = cardSerial;
	}
	
	@ExcelField(title="部门名称", align=2, sort=11)
	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="通行时间", align=2, sort=12)
	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}
	
	@ExcelField(title="通行门岗", align=2, sort=13)
	public String getPassStation() {
		return passStation;
	}

	public void setPassStation(String passStation) {
		this.passStation = passStation;
	}
	
	@ExcelField(title="通行类型", dictType="pass_type", align=2, sort=14)
	public String getPassType() {
		return passType;
	}

	public void setPassType(String passType) {
		this.passType = passType;
	}
	
}