/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.print.web;

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
import com.jeeplus.modules.print.entity.Print;
import com.jeeplus.modules.print.service.PrintService;

/**
 * 打印Controller
 * @author 打印
 * @version 2019-03-20
 */
@Controller
@RequestMapping(value = "${adminPath}/print/print")
public class PrintController extends BaseController {

	@Autowired
	private PrintService printService;
	
	@ModelAttribute
	public Print get(@RequestParam(required=false) String id) {
		Print entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = printService.get(id);
		}
		if (entity == null){
			entity = new Print();
		}
		return entity;
	}
	
	/**
	 * 打印列表页面
	 */
	@RequiresPermissions("print:print:list")
	@RequestMapping(value = {"list", ""})
	public String list(Print print, Model model) {
		model.addAttribute("print", print);
		return "modules/print/printList";
	}
	
		/**
	 * 打印列表数据
	 */
	@ResponseBody
	@RequiresPermissions("print:print:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Print print, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Print> page = printService.findPage(new Page<Print>(request, response), print); 
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑打印表单页面
	 */
	@RequiresPermissions(value={"print:print:view","print:print:add","print:print:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, Print print, Model model) {
		model.addAttribute("print", print);
		model.addAttribute("mode", mode);
		return "modules/print/printForm";
	}

	/**
	 * 保存打印
	 */
	@ResponseBody
	@RequiresPermissions(value={"print:print:add","print:print:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Print print, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(print);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		printService.save(print);//保存
		j.setSuccess(true);
		j.setMsg("保存打印成功");
		return j;
	}
	
	/**
	 * 删除打印
	 */
	@ResponseBody
	@RequiresPermissions("print:print:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Print print) {
		AjaxJson j = new AjaxJson();
		printService.delete(print);
		j.setMsg("删除打印成功");
		return j;
	}
	
	/**
	 * 批量删除打印
	 */
	@ResponseBody
	@RequiresPermissions("print:print:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			printService.delete(printService.get(id));
		}
		j.setMsg("删除打印成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("print:print:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Print print, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "打印"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Print> page = printService.findPage(new Page<Print>(request, response, -1), print);
    		new ExportExcel("打印", Print.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出打印记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("print:print:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Print> list = ei.getDataList(Print.class);
			for (Print print : list){
				try{
					printService.save(print);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条打印记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条打印记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入打印失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入打印数据模板
	 */
	@ResponseBody
	@RequiresPermissions("print:print:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "打印数据导入模板.xlsx";
    		List<Print> list = Lists.newArrayList(); 
    		new ExportExcel("打印数据", Print.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}