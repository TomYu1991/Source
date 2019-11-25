/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.consign.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 委托单/预约单管理Entity
 * @author 汤进国
 * @version 2019-01-17
 */
public class Consign extends DataEntity<Consign> {

	private static final long serialVersionUID = 1L;
	private String type;		// 类别代码
	private String consignNo;     //总委托单号
	private String consignId;		// 委托/预约单号
	private String consignUser;		// 委托/预约人
	private String consignDept;		// 委托/预约部门
	private String weightType;		// 称重类型
	private String equipNum;		// 设备编码
	private String prodCode;		// 品名代码
	private String prodCname;		// 品名中文
	private String sgCode;		// 牌号代码
	private String sgSign;		// 牌号（钢级）
	private String matSpecDesc;		// 材料规格描述
	private String billNo;		// 单据号
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间
	private String totalWt;		// 总重量
	private String surplusWt;     //剩余重量
	private String supplierName;		// 供货方
	private String consigneUser;		// 收货方
	private String content;		// 来访事由
	private String dealPersonNo;		// 被访者
	private String dealDept;		// 被访部门
	private String telNum;		// 被访者联系号码
	private String vehicleNo;		// 车牌号
	private String userName;		// 来访人
	private String carryCompanyName;		// 来访公司
	private String tel;		// 来访人联系号码
	private String passCode;		// 放行码
	private String blastFurnaceNo;		// 高炉号
	private String ladleNo;		// 铁水灌号
	private String consignState;		// 委托单状态
	private String companyCname;		// 公司中文名称
	private String transContactPerson;		// 运输联系人
	private String IDCard;		// 客户供应商身份证号
	private String transContactPersonTel;		// 运输联系人电话
	private String rfidNo;		// RFID卡号
	private String queueNum;    //排队车辆数量
    private String dataType;   //0正常，1未同步，2已同步
    private String weightState;		// 称重状态   0未过磅  1一次过磅  2二次过磅
	private String status;			//逻辑删除状态
	private String dataSources;			//数据来源
	private String defaultFlag;			//锁车标记
	private String matGrossWt;
	private String impWt;
	private String matWt;
	private Date createDate;
	private String searchFlag;
	private String ponderFlag;  //单次过磅标记
	private String moreRate;   //溢装量
	private String codeFlag;  //代码标记
	private String field1;  //确认收货
	private String field2;  //备用字段
	private String field3;  //备用字段
	private String field4;  //备用字段
	private String field5;  //备用字段
	private String fieldN1;  //备用字段
	private String fieldN2;  //备用字段
	private String fieldN3;  //备用字段
	private String remarks;  //备注
	public void Consign(){

	}

	@Override
	public String getRemarks() {
		return remarks;
	}

	@Override
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getFieldN3() {
		return fieldN3;
	}

	public void setFieldN3(String fieldN3) {
		this.fieldN3 = fieldN3;
	}

	public String getCodeFlag() {
		return codeFlag;
	}

	public void setCodeFlag(String codeFlag) {
		this.codeFlag = codeFlag;
	}

	public String getField2() {
		return field2;
	}

	public void setField2(String field2) {
		this.field2 = field2;
	}

	public String getField3() {
		return field3;
	}

	public void setField3(String field3) {
		this.field3 = field3;
	}

	public String getField4() {
		return field4;
	}

	public void setField4(String field4) {
		this.field4 = field4;
	}

	public String getField5() {
		return field5;
	}

	public void setField5(String field5) {
		this.field5 = field5;
	}

	public String getFieldN1() {
		return fieldN1;
	}

	public void setFieldN1(String fieldN1) {
		this.fieldN1 = fieldN1;
	}

	public String getFieldN2() {
		return fieldN2;
	}

	public void setFieldN2(String fieldN2) {
		this.fieldN2 = fieldN2;
	}

	public String getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(String searchFlag) {
		this.searchFlag = searchFlag;
	}

	public String getMatWt() {
		return matWt;
	}

	public void setMatWt(String matWt) {
		this.matWt = matWt;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	@Override
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getMatGrossWt() {
		return matGrossWt;
	}

	public void setMatGrossWt(String matGrossWt) {
		this.matGrossWt = matGrossWt;
	}

	public String getImpWt() {
		return impWt;
	}

	public void setImpWt(String impWt) {
		this.impWt = impWt;
	}

	public String getDataSources() {
		return dataSources;
	}

	public void setDataSources(String dataSources) {
		this.dataSources = dataSources;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getQueueNum() {
		return queueNum;
	}

	public void setQueueNum(String queueNum) {
		this.queueNum = queueNum;
	}

	public Consign() {
		super();
	}

	public Consign(String id){
		super(id);
	}

	@ExcelField(title="类别代码", dictType="consign_type", align=2, sort=1)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@ExcelField(title="委托/预约单号", align=2, sort=2)
	public String getConsignId() {
		return consignId;
	}

	public void setConsignId(String consignId) {
		this.consignId = consignId;
	}

	@ExcelField(title="委托/预约人", align=2, sort=3)
	public String getConsignUser() {
		return consignUser;
	}

	public void setConsignUser(String consignUser) {
		this.consignUser = consignUser;
	}

	@ExcelField(title="委托/预约部门", align=2, sort=4)
	public String getConsignDept() {
		return consignDept;
	}

	public void setConsignDept(String consignDept) {
		this.consignDept = consignDept;
	}

	@ExcelField(title="称重类型", dictType="weight_type", align=2, sort=5)
	public String getWeightType() {
		return weightType;
	}

	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}

	@ExcelField(title="磅秤", dictType="equip_num",align=2, sort=6)
	public String getEquipNum() {
		return equipNum;
	}

	public void setEquipNum(String equipNum) {
		this.equipNum = equipNum;
	}

	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}

