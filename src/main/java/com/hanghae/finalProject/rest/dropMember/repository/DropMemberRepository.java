package com.hanghae.finalProject.rest.dropMember.repository;

import com.hanghae.finalProject.rest.dropMember.dto.DropMember;
import com.hanghae.finalProject.rest.meeting.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DropMemberRepository extends JpaRepository<DropMember, Long> {
     Boolean existsByMeetingAndUserId(Meeting meeting, Long memberId);
     
}
