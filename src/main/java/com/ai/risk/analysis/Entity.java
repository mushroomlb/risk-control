package com.ai.risk.analysis;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Steven
 * @date 2019-05-27
 */
public class Entity {

	/**
	 * 服务名
	 */
	private String svcName;

	/**
	 * 主机名/
	 */
	private String hostIp;

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

	public Entity() {
		this.cnt = new AtomicLong(0L);
		this.ttc = new AtomicLong(0L);
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public String getIp() {
		return hostIp;
	}

	public void setIp(String ip) {
		this.hostIp = ip;
	}

	public AtomicLong getCnt() {
		return cnt;
	}

	public void setCnt(AtomicLong cnt) {
		this.cnt = cnt;
	}

	public AtomicLong getTtc() {
		return ttc;
	}

	public void setTtc(AtomicLong ttc) {
		this.ttc = ttc;
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
