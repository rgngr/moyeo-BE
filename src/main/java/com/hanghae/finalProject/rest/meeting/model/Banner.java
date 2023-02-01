package com.hanghae.finalProject.rest.meeting.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public enum Banner {
//     LOGO("https://hippoawsbucket.s3.ap-northeast-2.amazonaws.com/mainBanner/banner_logo.png"),
     GOOGLE_MEET("https://hippoawsbucket.s3.ap-northeast-2.amazonaws.com/mainBanner/Google-Meet.jpg"),
     DISCORD("https://hippoawsbucket.s3.ap-northeast-2.amazonaws.com/mainBanner/DISCORD.jfif"),
     GATHER_TOWN("https://hippoawsbucket.s3.ap-northeast-2.amazonaws.com/mainBanner/GATHER_TOWN.jfif"),
     ZEP("https://hippoawsbucket.s3.ap-northeast-2.amazonaws.com/mainBanner/banner_zep.png");
     
     private final String imgUrl;
     
     public static List<String> getImgList() {
          return Arrays.stream(values())
               .map(Banner::getImgUrl)
               .collect(Collectors.toList());
     }
     
}
