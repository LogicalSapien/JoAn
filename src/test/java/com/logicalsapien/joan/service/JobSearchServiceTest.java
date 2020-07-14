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
class JobSearchServiceTest {

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
    r1.put("salary_max", 20);
    results.add(r1);
    LinkedHashMap<String, Object> r2 = new LinkedHashMap<>();
    r2.put("salary_min", 30);
    r2.put("salary_max", 40);
    results.add(r2);
    Map<String, Object> responseBody = new LinkedHashMap<>();
    responseBody.put("results", results);
    // giving 51 to fetch paginated request
    responseBody.put("count", 51);
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_JSON);

    ResponseEntity apiResponse = new ResponseEntity<>(
            responseBody,
            header,
            HttpStatus.OK
    );

    Mockito.when(restTemplate.exchange(
        ArgumentMatchers.contains("/jobs/gb/search"),
        ArgumentMatchers.eq(HttpMethod.GET),
        ArgumentMatchers.isNull(),
        ArgumentMatchers.<ParameterizedTypeReference<Object>>any())
    ).thenReturn(apiResponse);

    JobSearchResponseDto expResponse = new JobSearchResponseDto();
    expResponse.setNoOfJobs(51L);
    expResponse.setAverageMinSalary(20d);
    expResponse.setAverageMaxSalary(30d);

    // Execute the service call
    JobSearchResponseDto actResponse
            = jobSearchService.calculateAverageJobSalary("jobname", "gb");

    // Assert the response
    Assertions.assertNotNull(actResponse);
    Assertions.assertEquals(expResponse, actResponse);

    // verify that the rest service is called twice
    Mockito.verify(restTemplate, Mockito.times(2))
            .exchange(ArgumentMatchers.contains("/jobs/gb/search"),
                    ArgumentMatchers.eq(HttpMethod.GET),
                    ArgumentMatchers.isNull(),
                    ArgumentMatchers.<ParameterizedTypeReference<Object>>any());
  }


  /**
   * Calculate average job salary - invalid Country - test.
   */
  @Test
  @DisplayName("Calculate average job salary - invalid Country - test")
  void calculateAverageJobSalaryInvalidCountryTest() {

    JobSearchResponseDto expResponse = new JobSearchResponseDto();
    expResponse.setNoOfJobs(0L);

    // Execute the service call
    JobSearchResponseDto actResponse
            = jobSearchService.calculateAverageJobSalary("jobname", null);

    // Assert the response
    Assertions.assertNotNull(actResponse);
    Assertions.assertEquals(expResponse, actResponse);

    // verify that the rest service is not called
    Mockito.verify(restTemplate, Mockito.times(0))
            .exchange(ArgumentMatchers.any(),
                    ArgumentMatchers.eq(HttpMethod.GET),
                    ArgumentMatchers.isNull(),
                    ArgumentMatchers.<ParameterizedTypeReference<Object>>any());
  }

}
