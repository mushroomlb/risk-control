package com.ai.risk.analysis.modules.warning.service.impl;

import com.ai.risk.analysis.modules.warning.service.BaseAccumulator;
import com.ai.risk.analysis.modules.warning.service.IServiceAndInstanceSV;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 服务 + 实例名累加器
 *
 * @author Steven
 * @date 2019-06-04
 */
@Slf4j
@Service
public class ServiceAndInstanceSVImpl extends BaseAccumulator implements IServiceAndInstanceSV {

	private static final String MEASUREMENT = "service_and_instance";

	@Override
	public void localAccumulation(String svcName, String instance, int elapsedTime) {
		super.accumulation(svcName, instance, elapsedTime);
	}

	@Override
	public void sinkToInflux() {
		long start = System.currentTimeMillis();
		int accumulatorCount = super.toInfluxDb(MEASUREMENT, "instance", 15);
		long cost = System.currentTimeMillis() - start;
		log.info(String.format("聚合: %6d 条, 耗时(ms): %5d", accumulatorCount, cost));
	}

}
