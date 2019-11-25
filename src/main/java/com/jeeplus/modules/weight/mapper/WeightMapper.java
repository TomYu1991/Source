/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weight.mapper;

import com.jeeplus.core.persistence.BaseMapper;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.persistence.annotation.MyBatisMapper;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.gatelog.entity.GateLog;
import com.jeeplus.modules.print.entity.Print;
import com.jeeplus.modules.station.entity.StationDevice;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.weight.entity.Weight;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 磅单管理MAPPER接口
 * @author jeeplus
 * @version 2018-12-25
 */
@MyBatisMapper
public interface WeightMapper extends BaseMapper<Weight> {
	int cancel(Weight w);

	List<Weight> queryInfoByVehicleNo(Weight w);

	List<Weight> queryInfoByLadleNo(Weight w);

    Weight queryInfoByConsignId(Weight w);

	Weight queryInfoByWeighNo(Weight w);

	void updateWeightByweighNo(Weight w);

	Consign queryByWeigh(String weighNO);

	List<Weight> queryVehicle(Weight w);

	List<Weight> queryInfoByVe(String vehicleNo);

	void saveAbnrType(Weight w);

	void insertWeightResult(Weight w);

	void updateweightFlag(Weight weight);

	void updateDateType(Weight weight);

	List<Weight> queryByConsignId(String consignId);

	void saveTarePic(Weight weight);

	void saveGrossPic(Weight weight);

	List<Weight> notSynchronized();

	void updateWeight(Weight weight);

	//根据委托单号查询未完成锁皮的磅单详情
	List<Weight> queryWeightByConsignId(Weight weight);

	Weight printbill(Weight weight);

	Weight printweight(Weight w);

	List<Weight>  queryUnWeight(Weight w);

  }