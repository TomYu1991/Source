/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightrecord.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 磅单信息记录Entity
 * @author 汤进国
 * @version 2019-04-18
 */
public class WeightRecord extends DataEntity<WeightRecord> {
	
	private static final long serialVersionUID = 1L;
	private String weighNo;		// 磅单号
	private String consignId;		// 委托单号
	private String vehicleNo;		// 车牌号
	private String ladleNo;		// 铁水罐号
	private String prodCname;		// 品名
	private String sgSign;		// 钢级
	private String matWt;		// 净重
	private String matGrossWt;		// 毛重
	private String impWt;		// 皮重
	private String status;		// 状态
	private String consigneUser;		// 收货方
	private String supplierName;		// 发货方
	private String fistStation;		// 一次过磅工作站
	private String secondStation;		// 二次过磅工作站
	private Date grosstime;		// 一次过磅时间
	private Date taretime;		// 二次过磅时间
	private String defaultFlag;		// 锁皮标记
	private String readyOne;		// 预留字段
	private String readyTwo;		// 预留字段
	private String readyThree;		// 预留字段
	private String readyFour;		// 预留字段
	private List<InitWeight> initWeightList = Lists.newArrayList();		// 子表列表
	private List<PrintRecord> printRecordList = Lists.newArrayList();		// 子表列表
	private List<UpdateWeightRecord> updateWeightRecordList = Lists.newArrayList();		// 子表列表
	private Date begintaretime;		// 过皮开始时间
	private Date endtaretime;		// 过皮结束时间
	private String searchFlag;

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public Date getBegintaretime() {
		return begintaretime;
	}

	public void setBegintaretime(Date begintaretime) {
		this.begintaretime = begintaretime;
	}

	public Date getEndtaretime() {
		return endtaretime;
	}

	public void setEndtaretime(Date endtaretime) {
		this.endtaretime = endtaretime;
	}

	public WeightRecord() {
		super();
	}

	public WeightRecord(String id){
		super(id);
	}

	@ExcelField(title="磅单号", align=2, sort=6)
	public String getWeighNo() {
		return weighNo;
	}

	public void setWeighNo(String weighNo) {
		this.weighNo = weighNo;
	}
	
	@ExcelField(title="委托单号", align=2, sort=7)
	public String getConsignId() {
		return consignId;
	}

	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}
	
	@ExcelField(title="车牌号", align=2, sort=8)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	@ExcelField(title="铁水罐号", align=2, sort=9)
	public String getLadleNo() {
		return ladleNo;
	}

	public void setLadleNo(String ladleNo) {
		this.ladleNo = ladleNo;
	}



	
	@ExcelField(title="品名", align=2, sort=10)
	public String getProdCname() {
		return prodCname;
	}

	public void setProdCname(String prodCname) {
		this.prodCname = prodCname;
	}
	
	@ExcelField(title="钢级", align=2, sort=11)
	public String getSgSign() {
		return sgSign;
	}

	public void setSgSign(String sgSign) {
		this.sgSign = sgSign;
	}
	
	@ExcelField(title="净重", align=2, sort=12)
	public String getMatWt() {
		return matWt;
	}

	public void setMatWt(String matWt) {
		this.matWt = matWt;
	}
	
	@ExcelField(title="毛重", align=2, sort=13)
	public String getMatGrossWt() {
		return matGrossWt;
	}

	public void setMatGrossWt(String matGrossWt) {
		this.matGrossWt = matGrossWt;
	}
	
	@ExcelField(title="皮重", align=2, sort=14)
	public String getImpWt() {
		return impWt;
	}

	public void setImpWt(String impWt) {
		this.impWt = impWt;
	}
	
	@ExcelField(title="状态", dictType="weigh_status", align=2, sort=15)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	@ExcelField(title="收货方", align=2, sort=16)
	public String getConsigneUser() {
		return consigneUser;
	}

	public void setConsigneUser(String consigneUser) {
		this.consigneUser = consigneUser;
	}
	
	@ExcelField(title="发货方", align=2, sort=17)
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	@ExcelField(title="一次过磅工作站", dictType="workststion_ip", align=2, sort=18)
	public String getFistStation() {
		return fistStation;
	}

	public void setFistStation(String fistStation) {
		this.fistStation = fistStation;
	}
	
	@ExcelField(title="二次过磅工作站", dictType="workststion_ip", align=2, sort=19)
	public String getSecondStation() {
		return secondStation;
	}

	public void setSecondStation(String secondStation) {
		this.secondStation = secondStation;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="一次过磅时间", align=2, sort=20)
	public Date getGrosstime() {
		return grosstime;
	}

	public void setGrosstime(Date grosstime) {
		this.grosstime = grosstime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="二次过磅时间", align=2, sort=21)
	public Date getTaretime() {
		return taretime;
	}

	public void setTaretime(Date taretime) {
		this.taretime = taretime;
	}
	
	@ExcelField(title="锁皮标记", dictType="default_flag", align=2, sort=22)
	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
	@ExcelField(title="预留字段", align=2, sort=23)
	public String getReadyOne() {
		return readyOne;
	}

	public void setReadyOne(String readyOne) {
		this.readyOne = readyOne;
	}
	
	@ExcelField(title="预留字段", align=2, sort=24)
	public String getReadyTwo() {
		return readyTwo;
	}

	public void setReadyTwo(String readyTwo) {
		this.readyTwo = readyTwo;
	}
	
	@ExcelField(title="预留字段", align=2, sort=25)
	public String getReadyThree() {
		return readyThree;
	}

	public void setReadyThree(String readyThree) {
		this.readyThree = readyThree;
	}
	
	@ExcelField(title="预留字段", align=2, sort=26)
	public String getReadyFour() {
		return readyFour;
	}

	public void setReadyFour(String readyFour) {
		this.readyFour = readyFour;
	}
	
	public List<InitWeight> getInitWeightList() {
		return initWeightList;
	}

	public void setInitWeightList(List<InitWeight> initWeightList) {
		this.initWeightList = initWeightList;
	}
	public List<PrintRecord> getPrintRecordList() {
		return printRecordList;
	}

	public void setPrintRecordList(List<PrintRecord> printRecordList) {
		this.printRecordList = printRecordList;
	}
	public List<UpdateWeightRecord> getUpdateWeightRecordList() {
		return updateWeightRecordList;
	}

	public void setUpdateWeightRecordList(List<UpdateWeightRecord> updateWeightRecordList) {
		this.updateWeightRecordList = updateWeightRecordList;
	}
}