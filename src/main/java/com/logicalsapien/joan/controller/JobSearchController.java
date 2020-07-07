package com.logicalsapien.joan.controller;

import com.logicalsapien.joan.model.JobSearchResponseDto;
import com.logicalsapien.joan.service.JobSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Job Search Controller.
 */
@RestController
@RequestMapping("/jobsearch")
public class JobSearchController {

  /**
   * Autowiring the JobSearch Service.
   */
  @Autowired
  private JobSearchService jobSearchService;

  /**
   * Get average salary for a particular Job Name.
   * @param jobName Job Name to be searched on
   * @param country Country to be searched on
   * @return Search Result
   */
  @GetMapping("averagesalary")
  public ResponseEntity<JobSearchResponseDto> calculateAverageJobSalary(
      @RequestParam("jobName") final String jobName,
      @RequestParam("country") final String country) {
    JobSearchResponseDto searchResult
        = jobSearchService.calculateAverageJobSalary(jobName, country);
    return new ResponseEntity<>(searchResult, HttpStatus.OK);
  }

}
