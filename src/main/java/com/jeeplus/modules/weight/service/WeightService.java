/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weight.service;

import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.mapper.ConsignMapper;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.controlqueue.entity.ControlQueue;
import com.jeeplus.modules.controlqueue.mapper.ControlQueueMapper;
import com.jeeplus.modules.controlqueue.service.ControlQueueService;
import com.jeeplus.modules.gatelog.entity.GateLog;
import com.jeeplus.modules.print.entity.Print;
import com.jeeplus.modules.station.entity.StationDevice;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.mapper.VehicleAccessRecordMapper;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.mapper.WeightMapper;
import com.jeeplus.modules.weightrecord.entity.InitWeight;
import com.jeeplus.modules.weightrecord.entity.PrintRecord;
import com.jeeplus.modules.weightrecord.entity.UpdateWeightRecord;
import com.jeeplus.modules.weightrecord.entity.WeightRecord;
import com.jeeplus.modules.weightrecord.mapper.InitWeightMapper;
import com.jeeplus.modules.weightrecord.mapper.PrintRecordMapper;
import com.jeeplus.modules.weightrecord.mapper.UpdateWeightRecordMapper;
import com.jeeplus.modules.weightrecord.mapper.WeightRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 磅单管理Service
 *
 * @author jeeplus
 * @version 2018-12-25
 */
@Service
@Transactional(readOnly = true)
public class WeightService extends CrudService<WeightMapper, Weight> {

    @Autowired
    private WeightMapper weightMapper;
    @Autowired
    VehicleAccessRecordMapper vehicleAccessRecordMapper;

    @Autowired
    ConsignService consignService;

    @Autowired
    ControlQueueService controlQueueService;
    @Autowired
    ConsignMapper consignMapper;
    @Autowired
    private InitWeightMapper initWeightMapper;

    @Autowired
    private PrintRecordMapper printRecordMapper;
    @Autowired
    private UpdateWeightRecordMapper updateWeightRecordMapper;

    public Weight get(String id) {
        return super.get(id);
    }

    public List<Weight> findList(Weight weight) {
        return super.findList(weight);
    }

    public Page<Weight> findPage(Page<Weight> page, Weight weight) {

        if (weight.getVehicleNo() != null && !"".equals(weight.getVehicleNo())) {
            String v = "%" + weight.getVehicleNo() + "%";
            weight.setVehicleNo(v);
        }
        if (weight.getProdCname() != null && !"".equals(weight.getProdCname())) {
            String v = "%" + weight.getProdCname() + "%";
            weight.setProdCname(v);
        }
        if (weight.getConsignId() != null && !"".equals(weight.getConsignId())) {
            String v = "%" + weight.getConsignId() + "%";
            weight.setConsignId(v);
        }
        if (weight.getWeighNo() != null && !"".equals(weight.getWeighNo())) {
            String v = "%" + weight.getWeighNo() + "%";
            weight.setWeighNo(v);
        }
        if (weight.getConsigneUser() != null && !"".equals(weight.getConsigneUser())) {
            String v = "%" + weight.getConsigneUser() + "%";
            weight.setConsigneUser(v);
        }
        if (weight.getSupplierName() != null && !"".equals(weight.getSupplierName())) {
            String v = "%" + weight.getSupplierName() + "%";
            weight.setSupplierName(v);
        }
        if ("".equals(page.getOrderBy()) || page.getOrderBy() == null) {
            page.setOrderBy("a.weigh_no desc");
        }

        return super.findPage(page, weight);
    }

