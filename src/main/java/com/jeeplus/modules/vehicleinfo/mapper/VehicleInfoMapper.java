/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.vehicleinfo.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.method.P;

import java.util.Date;
import java.util.List;

/**
 * 车辆信息表MAPPER接口
 * @author 汤进国
 * @version 2019-01-17
 */
@MyBatisMapper
public interface VehicleInfoMapper extends BaseMapper<VehicleInfo> {

    //根据车牌号查询车辆信息表车辆信息
    List<VehicleInfo> checkByVehicleNo(VehicleInfo vehicleInfo);
    List<VehicleInfo> getVehicleNoByRfid(VehicleInfo vehicleInfo);
    List<VehicleInfo> getVehicleNoBySrfid(VehicleInfo vehicleInfo);
    //接口方法
    int insertVehicleInfo(VehicleInfo vehicleInfo);

    int deleteVehicleInfo(VehicleInfo vehicleInfo);

    int cancelVehicleInfo(VehicleInfo vehicleInfo);
    //查询该车辆是否在有效期之内
    VehicleInfo queryValidity(String vehicleNo);
    //查询出厂车辆的放行码
    VehicleInfo getVehiclePassCode(String vehicleNo);

    //更新RFID卡号
    void updaterfid(VehicleInfo vehicleInfo);
    //更新轨道衡RFID卡号
    void updaterRailfid(VehicleInfo vehicleInfo);
}