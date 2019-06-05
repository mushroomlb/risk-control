package com.ai.risk.analysis.modules.warning.entity.unit;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 计数单元
 *
 * @author Steven
 * @date 2019-05-27
 */
@Data
public class PointUnit {

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

	public PointUnit() {
		this.cnt = new AtomicLong(0L);
		this.ttc = new AtomicLong(0L);
	}

}
