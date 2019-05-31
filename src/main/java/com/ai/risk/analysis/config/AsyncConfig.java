package com.ai.risk.analysis.config;

import com.ai.risk.analysis.ServicePoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Steven
 */
@Configuration
public class AsyncConfig {

	@Bean
	public LinkedBlockingQueue<ServicePoint> linkedBlockingQueue() {
		LinkedBlockingQueue<ServicePoint> queue = new LinkedBlockingQueue(1024 * 1024);
		return queue;
	}

}
