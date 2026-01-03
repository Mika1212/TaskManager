package taskmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import taskmanager.common.event.ProjectCreatedEvent;

@Configuration
public class ProjectKafkaConsumerConfig extends AbstractKafkaConsumerConfig {

    public ProjectKafkaConsumerConfig(KafkaProperties kafkaProperties) {
        super(kafkaProperties);
    }

    @Bean
    public ConsumerFactory<String, ProjectCreatedEvent> projectCreatedConsumerFactory() {
        return createConsumerFactory(
                "project-created-group",
                ProjectCreatedEvent.class
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProjectCreatedEvent>
    projectCreatedKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, ProjectCreatedEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(projectCreatedConsumerFactory());
        return factory;
    }
}
