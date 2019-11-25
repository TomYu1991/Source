/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.illegalinfo.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.illegalinfo.entity.IllegalInfo;

import java.util.List;

/**
 * 非法闯入信息记录MAPPER接口
 * @author illegalinfo
 * @version 2019-04-06
 */
@MyBatisMapper
public interface IllegalInfoMapper extends BaseMapper<IllegalInfo> {
   void savePhoto(IllegalInfo i);

    IllegalInfo queryLateInfoByVehicleNo(IllegalInfo i);

}