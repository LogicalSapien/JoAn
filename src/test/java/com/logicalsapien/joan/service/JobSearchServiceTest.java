package com.logicalsapien.joan.service;

import com.logicalsapien.joan.model.CategoryDto;
import com.logicalsapien.joan.model.CompanyDto;
import com.logicalsapien.joan.model.JobDetailsDto;
import com.logicalsapien.joan.model.JobSearchResponseDto;
import com.logicalsapien.joan.model.LocationDto;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.data.util.Pair;
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

    // create mocks
    Pair<ResponseEntity, JobSearchResponseDto> reqResp
            = mockServiceResponseAndGetExpected();

    Mockito.when(restTemplate.exchange(
            ArgumentMatchers.contains("/jobs/gb/search"),
            ArgumentMatchers.eq(HttpMethod.GET),
            ArgumentMatchers.isNull(),
            ArgumentMatchers.<ParameterizedTypeReference<Object>>any())
    ).thenReturn(reqResp.getFirst());

    // Execute the service call
    JobSearchResponseDto actResponse
            = jobSearchService.calculateAverageJobSalary("jobname", "gb", null);

    // Assert the response
    Assertions.assertNotNull(actResponse);
    Assertions.assertEquals(reqResp.getSecond(), actResponse);

    // verify that the rest service is called twice
    Mockito.verify(restTemplate, Mockito.times(1))
            .exchange(ArgumentMatchers.contains("/jobs/gb/search/1"),
                    ArgumentMatchers.eq(HttpMethod.GET),
                    ArgumentMatchers.isNull(),
                    ArgumentMatchers.<ParameterizedTypeReference<Object>>any());
    Mockito.verify(restTemplate, Mockito.times(1))
            .exchange(ArgumentMatchers.contains("/jobs/gb/search/2"),
                    ArgumentMatchers.eq(HttpMethod.GET),
                    ArgumentMatchers.isNull(),
                    ArgumentMatchers.<ParameterizedTypeReference<Object>>any());
  }

  private Pair<ResponseEntity, JobSearchResponseDto> mockServiceResponseAndGetExpected() {
    // create mock response
    LinkedHashMap<String, Object> r1 = new LinkedHashMap<>();
    r1.put("salary_min", 10);
    r1.put("salary_max", 20);
    r1.put("id", "id");
    r1.put("title", "Title");
    r1.put("description", "Description");
    r1.put("latitude", 55.90);
    r1.put("longitude", -3.90);
    Map<String, Object> location = new HashMap<>();
    location.put("display_name", "locationName");
    location.put("area", List.of("Area1", "Area2"));
    r1.put("location", location);
    Map<String, Object> category = new HashMap<>();
    category.put("tag", "Tag");
    category.put("label", "Label");
    r1.put("category", category);
    Map<String, Object> company = new HashMap<>();
    company.put("display_name", "Display Name");
    r1.put("company", company);

    LinkedHashMap<String, Object> r2 = new LinkedHashMap<>();
    r2.put("salary_min", 30);
    r2.put("salary_max", 40);

    LinkedHashMap<String, Object> r3 = new LinkedHashMap<>();

    List<LinkedHashMap> results = new ArrayList<>();
    results.add(r1);
    results.add(r2);
    results.add(r3);

    Map<String, Object> responseBody = new LinkedHashMap<>();
    responseBody.put("results", results);
    // giving 51 to fetch paginated request
    responseBody.put("count", 51);
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_JSON);

    JobSearchResponseDto expResponse = new JobSearchResponseDto();
    expResponse.setFetchedJobs(6L);
    expResponse.setTotalNoJobs(51L);
    expResponse.setAverageMinSalary(20d);
    expResponse.setAverageMaxSalary(30d);
    JobDetailsDto jd1 = new JobDetailsDto();
    jd1.setSalaryMin(10d);
    jd1.setSalaryMax(20d);
    jd1.setId("id");
    jd1.setTitle("Title");
    jd1.setDescription("Description");
    jd1.setLatitude(55.90);
    jd1.setLongitude(-3.90);
    LocationDto locationDto = new LocationDto();
    locationDto.setDisplayName("locationName");
    locationDto.setArea(List.of("Area1", "Area2"));
    jd1.setLocation(locationDto);
    CategoryDto categoryDto = new CategoryDto();
    categoryDto.setTag("Tag");
    categoryDto.setLabel("Label");
    jd1.setCategory(categoryDto);
    CompanyDto companyDto = new CompanyDto();
    companyDto.setDisplayName("Display Name");
    jd1.setCompany(companyDto);


    JobDetailsDto jd2 = new JobDetailsDto();
    jd2.setSalaryMin(30d);
    jd2.setSalaryMax(40d);
    JobDetailsDto jd3 = new JobDetailsDto();
    expResponse.setJobDetails(List.of(jd1, jd2, jd3, jd1, jd2, jd3));

    ResponseEntity apiResponse = new ResponseEntity<>(
            responseBody,
            header,
            HttpStatus.OK
    );
    return Pair.of(apiResponse, expResponse);
  }

  /**
   * Calculate average job salary - invalid Country - test.
   */
  @Test
  @DisplayName("Calculate average job salary - invalid Country - test")
  void calculateAverageJobSalaryInvalidCountryTest() {

    JobSearchResponseDto expResponse = new JobSearchResponseDto();
    expResponse.setFetchedJobs(0L);
    expResponse.setTotalNoJobs(0L);

    // Execute the service call
    JobSearchResponseDto actResponse
            = jobSearchService.calculateAverageJobSalary("jobname", "invalid", null);

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

  @Test
  @DisplayName("Calculate average job salary test")
  void getRandomJobInfoTest() {

    // create mocks
    Pair<ResponseEntity, JobSearchResponseDto> reqResp
            = mockServiceResponseAndGetExpected();

    Mockito.when(restTemplate.exchange(
            ArgumentMatchers.contains("/jobs/gb/search"),
            ArgumentMatchers.eq(HttpMethod.GET),
            ArgumentMatchers.isNull(),
            ArgumentMatchers.<ParameterizedTypeReference<Object>>any())
    ).thenReturn(reqResp.getFirst());

    // Execute the service call
    JobSearchResponseDto actResponse
            = jobSearchService.getRandomJobInfo(true);

    // Assert the response
    Assertions.assertNotNull(actResponse);
    Assertions.assertEquals(reqResp.getSecond(), actResponse);

    // verify that the rest service is called twice
    Mockito.verify(restTemplate, Mockito.times(1))
            .exchange(ArgumentMatchers.contains("/jobs/gb/search/1"),
                    ArgumentMatchers.eq(HttpMethod.GET),
                    ArgumentMatchers.isNull(),
                    ArgumentMatchers.<ParameterizedTypeReference<Object>>any());
    Mockito.verify(restTemplate, Mockito.times(1))
            .exchange(ArgumentMatchers.contains("/jobs/gb/search/2"),
                    ArgumentMatchers.eq(HttpMethod.GET),
                    ArgumentMatchers.isNull(),
                    ArgumentMatchers.<ParameterizedTypeReference<Object>>any());
  }

}
