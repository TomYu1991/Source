/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.swipecard.web;

import com.google.common.collect.Lists;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.common.utils.excel.ExportExcel;
import com.jeeplus.common.utils.excel.ImportExcel;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.passcheck.service.PassCheckService;
import com.jeeplus.modules.swipecard.entity.SwipeCard;
import com.jeeplus.modules.swipecard.service.SwipeCardService;
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
import java.util.List;
import java.util.Map;

/**
 * 刷卡人员权限Controller
 * @author zhanglumeng 
 * @version 2019-02-13
 */
@Controller
@RequestMapping(value = "${adminPath}/swipecard/swipeCard")
public class SwipeCardController extends BaseController {

	@Autowired
	private SwipeCardService swipeCardService;
    @Autowired
	private PassCheckService passCheckService;

	@ModelAttribute
	public SwipeCard get(@RequestParam(required=false) String id) {
		SwipeCard entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = swipeCardService.get(id);
		}
		if (entity == null){
			entity = new SwipeCard();
		}
		return entity;
	}

	/**
	 * 刷卡人员权限列表页面
	 */
	@RequiresPermissions("swipecard:swipeCard:list")
	@RequestMapping(value = {"list", ""})
	public String list(SwipeCard swipeCard, Model model) {
		model.addAttribute("swipeCard", swipeCard);
		return "modules/swipecard/swipeCardList";
	}

	/**
	 * 刷卡人员权限列表数据
	 */
	@ResponseBody
	@RequiresPermissions("swipecard:swipeCard:list")
	@RequestMapping(value = "data")
	public Map<String, Object> data(SwipeCard swipeCard, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<SwipeCard> page = swipeCardService.findPage(new Page<SwipeCard>(request, response), swipeCard);
		return getBootstrapData(page);
	}

	/**
	 * 查看，增加，编辑刷卡人员权限表单页面
	 */
	@RequiresPermissions(value={"swipecard:swipeCard:view","swipecard:swipeCard:add","swipecard:swipeCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "form/{mode}")
	public String form(@PathVariable String mode, SwipeCard swipeCard, Model model) {
		model.addAttribute("swipeCard", swipeCard);
		model.addAttribute("mode", mode);
		return "modules/swipecard/swipeCardForm";
	}

	/**
	 * 保存刷卡人员权限
	 */
	@ResponseBody
	@RequiresPermissions(value={"swipecard:swipeCard:add","swipecard:swipeCard:edit"},logical=Logical.OR)
	@RequestMapping(value = "save")
	public AjaxJson save(SwipeCard swipeCard, Model model) throws Exception{
		AjaxJson j = new AjaxJson();
		/**
		 * 后台hibernate-validation插件校验
		 */
		String errMsg = beanValidator(swipeCard);
		if (StringUtils.isNotBlank(errMsg)){
			j.setSuccess(false);
			j.setMsg(errMsg);
			return j;
		}
		//新增或编辑表单保存
		swipeCardService.save(swipeCard);//保存
		j.setSuccess(true);
		j.setMsg("保存刷卡人员权限成功");
		return j;
	}

	/**
	 * 删除刷卡人员权限
	 */
	@ResponseBody
	@RequiresPermissions("swipecard:swipeCard:del")
	@RequestMapping(value = "delete")
	public AjaxJson delete(SwipeCard swipeCard) {
		AjaxJson j = new AjaxJson();
		swipeCardService.delete(swipeCard);
		j.setMsg("删除刷卡人员权限成功");
		return j;
	}

	/**
	 * 批量删除刷卡人员权限
	 */
	@ResponseBody
	@RequiresPermissions("swipecard:swipeCard:del")
	@RequestMapping(value = "deleteAll")
	public AjaxJson deleteAll(String ids) {
		AjaxJson j = new AjaxJson();
		String idArray[] =ids.split(",");
		for(String id : idArray){
			swipeCardService.delete(swipeCardService.get(id));
		}
		j.setMsg("删除刷卡人员权限成功");
		return j;
	}

	/**
	 * 导出excel文件
	 */
	@ResponseBody
	@RequiresPermissions("swipecard:swipeCard:export")
	@RequestMapping(value = "export")
	public AjaxJson exportFile(SwipeCard swipeCard, HttpServletRequest request, HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "刷卡人员权限"+DateUtils.getDate("yyyyMMddHHmmss")+".xlsx";
			Page<SwipeCard> page = swipeCardService.findPage(new Page<SwipeCard>(request, response, -1), swipeCard);
			new ExportExcel("刷卡人员权限", SwipeCard.class).setDataList(page.getList()).write(response, fileName).dispose();
			j.setSuccess(true);
			j.setMsg("导出成功！");
			return j;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导出刷卡人员权限记录失败！失败信息："+e.getMessage());
		}
		return j;
	}

	/**
	 * 导入Excel数据

	 */
	@ResponseBody
	@RequiresPermissions("swipecard:swipeCard:import")
	@RequestMapping(value = "import")
	public AjaxJson importFile(@RequestParam("file")MultipartFile file, HttpServletResponse response, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			int successNum = 0;
			int failureNum = 0;
			StringBuilder failureMsg = new StringBuilder();
			ImportExcel ei = new ImportExcel(file, 1, 0);
			List<SwipeCard> list = ei.getDataList(SwipeCard.class);
			for (SwipeCard swipeCard : list){
				try{
					swipeCardService.save(swipeCard);
					successNum++;
				}catch(ConstraintViolationException ex){
					failureNum++;
				}catch (Exception ex) {
					failureNum++;
				}
			}
			if (failureNum>0){
				failureMsg.insert(0, "，失败 "+failureNum+" 条刷卡人员权限记录。");
			}
			j.setMsg( "已成功导入 "+successNum+" 条刷卡人员权限记录"+failureMsg);
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg("导入刷卡人员权限失败！失败信息："+e.getMessage());
		}
		return j;
	}

	/**
	 * 下载导入刷卡人员权限数据模板
	 */
	@ResponseBody
	@RequiresPermissions("swipecard:swipeCard:import")
	@RequestMapping(value = "import/template")
	public AjaxJson importFileTemplate(HttpServletResponse response) {
		AjaxJson j = new AjaxJson();
		try {
			String fileName = "刷卡人员权限数据导入模板.xlsx";
			List<SwipeCard> list = Lists.newArrayList();
			new ExportExcel("刷卡人员权限数据", SwipeCard.class, 1).setDataList(list).write(response, fileName).dispose();
			return null;
		} catch (Exception e) {
			j.setSuccess(false);
			j.setMsg( "导入模板下载失败！失败信息："+e.getMessage());
		}
		return j;
	}

	/**
	 * 判断是否可以刷卡开门
	 */
	@ResponseBody
	@RequestMapping(value = "swipe")
	public AjaxJson swipe(String ickh,HttpServletResponse response) {
		AjaxJson j = new AjaxJson();

		SwipeCard s = swipeCardService.findByIcCard(ickh);
		if(s != null && s.getId() != null){
		   j.setSuccess(true);
		   j.setMsg("开道闸！");
		}else{
		   j.setSuccess(false);
		   j.setMsg("没权限开道闸或已过有效期");
		}

		return j;
	}

	/**
	 * 查询是否有重复的卡号
	 * @param swipeCard
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "repeat")
	public AjaxJson repeat(SwipeCard swipeCard) {
		AjaxJson j = new AjaxJson();
		SwipeCard s = swipeCardService.get(swipeCard.getId());
		try {
			if(s != null && s.getIcnumber()!= null && !"".equals(s.getIcnumber().trim())){
				j.setSuccess(false);
				j.setMsg("已存在卡号");
				return j;
			}
			if(swipeCard != null && !"".equals(swipeCard.getIcnumber())){
				SwipeCard sAll= swipeCardService.findByIcCard(swipeCard.getIcnumber());
				if(sAll.getIcnumber()!= null && !"".equals(sAll.getIcnumber().trim())){
					j.setSuccess(false);
					j.setMsg("该卡号已存在");
					j.setObject("1");
					return j;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		j.setSuccess(true);
		return j;
	}
	/**
	 * 绑定IC卡
	 * @param swipeCard
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateIC")
	public AjaxJson updateIC(SwipeCard swipeCard) {
		AjaxJson j = new AjaxJson();
		try {
			swipeCardService.saveIC(swipeCard);
			j.setSuccess(true);
			j.setMsg("卡号绑定成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
	/**
	 * 查询绑卡地点是否为1号门岗
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "queryWorkStation")
	public AjaxJson queryWorkStation(HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		try {
			String ip = request.getRemoteAddr();
			if("10.12.241.107".equals(ip)){
				j.setSuccess(true);
			}else{
				j.setSuccess(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return j;
	}
}