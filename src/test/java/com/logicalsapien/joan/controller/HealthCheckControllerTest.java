package com.logicalsapien.joan.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Job Search Controller Tests.
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = HealthCheckController.class)
class HealthCheckControllerTest {

  /**
   * Mock Mvc.
   */
  @Autowired
  private MockMvc mockMvc;


  /**
   * Health check test.
   * @throws Exception exception
   */
  @Test
  @DisplayName("Health check test")
  void healthCheckTest() throws Exception {
    MockHttpServletResponse response
            = mockMvc.perform(
            get("/__health")
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn().getResponse();

    /* Assert */
    assertThat(response.getStatus(), equalTo(HttpStatus.OK.value()));
    assertThat(response.getContentAsString(),
            equalTo("Ok"));
  }

}
