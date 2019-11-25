/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.passchecksub.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.passchecksub.entity.PassCheckSub;

import java.util.List;

/**
 * 出门条明细MAPPER接口
 * @author 汤进国
 * @version 2019-01-17
 */
@MyBatisMapper
public interface PassCheckSubMapper extends BaseMapper<PassCheckSub> {


    List<PassCheckSub> findPassCheckSubList(String trnpAppNo);
    List<PassCheckSub> findCheckSubList(String trnpAppNo);

}