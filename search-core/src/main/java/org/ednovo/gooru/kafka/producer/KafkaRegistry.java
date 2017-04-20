package org.ednovo.gooru.kafka.producer;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class KafkaRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaRegistry.class);

	protected Properties props = new Properties();
	private static final int THREAD_POOL_SIZE = 7;
	private Producer<String, String> producer;
	private String topic = "activity-log";
	private ExecutorService executor;

	@Autowired
	private KafkaProperties kafkaProperties;

	@PostConstruct
	public void init() {
		LOGGER.trace("Initializing Kafka producer ...");
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.bootstrapServers);
		props.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.retries);
		props.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProperties.batchSize);
		props.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProperties.lingerMs);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, kafkaProperties.bufferMemory);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
		executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		this.topic = kafkaProperties.topicValue;
		try {
			producer = new KafkaProducer<>(props);
			LOGGER.trace("Kafka producer initialized!");
		} catch (Exception e) {
			LOGGER.error("Kafka producer is not initialized" + e.getMessage());
		}
	}

	public void push(String message) {
		ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, "search-event", message);
		try {
			getProducer().send(data);
		} catch (Exception e) {
			LOGGER.error("Errror while sending data via kafka producer :" + e);
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
				ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic,"search-event",  message);
				try {
					getProducer().send(data);
				} catch (Exception e) {
					LOGGER.error("Error while sending data via kafka producer :" + e);
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
	public void send(final String eventName, final String message) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, eventName, message);
				try {
					getProducer().send(data);
				} catch (Exception e) {
					LOGGER.error("Error while sending data via kafka producer :" + e);
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
	public void send(final String topic, final String eventName, final String message) {
		executor.submit(new Runnable() {
			@Override
			public void run() {
				ProducerRecord<String, String> data = new ProducerRecord<String, String>(topic, eventName, message);
				try {
					getProducer().send(data);
				} catch (Exception e) {
					LOGGER.error("Error while sending data via kafka producer :" + e);
				}
			}
		});
	}
	
	@PreDestroy
	public void onStop() {
		getProducer().close();
		executor.shutdown();
		LOGGER.info("Kafka Producer shutdown");
	}
	
	private Producer<String, String> getProducer() {
		return producer;
	}
}
