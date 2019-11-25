/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.synchroinfo.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import com.jeeplus.modules.sys.entity.User;

import java.util.Date;

/**
 * 数据异常信息Entity
 * @author 张鲁蒙
 * @version 2019-04-18
 */
public class DataSynchroInfo extends DataEntity<DataSynchroInfo> {
	
	private static final long serialVersionUID = 1L;
	private String id;
	private String consignId;		// 委托单号
	private String passCode;		// 出门条号
	private String vehicleNo;		// 车牌号
	private String remarks;  //备注
	private User createBy;
	private Date createDate;		// 创建时间
	private Date updateDate;		// 更新时间
	private Date biginCreateDate;		// 创建开始时间
	private Date endCreateDate;		// 创建结束时间
	private String code;    //委托单，出门条，车牌号
	private String type;		// 表类型
	private String operationType;  //操作类型
	private String status;   //状态
	private String searchFlag;//查询标记

		@Override
	public User getCreateBy() {
		return createBy;
	}

	@Override
	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

	@Override
	public Date getUpdateDate() {
		return updateDate;
	}

	@Override
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBiginCreateDate() {
		return biginCreateDate;
	}

	public void setBiginCreateDate(Date biginCreateDate) {
		this.biginCreateDate = biginCreateDate;
	}

	public Date getEndCreateDate() {
		return endCreateDate;
	}

	public void setEndCreateDate(Date endCreateDate) {
		this.endCreateDate = endCreateDate;
	}

	@Override
	public String getRemarks() {
		return remarks;
	}
	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public DataSynchroInfo() {
		super();
	}

	public DataSynchroInfo(String id){
		super(id);
	}

	@ExcelField(title="委托单号", align=2, sort=6)
	public String getConsignId() {
		return consignId;
	}

	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}
	
	@ExcelField(title="出门条号", align=2, sort=7)
	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
	
	@ExcelField(title="车牌号", align=2, sort=8)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="类型", dictType="synchro_type", align=2, sort=9)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}