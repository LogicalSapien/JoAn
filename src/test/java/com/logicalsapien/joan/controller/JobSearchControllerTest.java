package com.logicalsapien.joan.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logicalsapien.joan.model.JobSearchResponseDto;
import com.logicalsapien.joan.service.JobSearchService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Job Search Controller Tests.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = JobSearchController.class)
class JobSearchControllerTest {

  /**
   * Mock Mvc.
   */
  @Autowired
  private MockMvc mockMvc;

  /**
   * Mocking JobSearchService.
   */
  @MockBean
  private JobSearchService jobSearchService;

  /**
   * Get average salary for a particular Job Name test.
   * @throws Exception exception
   */
  @Test
  @DisplayName("Get average salary for a particular Job Name test")
  void calculateAverageJobSalaryTest() throws Exception {
    JobSearchResponseDto resp1 = new JobSearchResponseDto();
    resp1.setFetchedJobs(100L);
    resp1.setTotalNoJobs(100L);
    resp1.setAverageMinSalary(1000d);
    resp1.setAverageMaxSalary(2000d);
    resp1.setJobDetails(List.of());
    when(jobSearchService.calculateAverageJobSalary("nametest", "gb", null))
        .thenReturn(resp1);
    MockHttpServletResponse response
        = mockMvc.perform(
            get("/jobsearch/averagesalary?jobName=nametest&country=gb")
        .accept(MediaType.APPLICATION_JSON))
        .andReturn().getResponse();

    /* Assert */
    assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
    assertThat(response.getContentAsString(),
        equalTo(getJsonFromObject(resp1)));
    verify(jobSearchService, times(1))
        .calculateAverageJobSalary("nametest", "gb", null);
  }


  /**
   * Get random job details test.
   * @throws Exception exception
   */
  @Test
  @DisplayName("Get random job details test")
  void getRandomJobResponseTest() throws Exception {
    JobSearchResponseDto resp1 = new JobSearchResponseDto();
    resp1.setFetchedJobs(100L);
    resp1.setTotalNoJobs(100L);
    resp1.setAverageMinSalary(1000d);
    resp1.setAverageMaxSalary(2000d);
    resp1.setJobDetails(List.of());
    when(jobSearchService.getRandomJobInfo(true))
            .thenReturn(resp1);
    MockHttpServletResponse response
            = mockMvc.perform(
            get("/jobsearch/random")
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    /* Assert */
    assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
    assertThat(response.getContentAsString(),
            equalTo(getJsonFromObject(resp1)));
    verify(jobSearchService, times(1))
            .getRandomJobInfo(true);
  }

  /**
   * To Convert an Object to JSON String.
   * @param o Object
   * @return Object as String
   * @throws JsonProcessingException throws JsonProcessingException
   */
  private static String getJsonFromObject(
      final Object o) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return mapper.writeValueAsString(o);
  }

}
