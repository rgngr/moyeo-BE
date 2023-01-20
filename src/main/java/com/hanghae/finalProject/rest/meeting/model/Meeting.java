package com.hanghae.finalProject.rest.meeting.model;

import com.hanghae.finalProject.config.model.Timestamped;
import com.hanghae.finalProject.rest.meeting.dto.MeetingLinkRequestDto;
import com.hanghae.finalProject.rest.meeting.dto.MeetingRequestDto;
import com.hanghae.finalProject.rest.meeting.dto.MeetingUpdateRequestDto;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Entity
@NoArgsConstructor
public class Meeting extends Timestamped {

     @Id
     @GeneratedValue (strategy = GenerationType.IDENTITY)
     private Long id;
     
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="user_id", nullable = false)
     private User user; // 모임생성자

     @Column(nullable = false)
     private String title;

     @Column(nullable = false)
     private CategoryCode category;
     
     @Column(nullable = false)
     private LocalDate startDate;

     @Column(nullable = false)
     private LocalTime startTime;

     @Column(nullable = false)
     private int duration;

     @Column(nullable = false, length = 300)
     private String content;

     @Column(nullable = false)
     private int maxNum;

     @Column
     private PlatformCode platform;

     @Column
     private String link;

     @Column
     private boolean secret;

     @Column
     private String password;

     @Column
     private boolean deleted;

     public Meeting(MeetingRequestDto requestDto, User user) {
          this.title = requestDto.getTitle();
          this.category = requestDto.getCategory();
          this.startDate = requestDto.getStartDate();
          this.startTime =  requestDto.getStartTime();
          this.duration = requestDto.getDuration();
          this.content = requestDto.getContent();
          this.maxNum = requestDto.getMaxNum();
          this.platform = requestDto.getPlatform();
          this.link = requestDto.getLink();
          this.secret = requestDto.isSecret();
          this.password = requestDto.getPassword();
          this.user = user;
     }

     public void updateAll(MeetingUpdateRequestDto requestDto) {
          this.title = requestDto.getTitle();
          this.startDate = requestDto.getStartDate();
          this.startTime =  requestDto.getStartTime();
          this.duration = requestDto.getDuration();
          this.content = requestDto.getContent();
          this.platform = requestDto.getPlatform();
          this.link = requestDto.getLink();
          this.secret = requestDto.isSecret();
          this.password = requestDto.getPassword();
     }

     public void updateLink(MeetingLinkRequestDto requestDto) {
          this.platform = requestDto.getPlatform();
          this.link = requestDto.getLink();
     }

     public void deleteMeeting() {
          this.deleted = true;
     }


}
