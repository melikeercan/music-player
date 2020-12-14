package com.tidal.refactoring.exceptions;

import java.util.Map;

import org.springframework.util.StringUtils;

public class EntityNotFoundException extends BaseException {

  public EntityNotFoundException(Class clazz, String... searchParamsMap) {
    super(EntityNotFoundException
        .generateMessage(clazz.getSimpleName(),
            toMap(String.class, String.class, searchParamsMap)));
  }

  private static String generateMessage(String entity, Map<String, String> searchParams) {
    return StringUtils.capitalize(entity) + " entity not found with parameters " + searchParams;
  }
}
