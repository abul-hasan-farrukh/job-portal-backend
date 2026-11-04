package com.aimbitious.job.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.security.protocol}")
    private String securityProtocol;

    @Value("${spring.kafka.properties.sasl.mechanism}")
    private String saslMechanism;

    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String saslJaasConfig;

    //Kafka CA_CERT
    @Value("${KAFKA_CA_CERT}")
    private String kafkaCaCert;


    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {

        Map<String, Object> props = new HashMap<>();

        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers
        );

        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                "notification-service"
        );

        props.put(
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                "earliest"
        );

        props.put(
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,
                false
        );

        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class
        );

        // Aiven Kafka authentication
        props.put(
                CommonClientConfigs.SECURITY_PROTOCOL_CONFIG,
                securityProtocol
        );

        props.put(
                SaslConfigs.SASL_MECHANISM,
                saslMechanism
        );

        props.put(
                SaslConfigs.SASL_JAAS_CONFIG,
                saslJaasConfig
        );

        // Aiven Kafka SSL certificate configuration
        props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "PEM");
        props.put(SslConfigs.SSL_TRUSTSTORE_CERTIFICATES_CONFIG, kafkaCaCert);

        // JSON deserialization
        JsonDeserializer<Object> deserializer =
                new JsonDeserializer<>(Object.class);

        deserializer.addTrustedPackages(
                "com.aimbitious.job.event"
        );

        // Read event type from Kafka message headers
        deserializer.setUseTypeHeaders(true);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                deserializer
        );
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        // Acknowledge messages after successful listener execution
        factory.getContainerProperties().setAckMode(
                org.springframework.kafka.listener.ContainerProperties.AckMode.RECORD
        );

        return factory;
    }
}