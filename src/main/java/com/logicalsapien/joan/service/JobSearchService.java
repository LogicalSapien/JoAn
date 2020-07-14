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
   * @param maxResults Maximum results to fetch and consider
   * @return Search Result
   */
  JobSearchResponseDto calculateAverageJobSalary(
      String jobName, String country, Long maxResults);


  /**
   * Get random job info.
   * @param imFeelingLucky I'm feeling lucky - if true returns first 100 results only.
   * @return Search Result
   */
  JobSearchResponseDto getRandomJobInfo(boolean imFeelingLucky);
}
