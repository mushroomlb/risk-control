package com.ai.risk.analysis.accumulator.hbase;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Steven
 */
@Component
public class OpCodeAccumulator {

	private Map<String, AtomicLong> localCounts = new ConcurrentHashMap<>(1024 * 1024);

	/**
	 * 递增调用次数
	 *
	 * @param opCode
	 */
	public void increment(String opCode) {
		AtomicLong count = localCounts.get(opCode);
		if (null == count) {
			count = new AtomicLong(0L);
			localCounts.put(opCode, count);
		}
		count.incrementAndGet();
	}

	public Map<String, AtomicLong> getLocalCounts() {
		Map<String, AtomicLong> oldLocalCounts = localCounts;
		this.localCounts = new ConcurrentHashMap<>(1024 * 1024);
		return oldLocalCounts;
	}

}
