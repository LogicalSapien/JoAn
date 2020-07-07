package com.logicalsapien.joan.service;

import com.logicalsapien.joan.model.JobSearchResponseDto;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Job Search Service Tests.
 */
@SpringBootTest
public class JobSearchServiceTest {

  /**
   * Autowire JobSearchService.
   */
  @Autowired
  private JobSearchService jobSearchService;

  /**
   * Rest Template mock.
   */
  @MockBean
  private RestTemplate restTemplate;

  /**
   * Calculate average job salary test.
   */
  @Test
  @DisplayName("Calculate average job salary test")
  void calculateAverageJobSalaryTest() {

    // create mock response
    List<LinkedHashMap> results = new ArrayList<>();
    LinkedHashMap<String, Object> r1 = new LinkedHashMap<>();
    r1.put("salary_min", 10);
    results.add(r1);
    LinkedHashMap<String, Object> r2 = new LinkedHashMap<>();
    r2.put("salary_min", 30);
    results.add(r2);
    Map<String, Object> responseBody = new LinkedHashMap<>();
    responseBody.put("results", results);
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_JSON);
    ResponseEntity apiResponse = new ResponseEntity<>(
        responseBody,
        header,
        HttpStatus.OK
    );

    JobSearchResponseDto expResponse = new JobSearchResponseDto();
    expResponse.setNoOfJobs(2L);
    expResponse.setAverageSalary(20d);

    Mockito.when(restTemplate.exchange(
        ArgumentMatchers.contains("/v1/api/jobs/gb/search"),
        ArgumentMatchers.eq(HttpMethod.GET),
        ArgumentMatchers.isNull(),
        ArgumentMatchers.<ParameterizedTypeReference<Object>>any())
    ).thenReturn(apiResponse);

    // Execute the service call
    JobSearchResponseDto actResponse
        = jobSearchService.calculateAverageJobSalary("jobname", "gb");

    // Assert the response
    Assertions.assertNotNull(actResponse);
    Assertions.assertEquals(expResponse, actResponse);
  }

}
