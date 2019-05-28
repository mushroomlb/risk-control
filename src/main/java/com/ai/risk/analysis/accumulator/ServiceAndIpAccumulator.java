package com.ai.risk.analysis.accumulator;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Steven
 */
public class ServiceAndIpAccumulator extends BaseAccumulator {

	private static final Logger log = LoggerFactory.getLogger(ServiceAndIpAccumulator.class);

	private static final String MEASUREMENT = "service_and_ip";

	public void localAccumulation(String svcName, String ip, int elapsedTime) {
		super.accumulation(svcName, ip, elapsedTime);
	}

	public void toInfluxDb() {
		long start = System.currentTimeMillis();
		int accumulatorCount = super.toInfluxDb(MEASUREMENT, "Ip", 15);
		long cost = System.currentTimeMillis() - start;
		log.info(String.format("%30s, 聚合: %6d 条, 耗时(ms): %5d", "save to InfluxDB", accumulatorCount, cost));
	}



}
