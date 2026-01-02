package taskmanager.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import taskmanager.common.event.ProjectCreatedEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ProjectKafkaConsumerConfig extends AbstractKafkaConsumerConfig {

    @Bean
    public ConsumerFactory<String, ProjectCreatedEvent> projectCreatedConsumerFactory() {
        JsonDeserializer<ProjectCreatedEvent> deserializer = new JsonDeserializer<>(ProjectCreatedEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>(kafkaProps()); // метод с bootstrap-servers и др.
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProjectCreatedEvent> projectCreatedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProjectCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(projectCreatedConsumerFactory());
        return factory;
    }
}
