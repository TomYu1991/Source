/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.consign.service;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.mapper.ConsignMapper;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.mapper.VehicleAccessRecordMapper;
import com.jeeplus.modules.weight.entity.Weight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 委托单/预约单管理Service
 * @author 汤进国
 * @version 2019-01-17
 */
@Service
@Transactional(readOnly = true)
public class ConsignService extends CrudService<ConsignMapper, Consign> {

	@Autowired
	ConsignMapper consignmapper;
	@Autowired
	VehicleAccessRecordMapper vehicleAccessRecordMapper;

	public Consign get(String id) {
		return super.get(id);
	}
	
	public List<Consign> findList(Consign consign) {
		return super.findList(consign);
	}
	
	public Page<Consign> findPage(Page<Consign> page, Consign consign) {

		if(consign.getVehicleNo()!=null&&!"".equals(consign.getVehicleNo())){
			String v = "%"+consign.getVehicleNo()+"%";
			consign.setVehicleNo(v);
		}
		if(consign.getConsignId()!=null&&!"".equals(consign.getConsignId())){
			String v = "%"+consign.getConsignId()+"%";
			consign.setConsignId(v);
		}

		if(consign.getProdCname()!=null&&!"".equals(consign.getProdCname())){
			String v = "%"+consign.getProdCname()+"%";
			consign.setProdCname(v);
		}
		if(consign.getIDCard()!=null&&!"".equals(consign.getIDCard())){
			String v = "%"+consign.getIDCard()+"%";
			consign.setIDCard(v);
		}
		if("".equals(page.getOrderBy())||page.getOrderBy()==null){
			page.setOrderBy("a.end_time desc");
		}

		return super.findPage(page, consign);
	}
	
	@Transactional(readOnly = false)
	public void save(Consign consign) {

		if(consign.getWeightState()==null||"".equals(consign.getWeightState())){
			consign.setWeightState("0");
		}
		if(consign.getDataType()==null||"".equals(consign.getDataType())){
			consign.setDataType("0");
		}
		if(consign.getStatus()==null||"".equals(consign.getStatus())){
			consign.setStatus("0");
		}

		super.save(consign);
	}
	
	@Transactional(readOnly = false)
	public void delete(Consign consign) {
		super.delete(consign);
	}
	//作废
	@Transactional(readOnly = false)
	public int cancel(Consign consign) {
		consign.setStatus("2");
		return consignmapper.cancel(consign);
	}

	//查询当前车牌号是否已进厂
	public List<Consign>  checkStateByVehicleNo(Consign consign) {
		return consignmapper.checkStateByVehicleNo(consign);
	}

	//根据车牌号和当前时间查询有效的委托单
	public List<Consign>  findConsignByVehicleNo(Consign consign) {
		return consignmapper.findConsignByVehicleNo(consign);
	}

	//根据车牌号查询委托单
	public List<Consign>  queryConsignByV(Consign consign) {
		return consignmapper.queryConsignByV(consign);
	}
	//根据车牌号查询所有委托单
	public List<Consign>  queryConsignByVehicleNo(String vehicleNo) {
		Consign consign = new Consign();
		consign.setVehicleNo(vehicleNo);
		return consignmapper.queryConsignByV(consign);
	}
	//根据车牌号查询所有有效的未过磅委托单
	public List<Consign>  queryConsignByVe(String vehicleNo) {
		Consign consign = new Consign();
		consign.setVehicleNo(vehicleNo);
		return consignmapper.queryConsignByVehicleNo(consign);
	}
	//根据车牌号查询所有有效的锁皮委托单
	public List<Consign>  queryDefaultFlagConsign(String vehicleNo) {
		Consign consign = new Consign();
		consign.setVehicleNo(vehicleNo);
		return consignmapper.queryDefaultFlagConsign(consign);
	}

    //出门根据车牌号查询委托单
    public List<Consign>  checkOutByVehicleNo(Consign consign) {
        return consignmapper.checkOutByVehicleNo(consign);
    }


	//根据委托单号查询有效的委托单
	public Consign queryInfoByConsignId(String consignId) {
		Consign consign = new Consign();
		consign.setConsignId(consignId);
		return consignmapper.queryInfoByConsignId(consign);
	}

	//修改委托单进厂状态
	@Transactional(readOnly = false)
	public void updateConsignState(Consign consign) {

		consignmapper.updateConsignState(consign);
	}

	//查询当前是否有一次过磅的委托单
	public Consign checkWeightStatus(String vehicleNo) {
		Consign consign = new Consign();
		consign.setVehicleNo(vehicleNo);
		return consignmapper.checkWeightStatus(consign);
	}

