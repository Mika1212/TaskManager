package taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import taskmanager.common.event.UserCreatedEvent;

@Configuration
public class UserKafkaConsumerConfig extends AbstractKafkaConsumerConfig {

    public UserKafkaConsumerConfig(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Bean
    public ConsumerFactory<String, UserCreatedEvent> userCreatedConsumerFactory() {
        return createConsumerFactory(
                "task-service",
                UserCreatedEvent.class
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent>
    userCreatedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, UserCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(userCreatedConsumerFactory());
        return factory;
    }
}
