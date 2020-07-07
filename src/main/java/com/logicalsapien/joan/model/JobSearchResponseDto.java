package com.logicalsapien.joan.model;

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
   * Average Salary calculated.
   */
  private Double averageSalary;

}
