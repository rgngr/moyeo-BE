package com.hanghae.finalProject.rest.user.repository;

import com.hanghae.finalProject.rest.user.dto.MypageResponseDto;
import com.hanghae.finalProject.rest.user.model.User;

public interface UserCustomRepository {
     MypageResponseDto findByUserAndAttendantAndFollow(User user);
}
