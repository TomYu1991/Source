/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.consign.web;

import com.alibaba.fastjson.JSON;
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
import com.jeeplus.modules.impwthistory.entity.ImpWtHistory;
import com.jeeplus.modules.impwthistory.service.ImpWtHistoryService;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import com.jeeplus.modules.vehicleinfo.service.VehicleInfoService;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 委托单/预约单管理Controller
 *
 * @author 汤进国
 * @version 2019-01-17
 */
@Controller
@RequestMapping(value = "${adminPath}/consign/consign")
public class ConsignController extends BaseController {

    @Autowired
    private ConsignService consignService;
    //皮重历史
    @Autowired
    private ImpWtHistoryService impWtHistoryService;
    //磅单信息
    @Autowired
    private WeightService weightService;
    //车辆信息
    @Autowired
    private VehicleInfoService vehicleInfoService;

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
     * 委托单/预约单列表页面
     */
    @RequiresPermissions("consign:consign:list")
    @RequestMapping(value = {"list", ""})
    public String list(Consign consign, Model model) {
        model.addAttribute("consign", consign);
        return "modules/consign/consignList";
    }

    /**
     * 委托单/预约单列表数据
     */
    @ResponseBody
    @RequiresPermissions("consign:consign:list")
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
    @RequestMapping(value = "form")
    public String form(Consign consign, Model model) {
        model.addAttribute("consign", consign);
        return "modules/consign/consignForm";
    }

    @RequestMapping(value = "consignUpdateForm")
    public String consignUpdateForm(Consign consign, Model model) {
        model.addAttribute("consign", consign);
        return "modules/consign/consignUpdateForm";
    }

