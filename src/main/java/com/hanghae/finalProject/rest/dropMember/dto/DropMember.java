package com.hanghae.finalProject.rest.dropMember.dto;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class DropMember {
     
     @Id
     @GeneratedValue (strategy = GenerationType.IDENTITY)
     private Long id;
     
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="meeting_id")
     private Meeting meeting;
     
     // 강퇴된 유저
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="user_id")
     private User user;
     
     public DropMember(Meeting meeting, User user) {
          this.meeting = meeting;
          this.user = user;
     }
}