    @Transactional(readOnly = false)
    public void save(Weight weight) {
        Weight w = queryInfoByWeighNo(weight);
        UpdateWeightRecord uwr = new UpdateWeightRecord();
        StringBuilder str = new StringBuilder();
        if (w != null && weight != null) {
            if (w.getConsignId() != null && weight.getConsignId() != null && !w.getConsignId().equals(weight.getConsignId())) {
                str.append("原委托单号：" + w.getConsignId() + ",现委托单号：" + weight.getConsignId() + ";");
                Consign consign = new Consign();
                consign.setConsignId(weight.getConsignId());
                List<Consign> c = consignMapper.queryByConsignId(consign);
                if(c.size()>0){
                    weight.setProdCname(c.get(0).getProdCname());
                    weight.setSupplierName(c.get(0).getSupplierName());
                    if(c.get(0).getWeightState()!=null&&"1".equals(c.get(0).getWeightState())){
                        weight.setWeightFlag("0");
                    }else{
                        weight.setWeightFlag("1");
                    }
                    weight.setWeightType(c.get(0).getWeightType());
                    weight.setBlastFurnaceNo(c.get(0).getBlastFurnaceNo());
                    weight.setVehicleNo(c.get(0).getVehicleNo());
                    weight.setLadleNo(c.get(0).getLadleNo());
                    weight.setEquipNum(c.get(0).getEquipNum());
                    weight.setConsigneUser(c.get(0).getConsigneUser());
                    weight.setDefaultFlag(c.get(0).getDefaultFlag());
                }
            }
            if (w.getProdCname() != null && weight.getProdCname() != null && !w.getProdCname().equals(weight.getProdCname())) {
                str.append("原品名：" + w.getProdCname() + ",现品名：" + weight.getProdCname() + ";");
            }
            if (w.getWeightType() != null && weight.getWeightType() != null && !w.getWeightType().equals(weight.getWeightType())) {
                str.append("原称重类型：" + w.getWeightType() + ",现称重类型：" + weight.getWeightType() + ";");
            }
            if (w.getPonoLotNo() != null && weight.getPonoLotNo() != null && !w.getPonoLotNo().equals(weight.getPonoLotNo())) {
                str.append("原炉批号：" + w.getPonoLotNo() + ",现炉批号：" + weight.getPonoLotNo() + ";");
            }
            if (w.getBlastFurnaceNo() != null && weight.getBlastFurnaceNo() != null && !w.getBlastFurnaceNo().equals(weight.getBlastFurnaceNo())) {
                str.append("原高炉号：" + w.getBlastFurnaceNo() + ",现高炉号：" + weight.getBlastFurnaceNo() + ";");
            }
            if (w.getVehicleNo() != null && weight.getVehicleNo() != null && !w.getVehicleNo().equals(weight.getVehicleNo())) {
                str.append("原车牌号：" + w.getVehicleNo() + ",现车牌号：" + weight.getVehicleNo() + ";");
            }
            if (w.getLadleNo() != null && weight.getLadleNo() != null && !w.getLadleNo().equals(weight.getLadleNo())) {
                str.append("原铁水罐号：" + w.getLadleNo() + ",现铁水罐号：" + weight.getLadleNo() + ";");
            }
            if (w.getEquipNum() != null && weight.getEquipNum() != null && !w.getEquipNum().equals(weight.getEquipNum())) {
                str.append("原磅秤：" + w.getEquipNum() + ",现磅秤：" + weight.getEquipNum() + ";");
            }
            if (w.getMatWt() != null && weight.getMatWt() != null && !w.getMatWt().equals(weight.getMatWt())) {
                str.append("原净重：" + w.getMatWt() + ",现净重：" + weight.getMatWt() + ";");
            }
            if (w.getMatGrossWt() != null && weight.getMatGrossWt() != null && !w.getMatGrossWt().equals(weight.getMatGrossWt())) {
                str.append("原毛重：" + w.getMatGrossWt() + ",现毛重：" + weight.getMatGrossWt() + ";");
            }
            if (w.getImpWt() != null && weight.getImpWt() != null && !w.getImpWt().equals(weight.getImpWt())) {
                str.append("原皮重：" + w.getImpWt() + ",现皮重：" + weight.getImpWt() + ";");
            }
            if (w.getGrosstime() != null && weight.getGrosstime() != null && !w.getGrosstime().equals(weight.getGrosstime())) {
                str.append("原一次过磅时间：" + w.getGrosstime() + ",现一次过磅时间：" + weight.getGrosstime() + ";");
            }
            if (w.getTaretime() != null && weight.getTaretime() != null && !w.getTaretime().equals(weight.getTaretime())) {
                str.append("原二次过磅时间：" + w.getTaretime() + ",现二次过磅时间：" + weight.getTaretime() + ";");
            }
            if (w.getImpWt() != null && weight.getImpWt() != null && !w.getImpWt().equals(weight.getImpWt())) {
                str.append("原皮重：" + w.getImpWt() + ",现皮重：" + weight.getImpWt() + ";");
            }
            if (w.getStatus() != null && weight.getStatus() != null && !w.getStatus().equals(weight.getStatus())) {
                str.append("原磅单状态：" + w.getStatus() + ",现磅单状态：" + weight.getStatus() + ";");
            }
            if (w.getDefaultFlag() != null && weight.getDefaultFlag() != null && !w.getDefaultFlag().equals(weight.getDefaultFlag())) {
                str.append("原锁皮标记：" + w.getDefaultFlag() + ",现锁皮标记：" + weight.getDefaultFlag() + ";");
            }
            if (w.getRemarks() != null && weight.getRemarks() != null && !w.getRemarks().equals(weight.getRemarks())) {
                str.append("原备注：" + w.getRemarks() + ",现备注：" + weight.getRemarks() + ";");
            }
            if (w.getConsigneUser() != null && weight.getConsigneUser() != null && !w.getConsigneUser().equals(weight.getConsigneUser())) {
                str.append("原收货方：" + w.getConsigneUser() + ",现收货方：" + weight.getConsigneUser() + ";");
            }
            if (w.getSupplierName() != null && weight.getSupplierName() != null && !w.getSupplierName().equals(weight.getSupplierName())) {
                str.append("原发货方：" + w.getSupplierName() + ",现发货方：" + weight.getSupplierName() + ";");
            }
            if (w.getPrintNum() != null && weight.getPrintNum() != null && !w.getPrintNum().equals(weight.getPrintNum())) {
                str.append("原打印次数：" + w.getPrintNum() + ",现打印次数：" + weight.getPrintNum() + ";");
            }
        }
        uwr.setContent(str.toString());
        uwr.setOperation("3");
        uwr.setId(IdGen.uuid());
        uwr.setUpdateBy(UserUtils.getUser());
        uwr.setUpdateDate(new Date());
        uwr.setContent(str.toString());
        WeightRecord wr = new WeightRecord();
        wr.setWeighNo(weight.getWeighNo());
        uwr.setWeight(wr);
        updateWeightRecordMapper.insert(uwr);
        //新增或编辑表单保存
        if (weight.getMatWt() == null) {
            weight.setMatWt("0.00");
        }
        if (weight.getMatGrossWt() == null) {
            weight.setMatGrossWt("0.00");
        }
        if (weight.getImpWt() == null) {
            weight.setImpWt("0.00");
        }
        super.save(weight);
    }

