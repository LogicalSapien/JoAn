package com.logicalsapien.joan.utils;

import java.util.Objects;
import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.owasp.esapi.ESAPI;

/**
 * Common Utilities class.
 */
public final class CommonUtils {

  /**
   * Private constructor for the utility class as utility classes are not instantiated.
   */
  private CommonUtils() {

  }

  /**
   * Check if a string is valid.
   * @param value Input Value
   * @return Valid or Invalid response
   */
  public static boolean isValid(final String value) {
    return (Objects.nonNull(value) && !value.isEmpty() && value.trim().length() != 0);
  }

  /**#Method to strip dangerous scripts from input parameters.
   * @param input Input string to strip
   * @returnStripped response
   */
  public static String stripXss(final String input) {
    if (isValid(input)) {
      String canocicalized = ESAPI.encoder().canonicalize(input);
      canocicalized = canocicalized.replace("\0", "");
      canocicalized = Jsoup.clean(canocicalized, Whitelist.none());
      return StringEscapeUtils.escapeHtml(canocicalized);
    } else {
      return input;
    }
  }
}
