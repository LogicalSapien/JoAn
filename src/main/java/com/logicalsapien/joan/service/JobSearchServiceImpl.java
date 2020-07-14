package com.logicalsapien.joan.service;

import com.logicalsapien.joan.model.JobSearchResponseDto;
import java.util.LinkedHashMap;
import java.util.List;
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
   * Get average salary for a particular Job Name.
   * @param jobName Job Name to be searched on
   * @param country Country to be searched on
   * @return Search Result
   */
  @Override
  public JobSearchResponseDto calculateAverageJobSalary(
      final String jobName, final String country) {
    long totalCount = 0;
    long countForMinAverage = 0;
    long countForMaxAverage = 0;
    double minSum = 0;
    double maxSum = 0;
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
          LinkedHashMap<String, Object> responseBody = (LinkedHashMap) apiResponse.getBody();
          totalCount = Long.parseLong(responseBody.get("count").toString());
          List<LinkedHashMap<String, Object>> results
                  = (List<LinkedHashMap<String, Object>>) responseBody.get("results");
          // iterate through results
          for (LinkedHashMap<String, Object> result : results) {
            double salaryMin = Double.parseDouble(result.get("salary_min").toString());
            double salaryMax = Double.parseDouble(result.get("salary_max").toString());
            if (salaryMin > 0) {
              minSum = minSum + salaryMin;
              countForMinAverage++;
            }
            if (salaryMax > 0) {
              maxSum = maxSum + salaryMax;
              countForMaxAverage++;
            }
          }
        }
        if ((startingPage * resultsPerPage) < totalCount) {
          // continue loop and get next page
          startingPage++;
        } else {
          break;
        }
      }
    }
    JobSearchResponseDto responseDto = new JobSearchResponseDto();
    responseDto.setNoOfJobs(totalCount);
    if (countForMinAverage != 0) {
      responseDto.setAverageMinSalary(minSum / countForMinAverage);
    }
    if (countForMaxAverage != 0) {
      responseDto.setAverageMaxSalary(maxSum / countForMaxAverage);
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
}