    /**
     * 保存委托单/预约单
     */
    @ResponseBody
    @RequiresPermissions(value = {"consign:consign:add", "consign:consign:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(Consign consign, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(consign);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        consign.setStatus("0");
        if (consign.getSurplusWt() != null && !"".equals(consign.getSurplusWt())) {
            consignService.updatesurplusWt(consign);
        }
        //新增或编辑表单保存
        consignService.save(consign);//保存
        j.setSuccess(true);
        j.setMsg("保存委托单/预约单成功");
        return j;
    }

    /**
     * 删除委托单/预约单
     */
    @ResponseBody
    @RequestMapping(value = "delete")
    public AjaxJson delete(Consign consign) {
        AjaxJson j = new AjaxJson();
        consignService.delete(consign);
        j.setMsg("删除委托单/预约单成功");
        return j;
    }

    /**
     * 批量删除委托单/预约单
     */
    @ResponseBody
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            consignService.delete(consignService.get(id));
        }
        j.setMsg("删除委托单/预约单成功");
        return j;
    }

    /**
     * 作废委托单/预约单
     */
    @ResponseBody
    @RequiresPermissions("consign:consign:cancel")
    @RequestMapping(value = "cancel")
    public AjaxJson cancel(String ids, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            Consign c = new Consign();
            c.setId(id);
            consignService.cancel(c);
            //作废皮重历史
            if (c.getDefaultFlag() != null && "1".equals(c.getDefaultFlag())) {
                System.out.println("锁皮删除皮重");
                //锁皮作废皮重历史
                impWtHistoryService.deleteImpWtHistory(c.getConsignId());
                //删除锁皮用未完成磅单
                List<Weight> l = weightService.queryByConsignId(c.getConsignId());
                if (l.size() > 0) {
                    for (Weight w : l) {
                        if (w.getMatWt() == null || "".equals(w.getMatWt()) || "0.00".equals(w.getMatWt())) {
                            weightService.cancel(w);
                            //作废上传
                            InterfaceTest ift = new InterfaceTest();
                            ift.queryInterFaceList(w, request);
                        }
                    }
                }
            }
        }
        j.setMsg("删除委托单/预约单成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("consign:consign:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Consign consign, HttpServletRequest request, HttpServletResponse response) {
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

            String fileName = "委托单/预约单" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<Consign> page = consignService.findPage(new Page<Consign>(request, response, -1), consign);
            new ExportExcel("委托单/预约单", Consign.class).setDataList(page.getList()).write(response, fileName).dispose();
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
    @RequiresPermissions("consign:consign:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<Consign> list = ei.getDataList(Consign.class);
            String json = JSON.toJSONString(list);
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
    @RequiresPermissions("consign:consign:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "委托单/预约单数据导入模板.xlsx";
            List<Consign> list = Lists.newArrayList();
            new ExportExcel("委托单/预约单数据", Consign.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 委托单/预约单列表数据
     */
    @ResponseBody
    @RequestMapping(value = "weigh")
    public Map<String, Object> weigh(Consign consign, HttpServletRequest request, HttpServletResponse response, Model model) {

        ImpWtHistory iw = new ImpWtHistory();
        iw.setVehicleNo(consign.getVehicleNo());
        List<ImpWtHistory> l = impWtHistoryService.findList(iw);
        if (l.size() > 0) {

        }
        Consign cons = new Consign();
        cons.setVehicleNo(consign.getVehicleNo());
        List<Consign> lcons = consignService.findList(cons);
        for (Consign c : lcons) {
            c.setWeightState("1");
            consignService.update(c);
        }

        consign.setType("1");
        consign.setTotalWt("1");
        Page<Consign> page = consignService.findPage(new Page<Consign>(request, response), consign);
        //page.setList("");
        return getBootstrapData(page);
    }

    /**
     * 根据ID查询委托单
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "queryid")
    public AjaxJson queryid(String id) {
        AjaxJson j = new AjaxJson();

        Consign entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = consignService.get(id);
        }
        if (entity == null) {
            entity = new Consign();
        }
        j.setSuccess(true);
        j.setObject(entity.getWeightState());
        return j;
    }

    /**
     * 过磅列表展示
     *
     * @param consign
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "showweights")
    public Map<String, Object> showweights(Consign consign, HttpServletRequest request, HttpServletResponse response, Model model) {

        consign.setType("1");
        Page<Consign> page = new Page();
        String addr = request.getRemoteAddr();

        if (consign != null && consign.getVehicleNo() != null && !"".equals(consign.getVehicleNo())) {

            //判断磅秤
            if ("10.12.241.10".equals(addr) || "10.12.241.20".equals(addr)) {
                consign.setEquipNum("01");
            }
            if ("10.12.242.30".equals(addr)) {
                consign.setEquipNum("21");
            }
            if ("10.12.242.40".equals(addr) || "10.12.242.41".equals(addr)) {
                consign.setEquipNum("31");
            }
            List<Consign> l = consignService.findConsignByVehicleNo(consign);
            if (l.size() > 0) {
                for (Consign con : l) {
                    if (con.getDefaultFlag() != null && "1".equals(con.getDefaultFlag())) {
                        consign.setDefaultFlag("1");
                    }
                }
            }

            if ((consign.getConsignNo() != null && !"".equals(consign.getConsignNo())) || (consign.getVehicleNo() != null && !"".equals(consign.getVehicleNo())) || (consign.getLadleNo() != null && !"".equals(consign.getLadleNo()))) {

                List<Consign> weightlist = consignService.showweights(consign);
                if (weightlist != null && weightlist.size() > 0) {
                    page.setList(weightlist);
                    return getBootstrapData(page);
                } else {
                    //检查是否有未完成磅单
                    List<Weight> lw = weightService.queryUnWeightByVe(consign.getVehicleNo());
                    if(lw==null||lw.size()==0){
                        List<Consign> con = consignService.showweightsall(consign);
                        page.setList(con);
                    }
                }
            }
        }
        return getBootstrapData(page);
    }


    /**
     * 过磅列表展示
     *
     * @param consign
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "gdhshowweights")
    public Map<String, Object> gdhshowweights(Consign consign, HttpServletRequest request, HttpServletResponse response, Model model) {

        consign.setType("1");
        Page<Consign> page = new Page();

        String addr = request.getRemoteAddr();
        if (consign.getRfidNo() != null && !"undefined".equals(consign.getRfidNo())
                && !"".equals(consign.getRfidNo())) {
            List<VehicleInfo> v = vehicleInfoService.getVehicleNoByRfid(consign.getRfidNo());
            List<VehicleInfo> vs = vehicleInfoService.getVehicleNoBySrfid(consign.getRfidNo());
            if (v!=null&&v.size() > 0 && !"".equals(v.get(0).getVehicleNo())) {
                consign.setLadleNo(v.get(0).getVehicleNo());
                consign.setConsignDept(v.get(0).getGroupCode());
            }
            if (vs!=null&&vs.size() > 0 && !"".equals(vs.get(0).getVehicleNo())) {
                consign.setLadleNo(vs.get(0).getVehicleNo());
                consign.setConsignDept(vs.get(0).getGroupCode());
            }
        }
        consign.setDefaultFlag("1");
        if (consign != null && consign.getLadleNo() != null && !"".equals(consign.getLadleNo())) {

            //判断磅秤
            if ("10.12.242.40".equals(addr) || "10.12.242.41".equals(addr)) {
                consign.setEquipNum("31");
            }

                List<Consign> weightlist = consignService.showweights(consign);
                if (weightlist != null && weightlist.size() > 0) {
                    page.setList(weightlist);
                    return getBootstrapData(page);
                } else {
                    List<Consign> con = consignService.showweightsall(consign);
                    page.setList(con);
                }

        }
        return getBootstrapData(page);
    }

    /**
     * 解锁皮
     *
     * @param consignId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "deblocking")
    public AjaxJson deblocking(String consignId) {
        AjaxJson j = new AjaxJson();
        //根据委托单号查询委托单信息
        List<Consign> c = consignService.queryInterInfoByConsignId(consignId);
        //根据委托单号查询未完成锁皮的磅单详情
        List<Weight> w = weightService.queryWeightByConsignId(consignId);
        if (c.size() > 0 && c.get(0) != null) {
            if (!"1".equals(c.get(0).getDefaultFlag())) {
                j.setMsg("该委托单是非锁皮委托单");
                j.setSuccess(false);
                return j;
            }
            //委托单改为非锁皮状态
            c.get(0).setDefaultFlag("0");
            consignService.updateDefaultFlag(c.get(0));
            //作废皮重历史表
            List<ImpWtHistory> iwh = impWtHistoryService.queryImpWtHistoryByConsignId(c.get(0).getConsignId());
            if (iwh.size() > 0) {
                for (ImpWtHistory iw : iwh) {
                    impWtHistoryService.deleteImpWtHistory(iw.getConsignId());
                }
            }
            //如果存在未完成的磅单
            if (w.size() > 0 && w.get(0) != null) {
                //改变磅单状态
                w.get(0).setDefaultFlag("0");
                //如果是多次过磅
                //先皮后毛
                if (!"01".equals(c.get(0).getWeightType()) && !"06".equals(c.get(0).getWeightType()) && !"07".equals(c.get(0).getWeightType()) && !"08".equals(c.get(0).getWeightType())) {
                    //先毛后皮，存错数据
                    String matGrossWt = w.get(0).getImpWt();
                    w.get(0).setMatGrossWt(matGrossWt);
                    w.get(0).setImpWt("0.00");
                    w.get(0).setMatWt("0.00");
                }
                w.get(0).setUpdatetime(new Date());
                w.get(0).setUpdater(UserUtils.getUser().getLoginName());
                w.get(0).setRemarks("解锁皮");
                weightService.updateWeight(w.get(0));
                weightService.cancel(w.get(0));
            }
        }
        j.setSuccess(true);
        j.setMsg("解锁皮成功！");
        return j;
    }

    /**
     * 删除皮重历史
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "deleteImpWtH")
    public AjaxJson deleteImpWtH(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            impWtHistoryService.deleteByVehicleNo(id);
        }
        j.setMsg("删除皮重历史成功");
        return j;
    }
}