/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.consign.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.weight.entity.Weight;

import java.util.List;

/**
 * 委托单/预约单管理MAPPER接口
 * @author 汤进国
 * @version 2019-01-17
 */
@MyBatisMapper
public interface ConsignMapper extends BaseMapper<Consign> {
    int cancel(Consign consign);

    List<Consign> findConsignByVehicleNo(Consign consign);

    List<Consign> queryMaxEndTimeConsign();

    List<Consign> queryConsignByV(Consign consign);

    Consign queryInfoByConsignId(Consign consign);

    Consign checkWeightStatus(Consign consign);

    void updateConsignState(Consign consign);

    List<Consign> checkStateByVehicleNo(Consign consign);

    List<Consign> checkOutByVehicleNo(Consign consign);

    List<Consign> showweightsall (Consign consign);
    //接口方法
    int saveConsign(Consign consign);

    int deleteConsign(Consign consign);

    int cancelConsign(Consign consign);
    //根据车牌号查询
    Consign findValidityByVehicleNo(Consign consign);
    //根据车牌号查询
    Consign findlastByVehicleNo(Consign consign);

    //根据铁水罐号查询
    List<Consign> findValidityByLableNo(Consign consign);
    //根据身份证
    List<Consign> findInfoByIdCard(Consign consign);
    //过磅页面列表
    List<Consign> showweights(Consign consign);

    void updateweightState(Consign consign);

    List<Consign> queryByConsignId(Consign consign);

    List<Weight> queryFirstWeight(String consignId);

    //根据委托单号查询委托单详情
    List<Consign> queryInterInfoByConsignId(Consign consign);
    //根据磅秤查询有效车牌
    List<Consign> queryVehicleNoByEquipNum(Consign consign);
    //根据磅秤查询有效罐号
    List<Consign> queryLadleNoByEquipNum(Consign consign);

    void updateDefaultFlag(Consign consign);
    void updatesurplusWtByConsignId(Consign consign);
    void updatesurplusWtByConsignNo(Consign consign);


    //查詢超時車輛
    List<Consign> queryTimeOutConsign();

    List<Consign> queryConsignByVehicleNo(Consign consign);
    List<Consign> queryStateConsign(Consign consign);
    List<Consign> queryDefaultFlagConsign(Consign consign);

    //排除预约单
    List<Consign> passYY(Consign consign);

}