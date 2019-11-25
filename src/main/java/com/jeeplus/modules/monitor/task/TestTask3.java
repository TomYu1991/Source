package com.jeeplus.modules.monitor.task;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.monitor.entity.Task;
import com.jeeplus.modules.userpasscord.service.UserPassRecordService;
import com.jeeplus.modules.userpasscord.web.UserPassRecordController;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@DisallowConcurrentExecution
@Component
@Lazy(false)
public class TestTask3 extends Task {

    @Override
    public void run() {
        UserPassRecordService userPassRecordService = SpringContextHolder.getBean(UserPassRecordService.class);
        userPassRecordService.deleteUserPassInfo();
        //定时更新员工信息
        UserPassRecordController uprc =  new UserPassRecordController();
        uprc.saveUser();
        System.out.println("这是另一个测试任务TestTask3。");

    }
}
