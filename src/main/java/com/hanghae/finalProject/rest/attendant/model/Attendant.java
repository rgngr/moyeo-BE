package com.hanghae.finalProject.rest.attendant.model;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.function.Supplier;

@Getter
@Entity
@NoArgsConstructor
@Table(indexes = @Index(name = "idx__meetingId", columnList = "meeting_id"))
public class Attendant implements Supplier<Attendant> {

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
     private boolean entrance;

     @Column
     private boolean review;

     public Attendant(Meeting meeting, User user) {
          this.meeting = meeting;
          this.user = user;
          this.entrance = false;
     }

     public void cancelAttendant(Meeting meeting) {
          this.meeting = meeting;
          this.entrance = false;
     }

     public void enter(Meeting meeting) {
          this.meeting = meeting;
          this.entrance = true;
     }
     
     public void makeReview(Boolean review){
          this.review = review;
     }
     @Override
     public Attendant get() {
          return null;
     }
}
