package com.logicalsapien.joan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health Check Controller.
 */
@RestController
public class HealthCheckController {

  /**
   * Health Check api.
   * @return Ok String
   */
  @GetMapping("/__health")
  public ResponseEntity<String> healthCheck() {
    return new ResponseEntity<>("Ok", HttpStatus.OK);
  }
}
