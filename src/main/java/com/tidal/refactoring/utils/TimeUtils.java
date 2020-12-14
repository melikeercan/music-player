package com.tidal.refactoring.utils;

import java.util.Calendar;

public class TimeUtils {
  public static boolean isDurationValid(Integer duration) {
    return duration != null && duration >= 0;
  }

  public static boolean isReleaseDateValid(Integer releaseYear) {
    if (releaseYear == null || releaseYear < 0) {
      return false;
    }
    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
    return currentYear >= releaseYear;
  }

  public static boolean isDateValid(Long date) {
    if (date == null) {
      return false;
    }
    return Calendar.getInstance().getTime().getTime() > date;
  }

}
