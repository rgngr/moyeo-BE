package com.hanghae.finalProject.config.converter;

import com.hanghae.finalProject.rest.meeting.model.CategoryCode;
import org.springframework.core.convert.converter.Converter;

public class CategoryCodeRequestConverter implements Converter<String, CategoryCode> {
     @Override
     public CategoryCode convert(String category) {
          return CategoryCode.of(category);
     }
}
