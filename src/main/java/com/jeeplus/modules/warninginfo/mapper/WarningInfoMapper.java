/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warninginfo.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;

import java.util.List;

/**
 * 报警信息MAPPER接口
 * @author 张鲁蒙
 * @version 2019-03-05
 */
@MyBatisMapper
public interface WarningInfoMapper extends BaseMapper<WarningInfo> {

   List<WarningInfo> findInfoByVehicleNo(WarningInfo warningInfo);

   void updateDateType(WarningInfo warningInfo);

   void insertInter(WarningInfo warningInfo);

    List<WarningInfo> getApproveVehicleList(WarningInfo warningInfo);

    List<WarningInfo> getUnapproveVehicleList(WarningInfo warningInfo);

    List<WarningInfo> getWarningInfoVehicleList(WarningInfo warningInfo);
}