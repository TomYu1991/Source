/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightrecord.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.weightrecord.entity.WeightRecord;

/**
 * 磅单信息记录MAPPER接口
 * @author 汤进国
 * @version 2019-04-18
 */
@MyBatisMapper
public interface WeightRecordMapper extends BaseMapper<WeightRecord> {
    WeightRecord queryInfoByWeighNo(WeightRecord weightRecord);
}