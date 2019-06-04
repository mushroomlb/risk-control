package com.ai.risk.analysis.task;

import com.ai.risk.analysis.accumulator.hbase.OpCodeAccumulator;
import com.ai.risk.analysis.accumulator.hbase.ServiceAccumulator;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * HBase聚合
 *
 * @author Steven
 * @date 2019-06-04
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

		String now = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmm");
		String rowNamePrefix = now + "-" ;

		for (String svcName : serviceLocalCounts.keySet()) {
			long cnt = serviceLocalCounts.get(svcName).get();
			String rowName = rowNamePrefix + svcName;

		}
		serviceLocalCounts.clear();

		for (String opCode : opCodeLocalCounts.keySet()) {
			long cnt = opCodeLocalCounts.get(opCode).get();
			String rowName = rowNamePrefix + opCode;

		}
		opCodeLocalCounts.clear();

		System.out.println("HBaseSinkTask.scheduled...");
	}

}
