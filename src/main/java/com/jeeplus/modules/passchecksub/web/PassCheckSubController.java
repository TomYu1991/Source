/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.passchecksub.web;

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
import com.jeeplus.modules.passchecksub.entity.PassCheckSub;
import com.jeeplus.modules.passchecksub.service.PassCheckSubService;

/**
 * 出门条明细Controller
 * @author 汤进国
 * @version 2019-01-17
 */
@Controller
@RequestMapping(value = "${adminPath}/passchecksub/passCheckSub")
public class PassCheckSubController extends BaseController {

	@Autowired
	private PassCheckSubService passCheckSubService;
	
	@ModelAttribute
	public PassCheckSub get(@RequestParam(required=false) String id) {
		PassCheckSub entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = passCheckSubService.get(id);
		}
		if (entity == null){
			entity = new PassCheckSub();
		}
		return entity;
	}
	
	/**
	 * 出门条明细列表页面
	 */
	@RequestMapping(value = {"list", ""})
	public String list(PassCheckSub passCheckSub, Model model) {
		model.addAttribute("passCheckSub", passCheckSub);
		model.addAttribute("trnpApp", passCheckSub.getTrnpAppNo());
		return "modules/passchecksub/passCheckSubList";
	}
	
		/**
	 * 出门条明细列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(PassCheckSub passCheckSub, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PassCheckSub> page = passCheckSubService.findPage(new Page<PassCheckSub>(request, response), passCheckSub); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑出门条明细表单页面
	 */
	@RequiresPermissions(value={"passchecksub:passCheckSub:view","passchecksub:passCheckSub:add","passchecksub:passCheckSub:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PassCheckSub passCheckSub, Model model) {
		model.addAttribute("passCheckSub", passCheckSub);
		return "modules/passchecksub/passCheckSubForm";
	}

	/**
	 * 保存出门条明细
	 */
	@ResponseBody
	@RequiresPermissions(value={"passchecksub:passCheckSub:add","passchecksub:passCheckSub:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(PassCheckSub passCheckSub, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(passCheckSub);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		passCheckSubService.save(passCheckSub);//保存
		j.setSuccess(true);
		j.setMsg("保存出门条明细成功");
		return j;
	}
	
	/**
	 * 删除出门条明细
	 */
	@ResponseBody
	@RequiresPermissions("passchecksub:passCheckSub:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PassCheckSub passCheckSub) {
		AjaxJson j = new AjaxJson();
		passCheckSubService.delete(passCheckSub);
		j.setMsg("删除出门条明细成功");
		return j;
	}
	
	/**
	 * 批量删除出门条明细
	 */
	@ResponseBody
	@RequiresPermissions("passchecksub:passCheckSub:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			passCheckSubService.delete(passCheckSubService.get(id));
		}
		j.setMsg("删除出门条明细成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("passchecksub:passCheckSub:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(PassCheckSub passCheckSub, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "出门条明细"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PassCheckSub> page = passCheckSubService.findPage(new Page<PassCheckSub>(request, response, -1), passCheckSub);
    		new ExportExcel("出门条明细", PassCheckSub.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出出门条明细记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("passchecksub:passCheckSub:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PassCheckSub> list = ei.getDataList(PassCheckSub.class);
			for (PassCheckSub passCheckSub : list){
				try{
					passCheckSubService.save(passCheckSub);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条出门条明细记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条出门条明细记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入出门条明细失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入出门条明细数据模板
	 */
	@ResponseBody
	@RequiresPermissions("passchecksub:passCheckSub:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "出门条明细数据导入模板.xlsx";
    		List<PassCheckSub> list = Lists.newArrayList(); 
    		new ExportExcel("出门条明细数据", PassCheckSub.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}