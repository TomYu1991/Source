/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.station.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.devicemanage.entity.DeviceManage;
import com.jeeplus.modules.devicemanage.service.DeviceManageService;
import com.jeeplus.modules.station.entity.Device;
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
import org.springframework.web.bind.annotation.PathVariable;
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
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;

/**
 * 工作站管理Controller
 * @author 汤进国
 * @version 2019-01-02
 */
@Controller
@RequestMapping(value = "${adminPath}/station/workStation")
public class WorkStationController extends BaseController {

	@Autowired
	private WorkStationService workStationService;

	@ModelAttribute
	public WorkStation get(@RequestParam(required=false) String id) {
		WorkStation entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = workStationService.get(id);
		}
		if (entity == null){
			entity = new WorkStation();
		}
		return entity;
	}
	
	/**
	 * 工作站管理列表页面
	 */
	@RequiresPermissions("station:workStation:list")
	@RequestMapping(value = {"list", ""})
	public String list(WorkStation workStation, Model model) {
		model.addAttribute("workStation", workStation);
		return "modules/station/workStationList";
	}
	
		/**
	 * 工作站管理列表数据
	 */
	@ResponseBody
	@RequiresPermissions("station:workStation:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(WorkStation workStation, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WorkStation> page = workStationService.findPage(new Page<WorkStation>(request, response), workStation); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑工作站管理表单页面
	 */
	@RequiresPermissions(value={"station:workStation:view","station:workStation:add","station:workStation:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, WorkStation workStation, Model model) {
		model.addAttribute("workStation", workStation);
		model.addAttribute("mode", mode);
		return "modules/station/workStationForm";
	}

	/**
	 * 保存工作站管理
	 */
	@ResponseBody
	@RequiresPermissions(value={"station:workStation:add","station:workStation:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(WorkStation workStation, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(workStation);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		if(workStation!=null&&workStation.getWorkStation()!=null&&!"".equals(workStation.getWorkStation())){
			workStationService.save(workStation);//保存
			j.setSuccess(true);
			j.setMsg("保存工作站管理成功");
		}else{
			j.setSuccess(true);
			j.setMsg("数据不能为空");
		}
		return j;
	}
	
	/**
	 * 删除工作站管理
	 */
	@ResponseBody
	@RequiresPermissions("station:workStation:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(WorkStation workStation) {
		AjaxJson j = new AjaxJson();
		workStationService.delete(workStation);
		j.setMsg("删除工作站管理成功");
		return j;
	}
	
	/**
	 * 批量删除工作站管理
	 */
	@ResponseBody
	@RequiresPermissions("station:workStation:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			workStationService.delete(workStationService.get(id));
		}
		j.setMsg("删除工作站管理成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("station:workStation:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WorkStation workStation, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工作站管理"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WorkStation> page = workStationService.findPage(new Page<WorkStation>(request, response, -1), workStation);
    		new ExportExcel("工作站管理", WorkStation.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出工作站管理记录失败！失败信息："+e.getMessage());
		}
			return j;
    }
    
    @ResponseBody
    @RequestMapping(value = "detail")
	public WorkStation detail(String id) {
		return workStationService.get(id);
	}
	

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("station:workStation:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WorkStation> list = ei.getDataList(WorkStation.class);
			for (WorkStation workStation : list){
				try{
					workStationService.save(workStation);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条工作站管理记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条工作站管理记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入工作站管理失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入工作站管理数据模板
	 */
	@ResponseBody
	@RequiresPermissions("station:workStation:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "工作站管理数据导入模板.xlsx";
    		List<WorkStation> list = Lists.newArrayList(); 
    		new ExportExcel("工作站管理数据", WorkStation.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }


}