/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.passcheck.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.weight.entity.Weight;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 出门条信息MAPPER接口
 * @author 汤进国
 * @version 2019-01-17
 */
@MyBatisMapper
public interface PassCheckMapper extends BaseMapper<PassCheck> {

    List<PassCheck> findPassByVehicleNo(PassCheck p);

    void delByLogic(PassCheck p);

    void updateIdentTime(PassCheck p);

    @Transactional(readOnly = false)
    int insertPassCheckResult(PassCheck p);

    void updateDateType(PassCheck p);

    List<PassCheck> findTrnpAppNo(PassCheck p);

    void updateDeal(PassCheck p);

    List<PassCheck> getVehiclePassCheckList(PassCheck p);

    List<PassCheck> getVehiclePassCheckUList(PassCheck p);
}