	//接口方法
	//新增
	@Transactional(readOnly = false)
	public int saveConsign(Consign consign) {
		int id = consignmapper.saveConsign(consign);
		return id;
	}
	//删除
	@Transactional(readOnly = false)
	public int deleteConsign(Consign consign) {
		int id = consignmapper.deleteConsign(consign);
		return id;
	}
	//作废（逻辑删除
	@Transactional(readOnly = false)
	public int cancelConsign(Consign consign) {
		int id = consignmapper.cancelConsign(consign);
		return id;
	}
	//根据车牌号查询有效期
	public Consign findValidityByVehicleNo(Consign consign){
		return consignmapper.findValidityByVehicleNo(consign);
	}
	//根据铁水罐号查询有效期
	public List<Consign> findValidityByLableNo(Consign consign){
		return consignmapper.findValidityByLableNo(consign);
	}

	//根据车牌号查询有效期
	public List<Consign> findInfoByIdCard(String idcard){
		Consign consign = new Consign();
		consign.setIDCard(idcard);
		return consignmapper.findInfoByIdCard(consign);
	}
	//过磅页面列表
	public List<Consign> showweights(Consign consign){
		List<Consign> l = consignmapper.showweights(consign);
		for(Consign c :l){
			if(c.getMatWt()!=null&&!"0.00".equals(c.getMatWt())){
				l.remove(c);
			}
		}
		return l;
	}
	//超時車輛
	public List<Consign> queryTimeOutConsign(){

		List<Consign> l = consignmapper.queryTimeOutConsign();
		return l;
	}

	//超時車輛
	public Consign findlastByVehicleNo(Consign c){

		Consign l = consignmapper.findlastByVehicleNo(c);
		return l;
	}

	//超時車輛
	public List<Consign> queryMaxEndTimeConsign(){
		List<Consign> list = new ArrayList();
		//查询当天结束且不在车辆信息表中的委托单
		List<Consign> l = consignmapper.queryMaxEndTimeConsign();

		for(Consign consign :l){
			//判断该车是否有其他当前有效的委托单
			Consign cons =consignmapper.findlastByVehicleNo(consign);
			if(cons==null){
				//判断该车号是否在有效期内有进出记录
				VehicleAccessRecord var = new VehicleAccessRecord();
				var.setVehicleNo(consign.getVehicleNo());
				var.setBeginintoTime(consign.getStartTime());
				var.setEndintoTime(consign.getEndTime());
				List<VehicleAccessRecord> vl=vehicleAccessRecordMapper.queryLRecord(var);
				if(vl.size()>0&&vl.get(0).getIntoTime()!=null&&!"".equals(vl.get(0).getIntoTime())){
					if(consign.getVehicleNo()!=null&&!"".equals(consign.getVehicleNo())){
						list.add(consign);
					}
				}
			}
		}
		return list;
	}

	//根据总单号
	public List<Consign> findInfoByConsignNo(String consignNo){
		Consign consign = new Consign();
		consign.setConsignNo(consignNo);
		return consignmapper.findInfoByIdCard(consign);
	}
	//查询未过磅委托单
	public List<Consign> showweightsall (Consign consign){
		return consignmapper.showweightsall(consign);
	}
	@Transactional(readOnly = false)
    public void updateweightState(Consign consign){
		consignmapper.updateweightState(consign);
	}

	public List<Consign> queryByConsignId(String consignId){
		Consign c= new Consign();
		c.setConsignId(consignId);
		return consignmapper.queryByConsignId(c);
	}
	//根据委托单号查询委托单详情
	public List<Consign> queryInterInfoByConsignId(String consignId){
		Consign c= new Consign();
		c.setConsignId(consignId);
		return consignmapper.queryInterInfoByConsignId(c);
	}
	public List<Consign> queryVehicleNoByEquipNum(String equipNum){
		Consign consign=new Consign();
		consign.setEquipNum(equipNum);
		return consignmapper.queryVehicleNoByEquipNum(consign)	;
	}
	public List<Consign> queryLadleNoByEquipNum(String equipNum){
		Consign consign=new Consign();
		consign.setEquipNum(equipNum);
		return consignmapper.queryLadleNoByEquipNum(consign)	;
	}

	public List<Consign> queryStateConsign(String vehicleNo){
		Consign consign=new Consign();
		consign.setVehicleNo(vehicleNo);
		return consignmapper.queryStateConsign(consign)	;
	}

	public List<Weight> queryFirstWeight(String consignId){
		return consignmapper.queryFirstWeight(consignId);
	}

	@Transactional(readOnly = false)
	public void updateDefaultFlag(Consign consign){
		consignmapper.updateDefaultFlag(consign);
	}

	@Transactional(readOnly = false)
	public void updatesurplusWt(Consign consign){
		if(consign!=null&&consign.getConsignNo()!=null&&!"".equals(consign.getConsignNo())){

			consignmapper.updatesurplusWtByConsignNo(consign);
		}else{
			consignmapper.updatesurplusWtByConsignId(consign);
		}

	}

	//排除预约单
	public int passYy(Consign c){
		return consignmapper.passYY(c).size();
	}

}