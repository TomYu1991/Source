/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.gateconsign;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.service.VehicleAccessRecordService;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import com.jeeplus.modules.vehicleinfo.service.VehicleInfoService;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
import com.jeeplus.modules.warninginfo.service.WarningInfoService;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.service.WeightService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 门岗管理Controller
 *
 * @author jeeplus
 * @version 2018-12-19
 */
@Controller
@RequestMapping(value = "${adminPath}/gateconsign/consign")
public class GateConsignController extends BaseController {

    @Autowired
    private ConsignService consignService;
    //工作站
    @Autowired
    private WorkStationService workStationService;
    //车辆进出记录
    @Autowired
    private VehicleAccessRecordService vehicleAccessRecordService;
    //车辆信息
    @Autowired
    private VehicleInfoService vehicleInfoService;
    @Autowired
    private WarningInfoService warningInfoService;
    @Autowired
    private WeightService weightService;

    @ModelAttribute
    public Consign get(@RequestParam(required = false) String id) {
        Consign entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = consignService.get(id);
        }
        if (entity == null) {
            entity = new Consign();
        }
        return entity;
    }


    /**
     * 根据车牌号和当前时间查询有效的委托单
     */
    @ResponseBody
    @RequestMapping(value = {"checkConsign"})
    public AjaxJson findConsignByVehicleNo(String vehicleNo, String rfidNo,
                                           HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        String a = request.getRemoteAddr();
        Date date = new Date();
        WorkStation ws = workStationService.queryByStationIp(a);
        Consign con = new Consign();
        con.setVehicleNo(vehicleNo);
        //1。未识别到车牌
        if ((vehicleNo == null || "".equals(vehicleNo.trim())) && (rfidNo == null || "".equals(rfidNo.trim()))) {
            j.setSuccess(false);
            return j;
        }
        //根据RFID查询车牌
        if (rfidNo != null && !"".equals(rfidNo.trim())) {
            List<VehicleInfo> vi = vehicleInfoService.getVehicleNoByRfid(rfidNo.trim());
            if (vi.size() > 0) {
                vehicleNo = vi.get(0).getVehicleNo();
            }
        }
        j.setObject(vehicleNo);
        if (StringUtils.trim(vehicleNo) != null && !"".equals(StringUtils.trim(vehicleNo))) {
           /* List<WarningInfo> varl = warningInfoService.findInfoByVehicleNo(vehicleNo);
            if (varl.size() > 0) {
                //2.违章车辆不允许进门
                j.setMsg("违章车辆："+vehicleNo);
                j.setSuccess(false);
                return j;
            }*/
            //从工作站中获取放行码
            if (ws != null && ws.getInPassCode() != null) {
                String inPass = ws.getInPassCode();
                //1.查询车辆信息表中是否存在
                List<VehicleInfo> vi = vehicleInfoService.checkByVehicleNo(vehicleNo);
                if (vi.size() > 0) {
                    for (VehicleInfo v : vi) {
                        if (v.getTypeCode() != null && "01".equals(v.getTypeCode())) {
                            j.setErrorCode("10");
                            j.setSuccess(true);
                            j.setMsg("欢迎     " + vehicleNo);
                            return j;
                        }
                        if (v.getStartTime().getTime() < date.getTime()
                                && v.getEndTime().getTime() > date.getTime()) {
                            String[] vtrs = v.getPassCode().split(",");
                            for (String vs : vtrs) {
                                if (inPass.equals(vs)) {
                                    j.setMsg("欢迎     " + vehicleNo);
                                    j.setSuccess(true);
                                    return j;
                                }
                            }
                        }
                    }
                }
                //2,委托单信息
                List<Consign> consign = consignService.findConsignByVehicleNo(con);
                if (consign.size() > 0) {
                    for (Consign cons : consign) {
                        if (cons.getStartTime().getTime() < date.getTime()
                                && cons.getEndTime().getTime() > date.getTime()) {
                            String code = cons.getPassCode();
                            String[] codeArray = code.split(",");
                            for (String codes : codeArray) {
                                if (codes.equals(inPass)) {
                                    j.setMsg("欢迎     " + vehicleNo);
                                    j.setSuccess(true);
                                    return j;
                                }
                            }
                        }
                    }
                } else {
                    j.setMsg("无预约    " + vehicleNo);
                    j.setSuccess(false);
                    return j;
                }
            }
        }
        j.setSuccess(false);
        return j;
    }

    /**
     * 根据车牌号和当前时间查询有效的委托单(过磅页面)
     */
    @ResponseBody
    @RequestMapping(value = {"checkConsignByWeight"})
    public AjaxJson checkConsignByWeight(Consign consign, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String addr = request.getRemoteAddr();
        //根据车牌号或RFID查询对应委托单
        //1.根据RFID查询车牌
        if (consign.getRfidNo() != null && !"undefined".equals(consign.getRfidNo())
                && !"".equals(consign.getRfidNo())) {
            List<VehicleInfo> v = vehicleInfoService.getVehicleNoByRfid(consign.getRfidNo().trim());
            if (v.size() > 0 && !"".equals(v.get(0).getVehicleNo())) {
                consign.setVehicleNo(v.get(0).getVehicleNo());
            }
        }
        j.setObject(consign.getVehicleNo());
        if (consign.getVehicleNo() != null && !"".equals(consign.getVehicleNo().trim())) {
            //根据车牌查询有效未过磅委托单
            List<Consign> list = consignService.queryConsignByVe(consign.getVehicleNo());
            int count = consignService.passYy(consign);
            if (list != null && list.size() == 0 && count > 0) {
                j.setSuccess(true);
                return j;
            } else {
                //查询是否有一次过磅非锁皮委托
                List<Consign> clist = consignService.queryStateConsign(consign.getVehicleNo());
                if (clist != null && clist.size() > 0) {
                    for (Consign c : clist) {
                        //过二磅判断是否收货
                        if (c.getField1() != null && "1".equals(c.getField1())) {
                            List<Weight> w = weightService.queryUnWeight(c.getConsignId());
                            if (w.size() > 0) {
                                if (w.get(0).getAffirmFlag() == null || !"1".equals(w.get(0).getAffirmFlag())) {
                                    j.setMsg("现场未确认收货!请返回现场");
                                    j.setSuccess(false);
                                    return j;
                                }
                            }

                        }
                    }
                    j.setSuccess(true);
                    return j;
                } else {
                    //查询是否有一次过磅锁皮委托
                    List<Consign> dlist = consignService.queryDefaultFlagConsign(consign.getVehicleNo());
                    if (dlist != null && dlist.size() > 0) {
                        for (Consign c : clist) {
                            //过二磅判断是否收货
                            if (c.getField1() != null && "1".equals(c.getField1())) {
                                List<Weight> w = weightService.queryUnWeight(c.getConsignId());
                                if (w.get(0).getAffirmFlag() == null || !"1".equals(w.get(0).getAffirmFlag())) {
                                    j.setMsg("现场未确认收货!请返回现场");
                                    j.setSuccess(false);
                                    return j;
                                }
                            }
                        }
                        j.setSuccess(true);
                        return j;
                    } else {
                        j.setMsg("无委托单");
                        j.setSuccess(false);
                        return j;
                    }
                }
            }
        } else {
            if (consign.getRfidNo() != null && !"".equals(consign.getRfidNo().trim())) {
                j.setErrorCode("2");
                j.setMsg("RFID卡异常");
                j.setSuccess(false);
                return j;
            } else {
                j.setMsg("未识别到车牌");
                j.setSuccess(false);
                return j;
            }

        }

    }

    /**
     * 委托单/预约单列表页面。
     */
    @RequestMapping(value = {"list", ""})
    public String list(Consign consign, Model model) {

        model.addAttribute("consign", consign);
        return "modules/gateconsign/gateConsignList";
    }

    /**
     * 委托单/预约单列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(Consign consign, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<Consign> page = new Page<>();
        if (consign.getSearchFlag() != null && "1".equals(consign.getSearchFlag())) {
            page = consignService.findPage(new Page<Consign>(request, response), consign);
        }
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑委托单/预约单表单页面
     */
    @RequiresPermissions(value = {"gateconsign:consign:view"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(Consign consign, Model model) {
        model.addAttribute("consign", consign);
        return "modules/gateconsign/gateConsignForm";
    }


    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("gateconsign:consign:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Consign consign, HttpServletRequest request,
                               HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String param = URLDecoder.decode(request.getParameter("data"), "UTF-8");
            JSONObject jo = JSONObject.fromObject(param);

            if (jo.get("startTime") == null || "".equals(jo.get("startTime"))) {
                jo.remove("startTime");
            }
            if (jo.get("endTime") == null || "".equals(jo.get("endTime"))) {
                jo.remove("endTime");
            }
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setRootClass(Consign.class);
            String[] dateFamates = new String[]{"yyyy-MM-dd HH:mm:ss"};
            JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

            consign = (Consign) JSONObject.toBean(jo, Consign.class);

            String fileName =
                    "委托单/预约单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<Consign> page = consignService.findPage(new Page<Consign>(request, response,
                    -1), consign);
            new ExportExcel("委托单/预约单",
                    Consign.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出委托单/预约单记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("gateconsign:consign:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file,
                               HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<Consign> list = ei.getDataList(Consign.class);
            for (Consign consign : list) {
                consign.setDataSources("1");
                try {
                    consignService.save(consign);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条委托单/预约单记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条委托单/预约单记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入委托单/预约单失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入委托单/预约单数据模板
     */
    @ResponseBody
    @RequiresPermissions("gateconsign:consign:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "委托单/预约单数据导入模板.xlsx";
            List<Consign> list = Lists.newArrayList();
            new ExportExcel("委托单/预约单数据",
                    Consign.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 判断委托单余量
     */
    @ResponseBody
    @RequestMapping(value = "checkConsignState")
    public AjaxJson checkConsignState(String id, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            Consign c = consignService.get(id);
            //判断剩余量
            if (c.getTotalWt() != null && !"".equals(c.getTotalWt()) && Double.parseDouble(c.getTotalWt()) > 0.0) {
                Double zl = Double.parseDouble(c.getTotalWt());
                Double dqzl = Double.parseDouble(c.getSurplusWt());
                if (c.getMoreRate() != null && !"".equals(c.getMoreRate())) {
                    Double yzl = Double.parseDouble(c.getMoreRate()) / 100;
                    if (yzl > 0.0) {
                        Double minzl = zl * (1.00 - yzl);
                        if (dqzl > minzl) {
                            j.setMsg("委托已完成!");
                            j.setSuccess(false);
                            return j;
                        }
                    }
                }

            }
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
        }

        return j;
    }

    /**
     * 保存进门信息
     */
    @ResponseBody
    @RequestMapping(value = "saveVehicleIn")
    public AjaxJson saveVehicleIn(Consign consign, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {

            String a = request.getRemoteAddr();
            WorkStation ws = workStationService.queryByStationIp(a);
            if (ws != null) {

                //保存车辆进门信息
                VehicleAccessRecord vehicleaccessRecord = new VehicleAccessRecord();
                vehicleaccessRecord.setVehicleNo(consign.getVehicleNo());
                vehicleaccessRecord.setIntoTime(new Date());
                vehicleaccessRecord.setCreateDate(new Date());
                vehicleaccessRecord.setRemarks(ws.getWorkStation());
                vehicleaccessRecord.setIdcard("");
                vehicleaccessRecord.setRfidNo("2");
                List<VehicleInfo> vil = vehicleInfoService.checkByVehicleNo(consign.getVehicleNo());
                if (vil.size() > 0) {
                    for (VehicleInfo v : vil) {
                        vehicleaccessRecord.setTransContactPerson(v.getTransContactPerson());
                        vehicleaccessRecord.setTransContactPersonTel(v.getTransContactPersonTel());
                    }
                }
                //根据车牌号查询委托单信息
                List<Consign> cl = consignService.findConsignByVehicleNo(consign);
                if (cl.size() > 0) {
                    vehicleaccessRecord.setConsignType(cl.get(0).getType());
                    vehicleaccessRecord.setTransContactPerson(cl.get(0).getUserName());
                    vehicleaccessRecord.setTransContactPersonTel(cl.get(0).getTel());
                }
                vehicleAccessRecordService.save(vehicleaccessRecord);
                j.setSuccess(true);
            }

        } catch (Exception e) {
            j.setSuccess(false);
        }

        return j;
    }
}