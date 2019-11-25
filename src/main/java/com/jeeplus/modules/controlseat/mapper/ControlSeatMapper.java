/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.controlseat.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.controlseat.entity.ControlSeat;

/**
 * 集控室坐席表MAPPER接口
 * @author 汤进国
 * @version 2019-01-14
 */
@MyBatisMapper
public interface ControlSeatMapper extends BaseMapper<ControlSeat> {

   void updateSeatState(ControlSeat cs);

   ControlSeat findInfoByIp(ControlSeat cs);
}