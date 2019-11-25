/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightmonitor.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.gatelog.entity.GateLog;
import com.jeeplus.modules.print.entity.Print;
import com.jeeplus.modules.station.entity.StationDevice;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weightmonitor.entity.WeightMonitor;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 磅单管理MAPPER接口
 * @author jeeplus
 * @version 2018-12-25
 */
@MyBatisMapper
public interface WeightMonitorMapper extends BaseMapper<WeightMonitor> {
    //根据地磅和时间获取过磅数据
    List<WeightMonitor> findListByStationAndTime(WeightMonitor monitor);
  }