package com.ai.risk.analysis.modules.collect.task;

import com.ai.risk.analysis.modules.collect.accumulator.hbase.OpCodeAccumulator;
import com.ai.risk.analysis.modules.collect.accumulator.hbase.ServiceAccumulator;
import com.ai.risk.analysis.lijun.bean.CallCountUnit;
import com.ai.risk.analysis.lijun.util.HbaseOps;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
	private HbaseOps hbaseOps;

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

		List<CallCountUnit> serviceList = new ArrayList();
		List<CallCountUnit> opcodeList = new ArrayList();
		for (String svcName : serviceLocalCounts.keySet()) {
			String cnt = serviceLocalCounts.get(svcName).toString();

			CallCountUnit unit = new CallCountUnit();
			unit.setName(svcName);
			unit.setNumber(cnt);
			serviceList.add(unit);

			hbaseOps.serviceAccumulator(now, serviceList);

		}
		serviceLocalCounts.clear();

		for (String opCode : opCodeLocalCounts.keySet()) {
			String cnt = opCodeLocalCounts.get(opCode).toString();
			CallCountUnit unit = new CallCountUnit();
			unit.setName(opCode);
			unit.setNumber(cnt);
			opcodeList.add(unit);

			hbaseOps.opcodeAccumulator(now, opcodeList);

		}
		opCodeLocalCounts.clear();

		System.out.println("HBaseSinkTask.scheduled...");
	}

}
