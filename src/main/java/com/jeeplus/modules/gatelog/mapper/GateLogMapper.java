/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.gatelog.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.gatelog.entity.GateLog;

import java.util.List;

/**
 * 门岗日志MAPPER接口
 * @author jeeplus
 * @version 2018-12-22
 */
@MyBatisMapper
public interface GateLogMapper extends BaseMapper<GateLog> {

     GateLog countCarByVehicleNo(GateLog g);

    GateLog queryMaxCarNum();

    List<GateLog> queryGateLogbd(String weighNo);
}