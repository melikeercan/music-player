package com.tidal.refactoring.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseException extends RuntimeException {
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());
  private String content;

  public BaseException(String format) {
    super(format);
    this.setContent(format);
    logger.error("[TransactionId: " + UUID.randomUUID().toString() + "][Detail: " + content + "]");
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  protected static <K, V> Map<K, V> toMap(
      Class<K> keyType, Class<V> valueType, Object... entries) {
    if (entries.length % 2 == 1) {
      throw new IllegalArgumentException("Invalid entries");
    }
    return IntStream.range(0, entries.length / 2).map(i -> i * 2)
        .collect(HashMap::new,
            (m, i) -> m.put(keyType.cast(entries[i]), valueType.cast(entries[i + 1])),
            Map::putAll);
  }
}
