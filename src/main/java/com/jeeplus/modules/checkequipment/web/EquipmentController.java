/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.checkequipment.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.station.entity.WorkStation;

import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.service.WeightService;
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
import com.jeeplus.modules.checkequipment.entity.Equipment;
import com.jeeplus.modules.checkequipment.service.EquipmentService;

/**
 * 点检设备表Controller
 * @author 张鲁蒙
 * @version 2019-01-04
 */
@Controller
@RequestMapping(value = "${adminPath}/checkequipment/equipment")
public class EquipmentController extends BaseController {

	@Autowired
	private EquipmentService equipmentService;

	@Autowired
	private WeightService weightService;

	@ModelAttribute
	public Equipment get(@RequestParam(required=false) String id) {
		Equipment entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = equipmentService.get(id);
		}
		if (entity == null){
			entity = new Equipment();
		}
		return entity;
	}
	
	/**
	 * 点检设备表列表页面
	 */
	@RequiresPermissions("checkequipment:equipment:list")
	@RequestMapping(value = {"list", ""})
	public String list(Equipment equipment, Model model) {
		model.addAttribute("equipment", equipment);
		return "modules/checkequipment/equipmentList";
	}
	
		/**
	 * 点检设备表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("checkequipment:equipment:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Equipment equipment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Equipment> page = new Page<>();
		if(equipment.getSearchFlag()!=null&&"1".equals(equipment.getSearchFlag())){
			page = equipmentService.findPage(new Page<Equipment>(request, response), equipment);
		}
		return getBootstrapData(page);
	}

	/**
	 * 增加，编辑点检设备表表单页面
	 */
	@RequiresPermissions(value={"checkequipment:equipment:add","checkequipment:equipment:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Equipment equipment, Model model) {
		model.addAttribute("equipment", equipment);
		return "modules/checkequipment/equipmentForm";
	}
	/**
	 * 查看点检设备表表单页面
	 */
	@RequiresPermissions(value={"checkequipment:equipment:view"},logical=Logical.OR)
	@RequestMapping(value = "formRead")
	public String formRead(Equipment equipment, Model model) {
		model.addAttribute("equipment", equipment);
		return "modules/checkequipment/equipmentFormRead";
	}
	/**
	 * 保存点检设备表
	 */
	@ResponseBody
	@RequiresPermissions(value={"checkequipment:equipment:add","checkequipment:equipment:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public <list> AjaxJson save(Equipment equipment,Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(equipment);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		String equipname = equipment.getEquipment();
		String station = equipment.getStation().getWorkStation();
		if("".equals(equipname) || null == equipment){
			j.setSuccess(false);
			j.setMsg("设备名称不能为空");
			return j;
		}
		if("".equals(station) || null == station){
			j.setSuccess(false);
			j.setMsg("工作站不能为空");
			return j;
		}
		List l = equipmentService.findEquipmentUnique(equipname,station);
		if(l.size()<=0){
			//新增或编辑表单保存
			equipmentService.save(equipment);//保存
			j.setSuccess(true);
			j.setMsg("保存点检设备表成功");

		}else{
			//新增或编辑表单保存
			equipmentService.save(equipment);//保存
			j.setSuccess(false);
			j.setMsg("该工作站已存在相同的设备名称");
		}
		return j;
	}
	
	/**
	 * 删除点检设备表
	 */
	@ResponseBody
	@RequiresPermissions("checkequipment:equipment:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Equipment equipment) {
		AjaxJson j = new AjaxJson();
		equipmentService.delete(equipment);
		j.setMsg("删除点检设备表成功");
		return j;
	}
	
	/**
	 * 批量删除点检设备表
	 */
	@ResponseBody
	@RequiresPermissions("checkequipment:equipment:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			equipmentService.delete(equipmentService.get(id));
		}
		j.setMsg("删除点检设备表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("checkequipment:equipment:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Equipment equipment, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "点检设备表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Equipment> page = equipmentService.findPage(new Page<Equipment>(request, response, -1), equipment);
    		new ExportExcel("点检设备表", Equipment.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出点检设备表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("checkequipment:equipment:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Equipment> list = ei.getDataList(Equipment.class);
			for (Equipment equipment : list){
				try{
					equipmentService.save(equipment);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条点检设备表记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条点检设备表记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入点检设备表失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入点检设备表数据模板
	 */
	@ResponseBody
	@RequiresPermissions("checkequipment:equipment:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "点检设备表数据导入模板.xlsx";
    		List<Equipment> list = Lists.newArrayList(); 
    		new ExportExcel("点检设备表数据", Equipment.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}