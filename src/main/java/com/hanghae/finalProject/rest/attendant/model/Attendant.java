package com.hanghae.finalProject.rest.attendant.model;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Attendant {

     @Id
     @GeneratedValue (strategy = GenerationType.IDENTITY)
     private Long id;
     
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="meeting_id")
     private Meeting meeting;
     
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="user_id")
     private User user;

     @Column
     private boolean attend;

     @Column
     private boolean review;

     public Attendant(Meeting meeting, User user) {
          this.meeting = meeting;
          this.user = user;
          this.attend = true;
     }

     public void cancelAttendant(Meeting meeting) {
          this.meeting = meeting;
          this.attend = false;
     }
}
