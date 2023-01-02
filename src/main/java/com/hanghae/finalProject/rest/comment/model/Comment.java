package com.hanghae.finalProject.rest.comment.model;

import com.hanghae.finalProject.config.model.Timestamped;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@SQLDelete(sql = "UPDATE comment SET deleted = true WHERE id = ?")
public class Comment extends Timestamped {
     @Id
     @GeneratedValue (strategy = GenerationType.IDENTITY)
     private Long id;
     
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="meeting_id")
     private Meeting meeting; // 모임아이디
     
     @ManyToOne (fetch = FetchType.LAZY)
     @JoinColumn(name ="user_id")
     private User user; // 모임생성자

     @Column
     private String comment;

     @Column
     private boolean deleted = Boolean.FALSE;
}
