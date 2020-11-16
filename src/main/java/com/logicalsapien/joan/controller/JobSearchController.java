package com.logicalsapien.joan.controller;

import com.logicalsapien.joan.model.JobSearchRequestDto;
import com.logicalsapien.joan.model.JobSearchResponseDto;
import com.logicalsapien.joan.service.JobSearchService;
import com.logicalsapien.joan.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
   * Search for jobs..
   * @param jobSearchRequest Job Search Request
   * @return Search Result
   */
  @PostMapping()
  public ResponseEntity<JobSearchResponseDto> calculateAverageJobSalary(
          @RequestBody final JobSearchRequestDto jobSearchRequest) {
    JobSearchResponseDto searchResult
            = jobSearchService.searchJob(jobSearchRequest);
    return new ResponseEntity<>(searchResult, HttpStatus.OK);
  }

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
        = jobSearchService.calculateAverageJobSalary(
                CommonUtils.stripXss(jobName), CommonUtils.stripXss(country), null);
    return new ResponseEntity<>(searchResult, HttpStatus.OK);
  }


  /**
   * Get random job results.
   * @param imFeelingLucky Im feeling lucky boolean
   * @return Search Result
   */
  @GetMapping("random")
  public ResponseEntity<JobSearchResponseDto> getRandomJobResponse(
          @RequestParam(value = "imFeelingLucky", required = false,
                  defaultValue = "true") final boolean imFeelingLucky) {
    JobSearchResponseDto searchResult
            = jobSearchService.getRandomJobInfo(imFeelingLucky);
    return new ResponseEntity<>(searchResult, HttpStatus.OK);
  }

}
