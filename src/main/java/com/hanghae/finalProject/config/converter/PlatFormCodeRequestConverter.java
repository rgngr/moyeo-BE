package com.hanghae.finalProject.config.converter;

import com.hanghae.finalProject.rest.meeting.model.PlatformCode;
import org.springframework.core.convert.converter.Converter;

public class PlatFormCodeRequestConverter implements Converter<String, PlatformCode> {
     @Override
     public PlatformCode convert(String platform) {
          return PlatformCode.of(platform);
     }
}
