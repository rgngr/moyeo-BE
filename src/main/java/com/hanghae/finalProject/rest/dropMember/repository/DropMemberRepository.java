package com.hanghae.finalProject.rest.dropMember.repository;

import com.hanghae.finalProject.rest.dropMember.model.DropMember;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import com.hanghae.finalProject.rest.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DropMemberRepository extends JpaRepository<DropMember, Long> {
     Boolean existsByMeetingAndUser(Meeting meeting, User user);
}
