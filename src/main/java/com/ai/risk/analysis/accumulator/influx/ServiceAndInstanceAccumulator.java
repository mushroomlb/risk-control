package com.ai.risk.analysis.accumulator.influx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Steven
 */
@Component
public class ServiceAndInstanceAccumulator extends BaseAccumulator {

	private static final Logger log = LoggerFactory.getLogger(ServiceAndInstanceAccumulator.class);

	private static final String MEASUREMENT = "service_and_instance";

	public void localAccumulation(String svcName, String instance, int elapsedTime) {
		super.accumulation(svcName, instance, elapsedTime);
	}

	public void toInfluxDb() {
		long start = System.currentTimeMillis();
		int accumulatorCount = super.toInfluxDb(MEASUREMENT, "instance", 15);
		long cost = System.currentTimeMillis() - start;
		log.info(String.format("聚合: %6d 条, 耗时(ms): %5d", accumulatorCount, cost));
	}
}
