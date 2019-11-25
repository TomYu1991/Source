package com.jeeplus.modules.gatelog.web;

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
import com.jeeplus.modules.controlseat.entity.ControlSeat;
import com.jeeplus.modules.controlseat.service.ControlSeatService;
import com.jeeplus.modules.gatelog.entity.GateLog;
import com.jeeplus.modules.gatelog.service.GateLogService;
import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.passcheck.service.PassCheckService;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.swipecard.entity.SwipeCard;
import com.jeeplus.modules.swipecard.service.SwipeCardService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.service.VehicleAccessRecordService;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import com.jeeplus.modules.vehicleinfo.service.VehicleInfoService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 门岗日志Controller
 *
 * @author jeeplus
 * @version 2018-12-22
 */
@Controller
@RequestMapping(value = "${adminPath}/gatelog/gateLog")
public class GateLogController extends BaseController {

    @Autowired
    private GateLogService gateLogService;
    @Autowired
    private ConsignService consignService;
    @Autowired
    private WorkStationService workStationService;
    @Autowired
    private PassCheckService passCheckService;
    @Autowired
    private WeightService weightService;
    //车辆进出记录
    @Autowired
    private VehicleAccessRecordService vehicleAccessRecordService;
    //刷卡人员权限
    @Autowired
    private SwipeCardService swipeCardService;
    @Autowired
    private ControlSeatService controlSeatService;
    //车辆信息
    @Autowired
    private VehicleInfoService vehicleInfoService;

