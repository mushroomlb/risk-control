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

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
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

}
