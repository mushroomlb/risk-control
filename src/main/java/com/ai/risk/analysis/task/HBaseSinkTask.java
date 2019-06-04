package com.ai.risk.analysis.task;

import com.ai.risk.analysis.accumulator.hbase.OpCodeAccumulator;
import com.ai.risk.analysis.accumulator.hbase.ServiceAccumulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Steven
 */
@Component
public class HBaseSinkTask {

	@Autowired
	private ServiceAccumulator serviceAggregate;

	@Autowired
	private OpCodeAccumulator opCodeAggregate;

	@Scheduled(cron = "0 */5 * * * ?") // 每 5 分钟执行一次
	//@Scheduled(cron = "0 0 * * * ?") // 整点执行一次
	public void scheduled() {

		Map<String, AtomicLong> serviceLocalCounts = serviceAggregate.getLocalCounts();
		Map<String, AtomicLong> opCodeLocalCounts = opCodeAggregate.getLocalCounts();

		for (String svcName : serviceLocalCounts.keySet()) {
			//System.out.println(svcName + ": " + serviceLocalCounts.get(svcName));
		}
		serviceLocalCounts.clear();


		for (String opCode : opCodeLocalCounts.keySet()) {
			//System.out.println(opCode + ": " + opCodeLocalCounts.get(opCode));
		}
		opCodeLocalCounts.clear();
	}

}
