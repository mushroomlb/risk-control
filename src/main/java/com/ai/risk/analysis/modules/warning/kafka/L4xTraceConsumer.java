package com.ai.risk.analysis.modules.warning.kafka;

import com.ai.risk.analysis.modules.warning.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * LOG4X-TRACE-TOPIC 消费者
 * <p>
 * 使用 @KafkaListener 注解, 可以指定: 主题, 分区, 消费组
 *
 * @author Steven
 * @date 2019-05-24
 */
@Slf4j
@Component
public class L4xTraceConsumer {

	/**
	 * 链路 Topic
	 */
	private static final String TOPIC = "LOG4X-TRACE-TOPIC";

	/**
	 * 是否为主服务，用于快速判断
	 */
	private static final String CSF_SERVICE = "\"callType\":\"CSF\"";


	private static final int PRINT_STEP_SIZE = 100000;

	/**
	 * 入口服务类型
	 */
	@Value("${risk.entrance.name}")
	private String entranceName;

	/**
	 * 步长，即多少条记录打印一行日志
	 */
	@Value("${risk.step}")
	private int step;

	/**
	 * 分隔符
	 */
	@Value("${risk.seperatorChar}")
	private char seperatorChar;

	@Autowired
	private IServiceAndOpcodeSV serviceAndOpcodeSVImpl;

	@Autowired
	private IServiceAndIpSV ServiceAndIpSVImpl;

	@Autowired
	private IServiceAndInstanceSV serviceAndInstanceSVImpl;

	@Autowired
	private IServiceSV serviceSVImpl;

	@Autowired
	private IOpcodeSV opcodeSVImpl;

	private ObjectMapper mapper = new ObjectMapper();
	private AtomicLong index = new AtomicLong(0L);

	/**
	 * 本地数据聚合
	 *
	 * @param record
	 */
	private void accumulation(ConsumerRecord<?, String> record, int partition) {

		String value = record.value();
		if (value.indexOf(CSF_SERVICE) == -1) {
			return;
		}

		Map span = null;
		try {
			span = mapper.readValue(value, Map.class);
		} catch (IOException e) {

		}

		String callType = (String) span.get("callType");
		if (!entranceName.equals(callType)) {
			return;
		}

		String serviceName = (String) span.get("serviceName");
		Long startTime = (Long) span.get("startTime");
		Integer elapsedTime = (Integer) span.get("elapsedTime");
		String ip = getHostIp((String) span.get("hostName"));
		String instance = (String) span.get("appName");
		// boolean success = (boolean) span.get("success");
		Map extMap = (Map) span.get("ext");
		String opCode = (String) extMap.get("opCode");

		if (!assertNotBlank(serviceName, opCode, ip, instance)) {
			return;
		}

		serviceAndOpcodeSVImpl.localAccumulation(serviceName, opCode, elapsedTime);
		ServiceAndIpSVImpl.localAccumulation(serviceName, ip, elapsedTime);
		serviceAndInstanceSVImpl.localAccumulation(serviceName, instance, elapsedTime);

		serviceSVImpl.increment(serviceName);
		opcodeSVImpl.increment(opCode);

		long i = index.incrementAndGet();
		if (0 == i % PRINT_STEP_SIZE) {
			String now = DateFormatUtils.format(startTime, "yyyy-MM-dd HH:mm:ss");
			log.info(String.format("%s, 已处理: %12d, 当前分区: %2d", now, i, partition));
		}

	}

	/**
	 * 获取主机IP
	 *
	 * @param strHostName
	 * @return
	 */
	private String getHostIp(String strHostName) {
		String[] slice = StringUtils.split(strHostName, ',');
		if (null != slice) {
			for (String hostname : slice) {
				if (!"127.0.0.1".equals(hostname)) {
					return hostname;
				}
			}
		}
		return null;
	}

	/**
	 * 非空判断
	 *
	 * @param serviceName
	 * @param opCode
	 * @param ip
	 * @param instance
	 * @return
	 */
	private static final boolean assertNotBlank(String serviceName, String opCode, String ip, String instance) {

		if (StringUtils.isBlank(serviceName)) {
			return false;
		}

		if (StringUtils.isBlank(opCode)) {
			return false;
		}

		if (StringUtils.isBlank(ip)) {
			return false;
		}

		if (StringUtils.isBlank(instance)) {
			return false;
		}

		return true;
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "0")})
	public void listen0(ConsumerRecord<?, String> record) {
		accumulation(record, 0);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "1")})
	public void listen1(ConsumerRecord<?, String> record) {
		accumulation(record, 1);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "2")})
	public void listen2(ConsumerRecord<?, String> record) {
		accumulation(record, 2);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "3")})
	public void listen3(ConsumerRecord<?, String> record) {
		accumulation(record, 3);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "4")})
	public void listen4(ConsumerRecord<?, String> record) {
		accumulation(record, 4);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "5")})
	public void listen5(ConsumerRecord<?, String> record) {
		accumulation(record, 5);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "6")})
	public void listen6(ConsumerRecord<?, String> record) {
		accumulation(record, 6);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "7")})
	public void listen7(ConsumerRecord<?, String> record) {
		accumulation(record, 7);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "8")})
	public void listen8(ConsumerRecord<?, String> record) {
		accumulation(record, 8);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "9")})
	public void listen9(ConsumerRecord<?, String> record) {
		accumulation(record, 9);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "10")})
	public void listen10(ConsumerRecord<?, String> record) {
		accumulation(record, 10);
	}

	@KafkaListener(topicPartitions = {@TopicPartition(topic = TOPIC, partitions = "11")})
	public void listen11(ConsumerRecord<?, String> record) {
		accumulation(record, 11);
	}

}
