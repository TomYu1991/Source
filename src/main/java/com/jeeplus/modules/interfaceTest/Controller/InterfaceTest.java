package com.jeeplus.modules.interfaceTest.Controller;

import com.alibaba.fastjson.JSON;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.impwthistory.entity.ImpWtHistory;
import com.jeeplus.modules.interfaceTest.entry.*;
import com.jeeplus.modules.passcheck.entity.PassCheck;
import com.jeeplus.modules.passcheck.service.PassCheckService;
import com.jeeplus.modules.passchecksub.entity.PassCheckSub;
import com.jeeplus.modules.swipecard.entity.SwipeCard;
import com.jeeplus.modules.swipecard.service.SwipeCardService;
import com.jeeplus.modules.synchroinfo.entity.DataSynchroInfo;
import com.jeeplus.modules.synchroinfo.service.DataSynchroInfoService;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tools.entity.SysDataSource;
import com.jeeplus.modules.tools.service.SysDataSourceService;
import com.jeeplus.modules.tools.utils.MultiDBUtils;
import com.jeeplus.modules.vehicleinfo.entity.VehicleInfo;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
import com.jeeplus.modules.warninginfo.service.WarningInfoService;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.service.WeightService;
import com.jeeplus.modules.weightrecord.entity.UpdateWeightRecord;
import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import net.sf.json.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "${adminPath}/interface")
public class InterfaceTest extends HttpServlet {


    //磅单信息
    @Autowired
    private WeightService weightService;
    //同步异常信息
    @Autowired
    private DataSynchroInfoService dataSynchroInfoService;
    @Autowired
    private SysDataSourceService sysDataSourceService;

//    private static String url = "http://10.12.200.18:9080/dsdb/WebAccessManage";//;
    private static String url = "";
    @Autowired
    private SwipeCardService swipeCardService;

