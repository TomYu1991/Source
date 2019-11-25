/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.passcheck.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.passcheck.service.PassCheckService;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
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
import java.util.List;
import java.util.Map;

/**
 * 出门条信息Controller
 * @author 汤进国
 * @version 2019-01-17
 */
@Controller
@RequestMapping(value = "${adminPath}/passcheck/passCheck")
public class PassCheckController extends BaseController {

	@Autowired
	private PassCheckService passCheckService;
	@ModelAttribute
	public PassCheck get(@RequestParam(required=false) String id) {
		PassCheck entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = passCheckService.get(id);
		}
		if (entity == null){
			entity = new PassCheck();
		}
		return entity;
	}
	
	/**
	 * 出门条信息列表页面
	 */
	@RequiresPermissions("passcheck:passCheck:list")
	@RequestMapping(value = {"list", ""})
	public String list(PassCheck passCheck, Model model) {
		model.addAttribute("passCheck", passCheck);
		return "modules/passcheck/passCheckList";
	}
	
		/**
	 * 出门条信息列表数据
	 */
	@ResponseBody
	@RequiresPermissions("passcheck:passCheck:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(PassCheck passCheck, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<PassCheck> page = new Page<>();
		if(passCheck.getSearchFlag()!=null&&"1".equals(passCheck.getSearchFlag())){
		 page = passCheckService.findPage(new Page<PassCheck>(request, response), passCheck);
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑出门条信息表单页面
	 */
	@RequiresPermissions(value={"passcheck:passCheck:view","passcheck:passCheck:add","passcheck:passCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(PassCheck passCheck, Model model) {
		model.addAttribute("passCheck", passCheck);
		return "modules/passcheck/passCheckForm";
	}

	/**
	 * 保存出门条信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"passcheck:passCheck:add","passcheck:passCheck:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(PassCheck passCheck, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(passCheck);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		passCheckService.save(passCheck);//保存
		j.setSuccess(true);
		j.setMsg("保存出门条信息成功");
		return j;
	}
	
	/**
	 * 删除出门条信息
	 */
	@ResponseBody
	@RequiresPermissions("passcheck:passCheck:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(PassCheck passCheck) {
		AjaxJson j = new AjaxJson();
		passCheckService.delete(passCheck);
		j.setMsg("删除出门条信息成功");
		return j;
	}
	
	/**
	 * 批量删除出门条信息
	 */
	@ResponseBody
	@RequiresPermissions("passcheck:passCheck:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			passCheckService.delete(passCheckService.get(id));
		}
		j.setMsg("删除出门条信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("passcheck:passCheck:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(PassCheck passCheck, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			String  param= URLDecoder.decode(request.getParameter("data"),"UTF-8");
			JSONObject jo = JSONObject.fromObject(param);

			if(jo.get("startTime")==null||"".equals(jo.get("startTime"))){
				jo.remove("startTime");
			}
			if(jo.get("endTime")==null||"".equals(jo.get("endTime"))){
				jo.remove("endTime");
			}
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.setRootClass(PassCheck.class);
			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

			passCheck = (PassCheck)JSONObject.toBean(jo,PassCheck.class);

            String fileName = "出门条信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<PassCheck> page = passCheckService.findPage(new Page<PassCheck>(request, response, -1), passCheck);
    		new ExportExcel("出门条信息", PassCheck.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出出门条信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("passcheck:passCheck:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<PassCheck> list = ei.getDataList(PassCheck.class);
			for (PassCheck passCheck : list){
				try{
					passCheckService.save(passCheck);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条出门条信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条出门条信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入出门条信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入出门条信息数据模板
	 */
	@ResponseBody
	@RequiresPermissions("passcheck:passCheck:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "出门条信息数据导入模板.xlsx";
    		List<PassCheck> list = Lists.newArrayList(); 
    		new ExportExcel("出门条信息数据", PassCheck.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }


	/**
	 * 下载导入出门条信息数据模板
	 */
	@ResponseBody
	@RequestMapping(value = "passCheckInterFace")
	public AjaxJson passCheckInterFace(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();

		return j;
	}

}