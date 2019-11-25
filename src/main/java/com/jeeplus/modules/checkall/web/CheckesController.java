/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.checkall.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.jeeplus.modules.checkall.entity.Checkes;
import com.jeeplus.modules.checkequipment.entity.Equipment;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
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

import com.google.common.collect.Lists;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.modules.checkall.service.CheckesService;

/**
 * 点检表Controller
 * @author 张鲁蒙
 * @version 2019-01-07
 */
@Controller
@RequestMapping(value = "${adminPath}/checkall/checkes")
public class CheckesController extends BaseController {

	@Autowired
	private CheckesService checkesService;
	
	@ModelAttribute
	public Checkes get(@RequestParam(required=false) String id) {
		Checkes entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = checkesService.get(id);
		}
		if (entity == null){
			entity = new Checkes();
		}
		return entity;
	}
	
	/**
	 * 点检表列表页面
	 */
	@RequiresPermissions("checkall:checkes:list")
	@RequestMapping(value = {"list", ""})
	public String list(Checkes checkes, Model model) {
		model.addAttribute("checkes", checkes);
		return "modules/checkall/checkesList";
	}
	
		/**
	 * 点检表列表数据
	 */
	@ResponseBody
	@RequiresPermissions("checkall:checkes:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(Checkes checkes, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Checkes> page = new Page<>();
		if(checkes.getSearchFlag()!=null&&"1".equals(checkes.getSearchFlag())){
			page = checkesService.findPage(new Page<Checkes>(request, response), checkes);
		}
		return getBootstrapData(page);
	}

	/**
	 * 新增、修改点检表表单页面
	 */
	@RequiresPermissions(value={"checkall:checkes:view","checkall:checkes:add","checkall:checkes:edit"},logical=Logical.OR)
	@RequestMapping(value = "form")
	public String form(Checkes checkes, Model model) {
		model.addAttribute("checkes", checkes);
		return "modules/checkall/checkesFormPlan";
	}
    /**
     * 编辑点检表表单页面
     */
    @RequiresPermissions(value={"checkall:checkes:inspect"},logical=Logical.OR)
    @RequestMapping(value = "checck")
    public String formChecckes(Checkes checkes, Model model) {
        model.addAttribute("checkes", checkes);
        return "modules/checkall/checkesFormCheckes";
    }
	/**
	 * 审核、查看点检表表单页面
	 */
	@RequiresPermissions(value={"checkall:checkes:view","checkall:checkes:checkss"},logical=Logical.OR)
	@RequestMapping(value = "auditing")
	public String formAuditing(Checkes checkes, Model model) {
		model.addAttribute("checkes", checkes);
		return "modules/checkall/checkesFormAuditing";
	}

	/**
	 * 保存点检表
	 */
	@ResponseBody
	@RequiresPermissions(value={"checkall:checkes:add","checkall:checkes:edit","checkall:checkes:checkss"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(Checkes checkes, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(checkes);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		if ("已检查".equals(checkes.getCheckState())) {
			checkes.setCheckState("已审核");
            checkes.setCheckUsage(UserUtils.getUser());
		}
		if ("已计划".equals(checkes.getCheckState()) && !"".equals(checkes.getCheckResult()) && !"".equals(checkes.getCheckInspector())) {
			checkes.setCheckState("已检查");
		}
		if (!"".equals(checkes.getCheckNumber()) && null != checkes.getEquipment() && null == checkes.getCheckState()) {
			checkes.setCheckState("已计划");
		}

		try {
			getCheckes(checkes);
			checkesService.save(checkes);

			j.setSuccess(true);
			j.setMsg("保存相关信息成功");
			j.setData(checkes);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("请输入点检标准号" );
		}

		return j;
	}
	
	/**
	 * 删除点检表
	 */
	@ResponseBody
	@RequiresPermissions("checkall:checkes:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(Checkes checkes) {
		AjaxJson j = new AjaxJson();
		checkesService.delete(checkes);
		j.setMsg("删除点检表成功");
		return j;
	}
	
	/**
	 * 批量删除点检表
	 */
	@ResponseBody
	@RequiresPermissions("checkall:checkes:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			checkesService.delete(checkesService.get(id));
		}
		j.setMsg("删除点检表成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("checkall:checkes:export")
    @RequestMapping(value = "export")
    public AjaxJson exportFile(Checkes checkes, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "点检表"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<Checkes> page = checkesService.findPage(new Page<Checkes>(request, response, -1), checkes);
    		new ExportExcel("点检表", Checkes.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出点检表记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("checkall:checkes:import")
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<Checkes> list = ei.getDataList(Checkes.class);
			for (Checkes checkes : list){
				try{
					checkesService.save(checkes);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条点检表记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条点检表记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入点检表失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入点检表数据模板
	 */
	@ResponseBody
	@RequiresPermissions("checkall:checkes:import")
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "点检表数据导入模板.xlsx";
    		List<Checkes> list = Lists.newArrayList();
    		new ExportExcel("点检表数据", Checkes.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }
	/**
	 *返显设备检查方法和点检设备描述
	 * @param checkes
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = "getEquipment")
	public AjaxJson getEquipment(Checkes checkes, Model model) throws Exception {
		AjaxJson j = new AjaxJson();
		try {
			getCheckes(checkes);
			j.setSuccess(true);
			j.setMsg("保存相关信息成功");
			j.setData(checkes);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("请输入点检标准号" );
		}

		return j;

	}
	/**存放字段信息 */
	Checkes Checke = new Checkes();

	/**
	 * 查询设备检查方法和点检设备描述
	 * @param checkes
	 * @return
	 */
	private Checkes getCheckes(Checkes checkes) throws Exception{
		Equipment equipname = checkes.getEquipment();
		String equip = equipname.getEquipment();
		if (null != equip && !"".equals(equip)) {
			Checke= checkesService.getEquipment(equip);

			checkes.setCheckMethod(Checke.getCheckMethod());
			checkes.setMaintenExplanation(Checke.getMaintenExplanation());

		}
		return checkes;
	}
}