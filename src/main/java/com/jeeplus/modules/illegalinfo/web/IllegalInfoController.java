/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.illegalinfo.web;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.common.utils.PropertiesLoader;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.service.VehicleAccessRecordService;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import com.jeeplus.modules.vehicleinfo.service.VehicleInfoService;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
import com.jeeplus.modules.warninginfo.service.WarningInfoService;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.illegalinfo.entity.IllegalInfo;
import com.jeeplus.modules.illegalinfo.service.IllegalInfoService;

/**
 * 非法闯入信息记录Controller
 *
 * @author illegalinfo
 * @version 2019-04-06
 */
@Controller
@RequestMapping(value = "${adminPath}/illegalinfo/illegalInfo")
public class IllegalInfoController extends BaseController {

    @Autowired
    private IllegalInfoService illegalInfoService;
    @Autowired
    private ConsignService consignService;
    //工作站
    @Autowired
    private WorkStationService workStationService;
    //车辆信息
    @Autowired
    private VehicleInfoService vehicleInfoService;


    @ModelAttribute
    public IllegalInfo get(@RequestParam(required = false) String id) {
        IllegalInfo entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = illegalInfoService.get(id);
        }
        if (entity == null) {
            entity = new IllegalInfo();
        }
        return entity;
    }

    /**
     * illegalinfo列表页面
     */
    @RequiresPermissions("illegalinfo:illegalInfo:list")
    @RequestMapping(value = {"list", ""})
    public String list(IllegalInfo illegalInfo, Model model) {
        model.addAttribute("illegalInfo", illegalInfo);
        return "modules/illegalinfo/illegalInfoList";
    }

    /**
     * illegalinfo列表数据
     */
    @ResponseBody
    @RequiresPermissions("illegalinfo:illegalInfo:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(IllegalInfo illegalInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<IllegalInfo> page = new Page<>();
        if (illegalInfo.getSearchFlag() != null && "1".equals(illegalInfo.getSearchFlag())) {
            page = illegalInfoService.findPage(new Page<IllegalInfo>(request, response), illegalInfo);
        }
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑illegalinfo表单页面
     */
    @RequiresPermissions(value = {"illegalinfo:illegalInfo:view", "illegalinfo:illegalInfo:add", "illegalinfo:illegalInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form")
    public String form(IllegalInfo illegalInfo, Model model) {
        model.addAttribute("illegalInfo", illegalInfo);
        return "modules/illegalinfo/illegalInfoForm";
    }

    /**
     * 保存illegalinfo
     */
    @ResponseBody
    @RequiresPermissions(value = {"illegalinfo:illegalInfo:add", "illegalinfo:illegalInfo:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(IllegalInfo illegalInfo, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(illegalInfo);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        //新增或编辑表单保存
        illegalInfoService.save(illegalInfo);//保存
        j.setSuccess(true);
        j.setMsg("保存illegalinfo成功");
        return j;
    }

    /**
     * 删除illegalinfo
     */
    @ResponseBody
    @RequiresPermissions("illegalinfo:illegalInfo:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(IllegalInfo illegalInfo) {
        AjaxJson j = new AjaxJson();
        illegalInfoService.delete(illegalInfo);
        j.setMsg("删除illegalinfo成功");
        return j;
    }

    /**
     * 批量删除illegalinfo
     */
    @ResponseBody
    @RequiresPermissions("illegalinfo:illegalInfo:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            illegalInfoService.delete(illegalInfoService.get(id));
        }
        j.setMsg("删除illegalinfo成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("illegalinfo:illegalInfo:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(IllegalInfo illegalInfo, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String param = URLDecoder.decode(request.getParameter("data"), "UTF-8");
            JSONObject jo = JSONObject.fromObject(param);

            if (jo.get("begiPassTime") == null || "".equals(jo.get("begiPassTime"))) {
                jo.remove("begiPassTime");
            }
            if (jo.get("endPassTime") == null || "".equals(jo.get("endPassTime"))) {
                jo.remove("endPassTime");
            }
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setRootClass(IllegalInfo.class);
            String[] dateFamates = new String[]{"yyyy-MM-dd HH:mm:ss"};
            JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

            illegalInfo = (IllegalInfo) JSONObject.toBean(jo, IllegalInfo.class);

            String fileName = "监控点闯入信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<IllegalInfo> page = illegalInfoService.findPage(new Page<IllegalInfo>(request, response, -1), illegalInfo);
            new ExportExcel("监控点闯入信息", IllegalInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出illegalinfo记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("illegalinfo:illegalInfo:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<IllegalInfo> list = ei.getDataList(IllegalInfo.class);
            for (IllegalInfo illegalInfo : list) {
                try {
                    illegalInfoService.save(illegalInfo);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条illegalinfo记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条illegalinfo记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入illegalinfo失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入illegalinfo数据模板
     */
    @ResponseBody
    @RequiresPermissions("illegalinfo:illegalInfo:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "illegalinfo数据导入模板.xlsx";
            List<IllegalInfo> list = Lists.newArrayList();
            new ExportExcel("illegalinfo数据", IllegalInfo.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 判断String 是 null 或者""
     *
     * @return
     */
    public boolean isNullOrEmputy(String t) {
        if (t == null || "".equals(t)) {
            return true;
        }
        return false;
    }

    /**
     * 根据车牌号和判断车辆是否进入非法区域
     */
    @ResponseBody
    @RequestMapping(value = {"checkPower"})
    public AjaxJson checkPower(String vehicleNo, String rfidNo, String position, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        Consign consign = new Consign();
        consign.setVehicleNo(vehicleNo);
        //委托单
        String addr = request.getRemoteAddr();
        if (rfidNo != null && !"undefined".equals(rfidNo) && !"".equals(rfidNo)) {
            List<VehicleInfo> v = vehicleInfoService.getVehicleNoByRfid(rfidNo);
            if (v.size() > 0 && !"".equals(v.get(0).getVehicleNo())) {
                consign.setVehicleNo(v.get(0).getVehicleNo());
            }
        }
        List<Consign> c = consignService.checkOutByVehicleNo(consign);
        //查询工作站
        WorkStation work = workStationService.queryByDeviceNo(position);
        if (work != null && work.getWorkStation() != null) {
            if (c != null && c.size() > 0) {
                for (Consign con : c) {
                    //跟据收发货方查询工作站
                    List<WorkStation> wkc = workStationService.queryByStationName(con.getConsigneUser());
                    if (wkc.size() > 0) {
                        j.setSuccess(true);
                        return j;
                    }
                    List<WorkStation> wks = workStationService.queryByStationName(con.getSupplierName());
                    if (wks.size() > 0) {
                        j.setSuccess(true);
                        return j;
                    }
                    List<WorkStation> wkd = workStationService.queryByStationName(con.getDealDept());
                    if (wkd.size() > 0) {
                        j.setSuccess(true);
                        return j;
                    }
                }
                IllegalInfo wi = new IllegalInfo();
                wi.setVehicleNo(c.get(0).getVehicleNo());
                wi.setConsigneUser(c.get(0).getConsigneUser());
                wi.setSupplierName(c.get(0).getSupplierName());
                wi.setMonitorPosition(work.getWorkStation());
                wi.setPassTime(new Date());
                if ("1".equals(c.get(0).getType())) {
                    wi.setWarningInfo("车辆：" + c.get(0).getVehicleNo() + ",违规进入 " + work.getWorkStation() + ",应前往：" + c.get(0).getConsigneUser() + "/" + c.get(0).getSupplierName());
                } else {
                    wi.setWarningInfo("车辆：" + c.get(0).getVehicleNo() + ",违规进入 " + work.getWorkStation() + ",应前往：" + c.get(0).getDealDept());
                }

                wi.setCreateDate(new Date());
                illegalInfoService.save(wi);
                j.setMsg(wi.getWarningInfo());
                j.setObject(work.getStationIp());
                j.setSuccess(false);
                return j;
            } else {
                IllegalInfo wi = new IllegalInfo();
                wi.setVehicleNo(consign.getVehicleNo());
                wi.setMonitorPosition(work.getWorkStation());
                wi.setPassTime(new Date());
                wi.setWarningInfo("车辆：" + consign.getVehicleNo() + ",无委托信息进入 " + work.getWorkStation());
                wi.setCreateDate(new Date());
                illegalInfoService.save(wi);
                j.setMsg(wi.getWarningInfo());
                j.setObject(work.getStationIp());
                j.setSuccess(false);
                return j;
            }
        }
        j.setSuccess(true);
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "savePhoto")
    public AjaxJson savePhoto(String Photo, String vehicleNo, String rfidNo) {
        AjaxJson j = new AjaxJson();
        PropertiesLoader pro = new PropertiesLoader("/properties/config.properties");

        String zhuapaiPath = pro.getProperty("gatePath") + "";

        String http = "http://";
        String hostip = pro.getProperty("hostip") + "";
        String hostport = pro.getProperty("hostport") + "";
        String path = http + hostip + ":" + hostport + zhuapaiPath;

        try {
            if (rfidNo != null && !"undefined".equals(rfidNo) && !"".equals(rfidNo)) {
                List<VehicleInfo> v = vehicleInfoService.getVehicleNoByRfid(rfidNo);
                if (v.size() == 1 && !"".equals(v.get(0).getVehicleNo())) {
                    vehicleNo = v.get(0).getVehicleNo();
                }

            }
            //存放抓拍图片文件路径
            SimpleDateFormat sdfs = new SimpleDateFormat("yyyy/MM/dd/");
            String timefile = sdfs.format(new Date());
            IllegalInfo illegalInfo = illegalInfoService.queryLateInfoByVehicleNo(vehicleNo);
            illegalInfo.setTakePhoto(path + timefile + Photo);
            illegalInfoService.savePhoto(illegalInfo);
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "queryByPosition")
    public AjaxJson queryByPosition(String flag) {
        AjaxJson j = new AjaxJson();
        if ("1".equals(flag)) {
            List<WorkStation> w1 = workStationService.queryDeviceNoSBCH();
            j.setSuccess(true);
            j.setData(w1);
        }
        if ("2".equals(flag)) {
            List<WorkStation> w2 = workStationService.queryDeviceNoRFID();
            j.setSuccess(true);
            j.setData(w2);
        }
        return j;
    }
}