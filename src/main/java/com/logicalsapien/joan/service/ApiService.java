package com.logicalsapien.joan.service;

import static java.util.Map.entry;

import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Api Service class.
 */
@Service
public class ApiService {

  @Value("${adzuna.url}")
  private String url;

  @Value("${adzuna.api}")
  private String api;

  @Value("${adzuna.appId}")
  private String appId;

  @Value("${adzuna.appKey}")
  private String appKey;

  // this works for any number of elements:

  Map<String, String> countryMap = Map.ofEntries(
          entry("uk", "gb"),
          entry("gb", "gb")
  );

  /**
   * Method to get sanitized country short string for forming the url.
   * @param inputCountry Input Country
   */
  public String getCountryShort(final String inputCountry) {
    return countryMap.get(inputCountry.toLowerCase());
  }

  /**
   * Job Search Api.
   * @param country Country name
   * @param startingPage Starting page
   * @param resultsPerPage Results page
   * @param jobName job name to search
   * @return Url
   */
  public String getJobSearchApiUrl(final String country, final int startingPage,
                           final int resultsPerPage, final String jobName) {
    String sanitizedCountry = getCountryShort(country);
    String jobNameToSearch = jobName.toLowerCase();
    if (Objects.nonNull(sanitizedCountry) && Objects.nonNull(jobNameToSearch)) {
      StringBuilder urlToCall = new StringBuilder(url + "/" + api + "/jobs/");
      urlToCall.append(country.trim());
      urlToCall.append("/search/");
      urlToCall.append(startingPage);
      urlToCall.append("?app_id=" + appId);
      urlToCall.append("&app_key=" + appKey);
      urlToCall.append("&results_per_page=" + resultsPerPage);
      urlToCall.append("&title_only=" + jobName.trim());
      return urlToCall.toString();
    } else {
      return null;
    }
  }
}
