package com.hanghae.finalProject.rest.alarm.model;

import com.hanghae.finalProject.config.model.Timestamped;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Alarm extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_id", nullable = false)
    private User user;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name ="meeting_id", nullable = false)
    private Meeting meeting;

    @Embedded
    private AlarmContent content;

    @Embedded
    private RelatedUrl relatedUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AlarmType alarmType;

    @Column(nullable = false)
    private Boolean isRead;

    public Alarm(User user, Meeting meeting, AlarmType alarmType, String content, String relatedUrl, Boolean isRead) {
        this.user = user;
        this.meeting = meeting;
        this.alarmType = alarmType;
        this.content = new AlarmContent(content);
        this.relatedUrl = new RelatedUrl(relatedUrl);
        this.isRead = isRead;

    }

    public String getContent() {
        return content.getContent();
    }

    public String getRelatedUrl() {
        return relatedUrl.getRelatedUrl();
    }

}
