/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.passchecksub.entity;


import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 出门条明细Entity
 * @author 汤进国
 * @version 2019-01-17
 */
public class PassCheckSub extends DataEntity<PassCheckSub> {
	
	private static final long serialVersionUID = 1L;
	private String archiveFlag;		// 归档标记
	private String companyCode;		// 公司代码
	private String companyCname;		// 公司中文名称
	private String trnpAppNo;		// 运输申请单号
	private String prodCode;		// 品名代码
	private String prodCname;		// 品名中文
	private String matSpecDesc;		// 材料规格描述
	private String outStockQty;		// 出库数量
	private String measureUnit;		// 计量单位
	private String trnpApp;		// 出门条号
	private String vehicleNo;		// 车牌号
    private String passcode;
	private String subNum;
	private String prodNum;

	public String getProdNum() {
		return prodNum;
	}

	public void setProdNum(String prodNum) {
		this.prodNum = prodNum;
	}

	public String getSubNum() {
		return subNum;
	}

	public void setSubNum(String subNum) {
		this.subNum = subNum;
	}

	public String getPasscode() {
		return passcode;
	}

	public void setPasscode(String passcode) {
		this.passcode = passcode;
	}

	public String getTrnpApp() {
		return trnpApp;
	}

	public void setTrnpApp(String trnpApp) {
		this.trnpApp = trnpApp;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public PassCheckSub() {
		super();
	}

	public PassCheckSub(String id){
		super(id);
	}

	@ExcelField(title="归档标记", align=2, sort=6)
	public String getArchiveFlag() {
		return archiveFlag;
	}

	public void setArchiveFlag(String archiveFlag) {
		this.archiveFlag = archiveFlag;
	}
	
	@ExcelField(title="公司代码", align=2, sort=7)
	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
	@ExcelField(title="公司中文名称", align=2, sort=8)
	public String getCompanyCname() {
		return companyCname;
	}

	public void setCompanyCname(String companyCname) {
		this.companyCname = companyCname;
	}
	
	@ExcelField(title="运输申请单号", align=2, sort=9)
	public String getTrnpAppNo() {
		return trnpAppNo;
	}

	public void setTrnpAppNo(String trnpAppNo) {
		this.trnpAppNo = trnpAppNo;
	}
	
	@ExcelField(title="品名代码", align=2, sort=10)
	public String getProdCode() {
		return prodCode;
	}

	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	
	@ExcelField(title="品名中文", align=2, sort=11)
	public String getProdCname() {
		return prodCname;
	}

	public void setProdCname(String prodCname) {
		this.prodCname = prodCname;
	}
	
	@ExcelField(title="材料规格描述", align=2, sort=12)
	public String getMatSpecDesc() {
		return matSpecDesc;
	}

	public void setMatSpecDesc(String matSpecDesc) {
		this.matSpecDesc = matSpecDesc;
	}
	
	@ExcelField(title="出库数量", align=2, sort=13)
	public String getOutStockQty() {
		return outStockQty;
	}

	public void setOutStockQty(String outStockQty) {
		this.outStockQty = outStockQty;
	}
	
	@ExcelField(title="计量单位", align=2, sort=14)
	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	
}