package taskmanager.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractKafkaConsumerConfig {

    protected final KafkaProperties kafkaProperties;

    protected AbstractKafkaConsumerConfig(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }

    protected Map<String, Object> baseConsumerProps(String groupId) {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return props;
    }

    protected <T> ConsumerFactory<String, T> createConsumerFactory(String groupId, Class<T> eventClass) {
        Map<String, Object> props = new HashMap<>(baseConsumerProps(groupId));

        JsonDeserializer<T> jsonDeserializer = new JsonDeserializer<>(eventClass);
        jsonDeserializer.addTrustedPackages("taskmanager.common.event");
        jsonDeserializer.setUseTypeHeaders(false);

        ErrorHandlingDeserializer<T> valueDeserializer =
                new ErrorHandlingDeserializer<>(jsonDeserializer);

        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                valueDeserializer
        );
    }
}
