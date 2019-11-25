/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightmonitor.entity;

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
public class WeightMonitor extends DataEntity<WeightMonitor> {

    private static final long serialVersionUID = 1L;
    private String id;
    private Date createDate;
    private Date begintaretime;        // 过皮开始时间
    private Date endtaretime;        // 过皮结束时间
    private String searchFlag;
    private String workStation;
    private Double weightValue;
    private String VehicleNo;

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
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

    public String getSearchFlag() {
        return searchFlag;
    }

    public void setSearchFlag(String searchFlag) {
        this.searchFlag = searchFlag;
    }

    public String getWorkStation() {
        return workStation;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    public Double getWeightValue() {
        return weightValue;
    }

    public void setWeightValue(Double weightValue) {
        this.weightValue = weightValue;
    }
}