package com.logicalsapien.joan.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.net.URL;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


/**
 * Sample Controller Tests.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SampleControllerTest {

  /**
   * To bind the Random Port.
   */
  @LocalServerPort
  private int port;


  /**
   * Rest Template for testing the api.
   */
  @Autowired
  private TestRestTemplate restTemplate;

  /**
   * List all Planets test.
   * @throws Exception exception
   */
  @Test
  void listAllPlanetsTest() throws Exception {

    ResponseEntity<String> response = restTemplate.getForEntity(
            new URL("http://localhost:" + port + "/api/sample").toString(), String.class);

    assertThat(response.getBody(), equalTo("It Works..."));
  }

}
