package com.ai.risk.analysis.modules.warning.service;

/**
 * @author Steven
 */
public interface IServiceAndIpSV {
	void localAccumulation(String svcName, String ip, int elapsedTime);
	void sinkToInflux();
}
