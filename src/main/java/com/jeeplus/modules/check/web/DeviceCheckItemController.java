/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.check.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.check.entity.DeviceCheckConfig;
import com.jeeplus.modules.check.entity.DeviceCheckItem;
import com.jeeplus.modules.check.service.DeviceCheckConfigService;
import com.jeeplus.modules.check.service.DeviceCheckItemService;
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
import java.util.Map;

/**
 * 员工通行记录Controller
 * @author 汤进国
 * @version 2019-03-16
 */
@Controller
@RequestMapping(value = "${adminPath}/check/deviceCheckItem")
public class DeviceCheckItemController extends BaseController {

	@Autowired
	private DeviceCheckItemService deviceCheckItemService;
	@Autowired
	private DeviceCheckConfigService deviceCheckConfigService;
	@ModelAttribute
	public DeviceCheckItem get(@RequestParam(required=false) String id) {
		DeviceCheckItem entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = deviceCheckItemService.get(id);
		}
		if (entity == null){
			entity = new DeviceCheckItem();
		}
		return entity;
	}
	
	/**
	 * 设备点检配置子项页面
	 */
	@RequiresPermissions("check:deviceCheckItem:list")
	@RequestMapping(value = {"list", ""})
	public String list(DeviceCheckItem deviceCheckItem, Model model) {
		/*WorkStation w = new WorkStation();
		w.setType("1");
		List<WorkStation> list =workStationService.findList(w);
		model.addAttribute("list",list);*/
		String id = deviceCheckItem.getConfigId();
//		System.out.println(id);
		DeviceCheckConfig deviceCheckConfig = deviceCheckConfigService.get(id);
		model.addAttribute("deviceCheckConfig",deviceCheckConfig);
		model.addAttribute("deviceCheckItem", deviceCheckItem);
		return "modules/check/deviceCheckItemList";
	}



	
	/**
	 * 设备点检列表数据
	 */
	@ResponseBody
	@RequiresPermissions("check:DeviceCheckItem:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(DeviceCheckItem deviceCheckItem, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<DeviceCheckItem> page = new Page<>();
		if(deviceCheckItem.getSearchFlag()!=null&&"1".equals(deviceCheckItem.getSearchFlag())){
			page = deviceCheckItemService.findPage(new Page<DeviceCheckItem>(request, response), deviceCheckItem);
		}
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑员工通行记录表单页面
	 */
	@RequiresPermissions(value={"check:DeviceCheckItem:view","check:DeviceCheckItem:add","check:DeviceCheckItem:edit"},logical= Logical.OR)
	@RequestMapping(value = "form")
	public String form(DeviceCheckItem deviceCheckItem, Model model) {
		model.addAttribute("deviceCheckItem", deviceCheckItem);
		System.out.println("--------------------"+deviceCheckItem.getConfigId());
		return "modules/check/deviceCheckItemForm";
	}




	/**
	 * 保存配置信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"check:deviceCheckItem:add","check:deviceCheckItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(DeviceCheckItem deviceCheckItem, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(deviceCheckItem);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		deviceCheckItemService.save(deviceCheckItem);//保存
		j.setSuccess(true);
		j.setMsg("点检信息配置成功");
		return j;
	}

	/*
	*
	* @description 添加配置项目
	* @author yuzhongtong
	* @date 2019/6/11
	* @time 23:03
	* @param [deviceCheckItem, model]
	* @return java.lang.String
	*/
	@RequiresPermissions(value={"check:DeviceCheckItem:view","check:DeviceCheckItem:add","check:DeviceCheckItem:edit"},logical= Logical.OR)
	@RequestMapping(value = "addItem")
	public String addItem(DeviceCheckItem deviceCheckItem, Model model) {
//		System.out.println("-----------------------------------------"+deviceCheckItem.getItemId());
		model.addAttribute("deviceCheckItem", deviceCheckItem);
		return "modules/check/deviceCheckItemForm";
	}

	/**
	 * 保存配置信息
	 */
	@ResponseBody
	@RequiresPermissions(value={"check:deviceCheckItem:add","check:deviceCheckItem:edit"},logical=Logical.OR)
	@RequestMapping(value = "saveItem")
	public AjaxJson saveItem(DeviceCheckItem deviceCheckItem, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(deviceCheckItem);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		deviceCheckItemService.save(deviceCheckItem);//保存
		j.setSuccess(true);
		j.setMsg("点检信息配置成功");
		return j;
	}

	/**
	 * 查看，增加，编辑设备点检配置详情
	 */
	@RequiresPermissions(value={"check:DeviceCheckItem:view","check:DeviceCheckItem:add","check:DeviceCheckItem:edit"},logical= Logical.OR)
	@RequestMapping(value = "viewDetail")
	public String viewDetail(DeviceCheckItem deviceCheckItem, Model model) {
		System.out.println("---------------------------"+deviceCheckItem.getId());
		String id = deviceCheckItem.getId();
		deviceCheckItem = deviceCheckItemService.get(id);
		model.addAttribute("deviceCheckItem", deviceCheckItem);
		return "modules/check/deviceCheckItemDetail";
	}

//
//	/**
//	 * 删除员工通行记录
//	 */
//	@ResponseBody
//	@RequiresPermissions("userpasscord:DeviceCheckItem:del")
//	@RequestMapping(value = "delete")
//	public AjaxJson delete(DeviceCheckItem DeviceCheckItem) {
//		AjaxJson j = new AjaxJson();
//		deviceCheckItemService.delete(DeviceCheckItem);
//		j.setMsg("删除员工通行记录成功");
//		return j;
//	}
//
	/**
	 * 批量删除员工通行记录
	 */
	@ResponseBody
	@RequiresPermissions("check:deviceCheckItem:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			deviceCheckItemService.delete(deviceCheckItemService.get(id));
		}
		j.setMsg("删除配置信息成功");
		return j;
	}
