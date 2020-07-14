package com.logicalsapien.joan.service;

import com.logicalsapien.joan.model.JobSearchResponseDto;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Job Search Service Implementation.
 */
@Service
public class JobSearchServiceImpl implements JobSearchService {

  @Autowired
  RestTemplate restTemplate;

  @Value("${adzuna.url}")
  private String url;

  @Value("${adzuna.api}")
  private String api;

  @Value("${adzuna.appId}")
  private String appId;

  @Value("${adzuna.appKey}")
  private String appKey;
  
  /**
   * Total Count String.
   */
  private static final String TOTAL_COUNT = "totalCount";

  /**
   * Count for min average String.
   */
  private static final String MIN_AVERAGE_CNT = "countForMinAverage";

  /**
   * Count for max average String.
   */
  private static final String MAX_AVERAGE_CNT = "countForMaxAverage";

  /**
   * Salary Min Sum String.
   */
  private static final String MIN_SUM = "minSum";

  /**
   * Salary Max Sum String.
   */
  private static final String MAX_SUM = "maxSum";



  /**
   * Get average salary for a particular Job Name.
   * @param jobName Job Name to be searched on
   * @param country Country to be searched on
   * @return Search Result
   */
  @Override
  public JobSearchResponseDto calculateAverageJobSalary(
      final String jobName, final String country) {
    // Create a map to track various counters/values
    Map<String, Double> valueMap = new HashMap<>();
    valueMap.put(TOTAL_COUNT, 0d);
    // to keep the track on the number of min salaries added
    valueMap.put(MIN_AVERAGE_CNT, 0d);
    // to keep the track on the number of max salaries added
    valueMap.put(MAX_AVERAGE_CNT, 0d);
    // sum on all minimum salaries
    valueMap.put(MIN_SUM, 0d);
    // sum on all minimum salaries
    valueMap.put(MAX_SUM, 0d);
    // query adzuna api
    int startingPage = 1;
    int resultsPerPage = 50;
    String urlToCall = getApiUrl(country, startingPage, resultsPerPage, jobName);
    if (Objects.nonNull(urlToCall)) {
      // call the api as long all the results are fetched
      while (true) {
        ResponseEntity<Object> apiResponse = restTemplate
                .exchange(urlToCall, HttpMethod.GET, null,
                        new ParameterizedTypeReference<Object>() {});
        if (Objects.nonNull(apiResponse.getBody())) {
          parseResponseAndAddData(apiResponse, valueMap);
        }
        if ((startingPage * resultsPerPage) < valueMap.get(TOTAL_COUNT)) {
          // continue loop and get next page
          startingPage++;
        } else {
          break;
        }
      }
    }
    JobSearchResponseDto responseDto = new JobSearchResponseDto();
    responseDto.setNoOfJobs(valueMap.get(TOTAL_COUNT).longValue());
    if (valueMap.get(MIN_AVERAGE_CNT) != 0) {
      responseDto.setAverageMinSalary(
              valueMap.get(MIN_SUM) / valueMap.get(MIN_AVERAGE_CNT));
    }
    if (valueMap.get(MAX_AVERAGE_CNT) != 0) {
      responseDto.setAverageMaxSalary(
              valueMap.get(MAX_SUM) / valueMap.get(MAX_AVERAGE_CNT));
    }
    return responseDto;
  }

  private String getApiUrl(final String country, final int startingPage,
                           final int resultsPerPage, final String jobName) {
    if (Objects.nonNull(country) && Objects.nonNull(jobName)) {
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

  private void parseResponseAndAddData(final ResponseEntity<Object> apiResponse,
                                       final Map<String, Double> valueMap) {

    LinkedHashMap<String, Object> responseBody = (LinkedHashMap) apiResponse.getBody();
    valueMap.put(TOTAL_COUNT, Double.parseDouble(responseBody.get("count").toString()));
    List<LinkedHashMap<String, Object>> results
            = (List<LinkedHashMap<String, Object>>) responseBody.get("results");
    // iterate through results
    for (LinkedHashMap<String, Object> result : results) {
      if (Objects.nonNull(result.get("salary_min"))) {
        double salaryMin = Double.parseDouble(result.get("salary_min").toString());
        if (salaryMin > 0) {
          valueMap.put(MIN_SUM, valueMap.get(MIN_SUM) + salaryMin);
          valueMap.put(MIN_AVERAGE_CNT, valueMap.get(MIN_AVERAGE_CNT) + 1);
        }
      }
      if (Objects.nonNull(result.get("salary_max"))) {
        double salaryMax = Double.parseDouble(result.get("salary_max").toString());
        if (salaryMax > 0) {
          valueMap.put(MAX_SUM, valueMap.get(MAX_SUM) + salaryMax);
          valueMap.put(MAX_AVERAGE_CNT, valueMap.get(MAX_AVERAGE_CNT) + 1);
        }
      }
    }
  }
}
