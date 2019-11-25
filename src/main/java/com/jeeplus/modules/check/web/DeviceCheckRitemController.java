/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.check.entity.DeviceCheckRecord;
import com.jeeplus.modules.check.entity.DeviceCheckRitem;
import com.jeeplus.modules.check.service.DeviceCheckRecordService;
import com.jeeplus.modules.check.service.DeviceCheckRitemService;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 员工通行记录Controller
 * @author 汤进国
 * @version 2019-03-16
 */
@Controller
@RequestMapping(value = "${adminPath}/check/deviceCheckRitem")
public class DeviceCheckRitemController extends BaseController {

	@Autowired
	private DeviceCheckRitemService deviceCheckRitemService;
	@Autowired
	private DeviceCheckRecordService deviceCheckRecordService;
	@ModelAttribute
	public DeviceCheckRitem get(@RequestParam(required=false) String id) {
		DeviceCheckRitem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = deviceCheckRitemService.get(id);
		}
		if (entity == null){
			entity = new DeviceCheckRitem();
		}
		return entity;
	}

	/**
	 * 设备点检详情结果
	 */
	@RequiresPermissions("check:deviceCheckRitem:list")
	@RequestMapping(value = {"result"})
	public String result(DeviceCheckRitem deviceCheckRitem, Model model) {
		/*WorkStation w = new WorkStation();
		w.setType("1");
		List<WorkStation> list =workStationService.findList(w);
		model.addAttribute("list",list);*/
		String recordId = deviceCheckRitem.getRecordId();
		System.out.println("-------------------------"+recordId);
		DeviceCheckRecord record = deviceCheckRecordService.get(recordId);
		System.out.println("=========================="+record.getWorkingGroup());
		List<DeviceCheckRitem> deviceCheckRitemList = deviceCheckRitemService.findList(deviceCheckRitem);
		model.addAttribute("deviceCheckRecord", record);
		model.addAttribute("deviceCheckRitemList", deviceCheckRitemList);
		return "modules/check/deviceCheckResultForm";
	}

	/**
	 * 设备点检配置子项页面
	 */
	@RequiresPermissions("check:deviceCheckRitem:list")
	@RequestMapping(value = {"list", ""})
	public String list(DeviceCheckRitem deviceCheckRitem, Model model) {
		/*WorkStation w = new WorkStation();
		w.setType("1");
		List<WorkStation> list =workStationService.findList(w);
		model.addAttribute("list",list);*/
		String recordId = deviceCheckRitem.getRecordId();
		System.out.println("-------------------------"+recordId);
		DeviceCheckRecord record = deviceCheckRecordService.get(recordId);
		System.out.println("=========================="+record.getWorkingGroup());
		List<DeviceCheckRitem> deviceCheckRitemList = deviceCheckRitemService.findList(deviceCheckRitem);
		model.addAttribute("deviceCheckRecord", record);
		model.addAttribute("deviceCheckRitemList", deviceCheckRitemList);
		return "modules/check/deviceCheckRitemList";
	}


	/**
	 * 设备点检子项数据
	 */
	@ResponseBody
	@RequiresPermissions("check:DeviceCheckRitem:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DeviceCheckRitem deviceCheckRitem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DeviceCheckRitem> page = new Page<>();
		System.out.println("进来了...");
		if(deviceCheckRitem.getSearchFlag()!=null&&"1".equals(deviceCheckRitem.getSearchFlag())){
			page = deviceCheckRitemService.findPage(new Page<DeviceCheckRitem>(request, response), deviceCheckRitem);
			System.out.println("又进来了...");
		}
		System.out.println("返回去...");
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑员工通行记录表单页面
	 */
	@RequiresPermissions(value={"check:DeviceCheckRitem:view","check:DeviceCheckRitem:add","check:DeviceCheckRitem:edit"},logical= Logical.OR)
	@RequestMapping(value = "form")
	public String form(DeviceCheckRitem deviceCheckRitem, Model model) {
		model.addAttribute("deviceCheckRitem", deviceCheckRitem);
		System.out.println("--------------------"+deviceCheckRitem.getRecordId());
		return "modules/check/deviceCheckRitemForm";
	}



	/**
	 * 保存配置信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"check:deviceCheckRitem:add","check:deviceCheckRitem:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(DeviceCheckRitem deviceCheckRitem, Model model, HttpServletRequest request) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(deviceCheckRitem);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		Map<String,String> map = new HashMap<>();
		Enumeration enumeration = request.getParameterNames();
		while(enumeration.hasMoreElements()){
			String paraName = (String) enumeration.nextElement();
			map.put(paraName,request.getParameter(paraName));
		}
		String[] ids = request.getParameterValues("id");
		for (String id:ids
			 ) {
			System.out.println("==============ID==============="+id);
			DeviceCheckRitem ritem = deviceCheckRitemService.get(id);
			System.out.println();
			ritem.setCheckResult(request.getParameter(id+"result"));
			ritem.setRemarks(request.getParameter(id+"remarks"));
			deviceCheckRitemService.update(ritem);
			System.out.println("更新完成.....");
		}
		System.out.println(map);
		//deviceCheckRitemService.save(deviceCheckRitem);//保存
		j.setSuccess(true);
		j.setMsg("点检信息记录成功");
		DeviceCheckRecord deviceCheckRecord = deviceCheckRecordService.get(request.getParameter("recordId"));
		deviceCheckRecord.setIsFinished("1");//将是否完成状态改为1
		deviceCheckRecord.setGoupWorker(request.getParameter("groupWorker"));//补充点检人员信息
		deviceCheckRecord.setGroupLeader(request.getParameter("groupLeader"));//补充班组长信息
		deviceCheckRecord.setWorkingGroup(request.getParameter("workingGroup"));//补充班组信息
		Date date = new Date();
		deviceCheckRecord.setUpdateDate(date);//修改更新时间
		deviceCheckRecordService.finishRecord(deviceCheckRecord);
		return j;
	}




	/*
	*
	* @description 添加配置项目
	* @author yuzhongtong
	* @date 2019/6/11
	* @time 23:03
	* @param [deviceCheckRitem, model]
	* @return java.lang.String
	*/
	@RequiresPermissions(value={"check:DeviceCheckRitem:view","check:DeviceCheckRitem:add","check:DeviceCheckRitem:edit"},logical= Logical.OR)
	@RequestMapping(value = "addRitem")
	public String addRitem(DeviceCheckRitem deviceCheckRitem, Model model) {
//		System.out.println("-----------------------------------------"+deviceCheckRitem.getRitemId());
		model.addAttribute("deviceCheckRitem", deviceCheckRitem);
		return "modules/check/deviceCheckRitemForm";
	}

	/**
	 * 保存配置信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"check:deviceCheckRitem:add","check:deviceCheckRitem:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveRitem")
	public AjaxJson saveRitem(DeviceCheckRitem deviceCheckRitem, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(deviceCheckRitem);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		deviceCheckRitemService.save(deviceCheckRitem);//保存
		j.setSuccess(true);
		j.setMsg("点检信息配置成功");
		return j;
	}

	/**
	 * 查看，增加，编辑设备点检配置详情
	 */
	@RequiresPermissions(value={"check:DeviceCheckRitem:view","check:DeviceCheckRitem:add","check:DeviceCheckRitem:edit"},logical= Logical.OR)
	@RequestMapping(value = "viewDetail")
	public String viewDetail(DeviceCheckRitem deviceCheckRitem, Model model) {
		System.out.println("---------------------------"+deviceCheckRitem.getId());
		String id = deviceCheckRitem.getId();
		deviceCheckRitem = deviceCheckRitemService.get(id);
		model.addAttribute("deviceCheckRitem", deviceCheckRitem);
		return "modules/check/deviceCheckRitemDetail";
	}

//
//	/**
//	 * 删除员工通行记录
//	 */
//	@ResponseBody
//	@RequiresPermissions("userpasscord:DeviceCheckRitem:del")
//	@RequestMapping(value = "delete")
//	public AjaxJson delete(DeviceCheckRitem DeviceCheckRitem) {
//		AjaxJson j = new AjaxJson();
//		deviceCheckRitemService.delete(DeviceCheckRitem);
//		j.setMsg("删除员工通行记录成功");
//		return j;
//	}
//
	/**
	 * 批量删除员工通行记录
	 */
	@ResponseBody
	@RequiresPermissions("check:deviceCheckRitem:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			deviceCheckRitemService.delete(deviceCheckRitemService.get(id));
		}
		j.setMsg("删除配置信息成功");
		return j;
	}
