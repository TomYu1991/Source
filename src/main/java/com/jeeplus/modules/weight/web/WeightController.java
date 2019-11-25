/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weight.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.PropertiesLoader;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.controlqueue.entity.ControlQueue;
import com.jeeplus.modules.controlqueue.service.ControlQueueService;
import com.jeeplus.modules.errorimp.entity.ErrorImp;
import com.jeeplus.modules.errorimp.service.ErrorImpService;
import com.jeeplus.modules.gatelog.entity.GateLog;
import com.jeeplus.modules.impwthistory.entity.ImpWtHistory;
import com.jeeplus.modules.impwthistory.service.ImpWtHistoryService;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.print.service.PrintService;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import com.jeeplus.modules.vehicleinfo.service.VehicleInfoService;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.service.WeightService;
import com.jeeplus.modules.weightrecord.entity.PrintRecord;
import com.jeeplus.modules.weightrecord.entity.UpdateWeightRecord;
import com.jeeplus.modules.weightrecord.entity.WeightRecord;
import com.jeeplus.modules.weightrecord.mapper.UpdateWeightRecordMapper;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 磅单管理Controller
 *
 * @author jeeplus
 * @version 2018-12-25
 */
@Controller
@RequestMapping(value = "${adminPath}/weight/weight")
public class WeightController extends BaseController {

    @Autowired
    private WeightService weightService;
    //委托单
    @Autowired
    private ConsignService consignService;
    @Autowired
    private UpdateWeightRecordMapper updateWeightRecordMapper;
    //集控室排队
    @Autowired
    private ControlQueueService controlQueueService;
    //工作站
    @Autowired
    private WorkStationService workStationService;

    //皮重历史
    @Autowired
    private ImpWtHistoryService impWtHistoryService;

    //车辆信息
    @Autowired
    private VehicleInfoService vehicleInfoService;
    @Autowired
    private ErrorImpService errorImpService;

