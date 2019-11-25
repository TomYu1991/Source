package com.jeeplus.modules.accessreport.entity;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.modules.check.entity.DeviceCheckConfig;

import java.util.Date;

/**
 * @ClassName yuzhongtong
 * @Description TODO
 * @Author user
 * @Date 2019/7/31 17:04
 * @Version 1.0
 **/
public class QueryResult extends DataEntity<DeviceCheckConfig> {
    private static final long serialVersionUID = 1L;
    private int vehicleIn;//进门车辆
    private int vehicleOut;//出门车辆
    private int vehicleConsign;//预约车辆
    private int vehicleToFactory;//预约入厂车辆
    private int vehicleManual;//手动放行车辆
    private int approveVehicle;//批准过夜车辆
    private int unapproveVehicle;//未批准过夜车辆
    private int illegalVehicle;//违章车辆
    private int userIn;//进门人员
    private int userOut;//出门人员
    private int userConsign;//预约人员
    private int userToFactory;//人员入厂
    private int gatePass;//出门条
    private int gatePassu;//出门条未出门
    private int breakIn;//重要部位闯入
    private String type;
    private Date beginTime;
    private Date endTime;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getVehicleIn() {
        return vehicleIn;
    }

    public void setVehicleIn(int vehicleIn) {
        this.vehicleIn = vehicleIn;
    }

    public int getVehicleOut() {
        return vehicleOut;
    }

    public void setVehicleOut(int vehicleOut) {
        this.vehicleOut = vehicleOut;
    }

    public int getVehicleConsign() {
        return vehicleConsign;
    }

    public void setVehicleConsign(int vehicleConsign) {
        this.vehicleConsign = vehicleConsign;
    }

    public int getVehicleToFactory() {
        return vehicleToFactory;
    }

    public void setVehicleToFactory(int vehicleToFactory) {
        this.vehicleToFactory = vehicleToFactory;
    }

    public int getVehicleManual() {
        return vehicleManual;
    }

    public void setVehicleManual(int vehicleManual) {
        this.vehicleManual = vehicleManual;
    }

    public int getApproveVehicle() {
        return approveVehicle;
    }

    public void setApproveVehicle(int approveVehicle) {
        this.approveVehicle = approveVehicle;
    }

    public int getUnapproveVehicle() {
        return unapproveVehicle;
    }

    public void setUnapproveVehicle(int unapproveVehicle) {
        this.unapproveVehicle = unapproveVehicle;
    }

    public int getIllegalVehicle() {
        return illegalVehicle;
    }

    public void setIllegalVehicle(int illegalVehicle) {
        this.illegalVehicle = illegalVehicle;
    }

    public int getUserIn() {
        return userIn;
    }

    public void setUserIn(int userIn) {
        this.userIn = userIn;
    }

    public int getUserOut() {
        return userOut;
    }

    public void setUserOut(int userOut) {
        this.userOut = userOut;
    }

    public int getUserConsign() {
        return userConsign;
    }

    public void setUserConsign(int userConsign) {
        this.userConsign = userConsign;
    }

    public int getUserToFactory() {
        return userToFactory;
    }

    public void setUserToFactory(int userToFactory) {
        this.userToFactory = userToFactory;
    }

    public int getGatePass() {
        return gatePass;
    }

    public void setGatePass(int gatePass) {
        this.gatePass = gatePass;
    }

    public int getGatePassu() {
        return gatePassu;
    }

    public void setGatePassu(int gatePassu) {
        this.gatePassu = gatePassu;
    }

    public int getBreakIn() {
        return breakIn;
    }

    public void setBreakIn(int breakIn) {
        this.breakIn = breakIn;
    }
}