	@ExcelField(title="品名中文", align=2, sort=8)
	public String getProdCname() {
		return prodCname;
	}

	public void setProdCname(String prodCname) {
		this.prodCname = prodCname;
	}

	public String getSgCode() {
		return sgCode;
	}

	public void setSgCode(String sgCode) {
		this.sgCode = sgCode;
	}

	public String getSgSign() {
		return sgSign;
	}

	public void setSgSign(String sgSign) {
		this.sgSign = sgSign;
	}

	public String getMatSpecDesc() {
		return matSpecDesc;
	}

	public void setMatSpecDesc(String matSpecDesc) {
		this.matSpecDesc = matSpecDesc;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@ExcelField(title="总重量", align=2, sort=15)
	public String getTotalWt() {
		return totalWt;
	}

	public void setTotalWt(String totalWt) {
		this.totalWt = totalWt;
	}
	
	@ExcelField(title="供货方", align=2, sort=16)
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	
	@ExcelField(title="收货方", align=2, sort=17)
	public String getConsigneUser() {
		return consigneUser;
	}

	public void setConsigneUser(String consigneUser) {
		this.consigneUser = consigneUser;
	}
	
	@ExcelField(title="来访事由", align=2, sort=18)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	@ExcelField(title="被访者", align=2, sort=19)
	public String getDealPersonNo() {
		return dealPersonNo;
	}

	public void setDealPersonNo(String dealPersonNo) {
		this.dealPersonNo = dealPersonNo;
	}
	
	@ExcelField(title="被访部门", align=2, sort=20)
	public String getDealDept() {
		return dealDept;
	}

	public void setDealDept(String dealDept) {
		this.dealDept = dealDept;
	}
	
	@ExcelField(title="被访者联系号码", align=2, sort=21)
	public String getTelNum() {
		return telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}
	
	@ExcelField(title="车牌号", align=2, sort=22)
	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}
	
	@ExcelField(title="来访人", align=2, sort=23)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@ExcelField(title="来访公司", align=2, sort=24)
	public String getCarryCompanyName() {
		return carryCompanyName;
	}

	public void setCarryCompanyName(String carryCompanyName) {
		this.carryCompanyName = carryCompanyName;
	}
	
	@ExcelField(title="来访人联系号码", align=2, sort=25)
	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	
	@ExcelField(title="放行码", align=2, sort=26)
	public String getPassCode() {
		return passCode;
	}

	public void setPassCode(String passCode) {
		this.passCode = passCode;
	}
	
	@ExcelField(title="高炉号", align=2, sort=27)
	public String getBlastFurnaceNo() {
		return blastFurnaceNo;
	}

	public void setBlastFurnaceNo(String blastFurnaceNo) {
		this.blastFurnaceNo = blastFurnaceNo;
	}
	
	@ExcelField(title="铁水灌号", align=2, sort=28)
	public String getLadleNo() {
		return ladleNo;
	}

	public void setLadleNo(String ladleNo) {
		this.ladleNo = ladleNo;
	}
	
	@ExcelField(title="委托单状态", align=2, sort=29)
	public String getConsignState() {
		return consignState;
	}

	public void setConsignState(String consignState) {
		this.consignState = consignState;
	}
	
	@ExcelField(title="公司中文名称", align=2, sort=30)
	public String getCompanyCname() {
		return companyCname;
	}

	public void setCompanyCname(String companyCname) {
		this.companyCname = companyCname;
	}
	
	@ExcelField(title="运输联系人", align=2, sort=31)
	public String getTransContactPerson() {
		return transContactPerson;
	}

	public void setTransContactPerson(String transContactPerson) {
		this.transContactPerson = transContactPerson;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String IDCard) {
		this.IDCard = IDCard;
	}

	@ExcelField(title="运输联系人电话", align=2, sort=33)
	public String getTransContactPersonTel() {
		return transContactPersonTel;
	}

	public void setTransContactPersonTel(String transContactPersonTel) {
		this.transContactPersonTel = transContactPersonTel;
	}
	
	@ExcelField(title="RFID卡号", align=2, sort=34)
	public String getRfidNo() {
		return rfidNo;
	}

	public void setRfidNo(String rfidNo) {
		this.rfidNo = rfidNo;
	}

	public String getWeightState() {
		return weightState;
	}

	public void setWeightState(String weightState) {
		this.weightState = weightState;
	}

	@ExcelField(title="锁车标记", dictType = "default_flag",align=2, sort=35)
	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

	@ExcelField(title="总委托单号", align=2, sort=36)
	public String getConsignNo() {
		return consignNo;
	}

	public void setConsignNo(String consignNo) {
		this.consignNo = consignNo;
	}

	@ExcelField(title="剩余总量", align=2, sort=37)
	public String getSurplusWt() {
		return surplusWt;
	}

	public void setSurplusWt(String surplusWt) {
		this.surplusWt = surplusWt;
	}

	public String getPonderFlag() {
		return ponderFlag;
	}

	public void setPonderFlag(String ponderFlag) {
		this.ponderFlag = ponderFlag;
	}

	@ExcelField(title="溢装量", align=2, sort=38)
	public String getMoreRate() {
		return moreRate;
	}

	public void setMoreRate(String moreRate) {
		this.moreRate = moreRate;
	}

	@ExcelField(title="确认收货",dictType = "sure_flag", align=2, sort=39)
	public String getField1() {
		return field1;
	}

	public void setField1(String field1) {
		this.field1 = field1;
	}
}