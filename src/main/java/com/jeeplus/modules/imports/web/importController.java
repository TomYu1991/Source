/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.imports.web;

import com.alibaba.fastjson.JSON;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.interfaceTest.entry.JsonType;
import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.synchroinfo.entity.DataSynchroInfo;
import com.jeeplus.modules.synchroinfo.service.DataSynchroInfoService;
import com.jeeplus.modules.tools.service.SysDataSourceService;
import com.jeeplus.modules.tools.utils.MultiDBUtils;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
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
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入Controller
 * @author zhanglumeng 
 * @version 2019-02-22
 */
@Controller
@RequestMapping(value = "${adminPath}/imports/import")
public class importController extends BaseController {

	@Autowired
	private DataSynchroInfoService dataSynchroInfoService;

	@ModelAttribute
	public DataSynchroInfo get(@RequestParam(required=false) String id) {
		DataSynchroInfo entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = dataSynchroInfoService.get(id);
		}
		if (entity == null){
			entity = new DataSynchroInfo();
		}
		return entity;
	}
	/**
	 * synchroinfo列表页面
	 */
	@RequiresPermissions("imports:import:list")
	@RequestMapping(value = {"list", ""})
	public String list(DataSynchroInfo dataSynchroInfo, Model model) {
		model.addAttribute("dataSynchroInfo", dataSynchroInfo);
		return "modules/import/importList";
	}

	/**
	 * synchroinfo列表数据
	 */
	@ResponseBody
	@RequiresPermissions("synchroinfo:dataSynchroInfo:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DataSynchroInfo dataSynchroInfo, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DataSynchroInfo> page = new Page<>();
		if (dataSynchroInfo.getSearchFlag() != null && "1".equals(dataSynchroInfo.getSearchFlag())) {
			page = dataSynchroInfoService.findPage(new Page<DataSynchroInfo>(request, response), dataSynchroInfo);
		}
		return getBootstrapData(page);
	}
	/**
	 * 导入委托单Excel数据
	 */
	@ResponseBody
	@RequestMapping(value = "import1")
	private AjaxJson importFile1(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {

			ImportExcel ei = new ImportExcel(file, 1, 0);
			List list = ei.getDataList(Consign.class);
			JsonType jt = new JsonType();
			jt.setFlag("1");
            jt.setType("wtd");
			MultiDBUtils md = MultiDBUtils.get("native");
			String Ip = InetAddress.getLocalHost().getHostAddress();
			WorkStationService workStationService = SpringContextHolder.getBean(WorkStationService.class);
			WorkStation w = workStationService.queryWorkNameByStationIp(Ip);
            if(w != null){
				String workname = w.getWorkStation();

				InterfaceTest ift = new InterfaceTest();
				ift.commonwtd(jt,md,workname,list,"1");

				j.setSuccess(true);
				j.setMsg("导入委托单/预约单成功！");

            }else{
				j.setSuccess(false);
				j.setMsg("导入委托单/预约单失败!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			j.setSuccess(false);
			j.setMsg("导入委托单/预约单失败！失败信息：" + e.getMessage());
		}
		return j;
	}
	/**
	 * 导入出门条Excel数据
	 */
	@ResponseBody
	@RequestMapping(value = "import2")
	private AjaxJson importFile2(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {

			ImportExcel ei = new ImportExcel(file, 1, 0);
			List list = ei.getDataList(PassCheck.class);
			JsonType jt = new JsonType();
			jt.setFlag("1");
			jt.setType("cmt");
			MultiDBUtils md = MultiDBUtils.get("native");
			String Ip = InetAddress.getLocalHost().getHostAddress();
			WorkStationService workStationService = SpringContextHolder.getBean(WorkStationService.class);
			WorkStation w = workStationService.queryWorkNameByStationIp(Ip);
			if(w != null){
				String workname = w.getWorkStation();

				InterfaceTest ift = new InterfaceTest();
				ift.commoncmt(jt,md,workname,list,"1");

				j.setSuccess(true);
				j.setMsg("导入出门条成功！");

			}else{
				j.setSuccess(false);
				j.setMsg("导入出门条失败!");
			}
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入出门条失败！失败信息：" + e.getMessage());
		}
		return j;
	}
	/**
	 * 导入车辆信息Excel数据
	 */
	@ResponseBody
	@RequestMapping(value = "import3")
	private AjaxJson importFile3(@RequestParam("file") MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {

			ImportExcel ei = new ImportExcel(file, 1, 0);
			List list = ei.getDataList(VehicleInfo.class);
			JsonType jt = new JsonType();
			jt.setFlag("1");
			jt.setType("clxx");
			MultiDBUtils md = MultiDBUtils.get("native");
			String Ip = InetAddress.getLocalHost().getHostAddress();
			WorkStationService workStationService = SpringContextHolder.getBean(WorkStationService.class);
			WorkStation w = workStationService.queryWorkNameByStationIp(Ip);
			if(w != null){
				String workname = w.getWorkStation();

				InterfaceTest ift = new InterfaceTest();
				ift.commonclxx(jt,md,workname,list,"1");

				j.setSuccess(true);
				j.setMsg("导入车辆信息成功！");

			}else{
				j.setSuccess(false);
				j.setMsg("导入车辆信息失败!");
			}
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入车辆信息失败！失败信息：" + e.getMessage());
		}
		return j;
	}
}