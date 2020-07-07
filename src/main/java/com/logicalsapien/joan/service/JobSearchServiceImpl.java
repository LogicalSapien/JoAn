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
    double sum = 0;
    if (Objects.nonNull(jobName) && Objects.nonNull(country)) {
      // query adzuna api
      int startingPage = 1;
      int resultsPerPage = 1000;
      ResponseEntity<Object> apiResponse = restTemplate
          .exchange(url + "/" + api + "/jobs/" + country + "/search/" + startingPage + "?"
                  + "app_id=" + appId + "&app_key=" + appKey
                  + "&results_per_page=" + resultsPerPage + "&title_only=" + jobName,
              HttpMethod.GET, null, new ParameterizedTypeReference<Object>() {}
          );
      if ( Objects.nonNull(apiResponse.getBody())) {
        LinkedHashMap<String, Object> responseBody = (LinkedHashMap) apiResponse.getBody();
        List<LinkedHashMap<String, Object>> results
            = (List<LinkedHashMap<String, Object>>) responseBody.get("results");

        // iterate through results
        for (LinkedHashMap<String, Object> result : results) {
          sum = sum + Double.parseDouble(result.get("salary_min").toString());
          totalCount++;
        }
      }
    }
    JobSearchResponseDto responseDto = new JobSearchResponseDto();
    responseDto.setNoOfJobs(totalCount);
    if (totalCount != 0) {
      responseDto.setAverageSalary(sum / totalCount);
    }
    return responseDto;
  }
}
