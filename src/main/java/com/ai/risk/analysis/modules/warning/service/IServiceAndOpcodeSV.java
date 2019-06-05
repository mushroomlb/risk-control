package com.ai.risk.analysis.modules.warning.service;

/**
 * @author Steven
 */
public interface IServiceAndOpcodeSV {
	void localAccumulation(String svcName, String opCode, int elapsedTime);
	void sinkToInflux();
}
