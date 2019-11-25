/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.warninginfo.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.illegalinfo.entity.IllegalInfo;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.userpasscord.entity.UserPassRecord;
import com.jeeplus.modules.vehicleaccessrecord.entity.VehicleAccessRecord;
import com.jeeplus.modules.vehicleaccessrecord.service.VehicleAccessRecordService;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
import com.jeeplus.modules.warninginfo.service.WarningInfoService;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
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
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * 报警信息Controller
 * @author 张鲁蒙
 * @version 2019-03-05
 */
@Controller
@RequestMapping(value = "${adminPath}/warninginfo/warningInfo")
public class WarningInfoController extends BaseController {

	@Autowired
	private WarningInfoService warningInfoService;
	@Autowired
	private VehicleAccessRecordService vehicleAccessRecordService;

	@ModelAttribute
	public WarningInfo get(@RequestParam(required=false) String id) {
		WarningInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = warningInfoService.get(id);
		}
		if (entity == null){
			entity = new WarningInfo();
		}
		return entity;
	}
	
	/**
	 * 报警信息列表页面
	 */

	@RequestMapping(value = {"list", ""})
	public String list(WarningInfo warningInfo, Model model) {
		model.addAttribute("warningInfo", warningInfo);
		return "modules/warninginfo/warningInfoList";
	}
	
		/**
	 * 报警信息列表数据
	 */
	@ResponseBody
	@RequestMapping(value = "data")
	public Map<String, Object> data(WarningInfo warningInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<WarningInfo> page = new Page<>();
		if(warningInfo.getSearchFlag()!=null&&"1".equals(warningInfo.getSearchFlag())){
			page = warningInfoService.findPage(new Page<WarningInfo>(request, response), warningInfo);
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑报警信息表单页面
	 */
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, WarningInfo warningInfo, Model model) {
		model.addAttribute("warningInfo", warningInfo);
		model.addAttribute("mode", mode);
		return "modules/warninginfo/warningInfoForm";
	}

	/**
	 * 保存报警信息
	 */
	@ResponseBody
	@RequestMapping(value = "save")
	public AjaxJson save(WarningInfo warningInfo, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(warningInfo);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//取消违章标记
		VehicleAccessRecord vehicleaccessRecord = new VehicleAccessRecord();
		vehicleaccessRecord.setPeccancy("0");
		vehicleaccessRecord.setVehicleNo(warningInfo.getVehicleNo());
		vehicleAccessRecordService.savePeccancyInfo(vehicleaccessRecord);//保存
		//新增或编辑表单保存
		warningInfo.setState("2");
        warningInfo.setDataType("0");
		warningInfoService.save(warningInfo);//修改
		WarningInfo wi = warningInfoService.get(warningInfo);
		InterfaceTest ift = new InterfaceTest();
		ift.InterWarn(wi);
		j.setSuccess(true);
		j.setMsg("解除报警信息成功");
		return j;
	}
	
	/**
	 * 删除报警信息
	 */
	@ResponseBody
	@RequestMapping(value = "delete")
	public AjaxJson delete(WarningInfo warningInfo) {
		AjaxJson j = new AjaxJson();
		warningInfoService.delete(warningInfo);
		j.setMsg("删除报警信息成功");
		return j;
	}
	
	/**
	 * 批量删除报警信息
	 */
	@ResponseBody
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			warningInfoService.delete(warningInfoService.get(id));
		}
		j.setMsg("删除报警信息成功");
		return j;
	}
	
	/**
	 * 导出excel文件
	 */
	@ResponseBody
    @RequestMapping(value = "export")
    public AjaxJson exportFile(WarningInfo warningInfo, HttpServletRequest request, HttpServletResponse response) {
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
			jsonConfig.setRootClass(WarningInfo.class);
			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));

			warningInfo = (WarningInfo)JSONObject.toBean(jo,WarningInfo.class);

            String fileName = "报警信息"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
            Page<WarningInfo> page = warningInfoService.findPage(new Page<WarningInfo>(request, response, -1), warningInfo);
    		new ExportExcel("报警信息", WarningInfo.class).setDataList(page.getList()).write(response, fileName).dispose();
    		j.setSuccess(true);
    		j.setMsg("导出成功！");
    		return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出报警信息记录失败！失败信息："+e.getMessage());
		}
			return j;
    }

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
    @RequestMapping(value = "import")
   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<WarningInfo> list = ei.getDataList(WarningInfo.class);
			for (WarningInfo warningInfo : list){
				try{
					warningInfoService.save(warningInfo);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条报警信息记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条报警信息记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入报警信息失败！失败信息："+e.getMessage());
		}
		return j;
    }
	
	/**
	 * 下载导入报警信息数据模板
	 */
	@ResponseBody
    @RequestMapping(value = "import/template")
     public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
            String fileName = "报警信息数据导入模板.xlsx";
    		List<WarningInfo> list = Lists.newArrayList(); 
    		new ExportExcel("报警信息数据", WarningInfo.class, 1).setDataList(list).write(response, fileName).dispose();
    		return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
    }

}