package com.logicalsapien.joan.model;

import java.util.List;
import lombok.Data;

/**
 * Average Salary Response Dto.
 */
@Data
public class JobSearchResponseDto {

  /**
   * No Of Jobs fetched.
   */
  private Long noOfJobs;

  /**
   * Average Min Salary calculated.
   */
  private Double averageMinSalary;

  /**
   * Average Max Salary calculated.
   */
  private Double averageMaxSalary;

  /**
   * Job Details list.
   */
  private List<JobDetailsDto> jobDetails;

}
