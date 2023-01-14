package com.hanghae.finalProject.config.converter;

import com.hanghae.finalProject.rest.meeting.model.PlatformCode;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter (autoApply = true)
public class PlatformConverter implements AttributeConverter<PlatformCode, String> {
     
     @Override // enum을 DB에 어떤 값으로 넣을 것인지 정의
     public String convertToDatabaseColumn(PlatformCode attribute) {
          return attribute.name();
     }
  
     @Override // DB에서 읽힌 값에 따라 어떻게 enum랑 매칭 시킬 것인지 정의
     public PlatformCode convertToEntityAttribute(String dbData) {
          if(dbData == null){
               return null;
          }
          return Stream.of(PlatformCode.values())
               .filter(c -> c.name().equals(dbData))
               .findFirst()
               .orElse(null);
     }
}
