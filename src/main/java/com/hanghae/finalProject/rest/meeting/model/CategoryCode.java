package com.hanghae.finalProject.rest.meeting.model;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryCode {
     일단모여,
     취미모여,
     영화모여,
     공부모여,
     수다모여,
     게임모여,
     술모여,
     밥모여;
     
     public static CategoryCode of(String categoryStr){
          if(categoryStr == null){
               throw new IllegalArgumentException();
          }
          for(CategoryCode cc : CategoryCode.values()){
               if (cc.name().equals(categoryStr)) {
                    return cc;
               }
          }
          throw new RestApiException(Code.NO_SUCH_CATEGORY);
     }
}
