package com.tidal.refactoring.exceptions;

import java.util.Map;

import org.springframework.util.StringUtils;

public class EntityUpdateNotRequiredException extends BaseException {

  public EntityUpdateNotRequiredException(Class className, String... searchParamsMap) {
    super(EntityUpdateNotRequiredException
        .generateMessage(className.getSimpleName(),
            toMap(String.class, String.class, searchParamsMap)));
  }

  private static String generateMessage(String entity, Map<String, String> searchParams) {
    return StringUtils.capitalize(entity) + " update not required with parameters " + searchParams;
  }
}

