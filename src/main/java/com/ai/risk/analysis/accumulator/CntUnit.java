package com.ai.risk.analysis.accumulator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 技术单元
 *
 * @author Steven
 * @date 2019-05-27
 */
public class CntUnit {

	/**
	 * 调用次数
	 */
	private AtomicLong cnt;

	/**
	 * 调用总耗时
	 */
	private AtomicLong ttc;

	/**
	 * 耗时(最低)
	 */
	private int minCost = Integer.MAX_VALUE;

	/**
	 * 耗时(最高)
	 */
	private int maxCost = 0;

	public CntUnit() {
		this.cnt = new AtomicLong(0L);
		this.ttc = new AtomicLong(0L);
	}


	public AtomicLong getCnt() {
		return cnt;
	}

	public AtomicLong getTtc() {
		return ttc;
	}

	public int getMinCost() {
		return minCost;
	}

	public void setMinCost(int minCost) {
		this.minCost = minCost;
	}

	public int getMaxCost() {
		return maxCost;
	}

	public void setMaxCost(int maxCost) {
		this.maxCost = maxCost;
	}

}