    @ModelAttribute
    public GateLog get(@RequestParam(required = false) String id) {
        GateLog entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = gateLogService.get(id);
        }
        if (entity == null) {
            entity = new GateLog();
        }
        return entity;
    }

    /**
     * 门岗日志列表页面
     */
    @RequiresPermissions("gatelog:gateLog:list")
    @RequestMapping(value = {"list", ""})
    public String list(GateLog gateLog, Model model) {
        model.addAttribute("gateLog", gateLog);
        return "modules/gatelog/gateLogList";
    }

    /**
     * 门岗日志列表数据
     */
    @ResponseBody
    @RequiresPermissions("gatelog:gateLog:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(GateLog gateLog, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<GateLog> page = new Page<>();
        if (gateLog.getSearchFlag() != null && "1".equals(gateLog.getSearchFlag())) {
            page = gateLogService.findPage(new Page<GateLog>(request, response), gateLog);
        }
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑门岗日志表单页面
     */
    @RequiresPermissions(value = {"gatelog:gateLog:view", "gatelog:gateLog:add", "gatelog:gateLog:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form/{mode}")
    public String form(@PathVariable String mode, GateLog gateLog, Model model) {
        model.addAttribute("gateLog", gateLog);
        model.addAttribute("mode", mode);
        return "modules/gatelog/gateLogForm";
    }

    /**
     * 保存门岗日志
     */
    @ResponseBody
    @RequiresPermissions(value = {"gatelog:gateLog:save"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(GateLog gateLog) throws Exception {
        AjaxJson j = new AjaxJson();

        gateLogService.save(gateLog);//保存
        j.setSuccess(true);
        j.setMsg("保存门岗日志成功");
        return j;
    }

    /**
     * 删除门岗日志
     */
    @ResponseBody
    @RequiresPermissions("gatelog:gateLog:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(GateLog gateLog) {
        AjaxJson j = new AjaxJson();
        gateLogService.delete(gateLog);
        j.setMsg("删除门岗日志成功");
        return j;
    }

    /**
     * 批量删除门岗日志
     */
    @ResponseBody
    @RequiresPermissions("gatelog:gateLog:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            gateLogService.delete(gateLogService.get(id));
        }
        j.setMsg("删除门岗日志成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("gatelog:gateLog:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(GateLog gateLog, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String param = URLDecoder.decode(request.getParameter("data"), "UTF-8");
            JSONObject jo = JSONObject.fromObject(param);

            if (jo.get("beginDate") == null || "".equals(jo.get("beginDate"))) {
                jo.remove("beginDate");
            }
            if (jo.get("endDate") == null || "".equals(jo.get("endDate"))) {
                jo.remove("endDate");
            }
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setRootClass(GateLog.class);
            String[] dateFamates = new String[]{"yyyy-MM-dd HH:mm:ss"};
            JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

            gateLog = (GateLog) JSONObject.toBean(jo, GateLog.class);

            String fileName = "门岗日志" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<GateLog> page = gateLogService.findPage(new Page<GateLog>(request, response, -1), gateLog);
            new ExportExcel("门岗日志", GateLog.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出门岗日志记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("gatelog:gateLog:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<GateLog> list = ei.getDataList(GateLog.class);
            for (GateLog gateLog : list) {
                try {
                    gateLogService.save(gateLog);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条门岗日志记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条门岗日志记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入门岗日志失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入门岗日志数据模板
     */
    @ResponseBody
    @RequiresPermissions("gatelog:gateLog:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "门岗日志数据导入模板.xlsx";
            List<GateLog> list = Lists.newArrayList();
            new ExportExcel("门岗日志数据", GateLog.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }


    /**
     * 保存门岗日志
     */
    @ResponseBody
    @RequestMapping(value = "savePassLog")
    public AjaxJson savePassLog(String op, String exp, String ickh, String vehicleNo, String rfidNo, HttpServletRequest request) throws Exception {
        AjaxJson j = new AjaxJson();
        GateLog gateLog = new GateLog();
        //根据RFID查询车牌
        if (rfidNo != null && !"".equals(rfidNo)) {
            List<VehicleInfo> vi = vehicleInfoService.getVehicleNoByRfid(rfidNo);
            if (vi.size() > 0) {
                vehicleNo = vi.get(0).getVehicleNo();
            }
        }
        if (vehicleNo != null && !"".equals(vehicleNo)) {
            gateLog.setDataType("0");
            //根据车牌号查询相关的委托单信息或出门条信息
            if (vehicleNo != null && !"".equals(vehicleNo)) {
                //出门条
                PassCheck pass = new PassCheck();
                pass.setVehicleNo(vehicleNo);
                List<PassCheck> p = passCheckService.findPassByVehicleNo(pass);

                if (p.size() > 0) {
                    gateLog.setPassCode(p.get(0).getTrnpAppNo());
                    gateLog.setVehicleNo(p.get(0).getVehicleNo());
                }
            }
            if (ickh != null && !"".equals(ickh)) {
                SwipeCard s = swipeCardService.findByIcCard(ickh);
                if (s != null) {
                    gateLog.setRemarks(s.getJobNumber());
                    gateLog.setIcNumber(s.getIcnumber());
                    gateLog.setWorkName(s.getWorkName());
                }
            }
            gateLog.setOperation(op);
            gateLog.setException(exp);
            gateLog.setGateNum(request.getRemoteAddr());
            gateLog.setUserIP(request.getRemoteAddr());
            //增加保存操作日期和车牌号
            gateLog.setDate(new Date());
            gateLog.setVehicleNo(vehicleNo);
            //保存委托单号和磅单号
            List<Weight> w = weightService.queryInfoByVe(vehicleNo);
            if (w.size() > 0 && !"".equals(w.get(0).getId())) {
                gateLog.setConsignId(w.get(0).getConsignId());
                gateLog.setWeighNo(w.get(0).getWeighNo());
            }
            gateLogService.save(gateLog);//保存

            VehicleAccessRecord va = new VehicleAccessRecord();
            va.setVehicleNo(gateLog.getVehicleNo());
            VehicleAccessRecord var = vehicleAccessRecordService.queryLatelyRecord(va);
            if(var!=null){
                var.setIdcard(gateLog.getWorkName());
                var.setRfidNo("1");
                vehicleAccessRecordService.updateOpenInfo(var);
            }

            j.setSuccess(true);
            j.setMsg("保存门岗日志成功");
        }

        return j;
    }

    /**
     * 保存发送语音、LED日志
     */
    @ResponseBody
    @RequestMapping(value = "saveControlLog")
    public AjaxJson saveControlLog(String op, String exp, String content, HttpServletRequest request) throws Exception {
        AjaxJson j = new AjaxJson();
        GateLog gateLog = new GateLog();

        gateLog.setDataType("0");
        //新增或编辑表单保存
        ControlSeat cs = new ControlSeat();
        cs.setSeatIp(request.getRemoteAddr());
        ControlSeat cts = controlSeatService.findInfoByIp(cs);
        if (cts != null && cts.getSeatNum() != null && !"".equals(cts.getSeatNum())) {
            gateLog.setSeatNum(cts.getSeatNum());
        }

        gateLog.setGateNum("集控室");
        gateLog.setRemarks(content);
        gateLog.setU(UserUtils.getUser());
        gateLog.setOfficeId(UserUtils.getUser().getOffice().getName());
        gateLog.setCompanyId(UserUtils.getUser().getCompany().getName());
        gateLog.setOperation(op);
        gateLog.setException(exp);
        gateLog.setUserIP(request.getRemoteAddr());
        gateLogService.save(gateLog);//保存
        j.setSuccess(true);
        j.setMsg("保存发送语音、LED日志");
        return j;
    }
}