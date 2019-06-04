package com.ai.risk.analysis.task;

import com.ai.risk.analysis.accumulator.influx.ServiceAndInstanceAccumulator;
import com.ai.risk.analysis.accumulator.influx.ServiceAndIpAccumulator;
import com.ai.risk.analysis.accumulator.influx.ServiceAndOpCodeAccumulator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Influxdb 聚合
 *
 * @author Steven
 * @date 2019-05-26
 */
@Component
public class InfluxdbSinkTask {

	private static final Logger log = LoggerFactory.getLogger(InfluxdbSinkTask.class);

	@Autowired
	private ServiceAndOpCodeAccumulator serviceAndOpCodeAccumulator;

	@Autowired
	private ServiceAndIpAccumulator serviceAndIpAccumulator;

	@Autowired
	private ServiceAndInstanceAccumulator serviceAndInstanceAccumulator;

	/**
	 * 保留时长
	 */
	@Value("${lifecycle.hourTimeout}")
	private int hourTimeout;

	@Scheduled(initialDelay = 15000, fixedRate = 1000 * 60 * 1)
	public void scheduled() {
		serviceAndOpCodeAccumulator.toInfluxDb();
		serviceAndIpAccumulator.toInfluxDb();
		serviceAndInstanceAccumulator.toInfluxDb();
	}

}
