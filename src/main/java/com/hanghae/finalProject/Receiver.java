package com.hanghae.finalProject;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

//메시지 구독을 위한 Receiver 클래스
@Component
public class Receiver {
     private CountDownLatch latch = new CountDownLatch(1);
     
     public void receiveMessage(String message) {
          System.out.println("Received <" + message + ">");
          latch.countDown();
     }
     
     public CountDownLatch getLatch() {
          return latch;
     }
}
