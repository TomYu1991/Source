///**
// * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
// */
//package com.jeeplus.modules.consign_tmp.web;
//
//
//import com.jeeplus.common.json.AjaxJson;
//import com.jeeplus.core.web.BaseController;
//import com.jeeplus.modules.consign_tmp.entity.ConsignTmp;
//import com.jeeplus.modules.consign_tmp.service.ConsignTmpService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
//
//import java.util.*;
//
//import net.sf.json.JSONObject;
//import net.sf.json.JSONArray;
//
//
///**
// * 委托单/预约单管理Controller
// *
// * @author 汤进国
// * @version 2019-01-17
// */
//@Controller
//@RequestMapping(value = "${adminPath}/consignTmp/consignTmp")
//public class ConsignTmpController extends BaseController {
//
//    @Autowired
//    private ConsignTmpService consignTmpService;
//
//    @ResponseBody
//    @RequestMapping(value = "getConsignTmp")
//    public AjaxJson getConsignTmp() {
//        AjaxJson ajaxJson = new AjaxJson();
//        List<ConsignTmp> list = consignTmpService.findListByOptflag();
//        if (list != null && list.size() > 0) {
//
//            for (ConsignTmp ct : list) {
//                if (ct.getTypeCode() != null && !"".equals(ct.getTypeCode())) {
//                    JSONObject data = new JSONObject();
//                    data.put("type", "1");
//                    data.put("consignId", ct.getConsignId());
//                    data.put("consignUser", ct.getConsignUser());
//                    data.put("consignDept", ct.getConsignDept());
//                    data.put("weightType", ct.getWeightType());
//                    data.put("equipNum", ct.getEquipNum());
//                    data.put("prodCname", ct.getProdCname());
//                    data.put("sgCode", ct.getSgCode());
//                    data.put("sgSign", ct.getSgSign());
//                    data.put("matSpecDesc", ct.getMatSpecDesc());
//                    data.put("billNo", ct.getBillNo());
//                    data.put("startTime", ct.getStartTime());
//                    data.put("endTime", ct.getEndTime());
//                    data.put("totalWt", ct.getTotalWt());
//                    data.put("supplierName", ct.getSupplierName());
//                    data.put("consigneUser", ct.getConsigneUser());
//                    data.put("content", ct.getContent());
//                    data.put("dealPersonNo", ct.getDealPersonNo());
//                    data.put("dealDept", ct.getDealDept());
//                    data.put("telNum", ct.getTelNum());
//                    data.put("vehicleNo", ct.getVehicleNo());
//                    data.put("userName", ct.getUserName());
//                    data.put("carryCompanyName", ct.getCarryCompanyName());
//                    data.put("tel", ct.getTel());
//                    data.put("passCode", ct.getPassCode());
//                    data.put("blastFurnaceNo", ct.getBlastFurnaceNo());
//                    data.put("ladleNo", ct.getLadleNo());
//                    data.put("transContactPerson", ct.getTransContactPerson());
//                    data.put("IDCard", ct.getIDCard());
//                    data.put("transContactPersonTel", ct.getTransContactPersonTel());
//                    data.put("rfidNo", ct.getRfidNo());
//                    data.put("status", ct.getStatus());
//                    data.put("defaultFlag", ct.getDefaultFlag());
//                    data.put("consignNo", ct.getConsignNo());
//                    data.put("moreRate", ct.getMoreRate());
//                    data.put("ponderFlag", ct.getPonderFlag());
//                    data.put("codeFlag", ct.getCodeFlag());
//                    data.put("field1", ct.getField1());
//                    data.put("field2", ct.getField2());
//                    data.put("field3", ct.getField3());
//                    data.put("field4", ct.getField4());
//                    data.put("field5", ct.getField5());
//                    data.put("fieldN1", ct.getFieldN1());
//                    data.put("fieldN2", ct.getFieldN2());
//                    data.put("fieldN3", ct.getFieldN3());
//                    if ("bdqr".equals(ct.getTypeCode())) {
//                        data.put("weighNo", ct.getWeighNo());
//                        data.put("affirmFlag", ct.getAffirmFlag());
//                        data.put("confirmPersonNo", ct.getConfirmPersonNo());
//                        data.put("confirmTime", ct.getConfirmTime());
//                    }
//                    JSONArray arr = new JSONArray();
//                    arr.add(data);
//                    JSONObject ret = new JSONObject();
//                    ret.put("Type", ct.getTypeCode());
//                    ret.put("Flag", ct.getFlag());
//                    ret.put("data", arr);
//                    InterfaceTest interfaceTest = new InterfaceTest();
//                    String res = interfaceTest.sendPost("http://127.0.0.1:8080/a/interface/acceptData", ret);
//                    JSONArray jsonArray = JSONArray.fromObject(res);
//                    Boolean success = (Boolean) jsonArray.getJSONObject(0).get("success");
//                    String msg = (String) jsonArray.getJSONObject(0).get("msg");
//                    if (success) {
//                        //记录保存成功，将操作标记optFlag改为"1"
//                        ConsignTmp tmp = new ConsignTmp();
//                        tmp.setOptFlag("1");
//                        tmp.setId(ct.getId());
//                        consignTmpService.updateConsignTmp(tmp);
//                    } else {
//                        //记录保存失败
//                        System.out.println(msg);
//                    }
//                }
//
//            }
//        }
//
//        return ajaxJson;
//    }
//}
//}