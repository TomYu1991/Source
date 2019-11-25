/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.print.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * 打印Entity
 * @author 打印
 * @version 2019-03-20
 */
public class Print extends DataEntity<Print> {
	
	private static final long serialVersionUID = 1L;
	private String vehicleNo;		// 车牌号
	private String pronName;		// 货物名称
	private String supplierName;		// 供货方
	private String consigneUser;		// 收货方
	private String matGrossWt;		// 毛重
	private String matWt;		// 净重
	private String impWt;		// 皮重
	private String UseIP;		// 客户端IP
	private String weighNo;		// 磅单号
	private String gateNum;		// 工作站
	private String printNum;		// 打印次数
	private String printer;		// 打印人
	private String printTime;		// 打印时间
	private String weightType;//称重类型
	private Date grosstime;//称重类型
	private Date taretime;//称重类型
	private String operation; //打印类型
	private String defaultFlag; //是否锁皮

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public Date getGrosstime() {
		return grosstime;
	}

	public void setGrosstime(Date grosstime) {
		this.grosstime = grosstime;
	}

	public Date getTaretime() {
		return taretime;
	}

	public void setTaretime(Date taretime) {
		this.taretime = taretime;
	}

	public String getWeightType() {
		return weightType;
	}

	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}

	public Print() {
		super();
	}

	public Print(String id){
		super(id);
	}

	@ExcelField(title="车牌号", align=2, sort=1)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="货物名称", align=2, sort=2)
	public String getPronName() {
		return pronName;
	}

	public void setPronName(String pronName) {
		this.pronName = pronName;
	}
	
	@ExcelField(title="供货方", align=2, sort=3)
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	@ExcelField(title="收货方", align=2, sort=4)
	public String getConsigneUser() {
		return consigneUser;
	}

	public void setConsigneUser(String consigneUser) {
		this.consigneUser = consigneUser;
	}
	
	@ExcelField(title="毛重", align=2, sort=5)
	public String getMatGrossWt() {
		return matGrossWt;
	}

	public void setMatGrossWt(String matGrossWt) {
		this.matGrossWt = matGrossWt;
	}
	
	@ExcelField(title="净重", align=2, sort=6)
	public String getMatWt() {
		return matWt;
	}

	public void setMatWt(String matWt) {
		this.matWt = matWt;
	}
	
	@ExcelField(title="皮重", align=2, sort=7)
	public String getImpWt() {
		return impWt;
	}

	public void setImpWt(String impWt) {
		this.impWt = impWt;
	}
	
	@ExcelField(title="客户端IP", align=2, sort=8)
	public String getUseIP() {
		return UseIP;
	}

	public void setUseIP(String UseIP) {
		this.UseIP = UseIP;
	}
	
	@ExcelField(title="磅单号", align=2, sort=9)
	public String getWeighNo() {
		return weighNo;
	}

	public void setWeighNo(String weighNo) {
		this.weighNo = weighNo;
	}
	
	@ExcelField(title="工作站", align=2, sort=10)
	public String getGateNum() {
		return gateNum;
	}

	public void setGateNum(String gateNum) {
		this.gateNum = gateNum;
	}
	
	@ExcelField(title="打印次数", align=2, sort=11)
	public String getPrintNum() {
		return printNum;
	}

	public void setPrintNum(String printNum) {
		this.printNum = printNum;
	}
	
	@ExcelField(title="打印人", align=2, sort=12)
	public String getPrinter() {
		return printer;
	}

	public void setPrinter(String printer) {
		this.printer = printer;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="打印时间", align=2, sort=13)
	public String getPrintTime() {
		return printTime;
	}

	public void setPrintTime(String printTime) {
		this.printTime = printTime;
	}
	
}