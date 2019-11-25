/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.controlqueue.web;

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
import com.jeeplus.modules.controlqueue.entity.ControlQueue;
import com.jeeplus.modules.controlqueue.service.ControlQueueService;
import com.jeeplus.modules.controlseat.entity.ControlSeat;
import com.jeeplus.modules.controlseat.service.ControlSeatService;
import com.jeeplus.modules.impwthistory.entity.ImpWtHistory;
import com.jeeplus.modules.impwthistory.service.ImpWtHistoryService;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.service.WeightService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 集控室排队Controller
 *
 * @author 汤进国
 * @version 2019-01-10
 */
@Controller
@RequestMapping(value = "${adminPath}/controlqueue/controlQueue")
public class ControlQueueController extends BaseController {

    @Autowired
    private ControlQueueService controlQueueService;
    //磅单管理
    @Autowired
    private WeightService weightService;
    //工作站
    @Autowired
    private WorkStationService workStationService;
    @Autowired
    private ControlSeatService controlSeatService;
    //委托单
    @Autowired
    private ConsignService consignService;
    @Autowired
    private ImpWtHistoryService impWtHistoryService;

    @ModelAttribute
    public ControlQueue get(@RequestParam(required = false) String id) {
        ControlQueue entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = controlQueueService.get(id);
        }
        if (entity == null) {
            entity = new ControlQueue();
        }
        return entity;
    }


    /**
     * 修改排队状态
     */
    @ResponseBody
    @RequiresPermissions("controlqueue:controlQueue:handle")
    @RequestMapping(value = {"handle"})
    public AjaxJson handle(ControlQueue controlQueue, Model model, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        //判断当前坐席是否有正在处理的磅单
        //1.坐席编号
        ControlQueue c = controlQueueService.get(controlQueue.getId());
        Weight we = new Weight();
        we.setWeighNo(c.getWeightId());
        Weight w = weightService.queryInfoByWeighNo(we);
        if (c.getRemarks() != null) {
            if ("1".equals(c.getRemarks()) || "2".equals(c.getRemarks())) {
                c.setState("3");
                controlQueueService.updateState(c, w);
                j.setSuccess(false);
                j.setMsg("操作成功！");
                return j;
            }
        }
        ControlQueue cq = controlQueueService.checkSeatNum(c);
        if (cq != null && !cq.getId().equals(controlQueue.getId())) {
            j.setSuccess(false);
            j.setMsg("已有正在处理的磅单！");
            return j;
        }


        //查询返显信息

        WorkStation ws = workStationService.get(c.getStationId());
        //修改排队状态
        c.setState("2");
        controlQueueService.updateState(c, w);
        w.setStationId(c.getStationId());
        w.setWorkStation(ws.getWorkStation());
        if (w.getDefaultFlag() != null && "1".equals(w.getDefaultFlag())) {
            ImpWtHistory iw = impWtHistoryService.queryImpWtHistory(w.getConsignId());
            if (iw != null && iw.getImpWt() != null) {
                w.setOldImpWt(iw.getImpWt());
            }
        }

        j.setData(w);
        j.setSuccess(true);
        j.setMsg("修改排队状态成功");

        return j;
    }


    /**
     * 排队信息列表页面
     */
    @RequiresPermissions("controlqueue:controlQueue:list")
    @RequestMapping(value = {"list", ""})
    public String list(ControlQueue controlQueue, Model model) {
        model.addAttribute("controlQueue", controlQueue);
        return "modules/controlqueue/controlQueueList";
    }

    /**
     * 排队信息列表数据
     */
    @ResponseBody
    @RequiresPermissions("controlqueue:controlQueue:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(ControlQueue controlQueue, HttpServletRequest request, HttpServletResponse response, Model model) {
        ControlSeat cs = new ControlSeat();
        cs.setSeatIp(request.getRemoteAddr());
        ControlSeat cts = controlSeatService.findInfoByIp(cs);
        if (cts != null && cts.getSeatNum() != null && !"".equals(cts.getSeatNum())) {
            controlQueue.setSeatNum(cts.getSeatNum());
        }
        Page<ControlQueue> page = controlQueueService.findPage(new Page<ControlQueue>(request, response), controlQueue);
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑排队信息表单页面
     */
    @RequiresPermissions(value = {"controlqueue:controlQueue:view", "controlqueue:controlQueue:add", "controlqueue:controlQueue:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form/{mode}")
    public String form(@PathVariable String mode, ControlQueue controlQueue, Model model) {
        model.addAttribute("controlQueue", controlQueue);
        model.addAttribute("mode", mode);
        return "modules/controlqueue/controlQueueForm";
    }

    /**
     * 保存排队信息
     */
    @ResponseBody
    @RequiresPermissions(value = {"controlqueue:controlQueue:add", "controlqueue:controlQueue:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(ControlQueue controlQueue, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(controlQueue);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        //新增或编辑表单保存
        controlQueueService.save(controlQueue);//保存
        j.setSuccess(true);
        j.setMsg("保存排队信息成功");
        return j;
    }


    /**
     * 删除排队信息
     */
    @ResponseBody
    @RequiresPermissions("controlqueue:controlQueue:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(ControlQueue controlQueue) {
        AjaxJson j = new AjaxJson();
        controlQueueService.delete(controlQueue);
        j.setMsg("删除排队信息成功");
        return j;
    }

    /**
     * 批量删除排队信息
     */
    @ResponseBody
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();

        ControlQueue cs = controlQueueService.get(ids);

        if (cs != null) {
            String consignId = cs.getConsignId();
            List<Consign> c = consignService.queryByConsignId(consignId);
            if (c.size() > 0) {
                c.get(0).setWeightState("1");
                WorkStation w = workStationService.get(cs.getStationId());
                j.setObject(w.getStationIp());
                Weight wei = new Weight();
                wei.setWeighNo(cs.getWeightId());
                Weight ws = weightService.queryInfoByWeighNo(wei);
                if (cs.getRemarks() != null) {
                    if ("1".equals(cs.getRemarks()) || "2".equals(cs.getRemarks())) {
                        cs.setState("3");
                        controlQueueService.updateState(cs, ws);
                        j.setSuccess(false);
                        j.setMsg("操作成功！");
                        return j;
                    }
                }
                if (ws != null && c.size() > 0) {
                    if ("1".equals(ws.getDefaultFlag())) {
                        ws.setStatus("2");
                        ws.setRemarks("锁皮回退");
                        ws.setAbnrType("5");
                        controlQueueService.deleteQueue(cs, c.get(0), ws);
                        j.setSuccess(true);
                        j.setMsg("回退成功");
                        return j;
                    } else {
                        ws.setWeightFlag("0");
                        ws.setMatWt("0.00");
                        //如果称重方式是先皮后毛
                        if ("01".equals(c.get(0).getWeightType()) || "06".equals(c.get(0).getWeightType()) || "08".equals(c.get(0).getWeightType()) || "07".equals(c.get(0).getWeightType())) {
                            ws.setMatGrossWt("0.00");
                        } else {
                            ws.setImpWt("0.00");
                        }
                        ws.setUpdatetime(new Date());
                        ws.setUpdater(UserUtils.getUser().getLoginName());
                        ws.setRemarks("二次过磅后回退！");
                        InterfaceTest ift = new InterfaceTest();
                        ift.queryInterFaceList(ws, request);
                        controlQueueService.deleteQueue(cs, c.get(0), ws);
                        j.setSuccess(true);
                        j.setMsg("回退成功");
                        return j;
                    }
                }
            } else {
                controlQueueService.delete(cs);
            }
        }
        j.setSuccess(false);
        j.setMsg("回退失败");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("controlqueue:controlQueue:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ControlQueue controlQueue, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "排队信息" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<ControlQueue> page = controlQueueService.findPage(new Page<ControlQueue>(request, response, -1), controlQueue);
            new ExportExcel("排队信息", ControlQueue.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出排队信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("controlqueue:controlQueue:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<ControlQueue> list = ei.getDataList(ControlQueue.class);
            for (ControlQueue controlQueue : list) {
                try {
                    controlQueueService.save(controlQueue);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条排队信息记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条排队信息记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入排队信息失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入排队信息数据模板
     */
    @ResponseBody
    @RequiresPermissions("controlqueue:controlQueue:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "排队信息数据导入模板.xlsx";
            List<ControlQueue> list = Lists.newArrayList();
            new ExportExcel("排队信息数据", ControlQueue.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 获取排队状态
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "quereState")
    public AjaxJson quereState(ControlQueue controlQueue, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {

            ControlSeat cs = new ControlSeat();
            cs.setSeatIp(request.getRemoteAddr());
            ControlSeat cts = controlSeatService.findInfoByIp(cs);
            if (cts != null && cts.getSeatNum() != null && !"".equals(cts.getSeatNum())) {
                controlQueue.setSeatNum(cts.getSeatNum());
            }
            ControlQueue c = controlQueueService.countQueue(controlQueue);
            j.setData(c);
            j.setSuccess(true);
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }

    /**
     * 删除阻塞的排队信息
     *
     * @return
     */
    @ResponseBody
    @RequiresPermissions("controlqueue:controlQueue:handle")
    @RequestMapping(value = "remove")
    public AjaxJson remove( HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            String id = request.getParameter("id");
            ControlQueue queue =  new ControlQueue();
            queue.setId(id);
            queue.setState("3");
            controlQueueService.removeQueueById(queue);
            j.setSuccess(true);
            j.setMsg("成功删除排队信息");
        } catch (Exception e) {
            j.setSuccess(false);
        }
        return j;
    }


}