//package com.hanghae.finalProject.rest.attendant.service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//import javax.persistence.Entity;
//
//@Component
//@RequiredArgsConstructor
//public class AttendantMessageConsumers {
//     private static final String ATTENDANT_UPDATE_QUEUE = "attendant.update.queue";
//     private static final String ATTENDANT_COMPLETE_QUEUE = "attendant.complete.queue";
//
//     private final AttendantService attendantService;
//
//     @RabbitListener (queues = {ATTENDANT_UPDATE_QUEUE})
//     public void receiveMessageFromDirectExchangeWithOrderQueue(String message) {
////
////          Entity chargeOrder = MapperUtil.writeStringAsObject(message, Entity.class);
////          attendantService.sendAlarmTalkChargeOrderConfirm(chargeOrder);
//
//     }
//
//     @RabbitListener(queues = {ATTENDANT_COMPLETE_QUEUE})
//     public void receiveMessageFromDirectExchangeWithCompleteQueue(String message) {
////
////          Entity chargeOrder = MapperUtil.writeStringAsObject(message, Entity.class);
////          attendantService.sendAlarmTalkChargeOrderComplete(chargeOrder);
//     }
//}
