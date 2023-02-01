//package com.hanghae.finalProject.rest.sqs;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
//import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
//import org.springframework.messaging.handler.annotation.Headers;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//@Slf4j
//@Component
//public class SqsMessageListener {
//
//     @SqsListener (value = "moyeo.fifo", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//     public void listen(@Payload String message,
//                        @Headers Map<String, String> headers) {
//          log.info("message : " + message);
//          log.info("{}", headers);
//     }
//}