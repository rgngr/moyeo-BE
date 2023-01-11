package com.hanghae.finalProject.rest.alarm.model;

import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.util.function.SupplierUtils;

import javax.persistence.*;
import java.util.function.Supplier;

@Getter
@Entity
@NoArgsConstructor
public class Alarm implements Supplier<Alarm> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name ="meeting_id", nullable = false)
    private Meeting meeting;
    
    public Alarm(User user, Meeting meeting) {
        this.user = user;
        this.meeting = meeting;
    }
    
    @Override
    public Alarm get() {
        return null;
    }
}
