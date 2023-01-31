package com.hanghae.finalProject;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories (enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
@EnableJpaAuditing
@SpringBootApplication
public class FinalProjectApplication {
     // 메시지 발행자, 구독자, 그리고 메시지 큐를 스프링 부트에 등록하는 소스코드
     static final String topicExchangeName = "spring-boot-exchange";
     static final String queueName = "spring-boot";
     
     @Bean
     Queue queue() {
          return new Queue(queueName, false);
     }
     
     @Bean
     TopicExchange exchange() {
          return new TopicExchange(topicExchangeName);
     }
     
     @Bean
     Binding binding(Queue queue, TopicExchange exchange) {
          return BindingBuilder.bind(queue).to(exchange).with("foo.bar.#");
     }
     
     @Bean
     SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                              MessageListenerAdapter listenerAdapter) {
          SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
          container.setConnectionFactory(connectionFactory);
          container.setQueueNames(queueName);
          container.setMessageListener(listenerAdapter);
          return container;
     }
     
     @Bean
     MessageListenerAdapter listenerAdapter(Receiver receiver) {
          return new MessageListenerAdapter(receiver, "receiveMessage");
     }

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}
}