//
//	/**
//	 * 导出excel文件
//	 */
//	@ResponseBody
//	@RequiresPermissions("userpasscord:DeviceCheckRitem:export")
//    @RequestMapping(value = "export")
//    public AjaxJson exportFile(DeviceCheckRitem DeviceCheckRitem, HttpServletRequest request, HttpServletResponse response) {
//		AjaxJson j = new AjaxJson();
//		try {
//			String  param= URLDecoder.decode(request.getParameter("data"),"UTF-8");
//			JSONObject jo = JSONObject.fromObject(param);
//			if(jo.get("beginpassTime")==null||"".equals(jo.get("beginpassTime"))){
//				jo.remove("beginpassTime");
//			}
//			if(jo.get("endpassTime")==null||"".equals(jo.get("endpassTime"))){
//				jo.remove("endpassTime");
//			}
//			JsonRitem jsonRitem = new JsonRitem();
//			jsonRitem.setRootClass(DeviceCheckRitem.class);
//			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
//			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));
//			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));
//			DeviceCheckRitem = (DeviceCheckRitem)JSONObject.toBean(jo,DeviceCheckRitem.class);
//
//            String fileName = "员工通行记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
//            Page<DeviceCheckRitem> page = deviceCheckRitemService.findPage(new Page<DeviceCheckRitem>(request, response, -1), DeviceCheckRitem);
//    		new ExportExcel("员工通行记录", DeviceCheckRitem.class).setDataList(page.getList()).write(response, fileName).dispose();
//    		j.setSuccess(true);
//    		j.setMsg("导出成功！");
//    		return j;
//		} catch (Exception e) {
//			j.setSuccess(false);
//			j.setMsg("导出员工通行记录记录失败！失败信息："+e.getMessage());
//		}
//			return j;
//    }
//
//	/**
//	 * 导入Excel数据
//
//	 */
//	@ResponseBody
//	@RequiresPermissions("userpasscord:DeviceCheckRitem:import")
//    @RequestMapping(value = "import")
//   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		try {
//			int successNum = 0;
//			int failureNum = 0;
//			StringBuilder failureMsg = new StringBuilder();
//			ImportExcel ei = new ImportExcel(file, 1, 0);
//			List<DeviceCheckRitem> list = ei.getDataList(DeviceCheckRitem.class);
//			for (DeviceCheckRitem DeviceCheckRitem : list){
//				try{
//					deviceCheckRitemService.save(DeviceCheckRitem);
//					successNum++;
//				}catch(ConstraintViolationException ex){
//					failureNum++;
//				}catch (Exception ex) {
//					failureNum++;
//				}
//			}
//			if (failureNum>0){
//				failureMsg.insert(0, "，失败 "+failureNum+" 条员工通行记录记录。");
//			}
//			j.setMsg( "已成功导入 "+successNum+" 条员工通行记录记录"+failureMsg);
//		} catch (Exception e) {
//			j.setSuccess(false);
//			j.setMsg("导入员工通行记录失败！失败信息："+e.getMessage());
//		}
//		return j;
//    }
//
//	/**
//	 * 下载导入员工通行记录数据模板
//	 */
//	@ResponseBody
//	@RequiresPermissions("userpasscord:DeviceCheckRitem:import")
//    @RequestMapping(value = "import/template")
//     public AjaxJson importFileTemplate(HttpServletResponse response) {
//		AjaxJson j = new AjaxJson();
//		try {
//            String fileName = "员工通行记录数据导入模板.xlsx";
//    		List<DeviceCheckRitem> list = Lists.newArrayList();
//    		new ExportExcel("员工通行记录数据", DeviceCheckRitem.class, 1).setDataList(list).write(response, fileName).dispose();
//    		return null;
//		} catch (Exception e) {
//			j.setSuccess(false);
//			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
//		}
//		return j;
//    }
//	public static boolean isInteger(String str) {
//		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//		return pattern.matcher(str).matches();
//	}
//	/**
//	 * 6号岗亭人行道闸刷卡器
//	 */
//	@ResponseBody
//	@RequestMapping(value = "checkUserAccesspower")
//	public AjaxJson checkUserAccesspower(String ickh,HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		try {
//
//			List<DeviceCheckRitem> l =deviceCheckRitemService.findInfoByCard(ickh);
//			//如果有，放行
//			if(l.size()>0){
//				//是否是家属
//				String a = l.get(0).getPersonnelId().substring(0,2);
//				String b = l.get(0).getPersonnelId().substring(2,3);
//				if("JS".equals(a)&&isInteger(b)){
//					j.setSuccess(false);
//					return j;
//				}
//				l.get(0).setPassTime(new Date());
//				l.get(0).setPassStation(request.getRemoteAddr());
//				l.get(0).setCardSerial(ickh);
//				deviceCheckRitemService.save(l.get(0));
//				j.setSuccess(true);
//				return j;
//			}
//			j.setSuccess(true);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return j;
//	}
//	/**
//	 * 6号岗亭人行道闸身份证
//	 */
//	@ResponseBody
//	@RequestMapping(value = "checkUserpower")
//	public AjaxJson checkUserpower(String ickh,HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		try {
//			//查询是否有预约
//			List<ConsignTmp> lc = consignService.findInfoByIdCard(ickh);
//			if(lc!=null&&lc.size()>0){
//				//放行并保存
//				DeviceCheckRitem u = new DeviceCheckRitem();
//				u.setPassTime(new Date());
//				u.setCardSerial(ickh);
//				u.setDeptName(lc.get(0).getConsignDept());
//				u.setName(lc.get(0).getUserName());
//				u.setPassStation(request.getRemoteAddr());
//
//				deviceCheckRitemService.save(u);
//				j.setSuccess(true);
//				j.setMsg("欢迎，预约部门："+lc.get(0).getConsignDept());
//				return j;
//			}
//			j.setSuccess(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return j;
//	}
//	/**
//	 * 1/5号岗亭人行道闸
//	 */
//	@ResponseBody
//	@RequestMapping(value = "saveUserAccessRecord")
//	public AjaxJson saveUserAccessRecord(String ickh,HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		try {
//
//			List<DeviceCheckRitem> l =deviceCheckRitemService.findInfoByCard(ickh);
//
//			//如果有，放行
//			if(l.size()>0){
//					//放行//保存记录
//
//				l.get(0).setPassTime(new Date());
//				l.get(0).setPassStation(request.getRemoteAddr());
//				l.get(0).setCardSerial(ickh);
//				deviceCheckRitemService.save(l.get(0));
//				j.setMsg("欢迎，"+l.get(0).getPersonnelId());
//				j.setSuccess(true);
//				return j;
//			}
//
//			//如果没有，查询是否有预约
//			List<ConsignTmp> lc = consignService.findInfoByIdCard(ickh);
//			if(lc!=null&&lc.size()>0){
//				//放行并保存
//				DeviceCheckRitem u = new DeviceCheckRitem();
//				u.setPassTime(new Date());
//				u.setCardSerial(ickh);
//				u.setDeptName(lc.get(0).getDealDept());
//				u.setName(lc.get(0).getUserName());
//				u.setPassStation(request.getRemoteAddr());
//				deviceCheckRitemService.save(u);
//                j.setMsg("欢迎，预约部门："+lc.get(0).getConsignDept());
//				j.setSuccess(true);
//				return j;
//			}
//
//			j.setMsg("无预约信息");
//			j.setSuccess(false);
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//		return j;
//	}
//
//	@ResponseBody
//	@RequestMapping(value = "saveUserPassInfo")
//	public AjaxJson saveUser() {
//		AjaxJson j = new AjaxJson();
//		deviceCheckRitemService userPass = SpringContextHolder.getBean(deviceCheckRitemService.class);
//		try {
//			userPass.deleteUserPassInfo();
//			System.out.println("更新员工信息："+new Date());
//			MultiDBUtils md = MultiDBUtils.get("ryxx");
//			//同步磅单信息
//			List<DeviceCheckRitem> l = md.queryList(" SELECT p.SMT_PersonnelID personnelId,p.SMT_Name name,p.SMT_Sex sex,d.SMT_DeptName1 deptName,c.SMT_CardSerial cardSerial  \n" +
//					"\t  FROM smart_personnel p  left join smart_card c on p.SMT_PersonnelID = c.SMT_PersonnelID   \n" +
//					"\t   left join Smart_Dept1 d  on p.SMT_DeptCode1 = d.SMT_DeptCode1 ", DeviceCheckRitem.class);
//
//			//如果有，放行
//			if(l.size()>0){
//				for(DeviceCheckRitem u : l){
//
//					u.setId(IdGen.uuid());
//					userPass.saveUserPassInfo(u);
//				}
//				j.setSuccess(true);
//				j.setMsg("操作成功");
//				return j;
//			}
//			j.setSuccess(false);
//			j.setMsg("操作失败");
//
//
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return j;
//	}

}