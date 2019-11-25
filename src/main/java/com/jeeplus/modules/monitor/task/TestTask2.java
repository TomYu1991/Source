package com.jeeplus.modules.monitor.task;

import com.jeeplus.common.utils.IdGen;
import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.consign.entity.Consign;
import com.jeeplus.modules.consign.service.ConsignService;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.monitor.entity.Task;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.warninginfo.entity.WarningInfo;
import com.jeeplus.modules.warninginfo.service.WarningInfoService;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@DisallowConcurrentExecution
@Component
@Lazy(false)
public class TestTask2 extends Task {

    @Override
    public void run() {
        ConsignService consignService = SpringContextHolder.getBean(ConsignService.class);
        WarningInfoService warningInfoService = SpringContextHolder.getBean(WarningInfoService.class);

        List<Consign> cl = consignService.queryMaxEndTimeConsign();
        if(cl.size()>0){
            for(Consign consign :cl){
                    WarningInfo war = new WarningInfo();
                    war.setVehicleNo(consign.getVehicleNo());
                    war.setReason("过夜车辆");
                    war.setState("1");
                    war.setCreateDate(new Date());
                    User u = new User();
                    u.setLoginName("自动标记");
                    war.setCreateBy(u);
                    war.setDataType("0");
                    war.setDelFlag("0");
                    war.setId(IdGen.uuid());
                    warningInfoService.insertInter(war);

            }
        }
        System.out.println("标记过夜车辆成功");
    }
}
