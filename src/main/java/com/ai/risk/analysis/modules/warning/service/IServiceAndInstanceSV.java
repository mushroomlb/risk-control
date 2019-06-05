package com.ai.risk.analysis.modules.warning.service;

/**
 * @author Steven
 */
public interface IServiceAndInstanceSV {
	void localAccumulation(String svcName, String instance, int elapsedTime);
	void sinkToInflux();
}
