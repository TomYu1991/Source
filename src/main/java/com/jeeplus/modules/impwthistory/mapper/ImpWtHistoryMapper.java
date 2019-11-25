/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.impwthistory.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.impwthistory.entity.ImpWtHistory;

import java.util.List;

/**
 * 皮重历史MAPPER接口
 * @author 张鲁蒙
 * @version 2019-03-05
 */
@MyBatisMapper
public interface ImpWtHistoryMapper extends BaseMapper<ImpWtHistory> {

    ImpWtHistory queryImpWtHistory(ImpWtHistory i);

    void deleteImpWtHistory(ImpWtHistory i);

    void updatePic(ImpWtHistory i);

    void updateTime(ImpWtHistory i);

    void deleteByVehicleNo(ImpWtHistory i);

    List<ImpWtHistory> queryImpWtHistoryByConsignId(ImpWtHistory i);

    ImpWtHistory queryImpWtHistoryAvg(ImpWtHistory i);

}