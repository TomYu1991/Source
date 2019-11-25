/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weight.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 磅单管理Entity
 *
 * @author jeeplus
 * @version 2018-12-25
 */
public class Weight extends DataEntity<Weight> {

    private static final long serialVersionUID = 1L;
    private String id;
    private String consignId;        // 委托单号
    private String weighNo;        // 磅单号
    private String weightType;        // 称重类型
    private String seqNo;        // 序号
    private String prodCode;        // 品名代码
    private String prodCname;        // 品名中文
    private String sgCode;        // 牌号代码
    private String sgSign;        // 牌号（钢级）
    private String matSpecDesc;        // 材料规格描述
    private String matWt;        // 材料重量（净重）
    private String weightFlag;        // 称重标记
    private String vehicleNo;        // 车船号
    private String shipNo;        // 船号
    private String billNo;        // 单据号
    private String ponoLotNo;        // 炉批号
    private String blastFurnaceNo;        // 高炉号
    private String ladleNo;        // 铁水罐号
    private String warehouseno;        // 装卸料点
    private String tareHeadPic;        // 空磅车头抓拍
    private String tareTailPic;        // 空磅车尾抓拍
    private String tareTopPic;            // 空磅车顶抓拍
    private String grossHeadPic;        // 重磅车头抓拍
    private String grossTailPic;        // 重磅车尾抓拍
    private String grossTopPic;        // 重磅车顶抓拍
    private String dispatchtype;        // 过磅属性   0正常1手工
    private Date taretime;        // 过皮时间
    private Date grosstime;        // 过毛时间
    private String subNo;        // 子项号
    private String dealPersonNo;        // 处理人员工号
    private String equipNum;        // 设备编码
    private String customerCode;        // 客户代码
    private String matNum;        // 材料件数（根数）
    private String matGrossWt;        // 材料毛重
    private String impWt;        // 杂重（皮重）
    private String oldImpWt;        //历史皮重
    private String status;        // 状态
    private String consigneUser;        // 收货客户
    private String supplierName;        // 供应商名称
    private String productPackWt;        // 成品包装材料重量
    private String defaultFlag;        // 默认标记
    private Date createTime;        // 生成时间
    private String updater;        // 修改人
    private Date updatetime;        // 修改时间
    private String remarks;        // 备注
    private Date printTime;        // 打印时间
    private String dataType;  //数据类型  0。正常1.未同步2已同步
    private String netStatus;  //网络状态
    private String printNum;   //打印次数
    private String printer;    //打印人
    private String rfidNo;        //RFIDs卡号
    private String abnrType;   //异常类型
    private String billPic;   //票据截图url
    private String gateNum; //工作站
    private String stationId;
    private String deviceId;
    private boolean success;
    private Date createDate;
    private String fistStation;
    private String secondStation;
    private Date begingrosstime;        // 过毛开始时间
    private Date endgrosstime;        // 过毛结束时间
    private Date begintaretime;        // 过皮开始时间
    private Date endtaretime;        // 过皮结束时间
    private String searchFlag;
    private String consignDept;
    private String workStation;
    private String affirmFlag;   //确认标记
    private String confirmPersonNo;  //确认人
    private Date  confirmTime;         //确认时间
    private String field1;  //备用字段
    private String field2;  //备用字段
    private String field3;  //备用字段
    private String field4;  //备用字段
    private String field5;  //备用字段


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

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getWorkStation() {
        return workStation;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    public String getConsignDept() {
        return consignDept;
    }

    public void setConsignDept(String consignDept) {
        this.consignDept = consignDept;
    }

    public String getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(String searchFlag) {
        this.searchFlag = searchFlag;
    }

    public Date getBegingrosstime() {
        return begingrosstime;
    }

    public void setBegingrosstime(Date begingrosstime) {
        this.begingrosstime = begingrosstime;
    }

    public Date getEndgrosstime() {
        return endgrosstime;
    }

    public void setEndgrosstime(Date endgrosstime) {
        this.endgrosstime = endgrosstime;
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

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getFistStation() {
        return fistStation;
    }

    public void setFistStation(String fistStation) {
        this.fistStation = fistStation;
    }

    public String getSecondStation() {
        return secondStation;
    }

    public void setSecondStation(String secondStation) {
        this.secondStation = secondStation;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    private String msg;

    private String errorCode;

    private String type;

    public String getGateNum() {
        return gateNum;
    }

    public void setGateNum(String gateNum) {
        this.gateNum = gateNum;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBillPic() {
        return billPic;
    }

    public void setBillPic(String billPic) {
        this.billPic = billPic;
    }


    public String getAbnrType() {
        return abnrType;
    }

    public void setAbnrType(String abnrType) {
        this.abnrType = abnrType;
    }
    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "打印时间", align = 2, sort = 7)
    public Date getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Date printTime) {
        this.printTime = printTime;
    }

    public String getOldImpWt() {
        return oldImpWt;
    }

    public void setOldImpWt(String oldImpWt) {
        this.oldImpWt = oldImpWt;
    }

    public String getRfidNo() {
        return rfidNo;
    }

    public void setRfidNo(String rfidNo) {
        this.rfidNo = rfidNo;
    }

    public String getPrinter() {
        return printer;
    }

    public void setPrinter(String printer) {
        this.printer = printer;
    }

    public String getPrintNum() {
        return printNum;
    }

    public void setPrintNum(String printNum) {
        this.printNum = printNum;
    }

    public String getNetStatus() {
        return netStatus;
    }

    public void setNetStatus(String netStatus) {
        this.netStatus = netStatus;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Weight() {
        super();
    }

    public Weight(String id) {
        super(id);
    }

    @ExcelField(title = "委托单号", align = 2, sort = 1)
    public String getConsignId() {
        return consignId;
    }

    public void setConsignId(String consignId) {
        this.consignId = consignId;
    }

    @ExcelField(title = "磅单号", align = 2, sort = 3)
    public String getWeighNo() {
        return weighNo;
    }

    public void setWeighNo(String weighNo) {
        this.weighNo = weighNo;
    }

    @ExcelField(title = "称重类型", dictType = "weight_type", align = 2, sort = 4)
    public String getWeightType() {
        return weightType;
    }

    public void setWeightType(String weightType) {
        this.weightType = weightType;
    }

    @ExcelField(title = "序号", align = 2, sort = 5)
    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getProdCode() {
        return prodCode;
    }

    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    @ExcelField(title = "品名中文", align = 2, sort = 7)
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

    @ExcelField(title = "材料重量（净重）", align = 2, sort = 11)
    public String getMatWt() {
        return matWt;
    }

    public void setMatWt(String matWt) {
        this.matWt = matWt;
    }

    @ExcelField(title = "称重标记", dictType = "weight_flag", align = 2, sort = 12)
    public String getWeightFlag() {
        return weightFlag;
    }

    public void setWeightFlag(String weightFlag) {
        this.weightFlag = weightFlag;
    }

    @ExcelField(title = "车船号", align = 2, sort = 13)
    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    @ExcelField(title = "船号", align = 2, sort = 14)
    public String getShipNo() {
        return shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    @ExcelField(title = "单据号", align = 2, sort = 15)
    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    @ExcelField(title = "炉批号", align = 2, sort = 16)
    public String getPonoLotNo() {
        return ponoLotNo;
    }

    public void setPonoLotNo(String ponoLotNo) {
        this.ponoLotNo = ponoLotNo;
    }

    @ExcelField(title = "高炉号", align = 2, sort = 17)
    public String getBlastFurnaceNo() {
        return blastFurnaceNo;
    }

    public void setBlastFurnaceNo(String blastFurnaceNo) {
        this.blastFurnaceNo = blastFurnaceNo;
    }

    @ExcelField(title = "铁水罐号", align = 2, sort = 18)
    public String getLadleNo() {
        return ladleNo;
    }

    public void setLadleNo(String ladleNo) {
        this.ladleNo = ladleNo;
    }

    @ExcelField(title = "装卸料点", align = 2, sort = 19)
    public String getWarehouseno() {
        return warehouseno;
    }

    public void setWarehouseno(String warehouseno) {
        this.warehouseno = warehouseno;
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

    public String getGrossHeadPic() {
        return grossHeadPic;
    }

    public void setGrossHeadPic(String grossHeadPic) {
        this.grossHeadPic = grossHeadPic;
    }

    public String getGrossTailPic() {
        return grossTailPic;
    }

    public void setGrossTailPic(String grossTailPic) {
        this.grossTailPic = grossTailPic;
    }

    public String getGrossTopPic() {
        return grossTopPic;
    }

    public void setGrossTopPic(String grossTopPic) {
        this.grossTopPic = grossTopPic;
    }

    @ExcelField(title = "过磅属性", dictType = "dispatch_type", align = 2, sort = 22)
    public String getDispatchtype() {
        return dispatchtype;
    }

    public void setDispatchtype(String dispatchtype) {
        this.dispatchtype = dispatchtype;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "过皮时间", align = 2, sort = 23)
    public Date getTaretime() {
        return taretime;
    }

    public void setTaretime(Date taretime) {
        this.taretime = taretime;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "过毛时间", align = 2, sort = 24)
    public Date getGrosstime() {
        return grosstime;
    }

    public void setGrosstime(Date grosstime) {
        this.grosstime = grosstime;
    }

    public String getSubNo() {
        return subNo;
    }

    public void setSubNo(String subNo) {
        this.subNo = subNo;
    }

    @ExcelField(title = "处理人员工号", align = 2, sort = 26)
    public String getDealPersonNo() {
        return dealPersonNo;
    }

    public void setDealPersonNo(String dealPersonNo) {
        this.dealPersonNo = dealPersonNo;
    }

    @ExcelField(title = "设备编码", align = 2, sort = 27)
    public String getEquipNum() {
        return equipNum;
    }

    public void setEquipNum(String equipNum) {
        this.equipNum = equipNum;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    @ExcelField(title = "材料件数（根数）", align = 2, sort = 29)
    public String getMatNum() {
        return matNum;
    }

    public void setMatNum(String matNum) {
        this.matNum = matNum;
    }

    @ExcelField(title = "材料毛重", align = 2, sort = 30)
    public String getMatGrossWt() {
        return matGrossWt;
    }

    public void setMatGrossWt(String matGrossWt) {
        this.matGrossWt = matGrossWt;
    }

    @ExcelField(title = "杂重（皮重）", align = 2, sort = 31)
    public String getImpWt() {
        return impWt;
    }

    public void setImpWt(String impWt) {
        this.impWt = impWt;
    }

    @ExcelField(title = "状态", dictType = "cancel_flag", align = 2, sort = 32)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @ExcelField(title = "收货客户", align = 2, sort = 33)
    public String getConsigneUser() {
        return consigneUser;
    }

    public void setConsigneUser(String consigneUser) {
        this.consigneUser = consigneUser;
    }

    @ExcelField(title = "供应商名称", align = 2, sort = 34)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @ExcelField(title = "成品包装材料重量", align = 2, sort = 35)
    public String getProductPackWt() {
        return productPackWt;
    }

    public void setProductPackWt(String productPackWt) {
        this.productPackWt = productPackWt;
    }

    @ExcelField(title = "默认标记", dictType = "default_flag", align = 2, sort = 36)
    public String getDefaultFlag() {
        return defaultFlag;
    }

    public void setDefaultFlag(String defaultFlag) {
        this.defaultFlag = defaultFlag;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "生成时间", align = 2, sort = 37)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ExcelField(title = "修改人", align = 2, sort = 38)
    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "修改时间", align = 2, sort = 39)
    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
    @ExcelField(title = "确认标记",dictType = "sure_flag",align = 2, sort = 42)
    public String getAffirmFlag() {
        return affirmFlag;
    }

    public void setAffirmFlag(String affirmFlag) {
        this.affirmFlag = affirmFlag;
    }
    @ExcelField(title = "确认人工号", align = 2, sort = 40)
    public String getConfirmPersonNo() {
        return confirmPersonNo;
    }

    public void setConfirmPersonNo(String confirmPersonNo) {
        this.confirmPersonNo = confirmPersonNo;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelField(title = "确认时间", align = 2, sort = 41)
    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }
}