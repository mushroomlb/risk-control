package com.ai.risk.analysis.task;

import com.ai.risk.analysis.accumulator.hbase.OpCodeAccumulator;
import com.ai.risk.analysis.accumulator.hbase.ServiceAccumulator;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.hadoop.hbase.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
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
	private HbaseTemplate hbaseTemplate;

	@Autowired
	private Connection hbaseConnection;

	@Autowired
	private ServiceAccumulator serviceAggregate;

	@Autowired
	private OpCodeAccumulator opCodeAggregate;

	@Scheduled(cron = "0 */5 * * * ?") // 每 5 分钟执行一次
	//@Scheduled(cron = "0 0 * * * ?") // 整点执行一次
	public void scheduled() {

		hbaseConnection.

		Map<String, AtomicLong> serviceLocalCounts = serviceAggregate.getLocalCounts();
		Map<String, AtomicLong> opCodeLocalCounts = opCodeAggregate.getLocalCounts();

		String now = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmm");
		String rowNamePrefix = now + "-" ;

		for (String svcName : serviceLocalCounts.keySet()) {
			long cnt = serviceLocalCounts.get(svcName).get();
			String rowName = rowNamePrefix + svcName;
			hbaseTemplate.put("service", rowName, "info", "cnt", String.valueOf(cnt).getBytes());
		}
		serviceLocalCounts.clear();

		for (String opCode : opCodeLocalCounts.keySet()) {
			long cnt = opCodeLocalCounts.get(opCode).get();
			String rowName = rowNamePrefix + opCode;
			hbaseTemplate.put("opcode", rowName, "info", "cnt", String.valueOf(cnt).getBytes());
		}
		opCodeLocalCounts.clear();

		System.out.println("HBaseSinkTask.scheduled...");
	}

}
