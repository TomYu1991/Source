package com.jeeplus.modules.controlroom.entity;

/**
 * 集控室实体类
 */
public class Controlroom {

    private String status;          //坐席状态
    private String queueNum;        //排队数量
    private String consignId;		// 委托单号
    private String consignSubId;		// 委托单子项号
    private String weighNo;		// 磅单号
    private String matGrossWt;		// 材料毛重
    private String impWt;		// 杂重（皮重）
    private String consigneUser;		// 收货客户
    private String vehicleNo;		// 车船号
    private String prodCname;    //物资名称
    private String matWt;   //净重

    public String getMatWt() {
        return matWt;
    }
    public void setMatWt(String matWt) {
        this.matWt = matWt;
    }
    public String getProdCname() {
        return prodCname;
    }

    public void setProdCname(String prodCname) {
        this.prodCname = prodCname;
    }

    public String getConsignId() {
        return consignId;
    }

    public void setConsignId(String consignId) {
        this.consignId = consignId;
    }

    public String getConsignSubId() {
        return consignSubId;
    }

    public void setConsignSubId(String consignSubId) {
        this.consignSubId = consignSubId;
    }

    public String getWeighNo() {
        return weighNo;
    }

    public void setWeighNo(String weighNo) {
        this.weighNo = weighNo;
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

    public String getConsigneUser() {
        return consigneUser;
    }

    public void setConsigneUser(String consigneUser) {
        this.consigneUser = consigneUser;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getQueueNum() {
        return queueNum;
    }

    public void setQueueNum(String queueNum) {
        this.queueNum = queueNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
