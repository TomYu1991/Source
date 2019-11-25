/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightrecord.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.weightrecord.entity.WeightRecord;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 初始磅单数据表Entity
 * @author 汤进国
 * @version 2019-04-18
 */
public class InitWeight extends DataEntity<InitWeight> {
	
	private static final long serialVersionUID = 1L;
	private String vehicleNo;		// 车牌号
	private String ladleNo;		// 铁水罐号
	private String prodCname;		// 品名
	private Date weightTime;		// 称重时间
	private String weightWt;		// 重量
	private String defaultFlag;		// 锁皮标记
	private String stationIp;		// 过磅工作站
	private WeightRecord weight;		// 磅单号 父类
	private Date beginWeightTime;		// 开始 称重时间
	private Date endWeightTime;		// 结束 称重时间
	
	public InitWeight() {
		super();
	}

	public InitWeight(String id){
		super(id);
	}

	public InitWeight(WeightRecord weight){
		this.weight = weight;
	}

	@ExcelField(title="车牌号", align=2, sort=6)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="铁水罐号", align=2, sort=7)
	public String getLadleNo() {
		return ladleNo;
	}

	public void setLadleNo(String ladleNo) {
		this.ladleNo = ladleNo;
	}
	
	@ExcelField(title="品名", align=2, sort=8)
	public String getProdCname() {
		return prodCname;
	}

	public void setProdCname(String prodCname) {
		this.prodCname = prodCname;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="称重时间", align=2, sort=9)
	public Date getWeightTime() {
		return weightTime;
	}

	public void setWeightTime(Date weightTime) {
		this.weightTime = weightTime;
	}
	
	@ExcelField(title="重量", align=2, sort=10)
	public String getWeightWt() {
		return weightWt;
	}

	public void setWeightWt(String weightWt) {
		this.weightWt = weightWt;
	}
	
	@ExcelField(title="锁皮标记", dictType="default_flag", align=2, sort=11)
	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	
	@ExcelField(title="过磅工作站", dictType="workststion_ip", align=2, sort=12)
	public String getStationIp() {
		return stationIp;
	}

	public void setStationIp(String stationIp) {
		this.stationIp = stationIp;
	}
	
	public WeightRecord getWeight() {
		return weight;
	}

	public void setWeight(WeightRecord weight) {
		this.weight = weight;
	}
	
	public Date getBeginWeightTime() {
		return beginWeightTime;
	}

	public void setBeginWeightTime(Date beginWeightTime) {
		this.beginWeightTime = beginWeightTime;
	}
	
	public Date getEndWeightTime() {
		return endWeightTime;
	}

	public void setEndWeightTime(Date endWeightTime) {
		this.endWeightTime = endWeightTime;
	}
		
}