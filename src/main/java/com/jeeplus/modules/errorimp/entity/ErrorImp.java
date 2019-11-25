/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.errorimp.entity;

import java.util.Date;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 异常皮重信息Entity
 * @author 汤进国
 * @version 2019-05-05
 */
public class ErrorImp extends DataEntity<ErrorImp> {
	
	private static final long serialVersionUID = 1L;
	private String impWt;		// 异常皮重
	private String oldImp;		// 历史皮重
	private String vehicleNo;		// 车牌号
	private String stationIp;		// 工作站
	private String consignId;		// 委托单号
	private String prodCname;		// 品名
	private String beiyong1;		// 备用1
	private String beiyong2;		// 备用2
	private Date beginCreateDate;		// 开始 创建时间
	private Date endCreateDate;		// 结束 创建时间
	
	public ErrorImp() {
		super();
	}

	public ErrorImp(String id){
		super(id);
	}

	@ExcelField(title="异常皮重", align=2, sort=7)
	public String getImpWt() {
		return impWt;
	}

	public void setImpWt(String impWt) {
		this.impWt = impWt;
	}
	
	@ExcelField(title="历史皮重", align=2, sort=8)
	public String getOldImp() {
		return oldImp;
	}

	public void setOldImp(String oldImp) {
		this.oldImp = oldImp;
	}
	
	@ExcelField(title="车牌号", align=2, sort=9)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="工作站", dictType="station_ip", align=2, sort=10)
	public String getStationIp() {
		return stationIp;
	}

	public void setStationIp(String stationIp) {
		this.stationIp = stationIp;
	}
	
	@ExcelField(title="委托单号", align=2, sort=11)
	public String getConsignId() {
		return consignId;
	}

	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}
	
	@ExcelField(title="品名", align=2, sort=12)
	public String getProdCname() {
		return prodCname;
	}

	public void setProdCname(String prodCname) {
		this.prodCname = prodCname;
	}
	
	@ExcelField(title="备用1", align=2, sort=13)
	public String getBeiyong1() {
		return beiyong1;
	}

	public void setBeiyong1(String beiyong1) {
		this.beiyong1 = beiyong1;
	}
	
	@ExcelField(title="备用2", align=2, sort=14)
	public String getBeiyong2() {
		return beiyong2;
	}

	public void setBeiyong2(String beiyong2) {
		this.beiyong2 = beiyong2;
	}
	
	public Date getBeginCreateDate() {
		return beginCreateDate;
	}

	public void setBeginCreateDate(Date beginCreateDate) {
		this.beginCreateDate = beginCreateDate;
	}
	
	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}
		
}