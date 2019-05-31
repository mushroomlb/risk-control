package com.ai.risk.analysis;

/**
 * @author Steven
 */
public class ServicePoint {

	/**
	 * 服务名
	 */
	public String svcName;

	/**
	 * 操作工号
	 */
	public String opCode;

	/**
	 * 主机名/
	 */
	public String ip;

	/**
	 * 实例名
	 */
	public String instance;

	/**
	 * 是否成功
	 */
	public boolean success;

	/**
	 * 开始时间
	 */
	public long startTime;

	/**
	 * 耗时
	 */
	public int cost;
}
