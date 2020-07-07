package com.logicalsapien.joan.service;

import com.logicalsapien.joan.model.JobSearchResponseDto;

/**
 * Job Search Service.
 */
public interface JobSearchService {

  /**
   * Get average salary for a particular Job Name.
   * @param jobName Job Name to be searched on
   * @param country Country to be searched on
   * @return Search Result
   */
  JobSearchResponseDto calculateAverageJobSalary(
      String jobName, String country);
}
