package com.logicalsapien.joan.service;

import com.logicalsapien.joan.model.JobSearchResponseDto;
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
    // query adzuna api
    int startingPage = 1;
    int resultsPerPage = 1000;
    ResponseEntity<Object> apiResponse = restTemplate
        .exchange("https://api.adzuna.com/v1/api/jobs/" + country + "/search/" + startingPage + "?"
                + "app_id=" + appId + "&app_key=" + appKey
                + "&results_per_page=" + resultsPerPage + "&title_only=" + jobName,
            HttpMethod.GET, null, new ParameterizedTypeReference<Object>() {}
        );
    if (Objects.nonNull(apiResponse)
        && Objects.nonNull(apiResponse.getBody())) {
      Map<String, Object> responseBody = (LinkedHashMap) apiResponse.getBody();
      List<LinkedHashMap> results
          = (List<LinkedHashMap>) responseBody.get("results");

      // iterate through results
      for (LinkedHashMap result : results) {
        sum = sum + (Integer) result.get("salary_min");
        totalCount++;
      }
    }
    JobSearchResponseDto responseDto = new JobSearchResponseDto();
    responseDto.setNoOfJobs(totalCount);
    responseDto.setAverageSalary(sum / totalCount);
    return responseDto;
  }
}
