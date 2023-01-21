package com.hanghae.finalProject.rest.meeting.model;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PlatformCode {
     GATHER_TOWN,
     DISCORD,
     ZOOM,
     GOOGLE_MEET,
     ZEP,
     ETC;
     
     
     public static PlatformCode of(String platformStr){
          if(platformStr == null){
               throw new IllegalArgumentException();
          }
          for(PlatformCode cc : PlatformCode.values()){
               if (cc.name().equals(platformStr)) {
                    return cc;
               }
          }
          throw new RestApiException(Code.NO_SUCH_PLATFORM);
     }
}
