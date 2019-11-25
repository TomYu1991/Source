package com.jeeplus.modules.monitor.task;

import org.quartz.DisallowConcurrentExecution;

import com.jeeplus.modules.monitor.entity.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@DisallowConcurrentExecution
@Component
@Lazy(false)
public class TestTask1 extends Task{

	@Override
	public void run() {
		System.out.println("这是另一个测试任务TestTask1。");
		
	}

}
