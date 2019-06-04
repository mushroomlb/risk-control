package com.ai.risk.analysis.accumulator.hbase;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务调用量累加器
 *
 * @author Steven
 * @date 2019-06-04
 */
@Component
public class ServiceAccumulator {

	private Map<String, AtomicLong> localCounts = new ConcurrentHashMap<>(1024 * 1024);

	/**
	 * 递增调用次数
	 *
	 * @param svcName
	 */
	public void increment(String svcName) {
		AtomicLong count = localCounts.get(svcName);
		if (null == count) {
			count = new AtomicLong(0L);
			localCounts.put(svcName, count);
		}
		count.incrementAndGet();
	}

	public Map<String, AtomicLong> getLocalCounts() {
		Map<String, AtomicLong> oldLocalCounts = localCounts;
		this.localCounts = new ConcurrentHashMap<>(1024 * 1024);
		return oldLocalCounts;
	}

}