    @Transactional(readOnly = false)
    public synchronized void saveFistWeight(Weight weight, Consign consign) {

        StringBuilder result = new StringBuilder();
        result.append("BGDS");
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
        String time = sdf.format(new Date());
        result.append(time);
        String no = getWeightno();
        result.append(no);
        weight.setWeighNo(result.toString());//磅单号
        weight.setSeqNo(no);//序号
        WeightRecord wr = new WeightRecord();
        wr.setWeighNo(weight.getWeighNo());

        InitWeight iw = new InitWeight();
        iw.setProdCname(weight.getProdCname());
        iw.setVehicleNo(weight.getVehicleNo());
        iw.setLadleNo(weight.getLadleNo());
        iw.setDefaultFlag(weight.getDefaultFlag());
        iw.setStationIp(weight.getFistStation());
        iw.setWeightTime(new Date());
        iw.setId(IdGen.uuid());
        iw.setWeight(wr);
        iw.setRemarks("一次过磅");
        if ("01".equals(consign.getWeightType()) || "06".equals(consign.getWeightType()) || "07".equals(consign.getWeightType()) || "08".equals(consign.getWeightType())) {
            iw.setWeightWt(weight.getImpWt());
        } else {
            iw.setWeightWt(weight.getMatGrossWt());
        }
        initWeightMapper.insert(iw);
        super.save(weight);
        //将委托单子项状态改为 “一次过磅”
        if (consign != null && consign.getConsignId() != null) {
            consign.setWeightState("1");
            consignMapper.updateweightState(consign);
        }


    }

