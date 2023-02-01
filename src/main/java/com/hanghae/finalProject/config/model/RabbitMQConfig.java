//package com.hanghae.finalProject.config.model;
//
//import org.springframework.amqp.core.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class RabbitMQConfig {
//
//     private String exchange = "direct.exchange";
//     private String attendantUpdateRoutingKey = "attendant.update.route";
//     private String attendantCompleteRoutingKey = "attendant.complete.route";
//
//     private static final String ATTENDANT_UPDATE_QUEUE = "attendant.update.queue";
//     private static final String ATTENDANT_COMPLETE_QUEUE = "attendant.complete.queue";
//
//     @Bean
//     public List<Declarable> directBindings() {
//          Queue attendantUpdateQueue = new Queue(ATTENDANT_UPDATE_QUEUE,true);
//          Queue AttendantCompleteQueue = new Queue(ATTENDANT_COMPLETE_QUEUE, true);
//
//          DirectExchange directExchange = new DirectExchange(exchange);
//
//          return Arrays.asList(
//               attendantUpdateQueue,
//               AttendantCompleteQueue,
//               directExchange,
//               BindingBuilder.bind(attendantUpdateQueue).to(directExchange).with(attendantUpdateRoutingKey),
//               BindingBuilder.bind(AttendantCompleteQueue).to(directExchange).with(attendantCompleteRoutingKey)
//          );
//     }
//}
