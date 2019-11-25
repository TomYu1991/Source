/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.weightrecord.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.weightrecord.entity.WeightRecord;
import com.jeeplus.modules.weightrecord.service.WeightRecordService;
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
import java.util.List;
import java.util.Map;

/**
 * 磅单信息记录Controller
 *
 * @author 汤进国
 * @version 2019-04-18
 */
@Controller
@RequestMapping(value = "${adminPath}/weightrecord/weightRecord")
public class WeightRecordController extends BaseController {

    @Autowired
    private WeightRecordService weightRecordService;

    @ModelAttribute
    public WeightRecord get(@RequestParam(required = false) String id) {
        WeightRecord entity = null;
        if (StringUtils.isNotBlank(id)) {
            entity = weightRecordService.get(id);
        }
        if (entity == null) {
            entity = new WeightRecord();
        }
        return entity;
    }

    /**
     * 磅单信息记录列表页面
     */
    @RequiresPermissions("weightrecord:weightRecord:list")
    @RequestMapping(value = {"list", ""})
    public String list(WeightRecord weightRecord, Model model) {
        model.addAttribute("weightRecord", weightRecord);
        return "modules/weightrecord/weightRecordList";
    }

    /**
     * 磅单信息记录列表数据
     */
    @ResponseBody
    @RequiresPermissions("weightrecord:weightRecord:list")
    @RequestMapping(value = "data")
    public Map<String, Object> data(WeightRecord weightRecord, HttpServletRequest request, HttpServletResponse response, Model model) {
        Page<WeightRecord> page = new Page<>();
        if (weightRecord.getSearchFlag() != null && "1".equals(weightRecord.getSearchFlag())) {
            page = weightRecordService.findPage(new Page<WeightRecord>(request, response), weightRecord);
        }
        return getBootstrapData(page);
    }

    /**
     * 查看，增加，编辑磅单信息记录表单页面
     */
    @RequiresPermissions(value = {"weightrecord:weightRecord:view", "weightrecord:weightRecord:add", "weightrecord:weightRecord:edit"}, logical = Logical.OR)
    @RequestMapping(value = "form/{mode}")
    public String form(@PathVariable String mode, WeightRecord weightRecord, Model model) {
        model.addAttribute("weightRecord", weightRecord);
        model.addAttribute("mode", mode);
        return "modules/weightrecord/weightRecordForm";
    }

    /**
     * 保存磅单信息记录
     */
    @ResponseBody
    @RequiresPermissions(value = {"weightrecord:weightRecord:add", "weightrecord:weightRecord:edit"}, logical = Logical.OR)
    @RequestMapping(value = "save")
    public AjaxJson save(WeightRecord weightRecord, Model model) throws Exception {
        AjaxJson j = new AjaxJson();
        /**
         * 后台hibernate-validation插件校验
         */
        String errMsg = beanValidator(weightRecord);
        if (StringUtils.isNotBlank(errMsg)) {
            j.setSuccess(false);
            j.setMsg(errMsg);
            return j;
        }
        //新增或编辑表单保存
        weightRecordService.save(weightRecord);//保存
        j.setSuccess(true);
        j.setMsg("保存磅单信息记录成功");
        return j;
    }

    /**
     * 删除磅单信息记录
     */
    @ResponseBody
    @RequiresPermissions("weightrecord:weightRecord:del")
    @RequestMapping(value = "delete")
    public AjaxJson delete(WeightRecord weightRecord) {
        AjaxJson j = new AjaxJson();
        weightRecordService.delete(weightRecord);
        j.setMsg("删除磅单信息记录成功");
        return j;
    }

    /**
     * 批量删除磅单信息记录
     */
    @ResponseBody
    @RequiresPermissions("weightrecord:weightRecord:del")
    @RequestMapping(value = "deleteAll")
    public AjaxJson deleteAll(String ids) {
        AjaxJson j = new AjaxJson();
        String idArray[] = ids.split(",");
        for (String id : idArray) {
            weightRecordService.delete(weightRecordService.get(id));
        }
        j.setMsg("删除磅单信息记录成功");
        return j;
    }

    /**
     * 导出excel文件
     */
    @ResponseBody
    @RequiresPermissions("weightrecord:weightRecord:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WeightRecord weightRecord, HttpServletRequest request, HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "磅单信息记录" + DateUtils.getDate("yyyyMMddHHmmss") + ".xlsx";
            Page<WeightRecord> page = weightRecordService.findPage(new Page<WeightRecord>(request, response, -1), weightRecord);
            new ExportExcel("磅单信息记录", WeightRecord.class).setDataList(page.getList()).write(response, fileName).dispose();
            j.setSuccess(true);
            j.setMsg("导出成功！");
            return j;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导出磅单信息记录记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    @ResponseBody
    @RequestMapping(value = "detail")
    public WeightRecord detail(String id) {
        return weightRecordService.get(id);
    }


    /**
     * 导入Excel数据
     */
    @ResponseBody
    @RequiresPermissions("weightrecord:weightRecord:import")
    @RequestMapping(value = "import")
    public AjaxJson importFile(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
        AjaxJson j = new AjaxJson();
        try {
            int successNum = 0;
            int failureNum = 0;
            StringBuilder failureMsg = new StringBuilder();
            ImportExcel ei = new ImportExcel(file, 1, 0);
            List<WeightRecord> list = ei.getDataList(WeightRecord.class);
            for (WeightRecord weightRecord : list) {
                try {
                    weightRecordService.save(weightRecord);
                    successNum++;
                } catch (ConstraintViolationException ex) {
                    failureNum++;
                } catch (Exception ex) {
                    failureNum++;
                }
            }
            if (failureNum > 0) {
                failureMsg.insert(0, "，失败 " + failureNum + " 条磅单信息记录记录。");
            }
            j.setMsg("已成功导入 " + successNum + " 条磅单信息记录记录" + failureMsg);
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入磅单信息记录失败！失败信息：" + e.getMessage());
        }
        return j;
    }

    /**
     * 下载导入磅单信息记录数据模板
     */
    @ResponseBody
    @RequiresPermissions("weightrecord:weightRecord:import")
    @RequestMapping(value = "import/template")
    public AjaxJson importFileTemplate(HttpServletResponse response) {
        AjaxJson j = new AjaxJson();
        try {
            String fileName = "磅单信息记录数据导入模板.xlsx";
            List<WeightRecord> list = Lists.newArrayList();
            new ExportExcel("磅单信息记录数据", WeightRecord.class, 1).setDataList(list).write(response, fileName).dispose();
            return null;
        } catch (Exception e) {
            j.setSuccess(false);
            j.setMsg("导入模板下载失败！失败信息：" + e.getMessage());
        }
        return j;
    }


}