    /**
     * 委托单、出门条、车辆信息    增改删接口
     *
     * @throws IOException
     */
    @POST
    @ResponseBody
    @RequestMapping(value = "acceptData")
    public JSONArray acceptData(HttpServletRequest req, HttpServletResponse resp) {
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[]{"yyyy-MM-dd HH:mm:ss"}), true);
        List<ResultType> rtList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader br = null;
            String line = null;
            /*     br = new BufferedReader(new InputStreamReader(req.getInputStream(), "UTF-8"));*/
            req.setCharacterEncoding("UTF-8");
            br = req.getReader();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("接收到的数据" + sb.toString());
            JsonType jt = JSON.parseObject(sb.toString(), JsonType.class);
            //获取所有的数据源
            List<SysDataSource> dataSourceList = sysDataSourceService.queryEnname();
            for (SysDataSource s : dataSourceList) {
                if (!"ryxx".equals(s.getEnname())) {
                    MultiDBUtils md = MultiDBUtils.get(s.getEnname());
                    if (md == null) {
                        DataSynchroInfo syn = new DataSynchroInfo();
                        syn.setCreateDate(new Date());
                        syn.setRemarks("未找到" + s.getName() + "数据库");
                        dataSynchroInfoService.save(syn);
                    } else {
                        if (jt.getType() != null && !"".equals(jt.getType())) {
                            if ("wtd".equals(jt.getType())) {
                                //委托单类型
                                rtList = weituodan(jt, md, s.getName());
                            }
                            if ("cmt".equals(jt.getType())) {
                                //出门条处理
                                rtList = chumentiao(jt, md, s.getName());
                            }
                            if ("clxx".equals(jt.getType())) {
                                //车辆信息处理
                                rtList = cheliangxinxi(jt, md, s.getName());
                            }
                            if ("bdqr".equals(jt.getType())) {
                                //磅单确认处理
                                rtList = bangdanqueren(jt, md, s.getName(), rtList);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ResultType rt = new ResultType();
            rt.setSuccess(false);
            rt.setMsg(e.getClass().getName());
            rtList.add(rt);
        }
        System.out.println(JSONArray.fromObject(rtList));
        return JSONArray.fromObject(rtList);
    }

    /**
     * 委托单处理
     */
    private List<ResultType> weituodan(JsonType jt, MultiDBUtils md, String s) {
        List<ResultType> rtList = new ArrayList<>();
        //将json字符串转成json数组
        JSONArray ja = new JSONArray();
        for (int i = 0; i < jt.getData().size(); i++) {//循环json数组
            JSONObject ob = (JSONObject) jt.getData().get(i);//得到json对象
            if (ob.containsKey("dealTime")) {
                String dealTime = ob.getString("dealTime");//name这里是列名称，获取json对象中列名为name的值
                if (isNullOrEmputy(dealTime)) {
                    ob.remove("dealTime");
                }
            }
            if (ob.containsKey("approveTime")) {
                String approveTime = ob.getString("approveTime");
                if (isNullOrEmputy(approveTime)) {
                    ob.remove("approveTime");
                }
            }
            ja.add(ob);
        }
        //将json字符串转为接收类型
        List<Consign> consignList = JSONArray.toList(jt.getData(), Consign.class);

        rtList = commonwtd(jt, md, s, consignList, "0");

        return rtList;
    }

    /**
     * 委托单接口和同步公共方法
     */
    public List<ResultType> commonwtd(JsonType jt, MultiDBUtils md, String s, List<Consign> consignList, String flag) {
        List<ResultType> rtList = new ArrayList<>();

        if (consignList.size() > 0) {
            //委托单新增或修改
            if ("1".equals(jt.getFlag())) {
                rtList = weituodaninsert(jt, consignList, md, s, rtList, flag);
            }
            //委托单删除
            if ("2".equals(jt.getFlag())) {
                rtList = weituodandelete(jt, consignList, md, s, rtList, flag);
            }
            //委托单作废
            if ("3".equals(jt.getFlag())) {
                rtList = weituodancancel(jt, consignList, md, s, rtList, flag);
            }
        }
        return rtList;
    }

    /**
     * 委托单新增或修改
     *
     * @return
     */
    private List<ResultType> weituodaninsert(JsonType jt, List<Consign> consignList, MultiDBUtils md, String s, List<ResultType> rtList, String flag) {
        for (Consign c : consignList) {
            ResultType rt = new ResultType();
            try {
                if (c.getDefaultFlag() != null && "1".equals(c.getDefaultFlag())) {

                    List<ImpWtHistory> i = md.queryList("select consign_id consignId,start_time startTime,end_time endTime FROM imp_wt_history a where a.consign_id = '" + c.getConsignId() + "' and a.del_flag='0' order by a.create_date desc limit 1", ImpWtHistory.class);

                    if (i.size() > 0) {
                        //修改皮重历史
                        ImpWtHistory iw = new ImpWtHistory();
                        iw.setConsignId(c.getConsignId());
                        iw.setStartTime(i.get(0).getStartTime());
                        iw.setEndTime(i.get(0).getEndTime());

                        md.update("update imp_wt_history set start_time='" + iw.getStartTime() + "' ,end_time='" + iw.getEndTime() + "' where consign_id='" + iw.getConsignId() + "'");

                    }
                }
                //同步新增多个数据库
                rt = saveInsert(jt, c, md, s, flag);
                rtList.add(rt);
            } catch (Exception e) {
                e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(c.getConsignId() + "  " + e.getClass().getName());
                if ("1".equals(flag)) {
                    DataSynchroInfoService dataSynchroInfoService = SpringContextHolder.getBean(DataSynchroInfoService.class);
                    syn.setId(IdGen.uuid());
                    dataSynchroInfoService.saveRecord(syn);
                } else {
                    dataSynchroInfoService.save(syn);
                }
            }
        }
        return rtList;
    }

    /**
     * 同步保存委托单
     *
     * @param jt
     * @param c
     * @param md
     * @param s
     * @return
     */
    @Transactional
    public synchronized ResultType saveInsert(JsonType jt, Consign c, MultiDBUtils md, String s, String flag) {

        List<Consign> con = md.queryList("select a.weight_state weightState FROM consign a WHERE a.consign_id = '" + c.getConsignId() + "'", Consign.class);

        if (con.size() > 0) {
            //保留过磅状态
            if (con.get(0).getWeightState() != null && !"".equals(con.get(0).getWeightState())) {
                c.setWeightState(con.get(0).getWeightState());
            } else {
                c.setWeightState("0");
            }
            md.update("DELETE FROM consign WHERE consign_id = ?", c.getConsignId());
        } else {
            c.setWeightState("0");
        }

        ResultType rt = new ResultType();
        if (c.getType() != null && "1".equals(c.getType())) {
            if (c.getVehicleNo() == null || "".equals(c.getVehicleNo()) || c.getVehicleNo().length() < 2) {
                c.setVehicleNo(c.getLadleNo());
            }
            if (c.getPonderFlag() == null || "".equals(c.getPonderFlag())) {
                c.setPonderFlag("0");
            }
            if (c.getField1() == null || "".equals(c.getField1())) {
                c.setField1("0");
            }
            if (c.getDefaultFlag() == null || "".equals(c.getDefaultFlag())) {
                c.setDefaultFlag("0");
            }
            if (c.getMoreRate() == null || "".equals(c.getMoreRate())) {
                c.setMoreRate("0");
            }
        }
        c.setId(IdGen.uuid());
        c.setDataType("0");
        c.setStatus("0");
        c.setDataSources("0");
        c.setCreateDate(new Date());
        c.setSurplusWt("0.00");
        System.out.println("新增、修改委托单信息" + c);

        int id = md.update("INSERT INTO consign(id,bill_type,consign_id,consign_no,consign_user,consign_dept,weight_type," +
                        "equip_num,prod_cname,sg_code,sg_sign,mat_spec_desc,bill_no,surplus_wt,start_time,end_time,total_wt," +
                        "supplier_name,consigne_user,content,deal_person_no,deal_dept,tel_num,vehicle_no,user_name,carry_company_name," +
                        "tel,pass_code,blast_furnace_no,ladle_no,consign_state,trans_contact_person,id_card,trans_contact_person_tel," +
                        "rfid_no,data_type,weight_state,status,data_sources,create_date,default_flag,ponder_flag,more_rate,code_flag,field1," +
                        "field2,field3,field4,field5,field_n1,field_n2,field_n3) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?," +
                        "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", c.getId(), c.getType(), c.getConsignId(),
                c.getConsignNo(), c.getConsigneUser(), c.getConsignDept(), c.getWeightType(), c.getEquipNum(), c.getProdCname(), c.getSgCode(),
                c.getSgSign(), c.getMatSpecDesc(), c.getBillNo(), c.getSurplusWt(), c.getStartTime(), c.getEndTime(), c.getTotalWt(),
                c.getSupplierName(), c.getConsigneUser(), c.getContent(), c.getDealPersonNo(), c.getDealDept(), c.getTelNum(), c.getVehicleNo(), c.getUserName(),
                c.getCarryCompanyName(), c.getTel(), c.getPassCode(), c.getBlastFurnaceNo(), c.getLadleNo(), c.getConsignState(), c.getTransContactPerson(),
                c.getIDCard(), c.getTransContactPersonTel(), c.getRfidNo(), c.getDataType(), c.getWeightState(), c.getStatus(), c.getDataSources(), c.getCreateDate(),
                c.getDefaultFlag(), c.getPonderFlag(), c.getMoreRate(), c.getCodeFlag(), c.getField1(), c.getField2(), c.getField3(), c.getField4(), c.getField5(),
                c.getFieldN1(), c.getFieldN2(), c.getFieldN3());
        if (id == 1) {
            saveDataSynchroInfo(s, jt, c.getConsignId(), "0", flag, c.getVehicleNo());
            rt.setSuccess(true);
            rt.setMsg(s + ",保存委托单成功");
            rt.setErrorCode(c.getConsignId());
            rt.setType(jt.getType());

        } else {
            saveDataSynchroInfo(s, jt, c.getConsignId(), "1", flag, c.getVehicleNo());
            rt.setSuccess(false);
            rt.setMsg(s + ",保存委托单失败");
            rt.setErrorCode(c.getConsignId());
            rt.setType(jt.getType());
        }
        return rt;
    }

    /**
     * 委托单删除
     *
     * @return
     */
    private List<ResultType> weituodandelete(JsonType jt, List<Consign> consignList, MultiDBUtils md, String s, List<ResultType> rtList, String flag) {

        for (Consign c : consignList) {

            ResultType rt = new ResultType();
            System.out.println("删除委托单信息" + c);

            try {
                //同步删除所有数据库
                int id = md.update("DELETE FROM consign WHERE consign_id = ?", c.getConsignId());
                if (id <= 0) {
                    saveDataSynchroInfo(s, jt, c.getConsignId(), "1", flag, c.getVehicleNo());
                    rt.setSuccess(false);
                    rt.setMsg(s + ",委托单未找到，删除失败");
                    rt.setErrorCode(c.getConsignId());
                    rt.setType(jt.getType());
                } else {
                    saveDataSynchroInfo(s, jt, c.getConsignId(), "0", flag, c.getVehicleNo());
                    rt.setSuccess(true);
                    rt.setMsg(s + ",删除委托单成功");
                    rt.setErrorCode(c.getConsignId());
                    rt.setType(jt.getType());
                }

                List<Consign> con = md.queryList("SELECT weight_state,total_wt,surplus_wt,default_flag FROM consign a WHERE a.consign_id = '" + c.getConsignId() + "'order by a.end_time desc", Consign.class);
                if (con.size() > 0 && con.get(0).getDefaultFlag() != null && "1".equals(con.get(0).getDefaultFlag())) {
                    System.out.println("锁皮删除皮重");
                    //锁皮作废皮重历史
                    md.update("UPDATE imp_wt_history SET del_flag = '1' WHERE consign_id = ?", con.get(0).getConsignId());
                    //作废未完成磅单
                    List<Weight> wl = md.queryList(" SELECT mat_wt,remarks,status,updater,updatetime,weigh_no FROM weight a WHERE a.consign_id = '" + con.get(0).getConsignId() + "' AND a.status = 0", Weight.class);

                    for (Weight weight : wl) {
                        if (weight.getMatWt() == null && "0.00".equals(weight.getMatWt()) && "".equals(weight.getMatWt())) {
                            UpdateWeightRecord uwr = new UpdateWeightRecord();
                            uwr.setOperation("2");
                            uwr.setUpdateBy(UserUtils.getUser());
                            uwr.setUpdateDate(new Date());
                            uwr.setContent(weight.getRemarks());
                            uwr.setId(IdGen.uuid());
                            md.update("INSERT INTO update_weight_record(id,update_by,update_date,operation,content,ponder_no) VALUES (?,?,?,?,?,?)",
                                    uwr.getId(), uwr.getUpdateBy(), uwr.getUpdateDate(), uwr.getOperation(), uwr.getContent(), weight.getWeighNo());

                            md.update("UPDATE weight SET remarks = ?,status = ?,updater = ?,updatetime = ? WHERE weigh_no = ?", weight.getRemarks(), weight.getStatus(), weight.getUpdater(), weight.getUpdatetime(), weight.getWeighNo());
                        }
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(c.getConsignId() + "  " + e.getClass().getName());
                dataSynchroInfoService.save(syn);

            }
            rtList.add(rt);
        }
        return rtList;
    }

    /**
     * 委托单作废
     *
     * @return
     */
    private List<ResultType> weituodancancel(JsonType jt, List<Consign> consignList, MultiDBUtils md, String s, List<ResultType> rtList, String flag) {
        //List<ResultType> rtList=new ArrayList<>();
        for (Consign c : consignList) {
            ResultType rt = new ResultType();
            c.setStatus("2");
            System.out.println("作废委托单信息" + c);
            try {
                //同步作废1，3号岗亭数据库
                int id = md.update("UPDATE consign SET status = ? WHERE consign_id = ?", c.getStatus(), c.getConsignId());
                if (id <= 0) {
                    saveDataSynchroInfo(s, jt, c.getConsignId(), "1", flag, c.getVehicleNo());
                    rt.setSuccess(false);
                    rt.setMsg(s + ",作废委托单失败");
                    rt.setErrorCode(c.getConsignId());
                    rt.setType(jt.getType());
                } else {
                    saveDataSynchroInfo(s, jt, c.getConsignId(), "0", flag, c.getVehicleNo());
                    rt.setSuccess(true);
                    rt.setMsg(s + ",作废委托单成功");
                    rt.setErrorCode(c.getConsignId());
                    rt.setType(jt.getType());
                }
                List<Consign> con = md.queryList("SELECT weight_state,total_wt,surplus_wt,default_flag FROM consign a WHERE a.consign_id = '" + c.getConsignId() + "'order by a.end_time desc", Consign.class);
                if (con.get(0).getDefaultFlag() != null && "1".equals(con.get(0).getDefaultFlag())) {
                    System.out.println("锁皮删除皮重,删除一次过磅记录");
                    //锁皮作废皮重历史
                    md.update("UPDATE imp_wt_history SET del_flag = '1' WHERE consign_id = ?", con.get(0).getConsignId());
                    //作废未完成磅单
                    List<Weight> wl = md.queryList(" SELECT mat_wt,remarks,status,updater,updatetime,weigh_no FROM weight a WHERE a.consign_id = '" + con.get(0).getConsignId() + "' AND a.status = 0", Weight.class);
                    for (Weight weight : wl) {
                        if (weight.getMatWt() == null && "".equals(weight.getMatWt()) && "0.00".equals(weight.getMatWt())) {
                            UpdateWeightRecord uwr = new UpdateWeightRecord();
                            uwr.setOperation("2");
                            uwr.setUpdateBy(UserUtils.getUser());
                            uwr.setUpdateDate(new Date());
                            uwr.setContent(weight.getRemarks());
                            uwr.setId(IdGen.uuid());
                            md.update("INSERT INTO update_weight_record(id,update_by,update_date,operation,content,ponder_no) VALUES (?,?,?,?,?,?)",
                                    uwr.getId(), uwr.getUpdateBy(), uwr.getUpdateDate(), uwr.getOperation(), uwr.getContent(), weight.getWeighNo());

                            md.update("UPDATE weight SET remarks = ?,status = ?,updater = ?,updatetime = ? WHERE weigh_no = ?", weight.getRemarks(), weight.getStatus(), weight.getUpdater(), weight.getUpdatetime(), weight.getWeighNo());
                            weightService.cancel(weight);
                        }
                    }
                }
            } catch (Exception e) {
                //e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(c.getConsignId() + "  " + e.getClass().getName());
                dataSynchroInfoService.save(syn);
            }
            rtList.add(rt);
        }
        return rtList;
    }

    /**
     * 出门条处理
     *
     * @param jt
     */
    private List<ResultType> chumentiao(JsonType jt, MultiDBUtils md, String s) throws IOException {
        //将json字符串转成json数组
        JSONArray ja = new JSONArray();
        List<ResultType> rtList = new ArrayList<>();
        for (int i = 0; i < jt.getData().size(); i++) {//循环json数组
            JSONObject ob = (JSONObject) jt.getData().get(i);//得到json对象
            if (ob.containsKey("dealTime")) {
                String dealTime = ob.getString("dealTime");//name这里是列名称，获取json对象中列名为name的值
                if (isNullOrEmputy(dealTime)) {
                    ob.remove("dealTime");
                }
            }
            if (ob.containsKey("approveTime")) {
                String approveTime = ob.getString("approveTime");
                if (isNullOrEmputy(approveTime)) {
                    ob.remove("approveTime");
                }
            }
            ja.add(ob);
        }

        List<PassCheck> passCheckList = JSONArray.toList(jt.getData(), PassCheck.class);
        //List<ResultType> rtList=new ArrayList<>();
        rtList = commoncmt(jt, md, s, passCheckList, "0");
        return rtList;
    }

    /**
     * 出门条接口和同步公共方法
     */
    public List<ResultType> commoncmt(JsonType jt, MultiDBUtils md, String s, List<PassCheck> passCheckList, String flag) {
        List<ResultType> rtList = new ArrayList<>();
        if (passCheckList.size() > 0) {

            //出门条新增或修改
            if ("1".equals(jt.getFlag())) {
                rtList = chumentiaoinsert(jt, passCheckList, md, s, rtList, flag);
            }
            //出门条删除
            if ("2".equals(jt.getFlag())) {
                rtList = chumentiaodelete(jt, passCheckList, md, s, rtList, flag);
            }

            //出门条作废
            if ("3".equals(jt.getFlag())) {
                rtList = chumentiaocancel(jt, passCheckList, md, s, rtList, flag);
            }
        }
        return rtList;
    }

    /**
     * 出门条新增或修改
     *
     * @param jt
     * @param passCheckList
     * @param md
     * @param s
     * @return
     */
    private List<ResultType> chumentiaoinsert(JsonType jt, List<PassCheck> passCheckList, MultiDBUtils md, String s, List<ResultType> rtList, String flag) {
        //List<ResultType> rtList = new ArrayList<>();
        for (PassCheck p : passCheckList) {

            ResultType rt = new ResultType();
            System.out.println("新增、修改出门条信息" + p);
            try {
                //新增出门条
                rt = savecmtInsert(jt, p, md, s, flag);
                rtList.add(rt);
            } catch (Exception e) {
                e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(p.getTrnpAppNo() + "  " + e.getClass().getName());
                if ("1".equals(flag)) {
                    DataSynchroInfoService dataSynchroInfoService = SpringContextHolder.getBean(DataSynchroInfoService.class);
                    syn.setId(IdGen.uuid());
                    dataSynchroInfoService.saveRecord(syn);
                } else {
                    dataSynchroInfoService.save(syn);
                }
            }
        }
        return rtList;
    }

    /**
     * 同步保存出门条
     *
     * @param jt
     * @param p
     * @param md
     * @param s
     * @return
     */
    @Transactional
    public synchronized ResultType savecmtInsert(JsonType jt, PassCheck p, MultiDBUtils md, String s, String flag) {

        ResultType rt = new ResultType();
        int id;
        id = md.update("DELETE FROM pass_check WHERE trnp_app_no = ?", p.getTrnpAppNo());
        if (id > 0) {
            md.update("DELETE FROM pass_check_sub WHERE trnp_app_no = ?", p.getTrnpAppNo());
        }
        //新增出门条
        p.setId(IdGen.uuid());
        p.setDelFlag("0");
        p.setCreateDate(new Date());
        p.setCreateBy(UserUtils.getUser());
        p.setUpdateBy(UserUtils.getUser());
        p.setUpdateDate(new Date());
        if (isNullOrEmputy(p.getValidFlag())) {
            p.setValidFlag("0");
        }
        id = md.update("INSERT INTO pass_check (id,create_by,create_date,update_by,update_date,del_flag,trnp_app_no,user_name," +
                        "vehicle_no,trans_contact_person,deal_person_no,deal_person_name,deal_date,rfid_no,pass_code,type_code," +
                        "start_time,end_time,carry_company_name,apply_time,apply_person_no,approve_time,approve_person_no,valid_flag," +
                        "remark) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", p.getId(), p.getCreateBy().toString(), p.getCreateDate(), p.getUpdateBy().toString(), p.getUpdateDate(),
                p.getDelFlag(), p.getTrnpAppNo(), p.getUserName(), p.getVehicleNo(), p.getTransContactPerson(), p.getDealPersonNo(), p.getDealPersonName(), p.getDealDate(),
                p.getRfidNo(), p.getPassCode(), p.getTypeCode(), p.getStartTime(), p.getEndTime(), p.getCarryCompanyName(), p.getApplyTime(), p.getApplyPersonNo(), p.getApproveTime(),
                p.getApprovePersonNo(), p.getValidFlag(), p.getRemark());
        if (id == 1) {
            saveDataSynchroInfo(s, jt, p.getTrnpAppNo(), "0", flag, p.getVehicleNo());
            rt.setSuccess(true);
            rt.setMsg(s + ",保存出门条成功");
            rt.setErrorCode(p.getTrnpAppNo());
            rt.setType(jt.getType());
            //出门条明细新增
            if (p.getSubList() != null && !"".equals(p.getSubList())) {
                rt = chumentiaosubinsert(p, md, rt, jt);
            }
        } else {
            saveDataSynchroInfo(s, jt, p.getTrnpAppNo(), "1", flag, p.getVehicleNo());
            rt.setSuccess(false);
            rt.setMsg(s + ",保存出门条失败");
            rt.setErrorCode(p.getTrnpAppNo());
            rt.setType(jt.getType());

        }
        return rt;
    }

    /**
     * 出门条子项新增
     */
    private ResultType chumentiaosubinsert(PassCheck p, MultiDBUtils md, ResultType rt, JsonType jt) {
        //List<ResultType> rtList = new ArrayList<>();

        List<PassCheckSub> passSubList = JSONArray.toList(p.getSubList(), PassCheckSub.class);

        //新增出门条明细信息
        for (PassCheckSub ps : passSubList) {
            ps.setId(IdGen.uuid());
            ps.setCreateBy(UserUtils.getUser());
            ps.setCreateDate(new Date());
            ps.setUpdateBy(UserUtils.getUser());
            ps.setUpdateDate(new Date());
            int idsub = md.update("INSERT INTO pass_check_sub (id,create_by,create_date,update_by,update_date,del_flag,archive_flag,company_code," +
                            "company_cname,trnp_app_no,prod_code,prod_cname,mat_spec_desc,out_stock_qty,measure_unit,remarks) VALUES (?," +
                            "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", ps.getId(), ps.getCreateBy().toString(), ps.getCreateDate(), ps.getUpdateBy().toString(), ps.getUpdateDate(), ps.getDelFlag(), ps.getArchiveFlag(), ps.getCompanyCode(),
                    ps.getCompanyCname(), ps.getTrnpAppNo(), ps.getProdCode(), ps.getProdCname(), ps.getMatSpecDesc(), ps.getOutStockQty(), ps.getMeasureUnit(), ps.getRemarks());
            if (idsub != 1) {
                rt.setSuccess(false);
                rt.setMsg("保存出门条明细失败");
                rt.setErrorCode(p.getTrnpAppNo());
                rt.setType(jt.getType());
            }
        }
        return rt;
    }

    /**
     * 出门条删除
     *
     * @param jt
     * @param passCheckList
     * @param md
     * @param s
     * @return
     */
    private List<ResultType> chumentiaodelete(JsonType jt, List<PassCheck> passCheckList, MultiDBUtils md, String s, List<ResultType> rtList, String flag) {

        for (PassCheck p : passCheckList) {
            ResultType rt = new ResultType();
            System.out.println("删除出门条信息" + p);
            try {
                //同步删除1，3号岗亭数据库
                int id = md.update("DELETE FROM pass_check WHERE trnp_app_no = ?", p.getTrnpAppNo());
                if (id <= 0) {
                    saveDataSynchroInfo(s, jt, p.getTrnpAppNo(), "1", flag, p.getVehicleNo());
                    rt.setSuccess(false);
                    rt.setMsg(s + ",该出门条未找到，删除失败");
                    rt.setErrorCode(p.getTrnpAppNo());
                    rt.setType(jt.getType());
                } else {
                    //同步删除出门条明细
                    md.update("DELETE FROM pass_check_sub WHERE trnp_app_no = ?", p.getTrnpAppNo());

                    saveDataSynchroInfo(s, jt, p.getTrnpAppNo(), "0", flag, p.getVehicleNo());
                    rt.setSuccess(true);
                    rt.setMsg(s + ",删除出门条成功");
                    rt.setErrorCode(p.getTrnpAppNo());
                    rt.setType(jt.getType());
                }
                rtList.add(rt);

            } catch (Exception e) {
                //e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(p.getTrnpAppNo() + "  " + e.getClass().getName());
                dataSynchroInfoService.save(syn);
            }
        }
        return rtList;
    }

    /**
     * 出门条作废
     *
     * @param jt
     * @param passCheckList
     * @param md
     * @param s
     * @return
     */
    private List<ResultType> chumentiaocancel(JsonType jt, List<PassCheck> passCheckList, MultiDBUtils md, String s, List<ResultType> rtList, String flag) {
        for (PassCheck p : passCheckList) {
            ResultType rt = new ResultType();
            p.setDelFlag("1");
            System.out.println("作废出门条信息" + p);

            try {
                //同步删除所有数据库
                int id = md.update("UPDATE pass_check SET del_flag = ? WHERE trnp_app_no = ?", p.getDelFlag(), p.getTrnpAppNo());
                if (id <= 0) {
                    saveDataSynchroInfo(s, jt, p.getTrnpAppNo(), "1", flag, p.getVehicleNo());
                    rt.setSuccess(false);
                    rt.setMsg(s + ",作废出门条失败");
                    rt.setErrorCode(p.getTrnpAppNo());
                    rt.setType(jt.getType());
                } else {
                    saveDataSynchroInfo(s, jt, p.getTrnpAppNo(), "0", flag, p.getVehicleNo());
                    rt.setSuccess(true);
                    rt.setMsg(s + ",作废出门条成功");
                    rt.setErrorCode(p.getTrnpAppNo());
                    rt.setType(jt.getType());
                }
                rtList.add(rt);
            } catch (Exception e) {
                //e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(p.getTrnpAppNo() + "  " + e.getClass().getName());
                dataSynchroInfoService.save(syn);
            }
        }
        return rtList;
    }

    /**
     * 车辆信息处理
     *
     * @param jt
     */
    private List<ResultType> cheliangxinxi(JsonType jt, MultiDBUtils md, String s) {
        //将json字符串转成json数组
        JSONArray ja = new JSONArray();
        List<ResultType> rtList = new ArrayList<>();
        for (int i = 0; i < jt.getData().size(); i++) {//循环json数组
            JSONObject ob = (JSONObject) jt.getData().get(i);//得到json对象
            if (ob.containsKey("dealTime")) {
                String dealTime = ob.getString("dealTime");//name这里是列名称，获取json对象中列名为name的值
                if (isNullOrEmputy(dealTime)) {
                    ob.remove("dealTime");
                }
            }
            if (ob.containsKey("approveTime")) {
                String approveTime = ob.getString("approveTime");
                if (isNullOrEmputy(approveTime)) {
                    ob.remove("approveTime");
                }
            }
            ja.add(ob);

        }
        List<VehicleInfo> vehicleInfoList = JSONArray.toList(ja, VehicleInfo.class);

        rtList = commonclxx(jt, md, s, vehicleInfoList, "0");

        return rtList;
    }

    /**
     * 车辆信息接口和同步公共方法
     */
    public List<ResultType> commonclxx(JsonType jt, MultiDBUtils md, String s, List<VehicleInfo> vehicleInfoList, String flag) {
        List<ResultType> rtList = new ArrayList<>();

        //车辆信息新增或修改
        if ("1".equals(jt.getFlag())) {
            rtList = cheliangxinxiinsert(jt, vehicleInfoList, md, s, rtList, flag);
        }

        //车辆信息删除
        if ("2".equals(jt.getFlag())) {
            rtList = cheliangxinxidelete(jt, vehicleInfoList, md, s, rtList, flag);
        }
        //车辆信息作废
        if ("3".equals(jt.getFlag())) {
            rtList = cheliangxinxicancel(jt, vehicleInfoList, md, s, rtList, flag);
        }
        return rtList;
    }

    /**
     * 车辆信息新增或修改
     *
     * @return
     */
    private List<ResultType> cheliangxinxiinsert(JsonType jt, List<VehicleInfo> vehicleInfoList, MultiDBUtils md, String s, List<ResultType> rtList, String flag) {
        //List<ResultType> rtList = new ArrayList<>();
        ResultType rt = new ResultType();
        for (VehicleInfo vi : vehicleInfoList) {
            try {
                //查询车辆RFID
                //List<VehicleInfo> vis = vehicleInfoService.checkByVehicleNo(vi.getVehicleNo());
                String vehicleNo = vi.getVehicleNo();
                //System.out.println(vi.getVehicleNo());
                List<VehicleInfo> vis = md.queryList("SELECT rfid_no,srfid_no FROM vehicle_info WHERE vehicle_no = '" + vehicleNo + "' AND del_flag = '0' AND group_code = '" + vi.getGroupCode() + "' ORDER BY create_date DESC", VehicleInfo.class);
                List<VehicleInfo> vis2 = md.queryList("SELECT rfid_no,srfid_no FROM vehicle_info WHERE vehicle_no = '" + vehicleNo + "' AND del_flag = '1'  AND group_code = '" + vi.getGroupCode() + "' ORDER BY create_date DESC", VehicleInfo.class);
                if (vis.size() > 0 && notNullOrEmputy(vis.get(0).getRfidNo())) {
                    vi.setRfidNo(vis.get(0).getRfidNo());
                } else {
                    if (vis2.size() > 0 && notNullOrEmputy(vis2.get(0).getRfidNo())) {
                        vi.setRfidNo(vis2.get(0).getRfidNo());
                    }
                }
                if (vis.size() > 0 && notNullOrEmputy(vis.get(0).getSrfidNo())) {
                    vi.setSrfidNo(vis.get(0).getSrfidNo());
                } else {
                    if (vis2.size() > 0 && notNullOrEmputy(vis2.get(0).getSrfidNo())) {
                        vi.setSrfidNo(vis2.get(0).getSrfidNo());
                    }
                }
                System.out.println("新增、修改车辆信息" + vi);
                //新增车辆信息

                rt = saveclxxInsert(jt, vi, md, s, flag);
                rtList.add(rt);
            } catch (Exception e) {
                //e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(vi.getVehicleNo() + "  " + e.getClass().getName());
                if ("1".equals(flag)) {
                    DataSynchroInfoService dataSynchroInfoService = SpringContextHolder.getBean(DataSynchroInfoService.class);
                    syn.setId(IdGen.uuid());
                    dataSynchroInfoService.saveRecord(syn);
                } else {
                    dataSynchroInfoService.save(syn);
                }
            }
        }
        return rtList;
    }

    /**
     * 同步保存车辆信息
     *
     * @param jt
     * @param vi
     * @param md
     * @param s
     * @return
     */
    @Transactional
    public synchronized ResultType saveclxxInsert(JsonType jt, VehicleInfo vi, MultiDBUtils md, String s, String flag) {
        int id;
        ResultType rt = new ResultType();
        if (vi.getGroupCode() == null) {
            md.update("update vehicle_info set del_flag=1 WHERE vehicle_no = ? AND group_code is null", vi.getVehicleNo());
        } else {
            md.update("update vehicle_info set del_flag=1 WHERE vehicle_no = ? AND group_code = '" + vi.getGroupCode() + "'", vi.getVehicleNo());
        }
        //新增车辆信息
        vi.setId(IdGen.uuid());
        vi.setCreateDate(new Date());
        id = md.update("INSERT INTO vehicle_info(id,create_date,del_flag,type_code,vehicle_no,group_code,pass_code,rfid_no,group_company_name,dept_code,\n" +
                        "carry_company_name,op_dept_code,trans_contact_person,trans_contact_person_tel,wagon_type,qty,deal_time,approve_time,approve_person_no,start_time,\n" +
                        "end_time,remarks,srfid_no) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", vi.getId(), vi.getCreateDate(), vi.getDelFlag(), vi.getTypeCode(), vi.getVehicleNo(), vi.getGroupCode(),
                vi.getPassCode(), vi.getRfidNo(), vi.getGroupCompanyName(), vi.getDeptCode(), vi.getCarryCompanyName(), vi.getOpDeptCode(), vi.getTransContactPerson(), vi.getTransContactPersonTel(),
                vi.getWagonType(), vi.getQty(), vi.getDealTime(), vi.getApproveTime(), vi.getApprovePersonNo(), vi.getStartTime(), vi.getEndTime(), vi.getRemarks(), vi.getSrfidNo());
        if (id == 1) {
            saveDataSynchroInfo(s, jt, vi.getVehicleNo(), "0", flag, vi.getVehicleNo());
            rt.setSuccess(true);
            rt.setMsg(s + ",保存车辆信息成功");
            rt.setErrorCode(vi.getVehicleNo());
            rt.setType(jt.getType());

        } else {
            saveDataSynchroInfo(s, jt, vi.getVehicleNo(), "1", flag, vi.getVehicleNo());
            rt.setSuccess(false);
            rt.setMsg(s + ",保存车辆信息失败");
            rt.setErrorCode(vi.getVehicleNo());
            rt.setType(jt.getType());
        }
        return rt;
    }

    /**
     * 车辆信息删除
     *
     * @return
     */
    private List<ResultType> cheliangxinxidelete(JsonType jt, List<VehicleInfo> vehicleInfoList, MultiDBUtils md, String s, List<ResultType> rtList, String flag) {
        for (VehicleInfo vi : vehicleInfoList) {
            ResultType rt = new ResultType();
            System.out.println("删除车辆信息" + vi);
            int id = 0;
            try {
                if (vi.getGroupCode() == null) {
                    id = md.update("delete from vehicle_info  WHERE vehicle_no = ? AND group_code is null", vi.getVehicleNo());
                } else {
                    id = md.update("delete from vehicle_info  WHERE vehicle_no = ? AND group_code = '" + vi.getGroupCode() + "'", vi.getVehicleNo());
                }
                if (id <= 0) {

                    saveDataSynchroInfo(s, jt, vi.getVehicleNo(), "1", flag, vi.getVehicleNo());
                    rt.setSuccess(false);
                    rt.setMsg(s + "删除车辆信息失败");
                    rt.setErrorCode(vi.getVehicleNo());
                    rt.setType(jt.getType());
                } else {
                    saveDataSynchroInfo(s, jt, vi.getVehicleNo(), "0", flag, vi.getVehicleNo());
                    rt.setSuccess(true);
                    rt.setMsg(s + ",删除车辆信息成功");
                    rt.setErrorCode(vi.getVehicleNo());
                    rt.setType(jt.getType());
                }
                rtList.add(rt);
            } catch (Exception e) {
                //e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(vi.getVehicleNo() + "  " + e.getClass().getName());
                dataSynchroInfoService.save(syn);
            }
        }
        return rtList;
    }

    /**
     * 车辆信息作废
     *
     * @return
     */
    private List<ResultType> cheliangxinxicancel(JsonType jt, List<VehicleInfo> vehicleInfoList, MultiDBUtils md, String s, List<ResultType> rtList, String flag) {
        for (VehicleInfo vi : vehicleInfoList) {
            
            ResultType rt = new ResultType();
            System.out.println("作废车辆信息" + vi);
            try {
                int id = md.update("UPDATE vehicle_info SET del_flag = '1' WHERE vehicle_no = ? AND group_code = '" + vi.getGroupCode() + "'", vi.getVehicleNo());
                if (id <= 0) {
                    saveDataSynchroInfo(s, jt, vi.getVehicleNo(), "1", flag, vi.getVehicleNo());
                    rt.setSuccess(false);
                    rt.setMsg(s + ",作废车辆信息失败");
                    rt.setErrorCode(vi.getVehicleNo());
                    rt.setType(jt.getType());
                } else {
                    saveDataSynchroInfo(s, jt, vi.getVehicleNo(), "0", flag, vi.getVehicleNo());
                    rt.setSuccess(true);
                    rt.setMsg(s + ",作废车辆信息成功");
                    rt.setErrorCode(vi.getVehicleNo());
                    rt.setType(jt.getType());
                }
                rtList.add(rt);
            } catch (Exception e) {
                //e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(vi.getVehicleNo() + "  " + e.getClass().getName());
                dataSynchroInfoService.save(syn);
            }
        }
        return rtList;
    }

    /**
     * 磅单确认处理
     */
    private List<ResultType> bangdanqueren(JsonType jt, MultiDBUtils md, String s, List<ResultType> rtList) {
        //List<ResultType> rtList=new ArrayList<>();
        //将json字符串转为接收类型
        List<Weight> weightList = JSONArray.toList(jt.getData(), Weight.class);
        if (weightList.size() > 0) {
            //磅单修改
            if ("1".equals(jt.getFlag())) {
                rtList = bangdanqueren(jt, weightList, md, s, rtList);
            }
        }
        return rtList;
    }

    /**
     * 磅单修改
     */
    private List<ResultType> bangdanqueren(JsonType jt, List<Weight> weightList, MultiDBUtils md, String s, List<ResultType> rtList) {
        for (Weight w : weightList) {
            ResultType rt = new ResultType();
            try {
                String weighNo = w.getWeighNo();
                List<Weight> wt = md.queryList("SELECT id,weigh_no,vehicle_no FROM weight WHERE  weigh_no = '" + weighNo + "'", Weight.class);
                //同步删除所有数据库
                if (wt.size() > 0) {
                    //System.out.println("接收到的数据一次为：1."+w.getAffirmFlag()+"2."+w.getConfirmPersonNo()+"3."+w.getConfirmTime()+"4."+w.getWeighNo());
                    int id = md.update("UPDATE weight SET affirm_flag = ?,confirm_person_no = ?," +
                            "confirm_time = ? WHERE weigh_no = ?", w.getAffirmFlag(), w.getConfirmPersonNo(), w.getConfirmTime(), w.getWeighNo());
                    if (id <= 0) {
                        saveDataSynchroInfo(s, jt, w.getWeighNo(), "1", "0", wt.get(0).getVehicleNo());
                        rt.setSuccess(false);
                        rt.setMsg(s + "修改磅单失败");
                        rt.setErrorCode(w.getWeighNo());
                        rt.setType(jt.getType());
                        rtList.add(rt);
                    } else {
                        saveDataSynchroInfo(s, jt, w.getWeighNo(), "0", "0", wt.get(0).getVehicleNo());
                        rt.setSuccess(true);
                        rt.setMsg(s + ",修改磅单成功");
                        rt.setErrorCode(w.getWeighNo());
                        rt.setType(jt.getType());
                        rtList.add(rt);
                    }
                } else {
                    rt.setSuccess(false);
                    rt.setMsg(s + "的磅单未找到，修改失败");
                    rt.setErrorCode(w.getWeighNo());
                    rt.setType(jt.getType());
                    rtList.add(rt);
                }
            } catch (Exception e) {
                e.printStackTrace();
                rt.setSuccess(false);
                rt.setMsg(s + e.getClass().getName());
                rtList.add(rt);
                DataSynchroInfo syn = new DataSynchroInfo();
                syn.setCreateDate(new Date());
                syn.setStatus("1");
                syn.setCode(w.getWeighNo() + "  " + e.getClass().getName());
                dataSynchroInfoService.save(syn);
            }
        }
        return rtList;
    }

    /**
     * 保存同步日志信息
     */
    public void saveDataSynchroInfo(String data, JsonType jt, String code, String status, String flag, String vehicleNo) {
        DataSynchroInfo syn = new DataSynchroInfo();
        syn.setRemarks(data);
        syn.setType(jt.getType());
        syn.setOperationType(jt.getFlag());
        syn.setCode(code);
        syn.setStatus(status);
        syn.setCreateDate(new Date());
        syn.setVehicleNo(vehicleNo);
        if ("1".equals(flag)) {
            syn.setConsignId("excel导入数据");
            syn.setCreateBy(UserUtils.getUser());
            syn.setId(IdGen.uuid());
            syn.setDelFlag("0");
            syn.setUpdateDate(new Date());
            DataSynchroInfoService dataSynchroInfoService = SpringContextHolder.getBean(DataSynchroInfoService.class);
            dataSynchroInfoService.saveRecord(syn);
        } else {
            syn.setConsignId("接口导入数据");
            dataSynchroInfoService.save(syn);
        }
    }

    /**
     * 判断String 是 null 或者""
     *
     * @return
     */
    public boolean isNullOrEmputy(String t) {
        if (t == null || "".equals(StringUtils.trim(t))) {
            return true;
        }
        return false;
    }

    /**
     * 判断String 是 null 或者""
     *
     * @return
     */
    public boolean notNullOrEmputy(String t) {
        if (t != null && !"".equals(StringUtils.trim(t))) {
            return true;
        }
        return false;
    }

    /**
     * 磅单接口
     */
    @ResponseBody
    @RequestMapping(value = "queryInterFaceList")
    public AjaxJson queryInterFaceList(Weight weighNo, HttpServletRequest request) {
        WeightService weightSe = SpringContextHolder.getBean(WeightService.class);

        AjaxJson j = new AjaxJson();
        try {
            List<Weight> weight = new ArrayList<>();
            if (weighNo != null) {
                weight.add(weighNo);
            } else {
                weighNo = new Weight();
                weight.add(weighNo);
            }
            JsonType jt = new JsonType();
            jt.setType("bd");
            if (weighNo.getStatus() != null && "2".equals(weighNo.getStatus())) {
                jt.setFlag("3");
            } else {
                jt.setFlag("1");
            }

            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            List<InterWeight> wi = new ArrayList<>();
            for (Weight w : weight) {
                InterWeight wei = new InterWeight();
                if (w.getAbnrType() != null) {
                    wei.setABNR_TYPE(w.getAbnrType().trim());
                } else {
                    wei.setABNR_TYPE("");
                }
                if (w.getBillNo() != null) {
                    wei.setBILL_NO(w.getBillNo().trim());
                } else {
                    wei.setBILL_NO("");
                }
                if (w.getBillPic() != null) {
                    wei.setBILL_PIC(w.getBillPic().trim());
                } else {
                    wei.setBILL_PIC("");
                }
                if (w.getBlastFurnaceNo() != null) {
                    wei.setBLAST_FURNACE_NO(w.getBlastFurnaceNo().trim());
                } else {
                    wei.setBLAST_FURNACE_NO("");
                }
                if (w.getConsignId() != null) {
                    wei.setCONSIGN_ID(w.getConsignId().trim());
                } else {
                    wei.setCONSIGN_ID("");
                }
                if (w.getConsigneUser() != null) {
                    wei.setCONSIGNE_USER(w.getConsigneUser().trim());
                } else {
                    wei.setCONSIGNE_USER("");
                }
                if (w.getCreateTime() != null) {
                    wei.setCREATE_TIME(sDateFormat.format(w.getCreateTime()));
                } else {
                    wei.setCREATE_TIME("");
                }
                if (w.getCustomerCode() != null) {
                    wei.setCUSTOMER_CODE(w.getCustomerCode().trim());
                } else {
                    wei.setCUSTOMER_CODE("");
                }
                if (w.getDealPersonNo() != null) {
                    wei.setDEAL_PERSON_NO(w.getDealPersonNo().trim());
                } else {
                    wei.setDEAL_PERSON_NO("");
                }
                if (w.getDefaultFlag() != null) {
                    wei.setDEFAULT_FLAG(w.getDefaultFlag().trim());
                } else {
                    wei.setDEFAULT_FLAG("");
                }
                if (w.getEquipNum() != null) {
                    wei.setEQUIP_NUM(w.getEquipNum().trim());
                } else {
                    wei.setEQUIP_NUM("");
                }
                if (w.getImpWt() != null) {
                    wei.setIMP_WT(w.getImpWt().trim());
                } else {
                    wei.setIMP_WT("0");
                }
                if (w.getLadleNo() != null) {
                    wei.setLADLE_NO(w.getLadleNo().trim());
                } else {
                    wei.setLADLE_NO("");
                }
                if (w.getMatGrossWt() != null) {
                    wei.setMAT_GROSS_WT(w.getMatGrossWt().trim());
                } else {
                    wei.setMAT_GROSS_WT("0");
                }
                if (w.getMatNum() != null) {
                    wei.setMAT_NUM(w.getMatNum().trim());
                } else {
                    wei.setMAT_NUM("");
                }
                if (w.getMatSpecDesc() != null) {
                    wei.setMAT_SPEC_DESC(w.getMatSpecDesc().trim());
                } else {
                    wei.setMAT_SPEC_DESC("");
                }
                if (w.getMatWt() != null) {
                    wei.setMAT_WT(w.getMatWt().trim());
                } else {
                    wei.setMAT_WT("0");
                }
                if (w.getWeighNo() != null) {
                    wei.setPONDER_NO(w.getWeighNo().trim());
                } else {
                    wei.setPONDER_NO("");
                }
                if (w.getPonoLotNo() != null) {
                    wei.setPONO_LOT_NO(w.getPonoLotNo().trim());
                } else {
                    wei.setPONO_LOT_NO("");
                }
                if (w.getProdCname() != null) {
                    wei.setPROD_CNAME(w.getProdCname().trim());
                } else {
                    wei.setPROD_CNAME("");
                }
                if (w.getProdCode() != null) {
                    wei.setPROD_CODE(w.getProdCode().trim());
                } else {
                    wei.setPROD_CODE("");
                }
                if (w.getProductPackWt() != null) {
                    wei.setPRODUCT_PACK_WT(w.getProductPackWt().trim());
                } else {
                    wei.setPRODUCT_PACK_WT("");
                }
                if (w.getSeqNo() != null) {
                    wei.setSEQ_NO(w.getSeqNo().trim());
                } else {
                    wei.setSEQ_NO("0");
                }
                if (w.getSgCode() != null) {
                    wei.setSG_CODE(w.getSgCode().trim());
                } else {
                    wei.setSG_CODE("");
                }
                if (w.getSgSign() != null) {
                    wei.setSG_SIGN(w.getSgSign().trim());
                } else {
                    wei.setSG_SIGN("");
                }
                if (w.getShipNo() != null) {
                    wei.setSHIP_NO(w.getShipNo().trim());
                } else {
                    wei.setSHIP_NO("");
                }
                if (w.getStatus() != null) {
                    wei.setSTATUS(w.getStatus().trim());
                } else {
                    wei.setSTATUS("");
                }
                if (w.getSubNo() != null) {
                    wei.setSUB_NO(w.getSubNo().trim());
                } else {
                    wei.setSUB_NO("");
                }
                if (w.getBillNo() != null) {
                    wei.setBILL_NO(w.getBillNo().trim());
                } else {
                    wei.setBILL_NO("");
                }
                if (w.getSupplierName() != null) {
                    wei.setSUPPLIER_NAME(w.getSupplierName().trim());
                } else {
                    wei.setSUPPLIER_NAME("");
                }
                if (w.getTareHeadPic() != null) {
                    wei.setURL1(w.getTareHeadPic().trim());
                } else {
                    wei.setURL1("");
                }
                if (w.getTareTailPic() != null) {
                    wei.setURL2(w.getTareTailPic().trim());
                } else {
                    wei.setURL2("");
                }
                if (w.getTareTopPic() != null) {
                    wei.setURL3(w.getTareTopPic().trim());
                } else {
                    wei.setURL3("");
                }
                if (w.getGrossHeadPic() != null) {
                    wei.setURL4(w.getGrossHeadPic().trim());
                } else {
                    wei.setURL4("");
                }
                if (w.getGrossTailPic() != null) {
                    wei.setURL5(w.getGrossTailPic().trim());
                } else {
                    wei.setURL5("");
                }
                if (w.getGrossTopPic() != null) {
                    wei.setURL6(w.getGrossTopPic().trim());
                } else {
                    wei.setURL6("");
                }
                if (w.getVehicleNo() != null) {
                    wei.setVEHICLE_NO(w.getVehicleNo().trim());
                } else {
                    wei.setVEHICLE_NO("");
                }
                if (w.getWeightFlag() != null) {
                    wei.setWEIGH_FLAG(w.getWeightFlag().trim());
                } else {
                    wei.setWEIGH_FLAG("");
                }
                if (w.getTaretime() != null) {
                    wei.setWEIGHT_TIME(sDateFormat.format(w.getTaretime()));
                } else {
                    wei.setWEIGHT_TIME("");
                }
                if (w.getAffirmFlag() != null) {
                    wei.setAFFIRM_FLAG(w.getAffirmFlag().trim());
                } else {
                    wei.setAFFIRM_FLAG("");
                }
                if (w.getConfirmPersonNo() != null) {
                    wei.setCONFIRM_PERSON_NO(w.getConfirmPersonNo().trim());
                } else {
                    wei.setCONFIRM_PERSON_NO("");
                }
                if (w.getConfirmTime() != null) {
                    wei.setCONFIRM_TIME(sDateFormat.format(w.getConfirmTime()).trim());
                } else {
                    wei.setCONFIRM_TIME("");
                }
                if (w.getField1() != null) {
                    wei.setFIELD_1(w.getField1().trim());
                } else {
                    wei.setFIELD_1("");
                }
                if (w.getWeightType() != null) {
                    wei.setWEIGHT_TYPE(w.getWeightType().trim());
                } else {
                    wei.setWEIGHT_TYPE("");
                }
                if (w.getGrosstime() != null) {
                    wei.setWT_TIME(sDateFormat.format(w.getGrosstime()));
                } else {
                    wei.setWT_TIME("");
                }

                wi.add(wei);
            }

            JsonConfig jsonConfig = new JsonConfig();

            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

            JSONArray ja = JSONArray.fromObject(wi, jsonConfig);

            jt.setData(ja);
            System.out.println(JSONObject.fromObject(jt).toString());
            String a = sendPost(url, JSONObject.fromObject(jt));
            System.out.println("a：" + a);
            JSONArray rt = JSONArray.fromObject(a);
            List<Weight> stu = JSONArray.toList(rt, Weight.class);
            if (stu.size() > 0) {
                for (Weight w : stu) {
                    if (w.isSuccess()) {
                        for (Weight wei : weight) {
                            wei.setDataType("2");
                            j.setSuccess(true);
                            j.setMsg("磅单上传成功");
                            weightSe.updateDateType(wei);
                        }
                    } else {
                        for (Weight wei : weight) {
                            wei.setDataType("3");
                            j.setMsg("磅单上传失败");
                            j.setSuccess(false);
                            weightSe.updateDateType(wei);
                        }
                    }
                }
            } else {
                for (Weight wei : weight) {
                    wei.setDataType("3");
                    j.setMsg("磅单上传失败");
                    j.setSuccess(false);
                    weightSe.updateDateType(wei);
                }
            }
            System.out.println("保存磅单回馈信息");
            for (Weight s : stu) {
                s.setId(IdGen.uuid());
                s.setCreateDate(new Date());
                if (request != null) {
                    s.setType("bd,同步电脑IP:" + request.getRemoteAddr()+"同步类型："+jt.getFlag());
                }
                User u = UserUtils.getUser();
                if (u != null && u.getLoginName() != null) {
                    s.setType(s.getType() +"同步人"+ u.getLoginName()+"同步类型："+jt.getFlag());
                }
                weightSe.insertWeightResult(s);
            }
            j.setList(weight);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("错误信息：" + e);
            Weight wei = new Weight();
            wei.setDataType("3");
            j.setMsg("磅单上传失败");
            weightSe.updateDateType(wei);
            j.setSuccess(false);
        }
        return j;
    }

    //发送POST请求
    public static String sendPost(String url, JSONObject param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            // 打开和URL之间的连接
            conn = (HttpURLConnection) realUrl.openConnection();
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");    // POST方法

            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.connect();
            // 获取URLConnection对象对应的输出流
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param.toString());
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    @POST
    @ResponseBody
    @RequestMapping(value = "vedio")
    public void acceptData(String video) {
        try {
            JSONObject jsonObject = JSONObject.fromObject(video);
            AcceptPic stu = (AcceptPic) JSONObject.toBean(jsonObject, AcceptPic.class);
            String[] pics = stu.getPics().split("#");

            Weight w = new Weight();
            w.setWeighNo(stu.getBoundno());
            Weight wei = weightService.queryInfoByWeighNo(w);
            int i = 1;
            for (String pic : pics) {
                String[] p = pic.split(",");

                String getSignInfo = p[p.length];
                if (wei.getMatWt() != null && !"".equals(wei.getMatWt()) && !"0.00".equals(wei.getMatWt())) {
                    if (i == 1) {
                        w.setGrossTopPic(getSignInfo);
                    }
                    if (i == 2) {
                        w.setGrossTailPic(getSignInfo);
                    }
                    if (i == 3) {
                        w.setGrossHeadPic(getSignInfo);
                    }
                } else {
                    if (i == 1) {
                        w.setTareTopPic(getSignInfo);
                    }
                    if (i == 2) {
                        w.setTareTailPic(getSignInfo);
                    }
                    if (i == 3) {
                        w.setTareHeadPic(getSignInfo);
                    }
                }
                i++;
            }
            if (wei.getMatWt() != null && !"".equals(wei.getMatWt()) && !"0.00".equals(wei.getMatWt())) {
                weightService.saveTarePic(w);
            } else {
                weightService.saveGrossPic(w);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 出门条反馈
     */
    @ResponseBody
    @RequestMapping(value = "postPasscheck")
    public AjaxJson postPasscheck(String vehicleNo, String ickh) {
        AjaxJson j = new AjaxJson();
        PassCheckService passCheckSe = SpringContextHolder.getBean(PassCheckService.class);

        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            if (vehicleNo != null && !"".equals(vehicleNo)) {
                PassCheck passcheck = new PassCheck();
                if (ickh != null && !"".equals(ickh)) {
                    SwipeCard s = swipeCardService.findByIcCard(ickh);
                    if (s != null) {
                        passcheck.setValidFlag("2");
                        passcheck.setDealPersonNo(s.getJobNumber());
                        passcheck.setDealPersonName(s.getWorkName());
                        passcheck.setDealDate(new Date());
                    }
                } else {
                    passcheck.setValidFlag("2");
                    passcheck.setDealPersonNo("出门自动作废");
                    passcheck.setDealPersonName("出门自动作废");
                    passcheck.setDealDate(new Date());
                }
                passcheck.setVehicleNo(vehicleNo);
                //查询出门条号
                List<PassCheck> p = passCheckSe.findPassByVehicleNo(passcheck);

                List<PassFeedBack> pi = new ArrayList<>();

                if (p.size() > 0) {
                    passcheck.setId(p.get(0).getId());
                    passCheckSe.updateDeal(passcheck);

                    for (PassCheck pk : p) {
                        PassFeedBack rt = new PassFeedBack();
                        if (pk.getTrnpAppNo() != null) {
                            rt.setTRNP_APP_NO(pk.getTrnpAppNo().trim());
                        } else {
                            rt.setTRNP_APP_NO("");
                        }
                        if (passcheck.getDealPersonNo() != null) {
                            rt.setDEAL_PERSON_NO(passcheck.getDealPersonNo().trim());
                        } else {
                            rt.setDEAL_PERSON_NO("");
                        }
                        if (passcheck.getDealPersonName() != null) {
                            rt.setDEAL_PERSON_NAME(passcheck.getDealPersonName().trim());
                        } else {
                            rt.setDEAL_PERSON_NAME("");
                        }
                        if (passcheck.getDealDate() != null) {
                            rt.setDEAL_DATE(sDateFormat.format(passcheck.getDealDate()));
                        } else {
                            rt.setDEAL_DATE("");
                        }
                        pi.add(rt);
                    }
                    JsonType jt = new JsonType();
                    jt.setType("cmtfk");
                    jt.setFlag("1");

                    JsonConfig jsonConfig = new JsonConfig();
                    jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
                    JSONArray ja = JSONArray.fromObject(pi, jsonConfig);

                    jt.setData(ja);
                    System.out.println(JSONObject.fromObject(jt).toString());
                    String a = InterfaceTest.sendPost(url, JSONObject.fromObject(jt, jsonConfig));
                    System.out.println("a：" + a);
                    JSONArray jst = JSONArray.fromObject(a);

                    List<PassCheck> stu = JSONArray.toList(jst, PassCheck.class);

                    if (stu.size() > 0) {
                        for (PassCheck pc : stu) {
                            if (pc.isSuccess()) {
                                for (PassCheck wei : p) {
                                    wei.setDataType("2");
                                    passCheckSe.updateDateType(wei);
                                }
                            } else {
                                for (PassCheck wei : p) {
                                    wei.setDataType("3");
                                    passCheckSe.updateDateType(wei);
                                }
                            }
                        }
                    } else {
                        for (PassCheck wei : p) {
                            wei.setDataType("3");
                            passCheckSe.updateDateType(wei);
                        }
                    }
                    System.out.println("保存出门条回馈信息");
                    for (PassCheck s : stu) {
                        s.setId(IdGen.uuid());
                        s.setCreateDate(new Date());
                        int id = passCheckSe.insertPassCheckResult(s);
                    }
                    j.setSuccess(true);
                }
            }
        } catch (Exception e) {
            j.setSuccess(false);
            e.printStackTrace();
            System.out.println("错误信息：" + e);
            PassCheck wei = new PassCheck();
            wei.setDataType("3");
            passCheckSe.updateDateType(wei);
        }
        return j;
    }


    /**
     * 违章信息上传
     */
    @ResponseBody
    @RequestMapping(value = "InterWarn")
    public AjaxJson InterWarn(WarningInfo warningInfo) {
        AjaxJson j = new AjaxJson();
        WarningInfoService warningInfoService = SpringContextHolder.getBean(WarningInfoService.class);
        WeightService weightService = SpringContextHolder.getBean(WeightService.class);

        try {
            List<WarningInfo> warningInfos = new ArrayList<>();
            if (warningInfo != null) {
                warningInfos.add(warningInfo);
            } else {
                warningInfo = new WarningInfo();
                warningInfos.add(warningInfo);
            }
            JsonType jt = new JsonType();
            jt.setType("wz");
            if (warningInfo.getDelFlag() != null && "1".equals(warningInfo.getDelFlag())) {
                jt.setFlag("3");
            } else {
                jt.setFlag("1");
            }

            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

            List<InterWarnInfo> wi = new ArrayList<>();
            for (WarningInfo w : warningInfos) {
                InterWarnInfo wei = new InterWarnInfo();
                if (w.getUpdateDate() != null) {
                    wei.setDEAL_DATE(sDateFormat.format(w.getUpdateDate()));
                } else {
                    wei.setDEAL_DATE("");
                }
                if (w.getUpdateBy() != null) {
                    wei.setDEAL_PERSON_NO(w.getUpdateBy().getLoginName());
                } else {
                    wei.setDEAL_PERSON_NO("");
                }
                if (w.getState() != null) {
                    wei.setSTATUS(w.getState());
                } else {
                    wei.setSTATUS("1");
                }
                if (w.getVehicleNo() != null) {
                    wei.setVEHICLE_NO(w.getVehicleNo());
                } else {
                    wei.setVEHICLE_NO("");
                }
                if (w.getRemarks() != null) {
                    wei.setDEAL_DESC(w.getRemarks());
                } else {
                    wei.setDEAL_DESC("");
                }
                if (w.getCreateDate() != null) {
                    wei.setINPUT_DATE(sDateFormat.format(w.getCreateDate()));
                } else {
                    wei.setINPUT_DATE("");
                }
                if (w.getCreateBy().getLoginName() != null) {
                    wei.setINPUT_PERSON_NO(w.getCreateBy().getLoginName());
                } else {
                    wei.setINPUT_PERSON_NO("");
                }
                if (w.getReason() != null) {
                    wei.setRMA_CAUSE(w.getReason());
                } else {
                    wei.setRMA_CAUSE("");
                }
                if (w.getId() != null) {
                    wei.setROWGUID(w.getId());
                } else {
                    wei.setROWGUID("");
                }
                wi.add(wei);
            }

            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            JSONArray ja = JSONArray.fromObject(wi, jsonConfig);

            jt.setData(ja);
            System.out.println(JSONObject.fromObject(jt).toString());
            String a = sendPost(url, JSONObject.fromObject(jt));
            System.out.println("a：" + a);
            JSONArray rt = JSONArray.fromObject(a);
            List<Weight> stu = JSONArray.toList(rt, Weight.class);
            if (stu.size() > 0) {
                for (Weight w : stu) {
                    if (w.isSuccess()) {
                        for (WarningInfo wei : warningInfos) {
                            wei.setDataType("2");
                            warningInfoService.updateDateType(wei);
                        }
                    } else {
                        for (WarningInfo wei : warningInfos) {
                            wei.setDataType("3");
                            warningInfoService.updateDateType(wei);
                        }
                    }
                }
            } else {
                for (WarningInfo wei : warningInfos) {
                    wei.setDataType("3");
                    warningInfoService.updateDateType(wei);
                }
            }
            System.out.println("保存违章信息回馈信息");
            for (Weight s : stu) {
                s.setId(IdGen.uuid());
                s.setCreateDate(new Date());

                User u = UserUtils.getUser();
                if (u != null && u.getLoginName() != null) {
                    s.setType(s.getType() + u.getLoginName());
                }
                weightService.insertWeightResult(s);
            }
            j.setList(warningInfos);
            j.setSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("错误信息：" + e);
            WarningInfo wei = new WarningInfo();
            wei.setDataType("3");
            warningInfoService.updateDateType(wei);
            j.setSuccess(false);
        }
        return j;

    }
}
