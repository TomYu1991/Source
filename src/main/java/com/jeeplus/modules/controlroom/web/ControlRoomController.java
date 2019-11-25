/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.controlroom.web;

import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.core.web.BaseController;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.controlqueue.entity.ControlQueue;
import com.jeeplus.modules.controlqueue.service.ControlQueueService;
import com.jeeplus.modules.controlroom.entity.Controlroom;
import com.jeeplus.modules.controlseat.entity.ControlSeat;
import com.jeeplus.modules.controlseat.service.ControlSeatService;
import com.jeeplus.modules.station.entity.WorkStation;
import com.jeeplus.modules.station.service.WorkStationService;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.service.WeightService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 集中监控室管理Controller
 * @author jeeplus
 * @version 2018-12-19
 */
@Controller
@RequestMapping(value = "${adminPath}/controlroom/controlroom")
public class ControlRoomController extends BaseController {

	//工作站
	@Autowired
	private WorkStationService workStationService;
	//排队信息
	@Autowired
	private ControlQueueService controlQueueService;
	//磅单管理
	@Autowired
	private WeightService weightService;
	//坐席管理
	@Autowired
	private ControlSeatService controlSeatService;

	@Autowired
	private ConsignService consignService;

	/**
	 * 集中监控室管理页面
	 */
	@RequiresPermissions("control:controlroom:controlroom")
	@RequestMapping(value = {"controlroom", ""})
	public String list(Controlroom controlroom, Model model,HttpServletRequest request) {

		ControlQueue c = new ControlQueue();
		ControlSeat cs = new ControlSeat();
		cs.setSeatIp(request.getRemoteAddr());
		ControlSeat cts = controlSeatService.findInfoByIp(cs);
		if(cts!=null&&cts.getSeatNum()!=null&&!"".equals(cts.getSeatNum())){
			c.setSeatNum(cts.getSeatNum());
			controlroom.setStatus(cts.getSeatState());
		}

		ControlQueue cq = controlQueueService.countQueue(c);
		controlroom.setQueueNum(cq.getQueueNum());
		WorkStation w = new WorkStation();
		w.setType("2");
		List<WorkStation> list =workStationService.findList(w);
		List<WorkStation> newList = new ArrayList<WorkStation>();
		List tempSubList = list.subList(0,list.size()-1);
		newList.addAll(tempSubList);
		if(cts!=null&&cts.getSeatName()!=null){
			model.addAttribute("seatName",cts.getSeatName());
		}
		model.addAttribute("list",newList);
		model.addAttribute("controlroom",controlroom);
		return "modules/controlroom/controlroom";
	}

	/**
	 * 选中工作站
	 */

	@ResponseBody
	@RequestMapping(value = {"sure"})
	public AjaxJson sure(String id) {

		AjaxJson j = new AjaxJson();

		WorkStation wtype = workStationService.get(id);

		if("2".equals(wtype.getType())){//地磅
			j.setSuccess(true);
			j.setMsg(wtype.getStationIp());

		}

		return j;
	}

	/**
	 * 选中工作站获取磅单信息
	 */

	@ResponseBody
	@RequestMapping(value = {"weight"})
	public AjaxJson weight(String id) {
		AjaxJson j = new AjaxJson();

		List<Weight> ws = weightService.queryInfoByVehicleNo(id);

		if(ws!=null&&ws.size()>0){
			j.setSuccess(true);
			j.setData(ws.get(0));
		}

		return j;
	}

	/**
	 * 修改坐席状态
	 */

	@ResponseBody
	@RequestMapping(value = {"updateSeatState"})
	public AjaxJson updateSeatState(ControlSeat cs,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		ControlSeat c = new ControlSeat();
		c.setSeatIp(request.getRemoteAddr());
		ControlSeat cts = controlSeatService.findInfoByIp(c);
		if(cts!=null&&cts.getSeatNum()!=null&&!"".equals(cts.getSeatNum())){
			cs.setSeatNum(cts.getSeatNum());
		}

		controlSeatService.updateSeatState(cs);

		j.setSuccess(true);
		j.setMsg("修改坐席状态成功");

		return j;
	}


	/**
	 * 修改坐席状态
	 */
	@ResponseBody
	@RequestMapping(value = {"queryVehicleNoByEquipNum"})
	public AjaxJson queryVehicleNoByEquipNum(String ip,HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		String equipNum = null;
		List<Consign> cl= new ArrayList<>();
		//判断磅秤
		if("10.12.242.40".equals(ip)||"10.12.242.41".equals(ip)){
			equipNum="31";
			cl =consignService.queryLadleNoByEquipNum(equipNum);
		}else{
			if("10.12.241.10".equals(ip)||"10.12.241.20".equals(ip)){
				equipNum="01";
			}
			if("10.12.242.30".equals(ip)){
				equipNum="21";
			}
			cl =consignService.queryVehicleNoByEquipNum(equipNum);
		}
		if(cl.size()>0){
			j.setList(cl);
		}
		j.setSuccess(true);
		return j;
	}
}