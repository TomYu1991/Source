/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.consign_tmp.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign_tmp.entity.ConsignTmp;
import com.jeeplus.modules.weight.entity.Weight;

import java.util.List;

/**
 * 委托单/预约单管理MAPPER接口
 * @author 汤进国
 * @version 2019-01-17
 */
@MyBatisMapper
public interface ConsignTmpMapper extends BaseMapper<ConsignTmp> {

    List<ConsignTmp> findListByOptflag();

    void updateConsignTmp(ConsignTmp tmp);
}