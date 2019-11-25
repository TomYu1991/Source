package com.jeeplus.modules.monitor.task;

import com.jeeplus.common.utils.SpringContextHolder;
import com.jeeplus.modules.interfaceTest.Controller.InterfaceTest;
import com.jeeplus.modules.monitor.entity.Task;
import com.jeeplus.modules.userpasscord.web.UserPassRecordController;
import com.jeeplus.modules.weight.entity.Weight;
import com.jeeplus.modules.weight.service.WeightService;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时同步数据并获取人员信息
 */

@DisallowConcurrentExecution
@Component
@Lazy(false)
public class TestTask extends Task {

	@Override
	public void run() {
		System.out.println("更新同步数据的时间为："+new Date());
		WeightService weightService = SpringContextHolder.getBean(WeightService.class);

		//定时更新未同步的数据
		List<Weight> we =weightService.notSynchronized();
		if(we.size() >0){
			for(Weight wt : we){
				Weight w = new Weight();
				w.setWeighNo(wt.getWeighNo());
				Weight ws= weightService.queryInfoByWeighNo(w);
				InterfaceTest test = new InterfaceTest();
				test.queryInterFaceList(ws,null);
			}
		}
		//定时更新员工信息
		UserPassRecordController uprc =  new UserPassRecordController();
        uprc.saveUser();
		System.out.println("更新成功！"+new Date());
	}

}