//
//	/**
//	 * 导出excel文件
//	 */
//	@ResponseBody
//	@RequiresPermissions("userpasscord:DeviceCheckItem:export")
//    @RequestMapping(value = "export")
//    public AjaxJson exportFile(DeviceCheckItem DeviceCheckItem, HttpServletRequest request, HttpServletResponse response) {
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
//			JsonItem jsonItem = new JsonItem();
//			jsonItem.setRootClass(DeviceCheckItem.class);
//			String[] dateFamates = new String[] {"yyyy-MM-dd HH:mm:ss"};
//			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));
//			JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFamates));
//			DeviceCheckItem = (DeviceCheckItem)JSONObject.toBean(jo,DeviceCheckItem.class);
//
//            String fileName = "员工通行记录"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
//            Page<DeviceCheckItem> page = deviceCheckItemService.findPage(new Page<DeviceCheckItem>(request, response, -1), DeviceCheckItem);
//    		new ExportExcel("员工通行记录", DeviceCheckItem.class).setDataList(page.getList()).write(response, fileName).dispose();
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
//	@RequiresPermissions("userpasscord:DeviceCheckItem:import")
//    @RequestMapping(value = "import")
//   	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
//		AjaxJson j = new AjaxJson();
//		try {
//			int successNum = 0;
//			int failureNum = 0;
//			StringBuilder failureMsg = new StringBuilder();
//			ImportExcel ei = new ImportExcel(file, 1, 0);
//			List<DeviceCheckItem> list = ei.getDataList(DeviceCheckItem.class);
//			for (DeviceCheckItem DeviceCheckItem : list){
//				try{
//					deviceCheckItemService.save(DeviceCheckItem);
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
//	@RequiresPermissions("userpasscord:DeviceCheckItem:import")
//    @RequestMapping(value = "import/template")
//     public AjaxJson importFileTemplate(HttpServletResponse response) {
//		AjaxJson j = new AjaxJson();
//		try {
//            String fileName = "员工通行记录数据导入模板.xlsx";
//    		List<DeviceCheckItem> list = Lists.newArrayList();
//    		new ExportExcel("员工通行记录数据", DeviceCheckItem.class, 1).setDataList(list).write(response, fileName).dispose();
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
//			List<DeviceCheckItem> l =deviceCheckItemService.findInfoByCard(ickh);
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
//				deviceCheckItemService.save(l.get(0));
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
//				DeviceCheckItem u = new DeviceCheckItem();
//				u.setPassTime(new Date());
//				u.setCardSerial(ickh);
//				u.setDeptName(lc.get(0).getConsignDept());
//				u.setName(lc.get(0).getUserName());
//				u.setPassStation(request.getRemoteAddr());
//
//				deviceCheckItemService.save(u);
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
//			List<DeviceCheckItem> l =deviceCheckItemService.findInfoByCard(ickh);
//
//			//如果有，放行
//			if(l.size()>0){
//					//放行//保存记录
//
//				l.get(0).setPassTime(new Date());
//				l.get(0).setPassStation(request.getRemoteAddr());
//				l.get(0).setCardSerial(ickh);
//				deviceCheckItemService.save(l.get(0));
//				j.setMsg("欢迎，"+l.get(0).getPersonnelId());
//				j.setSuccess(true);
//				return j;
//			}
//
//			//如果没有，查询是否有预约
//			List<ConsignTmp> lc = consignService.findInfoByIdCard(ickh);
//			if(lc!=null&&lc.size()>0){
//				//放行并保存
//				DeviceCheckItem u = new DeviceCheckItem();
//				u.setPassTime(new Date());
//				u.setCardSerial(ickh);
//				u.setDeptName(lc.get(0).getDealDept());
//				u.setName(lc.get(0).getUserName());
//				u.setPassStation(request.getRemoteAddr());
//				deviceCheckItemService.save(u);
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
//		deviceCheckItemService userPass = SpringContextHolder.getBean(deviceCheckItemService.class);
//		try {
//			userPass.deleteUserPassInfo();
//			System.out.println("更新员工信息："+new Date());
//			MultiDBUtils md = MultiDBUtils.get("ryxx");
//			//同步磅单信息
//			List<DeviceCheckItem> l = md.queryList(" SELECT p.SMT_PersonnelID personnelId,p.SMT_Name name,p.SMT_Sex sex,d.SMT_DeptName1 deptName,c.SMT_CardSerial cardSerial  \n" +
//					"\t  FROM smart_personnel p  left join smart_card c on p.SMT_PersonnelID = c.SMT_PersonnelID   \n" +
//					"\t   left join Smart_Dept1 d  on p.SMT_DeptCode1 = d.SMT_DeptCode1 ", DeviceCheckItem.class);
//
//			//如果有，放行
//			if(l.size()>0){
//				for(DeviceCheckItem u : l){
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