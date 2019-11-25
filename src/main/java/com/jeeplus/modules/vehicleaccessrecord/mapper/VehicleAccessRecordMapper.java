/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.vehicleaccessrecord.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;

import java.util.List;

/**
 * 车辆进出记录MAPPER接口
 * @author 汤进国
 * @version 2019-01-18
 */
@MyBatisMapper
public interface VehicleAccessRecordMapper extends BaseMapper<VehicleAccessRecord> {


    void updateOutTime(VehicleAccessRecord v);

    VehicleAccessRecord queryLatelyRecord(VehicleAccessRecord v);

    void updateState(VehicleAccessRecord v);

    VehicleAccessRecord queryVehicleNum();

    void savePeccancyInfo(VehicleAccessRecord v);

    List<VehicleAccessRecord> queryRecord(VehicleAccessRecord v);
    List<VehicleAccessRecord> queryLRecord(VehicleAccessRecord v);
    void updatePic(VehicleAccessRecord v);
    void updateOpenInfo(VehicleAccessRecord v);

    //进门车辆
    int getInCount(VehicleAccessRecord vehicleAccessRecord);
    //出门车辆
    int getOutCount(VehicleAccessRecord v);
    //预约车辆
    int getConsignVehicleCount(VehicleAccessRecord v);
    //预约入厂车辆
    List<VehicleAccessRecord> getIntoFactoryVehicle(VehicleAccessRecord record);
    List<VehicleAccessRecord> getVehicleManualList(VehicleAccessRecord record);
}