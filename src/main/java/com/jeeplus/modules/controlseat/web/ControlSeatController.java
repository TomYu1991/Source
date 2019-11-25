/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.controlseat.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

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
import com.jeeplus.modules.controlseat.entity.ControlSeat;
import com.jeeplus.modules.controlseat.service.ControlSeatService;

/**
 * 集控室坐席表Controller
 * @author 汤进国
 * @version 2019-01-14
 */
@Controller
@RequestMapping(value = "${adminPath}/controlseat/controlSeat")
public class ControlSeatController extends BaseController {

	@Autowired
	private ControlSeatService controlSeatService;
	
	@ModelAttribute
	public ControlSeat get(@RequestParam(required=false) String id) {
		ControlSeat entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = controlSeatService.get(id);
		}
		if (entity == null){
			entity = new ControlSeat();
		}
		return entity;
	}
	
	/**
	 * 集控室坐席列表页面
	 */
	@RequiresPermissions("controlseat:controlSeat:list")
	@RequestMapping(value = {"list", ""})
	public String list(ControlSeat controlSeat, Model model) {
		model.addAttribute("controlSeat", controlSeat);
		return "modules/controlseat/controlSeatList";
	}
	
		/**
	 * 集控室坐席列表数据
	 */
	@ResponseBody
	@RequiresPermissions("controlseat:controlSeat:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(ControlSeat controlSeat, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<ControlSeat> page = controlSeatService.findPage(new Page<ControlSeat>(request, response), controlSeat); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑集控室坐席表单页面
	 */
	@RequiresPermissions(value={"controlseat:controlSeat:view","controlseat:controlSeat:add","controlseat:controlSeat:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, ControlSeat controlSeat, Model model) {
		model.addAttribute("controlSeat", controlSeat);
		model.addAttribute("mode", mode);
		return "modules/controlseat/controlSeatForm";
	}

	/**
	 * 保存集控室坐席
	 */
	@ResponseBody
	@RequiresPermissions(value={"controlseat:controlSeat:add","controlseat:controlSeat:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(ControlSeat controlSeat, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(controlSeat);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		controlSeatService.save(controlSeat);//保存
		j.setSuccess(true);
		j.setMsg("保存集控室坐席成功");
		return j;
	}
	
	/**
	 * 删除集控室坐席
	 */
	@ResponseBody
	@RequiresPermissions("controlseat:controlSeat:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(ControlSeat controlSeat) {
		AjaxJson j = new AjaxJson();
		controlSeatService.delete(controlSeat);
		j.setMsg("删除集控室坐席成功");
		return j;
	}
	
	/**
	 * 批量删除集控室坐席
	 */
	@ResponseBody
	@RequiresPermissions("controlseat:controlSeat:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			controlSeatService.delete(controlSeatService.get(id));
		}
		j.setMsg("删除集控室坐席成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("controlseat:controlSeat:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(ControlSeat controlSeat, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "集控室坐席"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<ControlSeat> page = controlSeatService.findPage(new Page<ControlSeat>(request, response, -1), controlSeat);
    		new ExportExcel("集控室坐席", ControlSeat.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出集控室坐席记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("controlseat:controlSeat:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<ControlSeat> list = ei.getDataList(ControlSeat.class);
			for (ControlSeat controlSeat : list){
				try{
					controlSeatService.save(controlSeat);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条集控室坐席记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条集控室坐席记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入集控室坐席失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入集控室坐席数据模板
	 */
	@ResponseBody
	@RequiresPermissions("controlseat:controlSeat:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "集控室坐席数据导入模板.xlsx";
    		List<ControlSeat> list = Lists.newArrayList(); 
    		new ExportExcel("集控室坐席数据", ControlSeat.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}