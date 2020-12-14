package com.tidal.refactoring.services;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseServiceImp {
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  public Logger getLogger() {
    return this.logger;
  }

  public Locale getLocale() {
    return Locale.forLanguageTag("en");
  }
}