    @Transactional(readOnly = false)
    public synchronized void saveSecondWeight(Weight weight, ControlQueue cq, Consign cs) {
        ControlQueueService controlQueueService = SpringContextHolder.getBean(ControlQueueService.class);
        if (weight != null) {
            WeightRecord wr = new WeightRecord();
            wr.setWeighNo(weight.getWeighNo());

            InitWeight iw = new InitWeight();
            iw.setProdCname(weight.getProdCname());
            iw.setVehicleNo(weight.getVehicleNo());
            iw.setLadleNo(weight.getLadleNo());
            iw.setDefaultFlag(weight.getDefaultFlag());
            iw.setStationIp(weight.getSecondStation());
            iw.setWeightTime(new Date());
            iw.setWeight(wr);
            iw.setId(IdGen.uuid());
            iw.setRemarks("二次过磅");

            if (cs != null && cs.getWeightType() != null && !"".equals(cs.getWeightType())) {
                if ("01".equals(cs.getWeightType()) || "06".equals(cs.getWeightType()) || "07".equals(cs.getWeightType()) || "08".equals(cs.getWeightType())) {
                    iw.setWeightWt(weight.getMatGrossWt());
                } else {
                    iw.setWeightWt(weight.getImpWt());
                }
            }

            if (weight.getDefaultFlag() != null && "1".equals(weight.getDefaultFlag())) {
                StringBuilder result = new StringBuilder();
                result.append("BGDS");
                SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
                String time = sdf.format(new Date());
                result.append(time);
                String no = getWeightno();
                result.append(no);
                weight.setWeighNo(result.toString());//磅单号
                cq.setWeightId(weight.getWeighNo());
                weight.setSeqNo(no);//序号
                weight.setId(IdGen.uuid());
                iw.setWeightWt(weight.getMatGrossWt());
                weightMapper.insert(weight);
            } else {
                weightMapper.update(weight);
                cs.setWeightState("0");
                consignMapper.updateweightState(cs);
            }
            if (iw != null) {
                initWeightMapper.insert(iw);
            }
            if(!"10.12.242.40".equals(weight.getSecondStation())||!"10.12.242.41".equals(weight.getSecondStation())){
                controlQueueService.save(cq);
            }
        }

    }

    @Transactional(readOnly = false)
    public void delete(Weight weight) {
        super.delete(weight);
    }

    //作废
    @Transactional(readOnly = false)
    public void cancel(Weight weight) {
        UpdateWeightRecord uwr = new UpdateWeightRecord();
        uwr.setOperation("2");
        uwr.setUpdateBy(UserUtils.getUser());
        uwr.setUpdateDate(new Date());
        uwr.setContent(weight.getRemarks());
        WeightRecord wr = new WeightRecord();
        wr.setWeighNo(weight.getWeighNo());
        uwr.setWeight(wr);
        uwr.setId(IdGen.uuid());
        updateWeightRecordMapper.insert(uwr);
        weightMapper.cancel(weight);
    }

    //	<!-- 根据车牌查询该车辆最近一次称重信息 -->
    public List<Weight> queryInfoByVehicleNo(String vehicleNo) {
        Weight w = new Weight();
        w.setVehicleNo(vehicleNo);
        return weightMapper.queryInfoByVehicleNo(w);
    }

    public List<Weight> queryInfoByLadleNo(String vehicleNo, String consignDept) {
        Weight w = new Weight();
        w.setVehicleNo(vehicleNo);
        w.setConsignDept(consignDept);
        return weightMapper.queryInfoByLadleNo(w);
    }

    //根据委托单号查询最近一条未完成称重信息
    public Weight queryInfoByConsignId(Weight weight) {

        return weightMapper.queryInfoByConsignId(weight);
    }

    //根据磅单号查询称重信息
    public Weight queryInfoByWeighNo(Weight weight) {

        return weightMapper.queryInfoByWeighNo(weight);
    }

    public Weight printbill(Weight weight) {

        return weightMapper.printbill(weight);
    }

    public Weight printweight(Weight weight) {

        return weightMapper.printweight(weight);
    }

    //录入打印信息
    @Transactional(readOnly = false)
    public void updateWeightByweighNo(Weight w) {
        weightMapper.updateWeightByweighNo(w);
    }

    ;

    public Consign queryByWeigh(String weighNo) {
        return weightMapper.queryByWeigh(weighNo);
    }

    public List<Weight> queryVehicle(String vehicleNo) {

        //查询车辆最近一次进入记录
        VehicleAccessRecord v = new VehicleAccessRecord();
        v.setVehicleNo(vehicleNo);
        Weight w = new Weight();
        w.setVehicleNo(vehicleNo);
        VehicleAccessRecord var = vehicleAccessRecordMapper.queryLatelyRecord(v);
        if (var != null) {
            w.setCreateTime(var.getIntoTime());
        }
        return weightMapper.queryVehicle(w);
    }

