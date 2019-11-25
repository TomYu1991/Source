/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.controlqueue.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.controlqueue.entity.ControlQueue;

import java.util.List;

/**
 * 集控室排队MAPPER接口
 * @author 汤进国
 * @version 2019-01-10
 */
@MyBatisMapper
public interface ControlQueueMapper extends BaseMapper<ControlQueue> {

    void updateState(ControlQueue c);

    void updateStateByWeight(ControlQueue c);

    ControlQueue checkSeatNum(ControlQueue c);

    ControlQueue countQueue(ControlQueue c);

    List<ControlQueue> getMinSeatNum();

    void updateStateByWeighId(ControlQueue c);

    ControlQueue countQueueAll();

    ControlQueue queryInfoByWeigh(String weigh);

//    void removeQueue(ControlQueue queue);
}