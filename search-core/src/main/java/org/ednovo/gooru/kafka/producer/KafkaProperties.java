package org.ednovo.gooru.kafka.producer;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class KafkaProperties {

	private static KafkaProperties instance = null;

	@Autowired
	@Resource(name = "configSettings")
	private Properties searchSettings;

	protected Properties getSearchSettings() {
		return this.searchSettings;
	}

	public KafkaProperties() {
		if (instance == null)
			instance = this;
	}

	public static final String METADATA_BROKER_LIST = "metadata.broker.list";
	public static final String ZK_CONSUMER_CONNECT = "zookeeper.connect";
	public static final String ZK_CONSUMER_GROUP = "group.id";
	public static final String TOPIC = "topic";
	public static final String KAFKA_SERVER_PORT = "kafkaServerPort";
	public static final int KAFKA_SERVER_PORT_VALUE = 9092;
	public static final String KAFKA_PRODUCER_BUFFER_SIZE = "kafkaProducerBufferSize";
	public static final int KAFKA_PRODUCER_BUFFER_SIZE_VALUE = 64 * 1024;
	public static final String CONNECTION_TIME_OUT = "connectionTimeOut";
	public static final int CONNECTION_TIME_OUT_VALUE = 100000;
	public static final String RECONNECT_INTERVAL = "reconnectInterval";
	public static final int RECONNECT_INTERVAL_VALUE = 10000;
	public static final String SERIALIZER_CLASS = "serializer.class";
	public static final String SERIALIZER_CLASS_VALUE = "kafka.serializer.StringEncoder";
	public static final String PRODUCER_TYPE = "producer.type";
	public static final String PRODUCER_TYPE_VALUE = "async";
	public static final String COMPRESSION_CODEC = "compression.codec";
	public static final String COMPRESSION_CODEC_VALUE = "1";
	public static final String ZK_SESSION_TIME_OUT_MS = "zookeeper.session.timeout.ms";
	public static final String ZK_SESSION_TIME_OUT_MS_VALUE = "10000";
	public static final String ZK_SYNCTIME_MS = "zookeeper.sync.time.ms";
	public static final String ZK_SYNCTIME_MS_VALUE = "200";
	public static final String AUTOCOMMIT_INTERVAL_MS = "auto.commit.interval.ms";
	public static final String AUTOCOMMIT_INTERVAL_MS_VALUE = "1000";
	public static final String FETCH_SIZE = "fetch.size";
	public static final String FETCH_SIZE_VALUE = "1048576";
	public static final String AUTO_OFFSET_RESET = "auto.offset.reset";
	public static final String AUTO_OFFSET_RESET_VALUE = "smallest";
	public static final String KAFKA_PREFIX = "kafka.";
	public static final String REQUEST_REQUIRED_ACKS = "request.required.acks";
	public static final String REQUEST_REQUIRED_ACKS_VALUE = "1";
	public static final String RETRY_BACKOFF_MS = "retry.backoff.ms";
	public static final String RETRY_BACKOFF_MS_VALUE = "3000";
	public static final String JOB_TOPIC = "job.topic";

	public String metadataBrokerList;
	public String zkConsumerConnectValue;
	public String consumerGroupIdValue;
	public String topicValue;
	public String conversionJobTopic;
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProperties.class);

	@PostConstruct
	public void init() {
		try {
			metadataBrokerList = instance.getSearchSettings().getProperty(KAFKA_PREFIX + METADATA_BROKER_LIST);
			zkConsumerConnectValue = instance.getSearchSettings().getProperty(KAFKA_PREFIX + ZK_CONSUMER_CONNECT);
			consumerGroupIdValue = instance.getSearchSettings().getProperty(KAFKA_PREFIX + ZK_CONSUMER_GROUP);
			topicValue = instance.getSearchSettings().getProperty(KAFKA_PREFIX + TOPIC);
			conversionJobTopic = instance.getSearchSettings().getProperty(KAFKA_PREFIX + JOB_TOPIC);
		} catch (Exception e) {
			LOGGER.info("kafka error while getting config setting fields value :" + e);
		}
	}

}