    public Page<Weight> findPages(Page<Weight> page, Weight weight) {
        return super.findPages(page, weight);
    }

    public List<Weight> queryInfoByVe(String vehicleNo) {
        return weightMapper.queryInfoByVe(vehicleNo);
    }

    //保存异常信息
    @Transactional(readOnly = false)
    public void saveAbnrType(Weight w) {
        weightMapper.saveAbnrType(w);
    }

    ;

    //保存接口返回
    @Transactional(readOnly = false)
    public void insertWeightResult(Weight w) {
        weightMapper.insertWeightResult(w);
    }

    ;

    //保存接口返回
    @Transactional(readOnly = false)
    public void updateDateType(Weight w) {
        weightMapper.updateDateType(w);
    }

    ;

    @Transactional(readOnly = false)
    public synchronized String getWeightno() {
        String currdate = DateUtils.getDate();
        List<Map<String, Object>> numlist = weightMapper.execSelectSql("select * from table_ser_num where tablename='weight' and serdate='" + currdate + "' for update");
        if (numlist == null || numlist.size() == 0) {
            weightMapper.execInsertSql("insert into table_ser_num values('" + IdGen.uuid() + "','weight','" + currdate + "','0001')");
            return "0001";
        } else {
            Map<String, Object> num = numlist.get(0);
            Integer int_sernum = new Integer(num.get("sernum").toString());
            int_sernum++;
            String sernum = "";
            if (int_sernum < 10) {
                sernum = "000" + int_sernum;
            } else if (int_sernum < 100) {
                sernum = "00" + int_sernum;
            } else if (int_sernum < 1000) {
                sernum = "0" + int_sernum;
            } else {
                sernum = int_sernum.toString();
            }
            weightMapper.execUpdateSql("update table_ser_num set sernum='" + sernum + "' where id='" + num.get("id").toString() + "'");
            return sernum;
        }
    }

    @Transactional(readOnly = false)
    public void updateweightFlag(Weight weight) {
        weightMapper.updateweightFlag(weight);
    }


    public List<Weight> queryByConsignId(String consignId) {
        return weightMapper.queryByConsignId(consignId);
    }

    @Transactional(readOnly = false)
    public void saveTarePic(Weight weight) {

        weightMapper.saveTarePic(weight);
    }

    @Transactional(readOnly = false)
    public void saveGrossPic(Weight weight) {
        weightMapper.saveGrossPic(weight);
    }

    public List<Weight> notSynchronized() {
        return weightMapper.notSynchronized();
    }

    @Transactional(readOnly = false)
    public void updateWeight(Weight weight) {
        weightMapper.updateWeight(weight);
    }

    //根据委托单号查询未完成锁皮的磅单详情
    public List<Weight> queryWeightByConsignId(String consignId) {
        Weight w = new Weight();
        w.setConsignId(consignId);
        return weightMapper.queryWeightByConsignId(w);
    }

    /**
     * 回退同时修改委托单和磅单状态
     *
     * @param consign
     * @param weight
     */
    @Transactional(readOnly = false)
    public void deleteQueue(Consign consign, Weight weight) {
        consignMapper.update(consign);
        //回退上传
        weightMapper.update(weight);

    }

    @Transactional(readOnly = false)
    public void savePrintRecord(PrintRecord pr) {
        printRecordMapper.insert(pr);

    }

    @Transactional(readOnly = false)
    public void changeWt(Weight weight) {
        UpdateWeightRecord uwr = new UpdateWeightRecord();
        uwr.setOperation("3");
        uwr.setUpdateBy(UserUtils.getUser());
        uwr.setUpdateDate(new Date());
        uwr.setContent("毛皮互换");
        WeightRecord wr = new WeightRecord();
        wr.setWeighNo(weight.getWeighNo());
        uwr.setWeight(wr);
        uwr.setId(IdGen.uuid());
        updateWeightRecordMapper.insert(uwr);
        weightMapper.update(weight);

    }

    //根据委托单查询未完成磅单
    public List<Weight> queryUnWeight(String consignId) {
        Weight w = new Weight();
        w.setConsignId(consignId);
        return weightMapper.queryUnWeight(w);
    }

    //根据委托单查询未完成磅单
    public List<Weight> queryUnWeightByVe(String vehicleNo) {
        Weight w = new Weight();
        w.setVehicleNo(vehicleNo);
        return weightMapper.queryUnWeight(w);
    }

}