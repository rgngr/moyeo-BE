package com.hanghae.finalProject.config.converter;

import com.hanghae.finalProject.config.errorcode.Code;
import com.hanghae.finalProject.config.exception.RestApiException;
import com.hanghae.finalProject.rest.meeting.model.CategoryCode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter (autoApply = true)
public class CategoryConverter implements AttributeConverter<CategoryCode, String> {
     
     @Override // enum을 DB에 어떤 값으로 넣을 것인지 정의
     public String convertToDatabaseColumn(CategoryCode attribute) {
          return attribute.getCategory();
     }
     
  
     @Override // DB에서 읽힌 값에 따라 어떻게 enum랑 매칭 시킬 것인지 정의
     public CategoryCode convertToEntityAttribute(String dbData) {
          if(dbData == null){
               return null;
          }
          return Stream.of(CategoryCode.values())
               .filter(c -> c.getCategory().equals(dbData))
               .findFirst()
               .orElse(null);
     }
}
