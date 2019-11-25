/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.gatepass;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.*;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.interfaceTest.entry.JsonType;
import com.jeeplus.modules.interfaceTest.entry.PassFeedBack;
import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.passcheck.service.PassCheckService;
import com.jeeplus.modules.passchecksub.entity.PassCheckSub;
import com.jeeplus.modules.passchecksub.service.PassCheckSubService;
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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
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
import java.util.*;

/**
 * 出门条信息Controller
 *
 * @author llh
 * @version 2018-12-24
 */
@Controller
@RequestMapping(value = "${adminPath}/gatepass/passcheck")
public class GatePassController extends BaseController {

    @Autowired
    private PassCheckService passCheckService;
    @Autowired
    private WorkStationService workStationService;
    //车辆信息
    @Autowired
    private VehicleInfoService vehicleInfoService;
    //预约单、委托单
    @Autowired
    private ConsignService consignService;
    //磅单信息
    @Autowired
    private WeightService weightService;
    //车辆进出记录
    @Autowired
    private VehicleAccessRecordService vehicleAccessRecordService;
    //出门条明细
    @Autowired
    private PassCheckSubService passCheckSubService;
    @Autowired
    private WarningInfoService warningInfoService;

    @ModelAttribute
    public PassCheck get(@RequestParam(required = false) String id) {
        PassCheck entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = passCheckService.get(id);
        }
        if (entity == null) {
            entity = new PassCheck();
        }
        return entity;
    }

    /**
     * 出门条信息列表页面
     */
    @RequestMapping(value = {"list", ""})
    public String list(PassCheck passCheck, Model model) {
        model.addAttribute("passCheck", passCheck);
        return "modules/gatepass/gatePassCheckList";
    }

    /**
     * 出门条信息列表数据
     */
    @ResponseBody
    @RequestMapping(value = "data")
    public Map<String, Object> data(PassCheck passCheck, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<PassCheck> page = new Page<>();
        if (passCheck.getSearchFlag() != null && "1".equals(passCheck.getSearchFlag())) {
            page = passCheckService.findPage(new Page<PassCheck>(request, response), passCheck);
        }

        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑出门条信息表单页面
     */
    @RequiresPermissions(value = {"gatepass:passcheck:view", "gatepass:passcheck:add", "gatepass:passcheck:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(PassCheck passCheck, Model model) {
        model.addAttribute("passCheck", passCheck);
        return "modules/gatepass/gatePassCheckForm";
    }

    /**
     * 保存出门条信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"gatepass:passcheck:add", "gatepass:passcheck:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(PassCheck passCheck, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(passCheck);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        //新增或编辑表单保存
        passCheckService.save(passCheck);//保存
        j.setSuccess(true);
        j.setMsg("保存出门条信息成功");
        return j;
    }

    /**
     * 删除出门条信息
     */
    @ResponseBody
    @RequiresPermissions("gatepass:passcheck:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(PassCheck passCheck) {
        AjaxJson j = new AjaxJson();
        passCheckService.delete(passCheck);
        j.setMsg("删除出门条信息成功");
        return j;
    }

    /**
     * 批量删除出门条信息
     */
    @ResponseBody
    @RequiresPermissions("gatepass:passcheck:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            passCheckService.delete(passCheckService.get(id));
        }
        j.setMsg("删除出门条信息成功");
        return j;
    }

    /**
     * 根据车牌号查询车辆信息、预约单/委托单和出门条等信息
     */
    @ResponseBody
    @RequestMapping(value = {"checkPass"})
    public AjaxJson findPassByVehicleNo(String vehicleNo, String rfidNo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        PassCheck p = new PassCheck();
        String a = request.getRemoteAddr();
        System.out.println("===================ip:"+a);
        WorkStation ws = workStationService.queryByStationIp(a);

        //1。未识别到车牌
        if ((vehicleNo == null || "".equals(vehicleNo.trim())) && (rfidNo == null || "".equals(rfidNo))) {
            j.setSuccess(false);
            return j;
        }
        if (vehicleNo != null && !"".equals(vehicleNo.trim())) {
            String errvehicleNo = vehicleNo.substring(1, 2);
            if (StringUtils.isNumeric(errvehicleNo)) {
                j.setSuccess(false);
                return j;
            }
        }

//根据RFID查询车牌
        if (rfidNo != null && !"".equals(rfidNo)) {
            List<VehicleInfo> vi = vehicleInfoService.getVehicleNoByRfid(rfidNo);
            if (vi.size() > 0) {
                vehicleNo = vi.get(0).getVehicleNo();
            }
        }
        j.setErrorCode(vehicleNo);

        if (vehicleNo != null && !"".equals(vehicleNo.trim())) {
//公务车直接放行
            List<VehicleInfo> vl = vehicleInfoService.checkByVehicleNo(vehicleNo);
            if (vl.size() > 0) {
                for (VehicleInfo v : vl) {
                    if (v.getTypeCode() != null && "01".equals(v.getTypeCode())) {
                        j.setObject("10");
                        j.setSuccess(true);
                        j.setMsg(v.getVehicleNo() + "     " + v.getGroupCompanyName());
                        return j;
                    }
                }
            }
            //2.违章车辆不允许出门
            List<WarningInfo> wi = warningInfoService.findInfoByVehicleNo(vehicleNo);
            if (wi.size() > 0) {
                System.out.println(new Date() + "," + wi.get(0).getVehicleNo() + "," + "违章信息:    " + wi.get(0).getReason());
                String msg = wi.get(0).getVehicleNo() + "     违章车辆!    违章信息:    " + wi.get(0).getReason();
                j.setMsg(msg);
                j.setSuccess(false);
                return j;
            }


            Date da = new Date();
            Date date = new Date();

            StringBuilder str = new StringBuilder();
            str.append(vehicleNo);


            //查询该车是重车还是空车
            //查询该车辆进门后是否过磅
            if (!"10.12.241.107".equals(a) && !"10.12.240.139".equals(a)) {
                List<Weight> vehicle = weightService.queryVehicle(vehicleNo);
                if (vehicle.size() > 0) {
                    String veh = vehicle.get(0).getWeightType();
                    //根据过磅类型判断空重车
                    if ("01".equals(veh) || "06".equals(veh) || "07".equals(veh) || "08".equals(veh)) {
                        if (null != vehicle.get(0).getMatWt() && !"0.00".equals(vehicle.get(0).getMatWt()) && !"".equals(vehicle.get(0).getMatWt())) {
                            str.append("    重车    净重：" + vehicle.get(0).getMatWt());
                        }
                    }
                }
            }


            //查询车辆是否有出门条信息
            p.setVehicleNo(vehicleNo);
            List<PassCheck> pcl = passCheckService.findPassByVehicleNo(p);

            if (pcl.size() > 0) {
                for (PassCheck pc : pcl) {
                    if (pc.getTypeName() != null) {
                        str.append("物资类别:" + pc.getTypeName());
                    }
                    if (pc.getStartTime().getTime() < da.getTime() && pc.getEndTime().getTime() > da.getTime()) {
                        if (ws != null && ws.getOutPassCode() != null) {
                            String[] strs = pc.getPassCode().split(",");
                            for (String s : strs) {
                                if (s.equals(ws.getOutPassCode())) {
                                    if ("07".equals(pc.getTypeCode())) {
                                        str.append("      出门条物资清单:");

                                        List<PassCheckSub> ps = passCheckSubService.findPassCheckSubList(pc.getTrnpAppNo());
                                        if (ps.size() > 0) {
                                            for (PassCheckSub pcs : ps) {
                                                str.append(pcs.getProdCname() + ",    件数：" + pcs.getSubNum() + ",    规格：" + pcs.getMatSpecDesc() + ",    合计：" + pcs.getProdNum() + pcs.getMeasureUnit() + "   ");
                                            }
                                        }

                                    } else {

                                        List<PassCheckSub> ps = passCheckSubService.findCheckSubList(pc.getTrnpAppNo());
                                        if (ps.size() > 0) {
                                            for (PassCheckSub pcs : ps) {
                                                str.append(pcs.getProdCname() + ",     规格：" + pcs.getMatSpecDesc() + ",     合计：" + pcs.getOutStockQty() + pcs.getMeasureUnit() + "   ");
                                            }
                                        }

                                    }

                                    j.setObject("3");
                                    j.setSuccess(true);
                                    j.setMsg(str.toString());
                                    return j;
                                }
                            }
                            j.setSuccess(false);
                            j.setMsg(vehicleNo + "     请从其他门岗出门!");
                            return j;
                        }
                    }

                }

            }
            //4.判断放行码，放行码符合自动开门
            Consign cos = new Consign();
            cos.setVehicleNo(vehicleNo);
            List<Consign> coslist = consignService.checkOutByVehicleNo(cos);
            if (ws != null && ws.getOutPassCode() != null) {

                String outPass = ws.getOutPassCode();
                List<VehicleInfo> veh = vehicleInfoService.checkByVehicleNo(vehicleNo);

                if (veh.size() > 0) {
                    for (VehicleInfo ve : veh) {
                        if (ve.getStartTime().getTime() < date.getTime() && ve.getEndTime().getTime() > date.getTime()) {
                            String PassCode = ve.getPassCode();
                            if (PassCode != null && !"".equals(PassCode)) {
                                String[] strs = PassCode.split(",");
                                for (String s : strs) {
                                    if (outPass.equals(s)) {
                                        j.setObject("2");
                                        j.setSuccess(true);
                                        j.setMsg(ve.getVehicleNo() + "     " + ve.getGroupCompanyName());
                                        return j;
                                    }
                                }
                                for (String s : strs) {
                                    if (ws.getInPassCode().equals(s)) {
                                        j.setObject("4");
                                        j.setSuccess(true);
                                        j.setMsg(ve.getVehicleNo() + "     " + ve.getGroupCompanyName() + ",请检查");
                                        return j;
                                    }
                                }
                                if (coslist.size() > 0) {
                                    for (Consign s : coslist) {
                                        if (s.getPassCode() != null) {
                                            if (s.getPassCode().equals(ws.getInPassCode())) {
                                                j.setSuccess(false);
                                                j.setMsg(vehicleNo + "     临时车辆，请检查!");
                                                return j;
                                            }
                                        }
                                    }
                                    j.setSuccess(false);
                                    j.setMsg(vehicleNo + "     请从其他门岗出门!");
                                    return j;
                                } else {
                                    j.setSuccess(false);
                                    j.setMsg(vehicleNo + "     请从其他门岗出门!");
                                    return j;
                                }
                            }
                        }
                    }
                }
            }


            if (coslist != null && coslist.size() > 0) {
                if (ws != null && ws.getInPassCode() != null) {
                    for (Consign s : coslist) {
                        if (s.getPassCode() != null) {
                            String[] strs = s.getPassCode().split(",");
                            for (String sr : strs) {
                                if (ws.getInPassCode().equals(sr)) {
                                    j.setSuccess(false);
                                    j.setMsg(vehicleNo + "     临时车辆，请检查!");
                                    return j;
                                }
                            }
                        }
                    }
                    j.setSuccess(false);
                    j.setMsg(vehicleNo + "     请从其他门岗出门!");
                    return j;
                }
            } else {
                if ("10.12.241.107".equals(a)) {
                    j.setSuccess(false);
                    j.setMsg(vehicleNo + "     临时车辆，请检查!");
                    return j;
                } else {
                    j.setSuccess(false);
                    j.setMsg(vehicleNo + "     过夜车辆，请检查!");
                    return j;
                }
            }
        }
        j.setSuccess(false);
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("gatepass:passcheck:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(PassCheck passCheck, HttpServletRequest request, HttpServletResponse response) {
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
            jsonConfig.setRootClass(PassCheck.class);
            String[] dateFamates = new String[]{"yyyy-MM-dd HH:mm:ss"};
            JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

            passCheck = (PassCheck) JSONObject.toBean(jo, PassCheck.class);

            String fileName = "出门条信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<PassCheck> page = passCheckService.findPage(new Page<PassCheck>(request, response, -1), passCheck);
            new ExportExcel("出门条信息", PassCheck.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出出门条信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("gatepass:passcheck:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse
            response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<PassCheck> list = ei.getDataList(PassCheck.class);
            for (PassCheck passCheck : list) {
                try {
                    passCheckService.save(passCheck);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条出门条信息记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条出门条信息记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入出门条信息失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入出门条信息数据模板
     */
    @ResponseBody
    @RequiresPermissions("gatepass:passcheck:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "出门条信息数据导入模板.xlsx";
            List<PassCheck> list = Lists.newArrayList();
            new ExportExcel("出门条信息数据", PassCheck.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 查看出门条信息，客户模板展示
     */
    @RequestMapping(value = "form1")
    public String form1(String trnpAppNo, Model model) {

        List<PassCheckSub> list = passCheckSubService.findPassCheckSubList(trnpAppNo);
        System.out.println(list.toString());
        model.addAttribute("list", list);
        return "modules/gatepass/gatePassCheckInfoForm";
    }

    /**
     * 保存抓拍图片信息
     */
    @ResponseBody
    @RequestMapping(value = {"saveTackPhoto"})
    public AjaxJson saveTackPhoto(String vehicleNo, String rfidNo, String ct, HttpServletRequest
            request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        PassCheck p = new PassCheck();
        String a = request.getRemoteAddr();
        WorkStation ws = workStationService.queryByStationIp(a);
        //1。未识别到车牌
        if ((vehicleNo == null || "".equals(vehicleNo)) && (rfidNo == null || "".equals(rfidNo))) {
            j.setMsg("未识别到车牌！");
            j.setSuccess(false);
            return j;
        }
        //根据RFID查询车牌
        if (rfidNo != null && !"".equals(rfidNo)) {
            List<VehicleInfo> vi = vehicleInfoService.getVehicleNoByRfid(rfidNo);
            if (vi.size() > 0) {
                vehicleNo = vi.get(0).getVehicleNo();
            }
        }
        //保存车辆出门信息
        VehicleAccessRecord vehicleaccessRecord = new VehicleAccessRecord();
        vehicleaccessRecord.setVehicleNo(vehicleNo);
        if (vehicleNo != null && !"".equals(vehicleNo)) {
            List<VehicleAccessRecord> ve = vehicleAccessRecordService.queryRecord(vehicleaccessRecord);

            PropertiesLoader pro = new PropertiesLoader("/properties/config.properties");

            String gatePath = pro.getProperty("gatePath") + "";
            String http = "http://";
            String hostip = pro.getProperty("hostip") + "";
            String hostport = pro.getProperty("hostport") + "";
            String path = http + hostip + ":" + hostport + gatePath;

            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy/MM/dd/");
            String timefile = sdfs.format(new Date());
            if (ve.size() > 0) {
                ve.get(0).setTakePhotos(path + timefile + ct);

                vehicleAccessRecordService.updatePic(ve.get(0));
            }
        }

        return j;
    }

    /**
     * 保存出门信息
     */
    @ResponseBody
    @RequestMapping(value = "saveVehicleOut")
    public AjaxJson saveVehicleOut(Consign consign, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {

            String a = request.getRemoteAddr();
            WorkStation ws = workStationService.queryByStationIp(a);
            if (ws != null) {

                //保存车辆出门信息
                VehicleAccessRecord vehicleaccessRecord = new VehicleAccessRecord();
                vehicleaccessRecord.setVehicleNo(consign.getVehicleNo());
                vehicleaccessRecord.setOutTime(new Date());
                vehicleaccessRecord.setCreateDate(new Date());
                vehicleaccessRecord.setRemarks(ws.getWorkStation());
                vehicleaccessRecord.setIdcard("");
                if (consign.getRemarks() != null && "1".equals(consign.getRemarks())) {
                    vehicleaccessRecord.setRfidNo("2");
                } else {
                    vehicleaccessRecord.setRfidNo("3");
                }

                List<VehicleInfo> vil = vehicleInfoService.checkByVehicleNo(consign.getVehicleNo());
                if (vil.size() > 0) {
                    for (VehicleInfo v : vil) {
                        vehicleaccessRecord.setTransContactPerson(v.getTransContactPerson());
                        vehicleaccessRecord.setTransContactPersonTel(v.getTransContactPersonTel());
                    }
                }
                //根据车牌号查询委托信息

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

    /**
     * 作废出门条
     */
    @ResponseBody
    @RequestMapping(value = "deletePass")
    public AjaxJson deletePass(String vehicleNo, String rfidNo, String ickh) {
        AjaxJson j = new AjaxJson();
        try {
            //根据RFID查询车牌
            if (rfidNo != null && !"".equals(rfidNo)) {
                List<VehicleInfo> vi = vehicleInfoService.getVehicleNoByRfid(rfidNo);
                if (vi.size() > 0) {
                    vehicleNo = vi.get(0).getVehicleNo();
                }
            }
            //查询是否有出门条，给四级反馈
            InterfaceTest ift = new InterfaceTest();
            ift.postPasscheck(vehicleNo, ickh);
            PassCheck p = new PassCheck();
            p.setVehicleNo(vehicleNo);
            System.out.println("删除出门条");
            p.setValidFlag("2");
            passCheckService.delByLogic(p);

            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }

}