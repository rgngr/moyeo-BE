package com.hanghae.finalProject.rest.meeting.model;

import com.hanghae.finalProject.config.model.Timestamped;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Meeting extends Timestamped {
     @Id
     @GeneratedValue (strategy = GenerationType.IDENTITY)
     private Long id;
     
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="user_id")
     private User user; // 모임생성자
     
     @Column(nullable = false)
     private LocalDateTime startDate;
     
     
     
     
}
