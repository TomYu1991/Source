/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.impwthistory.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

import java.util.Date;

/**
 * 皮重历史Entity
 * @author 张鲁蒙
 * @version 2019-03-05
 */
public class ImpWtHistory extends DataEntity<ImpWtHistory> {
	
	private static final long serialVersionUID = 1L;
	private String consignId;
	private String vehicleNo;		// 车牌号
	private String impWt;		// 皮重
	private String tareHeadPic;		// 空磅车头抓拍
	private String tareTailPic;		// 空磅车尾抓拍
	private String tareTopPic;			// 空磅车顶抓拍
	private Date startTime;
	private Date endTime;
	private String impAvg;

	public String getImpAvg() {
		return impAvg;
	}

	public void setImpAvg(String impAvg) {
		this.impAvg = impAvg;
	}

	public String getConsignId() {
		return consignId;
	}

	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getTareHeadPic() {
		return tareHeadPic;
	}

	public void setTareHeadPic(String tareHeadPic) {
		this.tareHeadPic = tareHeadPic;
	}

	public String getTareTailPic() {
		return tareTailPic;
	}

	public void setTareTailPic(String tareTailPic) {
		this.tareTailPic = tareTailPic;
	}

	public String getTareTopPic() {
		return tareTopPic;
	}

	public void setTareTopPic(String tareTopPic) {
		this.tareTopPic = tareTopPic;
	}

	public ImpWtHistory() {
		super();
	}

	public ImpWtHistory(String id){
		super(id);
	}

	@ExcelField(title="车牌号", align=2, sort=7)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="皮重", align=2, sort=8)
	public String getImpWt() {
		return impWt;
	}

	public void setImpWt(String impWt) {
		this.impWt = impWt;
	}

	public void deleteByConsignId(ImpWtHistory i){

	}

}