    @ModelAttribute
    public Weight get(@RequestParam(required = false) String id) {

        Weight entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = weightService.get(id);
        }
        if (entity == null) {
            entity = new Weight();
        }
        return entity;
    }

    /**
     * 磅单管理列表页面
     */
    @RequiresPermissions("weight:weight:list")
    @RequestMapping(value = {"list", ""})
    public String list(Weight weight, Model model) {

        model.addAttribute("weight", weight);
        return "modules/weight/weightList";
    }

    /**
     * 进出厂车辆过磅页面
     */

    @RequestMapping(value = {"weightConsign"})
    public String weightConsign(Consign consign, Model model) {
        model.addAttribute("weight", consign);
        return "modules/weight/weightConsign";
    }

    /**
     * 内转车辆、轨道衡过磅页面
     */

    @RequestMapping(value = {"weightgdh"})
    public String weigh(Consign consign, Model model) {
        model.addAttribute("weight", consign);
        return "modules/weight/weightgdh";
    }


    /**
     * 磅单管理列表数据
     */
    @ResponseBody
    @RequiresPermissions("weight:weight:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(Weight weight, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Weight> page = new Page<>();
        if (weight.getSearchFlag() != null && "1".equals(weight.getSearchFlag())) {
            page = weightService.findPage(new Page<Weight>(request, response), weight);
        }
        return getBootstrapData(page);
    }

    /**
     * 增加，编辑磅单管理表单页面
     */
    @RequiresPermissions(value = {"weight:weight:view", "weight:weight:add", "weight:weight:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Weight weight, Model model) {
        model.addAttribute("weight", weight);
        return "modules/weight/weightForm";
    }

    /**
     * 查看磅单管理表单页面
     */
    @RequiresPermissions(value = {"weight:weight:view", "weight:weight:add", "weight:weight:edit"}, logical = Logical.OR)
    @RequestMapping(value = "checkform")
    public String checkform(Weight weight, Model model) {
        model.addAttribute("weight", weight);
        return "modules/weight/weightcheckForm";
    }

    /**
     * 进出厂车辆保存磅单（检斤:一次过磅）
     */
    @ResponseBody
    @RequestMapping(value = "saveweightConsign")
    public AjaxJson saveweightConsign(String id, String zl, HttpServletRequest request) throws Exception {
        AjaxJson j = new AjaxJson();
        System.out.println("后台开始保存");
        Weight weight = new Weight();
        try {
            WorkStation ws = workStationService.queryByStationIp(request.getRemoteAddr());
            //获取委托单信息
            Consign cs = new Consign();
            if (id != null && !"".equals(id)) {
                cs = consignService.get(id);
            }

            if (cs != null && cs.getConsignId() != null) {
                //非锁皮车辆第一次称重
                //委托单状态为 “未过磅”
                if (cs.getDefaultFlag() == null || (cs.getDefaultFlag() != null && !"1".equals(cs.getDefaultFlag()))) {
                    System.out.println("一次非锁皮称重");
                    //查询当前委托单状态是否为一次过磅
                    Consign cc = consignService.queryInfoByConsignId(cs.getConsignId());
                    if (cc.getWeightState() != null && "1".equals(cc.getWeightState())) {
                        System.out.println("当前委托单有未完成的磅单");
                        j.setSuccess(false);
                        return j;
                    }

                    weight.setProdCode(cs.getProdCode());
                    weight.setProdCname(cs.getProdCname());
                    weight.setSgCode(cs.getSgCode());
                    weight.setSgSign(cs.getSgSign());
                    weight.setField1(cs.getField1());
                    weight.setAbnrType("0");
                    if (cs.getVehicleNo() != null && !"".equals(cs.getVehicleNo().trim())) {
                        weight.setVehicleNo(cs.getVehicleNo());
                    } else {
                        weight.setVehicleNo(cs.getLadleNo());
                    }
                    weight.setSupplierName(cs.getSupplierName());
                    weight.setConsignId(cs.getConsignId());
                    weight.setBillNo(cs.getBillNo());
                    weight.setBlastFurnaceNo(cs.getBlastFurnaceNo());
                    weight.setConsigneUser(cs.getConsigneUser());
                    weight.setCreateTime(new Date());
                    weight.setDefaultFlag("0");//1锁车，0空车
                    weight.setEquipNum(cs.getEquipNum());
                    weight.setTaretime(new Date());//二次过磅时间
                    weight.setGrosstime(new Date());//一次过磅时间
                    //如果称重方式是先皮后毛
                    if ("01".equals(cs.getWeightType()) || "06".equals(cs.getWeightType()) || "07".equals(cs.getWeightType()) || "08".equals(cs.getWeightType())) {
                        weight.setImpWt(zl);//皮重
                        weight.setMatGrossWt("0.00");//毛重
                        weight.setMatWt("0.00");//净重
                    } else {
                        weight.setMatGrossWt(zl);//毛重
                        weight.setImpWt("0.00");//皮重
                        weight.setMatWt("0.00");//净重
                    }
                    weight.setStatus("0");
                    weight.setLadleNo(cs.getLadleNo());
                    weight.setMatSpecDesc(cs.getMatSpecDesc());
                    weight.setWeightType(cs.getWeightType());
                    weight.setRfidNo(cs.getRfidNo());
                    weight.setFistStation(request.getRemoteAddr());
                    if (ws != null) {
                        weight.setWorkStation(ws.getWorkStation());
                    }
                    weight.setDataType("0");
                    weight.setWeightFlag("0");

                    weightService.saveFistWeight(weight, cs);
                    InterfaceTest ift = new InterfaceTest();
                    ift.queryInterFaceList(weight, request);
                    j.setData(weight);
                    j.setSuccess(true);
                    j.setObject(weight.getWeighNo());
                    j.setMsg("保存票据成功");
                    return j;
                }

                if (cs.getDefaultFlag() != null && "1".equals(cs.getDefaultFlag())) {
                    System.out.println("一次锁皮保存皮重");
                    //判断皮重是否符合要求
                    ImpWtHistory impwh = impWtHistoryService.queryImpWtHistoryAvg(cs.getVehicleNo());

                    if (impwh != null && impwh.getImpAvg() != null) {
                        Double dqzl = Double.parseDouble(zl);
                        Double lspz = Double.parseDouble(impwh.getImpAvg());
                        if (Math.abs(dqzl - lspz) > 200) {
                            //保存异常历史皮重
                            ErrorImp ei = new ErrorImp();
                            ei.setConsignId(cs.getConsignId());
                            ei.setImpWt(zl);
                            ei.setOldImp(impwh.getImpAvg());
                            ei.setStationIp(ws.getStationIp());
                            ei.setVehicleNo(cs.getVehicleNo());
                            ei.setProdCname(cs.getProdCname());
                            ei.setCreateDate(new Date());
                            errorImpService.save(ei);
                            //提示司磅员（暂缓）
                            ControlQueue cq = new ControlQueue();
                            cq.setContent("皮重异常");
                            cq.setState("1");
                            cq.setWeightId(weight.getWeighNo());
                            cq.setVehicleNo(weight.getVehicleNo());
                            cq.setConsignId(weight.getConsignId());
                            cq.setStationId(ws.getId());
                            cq.setRemarks("2");
                            controlQueueService.save(cq);
                        }
                    }

                    //查询当前委托单状态是否为一次过磅
                    List<Consign> cc = consignService.showweights(cs);
                    if (cc.size() > 0) {
                        System.out.println("当前委托单有未完成的磅单");
                        j.setSuccess(false);
                        return j;
                    }

                    ImpWtHistory iwh = new ImpWtHistory();
                    iwh.setCreateDate(new Date());
                    iwh.setVehicleNo(cs.getVehicleNo());
                    iwh.setImpWt(zl);
                    iwh.setConsignId(cs.getConsignId());
                    iwh.setDelFlag("0");
                    iwh.setStartTime(cs.getStartTime());
                    iwh.setEndTime(cs.getEndTime());
                    iwh.setRemarks(request.getRemoteAddr());
                    impWtHistoryService.save(iwh);

                    Weight w = new Weight();


                    if (cs.getVehicleNo() != null && !"".equals(cs.getVehicleNo())) {
                        w.setVehicleNo(cs.getVehicleNo());
                    } else {
                        w.setVehicleNo(cs.getLadleNo());
                    }
                    weight.setField1(cs.getField1());
                    w.setEquipNum(cs.getEquipNum());
                    w.setSupplierName(cs.getSupplierName());
                    w.setConsigneUser(cs.getConsigneUser());
                    w.setConsignId(cs.getConsignId());
                    w.setTaretime(new Date());
                    w.setGrosstime(new Date());
                    w.setWeightType(cs.getWeightType());
                    w.setFistStation(request.getRemoteAddr());
                    w.setCreateTime(new Date());
                    w.setImpWt(zl);
                    w.setStatus("0");
                    w.setProdCname(cs.getProdCname());
                    w.setWeightFlag("0");
                    w.setAbnrType("5");//锁皮皮重
                    w.setDefaultFlag("1");
                    if (ws != null) {
                        w.setWorkStation(ws.getWorkStation());
                    }
                    w.setDataType("0");

                    weightService.saveFistWeight(w, cs);
                    j.setData(w);
                    j.setObject(w.getWeighNo());
                    j.setSuccess(true);
                    return j;

                }
            } else {
                if ("10.12.242.40".equals(request.getRemoteAddr()) || "10.12.242.41".equals(request.getRemoteAddr())) {
                    Consign c = new Consign();
                    weight.setConsignId("");
                    weight.setAbnrType("0");
                    weight.setCreateTime(new Date());
                    weight.setDefaultFlag("0");//1锁车，0空车
                    weight.setTaretime(new Date());//二次过磅时间
                    weight.setGrosstime(new Date());//一次过磅时间
                    weight.setMatGrossWt(zl);//毛重
                    weight.setImpWt("0.00");//皮重
                    weight.setMatWt("0.00");//净重

                    weight.setFistStation(request.getRemoteAddr());
                    if (ws != null) {
                        weight.setWorkStation(ws.getWorkStation());
                    }
                    weight.setWeightType("03");
                    weight.setStatus("1");
                    weight.setRemarks("轨道衡强制保存重量");
                    weight.setDataType("0");
                    weight.setWeightFlag("0");
                    weightService.saveFistWeight(weight, c);
                    j.setObject(weight.getWeighNo());
                    j.setSuccess(true);
                    j.setData(weight);
                    return j;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            return j;

        }
        return j;

    }

    /**
     * 进出厂车辆保存磅单（检斤:二次过磅）
     */
    @ResponseBody
    @RequestMapping(value = "updateweightConsign")
    public AjaxJson updateweightConsign(String id, String zl, String billpic, HttpServletRequest request) throws Exception {
        AjaxJson j = new AjaxJson();
        System.out.println("后台开始保存");
        Weight weight = new Weight();
        WorkStation ws = workStationService.queryByStationIp(request.getRemoteAddr());
        //获取委托单信息
        Consign cs = consignService.get(id);
        if (cs != null) {
            if (cs.getDefaultFlag() != null && "1".equals(cs.getDefaultFlag())) {
                //根据委托查询最近一条有效期内的历史皮重
                ImpWtHistory ih = impWtHistoryService.queryImpWtHistory(cs.getConsignId());
                if (ih != null) {

                    Weight w = new Weight();
                    w.setConsignId(cs.getConsignId());
                    ControlQueue cq = new ControlQueue();
                    //  查有没有未完成的记录，如果有就更新

                    weight.setProdCode(cs.getProdCode());
                    weight.setProdCname(cs.getProdCname());
                    weight.setSgCode(cs.getSgCode());
                    weight.setSgSign(cs.getSgSign());
                    weight.setAbnrType("0");
                    weight.setWeightFlag("1");
                    if (cs.getVehicleNo() != null && !"".equals(cs.getVehicleNo())) {
                        weight.setVehicleNo(cs.getVehicleNo());
                    } else {
                        weight.setVehicleNo(cs.getLadleNo());
                    }
                    weight.setSupplierName(cs.getSupplierName());
                    weight.setConsignId(cs.getConsignId());
                    weight.setStatus("0");
                    weight.setBillNo(cs.getBillNo());
                    weight.setBlastFurnaceNo(cs.getBlastFurnaceNo());
                    weight.setConsigneUser(cs.getConsigneUser());
                    weight.setCreateTime(new Date());
                    weight.setCustomerCode("");
                    weight.setDealPersonNo(cs.getDealPersonNo());
                    weight.setDefaultFlag("1");
                    weight.setEquipNum(cs.getEquipNum());
                    weight.setMatGrossWt(zl);//毛重
                    weight.setTaretime(new Date());
                    weight.setImpWt(ih.getImpWt());//皮重
                    weight.setGrosstime(ih.getCreateDate());
                    weight.setTareHeadPic(ih.getTareHeadPic());
                    weight.setTareTailPic(ih.getTareTailPic());
                    weight.setTareTopPic(ih.getTareTopPic());
                    weight.setFistStation(ih.getRemarks());
                    //计算净重
                    Double m = Double.parseDouble(zl);
                    Double jz = m - Double.parseDouble(ih.getImpWt());
                    weight.setMatWt(String.valueOf(jz));
                    if ("0.0".equals(weight.getMatWt())) {
                        j.setSuccess(false);
                        j.setMsg("净重为0，异常请检查");
                        return j;
                    }
                    //修改剩余委托总量
                    if (cs.getTotalWt() != null && !"0.00".equals(cs.getTotalWt()) && !"".equals(cs.getTotalWt())) {
                        Double zz = Double.parseDouble(cs.getSurplusWt());
                        Double syzz = zz + jz;
                        cs.setSurplusWt(syzz.toString());
                        System.out.println("修改剩余委托总量" + syzz.toString());
                        consignService.updatesurplusWt(cs);
                    }

                    weight.setLadleNo(cs.getLadleNo());
                    weight.setMatSpecDesc(cs.getMatSpecDesc());
                    weight.setPonoLotNo("");
                    weight.setField1(cs.getField1());
                    weight.setWeightType(cs.getWeightType());
                    weight.setRfidNo(cs.getRfidNo());
                    weight.setSecondStation(request.getRemoteAddr());
                    weight.setUpdater(UserUtils.getUser().getLoginName());
                    weight.setUpdatetime(new Date());
                    if (billpic != null) {
                        String bill = billpic.substring(billpic.lastIndexOf("/") + 1);
                        String paths = request.getRemoteAddr();
                        String pa = "http://" + paths + ":8090/images/";
                        weight.setBillPic(pa + bill);
                        cq.setBillPic(pa + bill);
                    }

                    weight.setDataType("0");
                    //申请打印磅单
                    cq.setContent("请求打印磅单");
                    cq.setState("1");
                    cq.setVehicleNo(weight.getVehicleNo());
                    cq.setConsignId(weight.getConsignId());
                    cq.setRfidNo(weight.getRfidNo());
                    cq.setWeightId(weight.getWeighNo());
                    if (ws != null) {
                        cq.setStationId(ws.getId());
                    }
                    //保存磅单
                    weightService.saveSecondWeight(weight, cq, cs);//保存
                    j.setData(weight);
                    j.setObject(weight.getWeighNo());
                    j.setSuccess(true);
                    j.setMsg("保存磅单成功");
                    return j;
                } else {
                    j.setSuccess(false);
                    j.setMsg("无历史皮重");
                    return j;
                }
            }

            //非锁皮车辆第二次称重
            if (null == cs.getDefaultFlag() || (cs.getDefaultFlag() != null && !"1".equals(cs.getDefaultFlag()))) {
                Weight wc = new Weight();
                wc.setConsignId(cs.getConsignId());
                weight = weightService.queryInfoByConsignId(wc);
                if (weight != null && weight.getWeightFlag() != null && "0".equals(weight.getWeightFlag())) {
                    //判断一次过磅与二次过磅时间差是否超过六个小时
                    long ftime = weight.getGrosstime().getTime();
                    long stime = System.currentTimeMillis();
                    long time = (stime - ftime) / 1000 / 60 / 60;
                    if (time >= 6) {
                        //提示司磅员
                        ControlQueue cq = new ControlQueue();
                        cq.setContent("两次过磅时间差超过六小时");
                        cq.setState("1");
                        cq.setVehicleNo(weight.getVehicleNo());
                        cq.setConsignId(weight.getConsignId());
                        cq.setWeightId(weight.getWeighNo());
                        if (ws != null) {
                            cq.setStationId(ws.getId());
                        }
                        cq.setRemarks("1");
                        controlQueueService.save(cq);
                    }
                    Consign cons = consignService.queryInfoByConsignId(cs.getConsignId());
                    weight.setConsigneUser(cons.getConsigneUser());
                    weight.setSupplierName(cons.getSupplierName());
                    weight.setField1(cons.getField1());
                    weight.setTaretime(new Date());//二次过磅时间
                    //如果称重方式是先皮后毛
                    if ("01".equals(cs.getWeightType()) || "06".equals(cs.getWeightType()) || "07".equals(cs.getWeightType()) || "08".equals(cs.getWeightType())) {
                        //计算净重
                        Double p = Double.parseDouble(weight.getImpWt());
                        Double m = Double.parseDouble(zl);
                        Double jz = m - p;
                        weight.setMatWt(String.valueOf(jz));
                        //毛重
                        weight.setMatGrossWt(zl);

                    } else {
                        //计算净重
                        Double m = Double.parseDouble(weight.getMatGrossWt());
                        Double p = Double.parseDouble(zl);
                        Double jz = m - p;
                        weight.setMatWt(String.valueOf(jz));
                        //皮重
                        weight.setImpWt(zl);

                    }
                    if ("0.0".equals(weight.getMatWt())) {
                        j.setSuccess(false);
                        j.setMsg("净重为0，异常请检查");
                        return j;
                    }
                    //修改剩余委托总量
                    if (cs.getTotalWt() != null && !"0.00".equals(cs.getTotalWt()) && !"".equals(cs.getTotalWt())) {
                        Double zz = Double.parseDouble(cs.getSurplusWt());
                        Double jz = Double.parseDouble(weight.getMatWt());
                        Double syzz = zz + jz;
                        cs.setSurplusWt(syzz.toString());
                        System.out.println("修改剩余委托总量" + syzz.toString());
                        consignService.updatesurplusWt(cs);
                    }
                    weight.setUpdater(UserUtils.getUser().getLoginName());
                    weight.setUpdatetime(new Date());
                    weight.setSecondStation(request.getRemoteAddr());
                    weight.setPrintNum("0");
                    weight.setAbnrType("0");
                    //如果净重小于0.磅单标记为异常
                    if (0.00 > Double.parseDouble(weight.getMatWt())) {
                        weight.setAbnrType("2");//毛皮反转
                        weightService.saveAbnrType(weight);
                    }

                    weightService.queryByWeigh(weight.getWeighNo());
                    //申请打印磅单
                    ControlQueue cq = new ControlQueue();
                    cq.setContent("请求打印磅单");
                    cq.setState("1");
                    cq.setVehicleNo(weight.getVehicleNo());
                    cq.setConsignId(weight.getConsignId());
                    cq.setWeightId(weight.getWeighNo());
                    if (ws != null) {
                        cq.setStationId(ws.getId());
                    }


                    if (billpic != null) {
                        String bill = billpic.substring(billpic.lastIndexOf("/") + 1);
                        String paths = request.getRemoteAddr();
                        String pa = "http://" + paths + ":8090/images/";
                        System.out.println(pa + bill);
                        weight.setBillPic(pa + bill);
                        cq.setBillPic(pa + bill);
                    }
                    weight.setDataType("0");
                    weight.setWeightFlag("1");
                    weightService.saveSecondWeight(weight, cq, cs);
                    j.setData(weight);
                    j.setObject(weight.getWeighNo());
                    j.setSuccess(true);
                    j.setMsg("保存磅单成功");
                    return j;
                } else {
                    j.setSuccess(false);
                    j.setMsg("委托单状态异常");
                    return j;
                }
            }
        }
        j.setSuccess(false);
        j.setMsg("无委托单");
        return j;
    }

    /**
     * 保存磅单管理
     */
    @ResponseBody
    @RequiresPermissions(value = {"weight:weight:add", "weight:weight:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(Weight weight, Model model, HttpServletRequest request) throws Exception {
        AjaxJson j = new AjaxJson();
        GateLog gl = new GateLog();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(weight);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }

        weightService.save(weight);//保存
        //同步数据到产供销系统
        System.out.println("上传磅单");
        InterfaceTest i = new InterfaceTest();
        i.queryInterFaceList(weight, request);
        j.setSuccess(true);
        j.setMsg("保存磅单管理成功");
        return j;
    }


    /**
     * 作废磅单管理
     */
    @ResponseBody
    @RequiresPermissions("weight:weight:cancel")
    @RequestMapping(value = "cancel")
    public AjaxJson cancel(Weight weight, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        weight.setStatus("2");
        weight.setUpdatetime(new Date());
        weight.setUpdater(UserUtils.getUser().getLoginName());
        if ("1".equals(weight.getDefaultFlag())) {
            weightService.cancel(weight);
        } else {
            //回复委托单过磅状态
            Consign cn = consignService.queryInfoByConsignId(weight.getConsignId());
            if (cn != null) {
                cn.setWeightState("0");
                consignService.update(cn);
            }
            weightService.cancel(weight);
        }

        ControlQueue cq = new ControlQueue();
        cq.setWeightId(weight.getWeighNo());
        List<ControlQueue> lc = controlQueueService.findList(cq);
        if (lc.size() > 0) {
            cq.setState("3");
            controlQueueService.updateStateByWeighId(cq);
        }
        System.out.println("上传磅单");
        InterfaceTest inf = new InterfaceTest();
        inf.queryInterFaceList(weight, request);

        j.setSuccess(true);
        j.setMsg("作废磅单成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("weight:weight:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Weight weight, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String param = URLDecoder.decode(request.getParameter("data"), "UTF-8");
            JSONObject jo = JSONObject.fromObject(param);

            if (jo.get("begintaretime") == null || "".equals(jo.get("begintaretime"))) {
                jo.remove("begintaretime");
            }
            if (jo.get("endtaretime") == null || "".equals(jo.get("endtaretime"))) {
                jo.remove("endtaretime");
            }
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setRootClass(Weight.class);
            String[] dateFamates = new String[]{"yyyy-MM-dd HH:mm:ss"};
            JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

            weight = (Weight) JSONObject.toBean(jo, Weight.class);

            String fileName = "磅单管理" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<Weight> page = weightService.findPage(new Page<Weight>(request, response, -1), weight);
            new ExportExcel("磅单管理", Weight.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出磅单管理记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("weight:weight:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<Weight> list = ei.getDataList(Weight.class);
            for (Weight weight : list) {
                try {
                    weightService.save(weight);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条磅单管理记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条磅单管理记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入磅单管理失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入磅单管理数据模板
     */
    @ResponseBody
    @RequiresPermissions("weight:weight:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "磅单管理数据导入模板.xlsx";
            List<Weight> list = Lists.newArrayList();
            new ExportExcel("磅单管理数据", Weight.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 打印票据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dyPrint")
    public AjaxJson dyPrint(String weighNo, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        //查询磅单相关信息
        Weight ww = new Weight();
        ww.setWeighNo(weighNo);
        Weight w = weightService.printbill(ww);

        try {
            j.setData(w);
            j.setSuccess(true);
            j.setMsg("打印票据成功!");
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("打印票据失败!");
        }
        return j;
    }

    /**
     * 打印磅单
     *
     * @param weighNo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dyWeight")
    public AjaxJson dyWeight(String weighNo, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        try {
            ControlQueue cq = new ControlQueue();
            cq.setWeightId(weighNo);
            cq.setState("3");
            controlQueueService.updateStateByWeight(cq);
            //查询磅单相关信息
            Weight we = new Weight();
            we.setWeighNo(weighNo);
            Weight w = weightService.printweight(we);

            if (w != null && !"".equals(w)) {

                if ("3".equals(w.getPrintNum())) {
                    j.setSuccess(false);
                    j.setMsg("超出打印次数!");
                    return j;
                }
            }

            w.setDealPersonNo(UserUtils.getUser().getLoginName());//处理人员工号
            weightService.updateweightFlag(w);
            j.setData(w);
            j.setSuccess(true);
            j.setMsg("打印磅单成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return j;
    }

    /**
     * 保存日志并修改磅单打印记录
     */
    @ResponseBody
    @RequestMapping(value = {"changePrint"})
    public AjaxJson changePrint(String weighNo, String ip, String flag, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        PrintRecord pr = new PrintRecord();
        pr.setId(IdGen.uuid());
        try {
            //查询磅单相关信息
            Weight we = new Weight();
            we.setWeighNo(weighNo);
            Weight w = weightService.queryInfoByWeighNo(we);

            System.out.println("ip==============" + ip);
            if (ip == null || "".equals(ip) || "undefined".equals(ip)) {
                ip = request.getRemoteAddr();
            }
            WorkStation ws = workStationService.queryByStationIp(ip);
            if (w != null) {
                pr.setVehicleNo(w.getVehicleNo());
                pr.setCreateBy(UserUtils.getUser());
                pr.setCreateDate(new Date());
                if (ws != null) {
                    if ("1".equals(flag)) {
                        if ("1".equals(w.getWeightFlag()) || "2".equals(w.getWeightFlag())) {
                            pr.setOperation("打印磅单");
                            if (ws.getWorkStation() != null && !"".equals(ws.getWorkStation())) {
                                pr.setStationIp(ws.getWorkStation());
                            }
                            //保存打印次数
                            saveNum(w);
                        } else {
                            pr.setOperation("打印票据");
                            if (ws.getWorkStation() != null && !"".equals(ws.getWorkStation())) {
                                pr.setStationIp(ws.getWorkStation());
                            }
                        }

                    } else {
                        if ("true".equals(flag)) {
                            pr.setOperation("补打票据");
                            if (ws.getWorkStation() != null && !"".equals(ws.getWorkStation())) {
                                pr.setStationIp(ws.getWorkStation());
                            }
                        }
                        if ("false".equals(flag)) {
                            pr.setOperation("补打磅单");
                            if (ws.getWorkStation() != null && !"".equals(ws.getWorkStation())) {
                                pr.setStationIp(ws.getWorkStation());
                            }
                            //保存打印次数
                            saveNum(w);
                        }
                    }
                }
                WeightRecord wr = new WeightRecord();
                wr.setWeighNo(weighNo);

                pr.setWeight(wr);
                weightService.savePrintRecord(pr);
                j.setSuccess(true);
            }
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 打印次数
     *
     * @param w
     */
    public void saveNum(Weight w) {
        if (null == w.getPrintNum() || "".equals(w.getPrintNum().trim())) {
            w.setPrintNum("0");
        }
        int i = Integer.parseInt(w.getPrintNum());
        int num = i + 1;
        w.setPrinter(UserUtils.getUser().getLoginName());
        w.setPrintNum(String.valueOf(num));
        w.setPrintTime(new Date());
        weightService.updateWeightByweighNo(w);
    }


    /**
     * 通过磅单号获取委托单相关信息
     */
    @ResponseBody
    @RequestMapping(value = "getConsignByWeigh")
    public AjaxJson getConsignByWeigh(String weighNo) {
        AjaxJson j = new AjaxJson();
        try {
            Consign c = weightService.queryByWeigh(weighNo);
            j.setData(c);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 交换毛重皮重，重新计算净重
     */
    @ResponseBody
    @RequestMapping(value = "change")
    public AjaxJson change(String id, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            Weight c = weightService.get(id);
            //皮重净重互换
            String m = c.getMatGrossWt();
            String p = c.getImpWt();
            c.setMatGrossWt(p);
            c.setImpWt(m);
            Double pz = Double.parseDouble(m);
            Double mz = Double.parseDouble(p);
            Double jz = mz - pz;
            c.setMatWt(String.valueOf(jz));
            //抓拍、时间互换
            String mct = c.getTareHeadPic();
            String mcw = c.getTareTailPic();
            String mcd = c.getTareTopPic();
            Date msj = c.getTaretime();
            String pct = c.getGrossHeadPic();
            String pcw = c.getGrossTailPic();
            String pcd = c.getGrossTopPic();
            Date psj = c.getGrosstime();

            c.setTaretime(psj);
            c.setTareTopPic(pcd);
            c.setTareTailPic(pcw);
            c.setTareHeadPic(pct);
            c.setGrosstime(msj);
            c.setGrossTopPic(mcd);
            c.setGrossTailPic(mcw);
            c.setTareHeadPic(mct);
            weightService.changeWt(c);

            InterfaceTest ift = new InterfaceTest();
            ift.queryInterFaceList(c, request);
            System.out.println("上传磅单");
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 同步数据
     */
    @ResponseBody
    @RequestMapping(value = "cheshijk")
    public AjaxJson cheshijk(String weighNos, HttpServletRequest request) {

        AjaxJson j = new AjaxJson();
        String weighNoArray[] = weighNos.split(",");
        try {
            for (String weighNo : weighNoArray) {
                Weight w = new Weight();
                w.setWeighNo(weighNo);
                Weight we = weightService.queryInfoByWeighNo(w);
                if (we.getAbnrType() != null && !"5".equals(we.getAbnrType())) {
                    InterfaceTest test = new InterfaceTest();
                    test.queryInterFaceList(we, request);
                    System.out.println("上传磅单");
                    j.setSuccess(true);

                } else {
                    System.out.println("上传磅单");
                    j.setSuccess(false);
                    j.setMsg("锁皮皮重，不允许同步的磅单！");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j;
    }

    /**
     * 保存抓拍图片
     */
    @ResponseBody
    @RequestMapping(value = "saveweightpic")
    public AjaxJson saveweightpic(String weighNo, String ct, String cw, String cd, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        PropertiesLoader pro = new PropertiesLoader("/properties/config.properties");

        String zhuapaiPath = pro.getProperty("zhuapaiPath") + "";

        String http = "http://";
        String hostip = pro.getProperty("hostip") + "";
        String hostport = pro.getProperty("hostport") + "";
        String path = http + hostip + ":" + hostport + zhuapaiPath;

        try {
            //存放抓拍图片文件路径
            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy/MM/dd/");
            String timefile = sdfs.format(new Date());

            Weight w = new Weight();
            w.setWeighNo(weighNo);
            Weight weight = weightService.queryInfoByWeighNo(w);

            if (null != weight.getMatWt() && !"0.00".equals(weight.getMatWt()) && !"".equals(weight.getMatWt())) {
                System.out.println(weight.getMatWt());
                System.out.println("保存二次过磅图片");
                weight.setGrossTopPic(path + timefile + ct);
                weight.setGrossTailPic(path + timefile + cw);
                weight.setGrossHeadPic(path + timefile + cd);
                weightService.saveGrossPic(weight);
            } else {
                if (weight.getDefaultFlag() != null && "1".equals(weight.getDefaultFlag())) {
                    ImpWtHistory iwh = impWtHistoryService.queryImpWtHistory(weight.getConsignId());
                    iwh.setTareHeadPic(path + timefile + ct);
                    iwh.setTareTailPic(path + timefile + cw);
                    iwh.setTareTopPic(path + timefile + cd);
                    impWtHistoryService.updatePic(iwh);
                    weight.setTareHeadPic(path + timefile + ct);
                    weight.setTareTailPic(path + timefile + cw);
                    weight.setTareTopPic(path + timefile + cd);
                    weightService.saveTarePic(weight);
                } else {
                    weight.setTareHeadPic(path + timefile + ct);
                    weight.setTareTailPic(path + timefile + cw);
                    weight.setTareTopPic(path + timefile + cd);
                    weightService.saveTarePic(weight);

                }
                System.out.println("保存一次过磅图片");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return j;
    }


    /**
     * 作废磅单页面
     */
    @RequestMapping(value = "weightCancel")
    public String peccancyForm(Weight weight, Model model) {
        weight = weightService.queryInfoByWeighNo(weight);
        model.addAttribute("weight", weight);
        return "modules/weight/weightCancel";
    }

    /**
     * 磅单管理回退
     */
    @ResponseBody
    @RequestMapping(value = "back")
    public AjaxJson back(String weighNo) {
        AjaxJson j = new AjaxJson();

        Weight we = new Weight();
        we.setWeighNo(weighNo);
        Weight wt = weightService.queryInfoByWeighNo(we);
        if (wt != null) {
            List<Consign> c = consignService.queryByConsignId(wt.getConsignId());

            if (c.size() > 0) {
                if ("1".equals(wt.getDefaultFlag())) {
                    j.setSuccess(false);
                    j.setMsg("该磅单是锁皮类型，如需回退请作废！");
                    return j;
                } else {
                    c.get(0).setWeightState("1");
                    wt.setWeightFlag("0");
                    wt.setMatWt("0.00");
                    //如果称重方式是先皮后毛
                    if ("01".equals(c.get(0).getWeightType()) || "06".equals(c.get(0).getWeightType()) || "07".equals(c.get(0).getWeightType()) || "08".equals(c.get(0).getWeightType())) {
                        wt.setMatGrossWt("0.00");
                    } else {
                        wt.setImpWt("0.00");
                    }
                    wt.setUpdatetime(new Date());
                    wt.setUpdater(UserUtils.getUser().getLoginName());
                    wt.setRemarks("二次过磅后回退！");
                    weightService.deleteQueue(c.get(0), wt);
                    j.setSuccess(true);
                    j.setMsg("回退成功！");
                }
            }
        }
        return j;
    }

    /**
     * 磅单
     * 上传
     */
    @ResponseBody
    @RequestMapping(value = "uploadWeight")
    public AjaxJson uploadWeight(String weighNo, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        //修改排队状态
        ControlQueue cq = new ControlQueue();
        cq.setWeightId(weighNo);
        cq.setState("3");
        controlQueueService.updateStateByWeight(cq);
        //磅单上传
        Weight we = new Weight();
        InterfaceTest ift = new InterfaceTest();

        we.setWeighNo(weighNo);
        Weight wt = weightService.queryInfoByWeighNo(we);
        if (wt != null) {
            j = ift.queryInterFaceList(wt, request);
        }
        Consign cs = consignService.queryInfoByConsignId(wt.getConsignId());
        if (cs != null && cs.getPonderFlag() != null && "1".equals(cs.getPonderFlag())) {
            consignService.cancel(cs);
        }
        return j;
    }


    /**
     * 二维码解析
     */
    @ResponseBody
    @RequestMapping(value = "analysis")
    public AjaxJson analysis(String ewm) {
        AjaxJson j = new AjaxJson();
        try {
            System.out.println("二维码" + ewm);
            String str = hexStr2Str(ewm);
            //根据委托单号查询车牌号
          /*  if (str != null) {
                j.setObject(str);
                ConsignTmp c = consignService.queryInfoByConsignId(str);
                if (c != null && c.getVehicleNo() != null) {
                    j.setErrorCode(c.getVehicleNo());
                    j.setMsg(c.getVehicleNo());
                } else {
                    j.setSuccess(false);
                    j.setMsg("委托单已过期！");
                    return j;
                }
            }*/
            j.setObject(str);
            System.out.println("委托单号" + str);
            j.setSuccess(true);

        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setMsg("系统异常，请联系司磅员！");
            return j;
        }
        return j;
    }

    public String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    /**
     * 轨道衡过磅
     */
    @ResponseBody
    @RequestMapping(value = {"checkConsignGdh"})
    public AjaxJson queryConsignGdh(Consign consign, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            j.setObject(request.getRemoteAddr());
            //根据设备编号查询对应工作站
            WorkStation ws = workStationService.queryByStationIp(request.getRemoteAddr());
            //根据车牌号或RFID查询对应委托单
            if (consign.getRfidNo() != null && !"undefined".equals(consign.getRfidNo())
                    && !"".equals(consign.getRfidNo())) {
                List<VehicleInfo> v = vehicleInfoService.getVehicleNoByRfid(consign.getRfidNo());
                List<VehicleInfo> vs = vehicleInfoService.getVehicleNoBySrfid(consign.getRfidNo());
                if (v != null && v.size() > 0 && !"".equals(v.get(0).getVehicleNo())) {
                    consign.setLadleNo(v.get(0).getVehicleNo());
                    consign.setConsignDept(v.get(0).getGroupCode());
                }
                if (vs != null && vs.size() > 0 && !"".equals(vs.get(0).getVehicleNo())) {
                    consign.setLadleNo(vs.get(0).getVehicleNo());
                    consign.setConsignDept(vs.get(0).getGroupCode());
                }
            }
            if (consign.getLadleNo() != null && !"".equals(consign.getLadleNo())) {
                j.setErrorCode(consign.getLadleNo());
                //查询当前铁水罐有效期内最近一条委托单
                List<Consign> consign1 = consignService.findValidityByLableNo(consign);
                //过二磅判断是否收货
                if (consign1 != null && consign1.size() > 0) {
                    if (consign1.get(0).getWeightState() != null && "1".equals(consign1.get(0).getWeightState())) {
                        if (consign1.get(0).getField1() != null && "1".equals(consign1.get(0).getField1())) {
                            List<Weight> ww = weightService.queryUnWeight(consign1.get(0).getConsignId());
                            if (ww.size() > 0 && ww.get(0).getAffirmFlag() == null || !"1".equals(ww.get(0).getAffirmFlag())) {
                                j.setMsg("现场未确认收货!请返回现场");
                                j.setSuccess(false);
                                return j;
                            }
                        }
                    } else {
                        Date date = new Date();
                        if (consign1.get(0).getStartTime() != null && consign1.get(0).getEndTime() != null
                                && consign1.get(0).getStartTime().getTime() < date.getTime()
                                && consign1.get(0).getEndTime().getTime() > date.getTime()) {
                            List<Weight> lw = weightService.queryUnWeight(consign1.get(0).getConsignId());
                            if (lw != null && lw.size() > 0) {
                                j.setMsg("有未完成磅单!");
                                j.setSuccess(false);
                                return j;
                            }
                        }
                    }
                    for (Consign c : consign1) {
                        if (c.getWeightState() != null && "1".equals(c.getWeightState())) {
                            j.setData(c);
                            j.setSuccess(true);
                            return j;
                        }
                    }
                    j.setData(consign1.get(0));
                    j.setSuccess(true);
                    return j;
                } else {
                    j.setMsg("无有效委托单");
                    j.setSuccess(false);
                    return j;
                }
            }
        } catch (Exception e) {
            j.setSuccess(false);
            e.printStackTrace();
        }
        return j;
    }

    /**
     * 轨道衡合并数据
     *
     * @param weighNos
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mergeWeight")
    public AjaxJson mergeWeight(String weighNos, HttpServletRequest request) {

        AjaxJson j = new AjaxJson();
        Weight weight1 = new Weight();
        Weight weight2 = new Weight();
        String weighNoArray[] = weighNos.split(",");
        if (weighNoArray.length != 2) {
            j.setSuccess(false);
            j.setMsg("选中磅单数量不为2，请检查！");
            return j;
        }
        try {
            for (String weighNo : weighNoArray) {
                Weight w = new Weight();
                w.setWeighNo(weighNo);
                Weight we = weightService.queryInfoByWeighNo(w);
                if (we.getWeightType() != null && !"03".equals(we.getWeightType())) {
                    j.setSuccess(false);
                    j.setMsg("选中磅单称重类型不是轨道过磅，请重新选择！");
                    return j;
                }
                if (we.getWeightFlag() != null && !"0".equals(we.getWeightFlag())) {
                    j.setSuccess(false);
                    j.setMsg("选中磅单不是一次过磅状态，请重新选择！");
                    return j;
                }
                if (weight1 != null && weight1.getWeighNo() != null && !"".equals(weight1.getWeighNo())) {
                    weight2 = we;
                } else {
                    weight1 = we;
                }
            }
            Double a = Double.parseDouble(weight1.getMatGrossWt());
            Double b = Double.parseDouble(weight2.getMatGrossWt());
            weight1.setRemarks("合并磅单，磅单号1：" + weight1.getWeighNo() + ",磅单号2：" + weight2.getWeighNo());
            if (weight1.getConsignId() != null && !"".equals(weight1.getConsignId())) {
                Consign cons = consignService.queryInfoByConsignId(weight1.getConsignId());
                if (cons != null) {
                    weight1.setConsigneUser(cons.getConsigneUser());
                    weight1.setSupplierName(cons.getSupplierName());
                    weight1.setField1(cons.getField1());
                    weight2.setConsigneUser(cons.getConsigneUser());
                    weight2.setSupplierName(cons.getSupplierName());
                    weight2.setField1(cons.getField1());
                }
            }
            if (weight2.getConsignId() != null && !"".equals(weight2.getConsignId())) {
                Consign cons = consignService.queryInfoByConsignId(weight1.getConsignId());
                if (cons != null) {
                    weight1.setConsigneUser(cons.getConsigneUser());
                    weight1.setSupplierName(cons.getSupplierName());
                    weight1.setField1(cons.getField1());
                    weight2.setConsigneUser(cons.getConsigneUser());
                    weight2.setSupplierName(cons.getSupplierName());
                    weight2.setField1(cons.getField1());
                }
            }
            if (a > b) {
                weight1.setImpWt(weight2.getMatGrossWt());
                weight1.setGrossHeadPic(weight2.getTareHeadPic());
                weight1.setGrossTailPic(weight2.getTareTailPic());
                weight1.setGrossTopPic(weight2.getTareTopPic());
                weight1.setTaretime(weight2.getGrosstime());
                weight1.setWeightFlag("1");
                weight1.setSecondStation(weight2.getFistStation());
                Double c = a - b;
                weight1.setMatWt(String.valueOf(c));
                if (weight1.getConsignId() != null && !"".equals(weight1.getConsignId())) {
                    Consign cs = consignService.queryInfoByConsignId(weight1.getConsignId());
                    if (cs != null && cs.getPonderFlag() != null && "1".equals(cs.getPonderFlag())) {
                        consignService.cancel(cs);
                    }
                }
                weightService.update(weight1);
                j.setSuccess(true);
                j.setMsg("合并数据成功,磅单号为" + weight1.getWeighNo());

            } else {
                weight2.setImpWt(weight1.getMatGrossWt());
                weight2.setGrossHeadPic(weight1.getTareHeadPic());
                weight2.setGrossTailPic(weight1.getTareTailPic());
                weight2.setGrossTopPic(weight1.getTareTopPic());
                weight2.setTaretime(weight1.getGrosstime());
                weight2.setWeightFlag("1");
                weight2.setSecondStation(weight1.getFistStation());
                Double c = b - a;
                weight2.setMatWt(String.valueOf(c));
                if (weight2.getConsignId() != null && !"".equals(weight2.getConsignId())) {
                    Consign cs = consignService.queryInfoByConsignId(weight2.getConsignId());
                    if (cs != null && cs.getPonderFlag() != null && "1".equals(cs.getPonderFlag())) {
                        consignService.cancel(cs);
                    }
                }
                weightService.update(weight2);
                j.setSuccess(true);
                j.setMsg("合并数据成功,磅单号为" + weight2.getWeighNo());
            }
            //合并记录
            UpdateWeightRecord uwr = new UpdateWeightRecord();
            uwr.setOperation("3");
            uwr.setUpdateBy(UserUtils.getUser());
            uwr.setUpdateDate(new Date());
            uwr.setContent(weight1.getRemarks());
            WeightRecord wr = new WeightRecord();
            wr.setWeighNo(weight1.getWeighNo());
            uwr.setWeight(wr);
            uwr.setId(IdGen.uuid());
            updateWeightRecordMapper.insert(uwr);
            weight1.setRemarks("合并磅单，磅单号1：" + weight1.getWeighNo() + ",磅单号2：" + weight2.getWeighNo());
            //合并记录
            UpdateWeightRecord uwr2 = new UpdateWeightRecord();
            uwr2.setOperation("3");
            uwr2.setUpdateBy(UserUtils.getUser());
            uwr2.setUpdateDate(new Date());
            uwr2.setContent(weight1.getRemarks());
            WeightRecord wr2 = new WeightRecord();
            wr2.setWeighNo(weight2.getWeighNo());
            uwr2.setWeight(wr2);
            uwr2.setId(IdGen.uuid());
            updateWeightRecordMapper.insert(uwr2);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("系统异常");
            e.printStackTrace();
        }
        return j;
    }

    /*
     *
     * @description TODO
     * @author yuzhongtong
     * @date 2019/10/8
     * @time 11:34
     * @param [consignId, vehicleNo]
     * @return com.jeeplus.common.json.AjaxJson
     */
    @ResponseBody
    @RequestMapping(value = "checkWeight")
    public AjaxJson checkWeight(String consignId) {
        AjaxJson j = new AjaxJson();
        Weight weight = new Weight();
        if (consignId != null && !"".equals(consignId)) {
            weight.setConsignId(consignId);
            List<Weight> weightList = weightService.findList(weight);
            if (weightList != null && weightList.size() > 0) {
                j.setData(weightList.get(0));
                j.setMsg("查询成功！");
                j.setSuccess(true);
            }else{
                j.setMsg("未查到有效磅单！");
                j.setSuccess(false);
            }
        }else{
            j.setMsg("参数错误！");
            j.setSuccess(false);
        }
        return j;
    }

}
