package com.logicalsapien.joan.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Sample Controller.
 */
@RestController
@RequestMapping("/api")
public class SampleController {

  /**
   * get Api to return list of all planets.
   * @return List of Planets
   */
  @GetMapping("sample")
  public ResponseEntity<String> getSampleResponse() {
    return new ResponseEntity<>("It Works...", HttpStatus.OK);
  }

}
