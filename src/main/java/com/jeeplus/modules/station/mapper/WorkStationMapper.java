/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.station.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.station.entity.WorkStation;

import java.util.List;

/**
 * 工作站管理MAPPER接口
 * @author 汤进国
 * @version 2019-01-02
 */
@MyBatisMapper
public interface WorkStationMapper extends BaseMapper<WorkStation> {


    WorkStation queryWorkNameByStationIp(WorkStation w);

    List<WorkStation> queryDeviceNoSBCH();

    List<WorkStation> queryDeviceNoRFID();

    WorkStation queryByDeviceNo(String deviceNo);

    WorkStation queryByStationIp(WorkStation w);

    List<WorkStation> queryByStationName(WorkStation w);

}