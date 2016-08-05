package org.ednovo.gooru.kafka.producer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

@Component
public class KafkaProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);

	protected Properties props = new Properties();
	private static final int THREAD_POOL_SIZE = 7;
	private Producer<String, String> producer;
	private String topic = "activity-log";
	private ExecutorService executor;

	@Autowired
	private KafkaProperties kafkaProperties;

	@PostConstruct
	public void init() {
		props.put(KafkaProperties.SERIALIZER_CLASS, KafkaProperties.SERIALIZER_CLASS_VALUE);
		props.put(KafkaProperties.METADATA_BROKER_LIST, kafkaProperties.metadataBrokerList);
		props.put(KafkaProperties.PRODUCER_TYPE, KafkaProperties.PRODUCER_TYPE_VALUE);
		props.put(KafkaProperties.REQUEST_REQUIRED_ACKS, KafkaProperties.REQUEST_REQUIRED_ACKS_VALUE);
		props.put(KafkaProperties.RETRY_BACKOFF_MS, KafkaProperties.RETRY_BACKOFF_MS_VALUE);
		executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try {
			producer = new Producer<String, String>(new ProducerConfig(props));
			LOGGER.debug("KafkaHandler producer initialized");
		} catch (Exception e) {
			LOGGER.error("KafkaHandler producer is not initialized" + e.getMessage());
		}
	}

	public void init(String ip, String portNo, String topic) {
		this.topic = topic;
		props.put(KafkaProperties.METADATA_BROKER_LIST, buildEndPoint(ip, portNo));
		props.put(KafkaProperties.SERIALIZER_CLASS, KafkaProperties.SERIALIZER_CLASS_VALUE);
		props.put(KafkaProperties.REQUEST_REQUIRED_ACKS, KafkaProperties.REQUEST_REQUIRED_ACKS_VALUE);
		props.put(KafkaProperties.RETRY_BACKOFF_MS, KafkaProperties.RETRY_BACKOFF_MS_VALUE);
		props.put(KafkaProperties.PRODUCER_TYPE, KafkaProperties.PRODUCER_TYPE_VALUE);
		executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try {
			producer = new Producer<String, String>(new ProducerConfig(props));
			LOGGER.debug("KafkaEventHandler producer initialized");
		} catch (Exception e) {
			LOGGER.error("KafkaEventHandler producer is not initialized" + e.getMessage());
		}
	}

	public void send(String message, String indexType, String topicName) {
		LOGGER.info("Index messgage : " + message + "  topic name : " + topicName);
		KeyedMessage<String, String> data = new KeyedMessage<String, String>(topicName, message);
		try {
			producer.send(data);
		} catch (Exception e) {
			LOGGER.info("Errror while sending data via kafka producer :" + e);
		}
	}

	public void push(String message, String topic) {
		KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, message);
		try {
			producer.send(data);
		} catch (Exception e) {
			LOGGER.info("Errror while sending data via kafka producer :" + e);
		}
	}

	public void push(String message) {
		KeyedMessage<String, String> data = new KeyedMessage<String, String>(kafkaProperties.conversionJobTopic, message);
		try {
			producer.send(data);
		} catch (Exception e) {
			LOGGER.info("Errror while sending data via kafka producer :" + e);
		}
	}

	/**
	 * Send data in asynchronous way on thread level
	 * 
	 * @param message
	 *            message needs to be send
	 */
	public void send(final String message) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, message);
				try {
					producer.send(data);
				} catch (Exception e) {
					LOGGER.info("Error while sending data via kafka producer :" + e);
				}
			}
		});
	}

	/**
	 * Send data in asynchronous way on thread level
	 * 
	 * @param topic
	 *            topic needs to be pushed
	 * @param message
	 *            message needs to be send
	 */
	public void send(final String topic, final String message) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, message);
				try {
					producer.send(data);
				} catch (Exception e) {
					LOGGER.info("Error while sending data via kafka producer :" + e);
				}
			}
		});
	}

	private static String buildEndPoint(String ip, String portNo) {
		StringBuffer stringBuffer = new StringBuffer();
		String[] ips = ip.split(",");
		String[] ports = portNo.split(",");
		for (int count = 0; count < ips.length; count++) {
			if (stringBuffer.length() > 0) {
				stringBuffer.append(",");
			}
			if (count < ports.length) {
				stringBuffer.append(ips[count] + ":" + ports[count]);
			} else {
				stringBuffer.append(ips[count] + ":" + ports[0]);
			}
		}
		return stringBuffer.toString();
	}

}
