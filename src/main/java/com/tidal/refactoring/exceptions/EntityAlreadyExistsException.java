package com.tidal.refactoring.exceptions;

import java.util.Map;

import org.springframework.util.StringUtils;

public class EntityAlreadyExistsException extends BaseException {

  public EntityAlreadyExistsException(Class clazz, String... searchParamsMap) {
    super(EntityAlreadyExistsException
        .generateMessage(clazz.getSimpleName(),
            toMap(String.class, String.class, searchParamsMap)));
  }

  private static String generateMessage(String entity, Map<String, String> searchParams) {
    return StringUtils.capitalize(entity) + " entity already exists with parameters "
        + searchParams;
  }
}
