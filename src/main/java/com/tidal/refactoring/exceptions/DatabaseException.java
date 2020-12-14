package com.tidal.refactoring.exceptions;

import java.util.Map;

import org.springframework.util.StringUtils;

public class DatabaseException extends BaseException {

  public DatabaseException(Class clazz, String... searchParamsMap) {
    super(DatabaseException
        .generateMessage(clazz.getSimpleName(),
            toMap(String.class, String.class, searchParamsMap)));
  }

  private static String generateMessage(String entity, Map<String, String> searchParams) {
    return StringUtils.capitalize(entity) + " database error " + searchParams;
  }
}
