package com.hanghae.finalProject.rest.meeting.model;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CategoryCode {
     술먹자("술먹자"),
     밥먹자("밥먹자");
     
     private final String category;
     
     public static CategoryCode of(String categoryStr){
          if(categoryStr == null){
               throw new IllegalArgumentException();
          }
          for(CategoryCode cc : CategoryCode.values()){
               if (cc.category.equals(categoryStr)) {
                    return cc;
               }
          }
          throw new RestApiException(Code.NO_SUCH_CATEGORY);
     }
}
