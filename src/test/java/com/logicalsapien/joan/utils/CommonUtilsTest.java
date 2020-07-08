package com.logicalsapien.joan.utils;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.owasp.esapi.ESAPI;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Common utils test.
 */
@SpringBootTest
class CommonUtilsTest {

  /**
   * Strip Xss Test.
   */
  @Test
  void stripXssTest() {
    Assertions.assertEquals(
            "sample text", CommonUtils.stripXss("sample text \0 <script></script>"));
  }

  /**
   * Strip Xss Null Test.
   */
  @Test
  void stripXssNullTest() {
    Assertions.assertEquals(null, CommonUtils.stripXss(null));
  }
}
