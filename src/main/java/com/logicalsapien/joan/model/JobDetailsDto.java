package com.logicalsapien.joan.model;

import lombok.Data;

/**
 * Job Details Dto.
 */
@Data
public class JobDetailsDto {

  /**
   * Job id.
   */
  private String id;

  /**
   * Title.
   */
  private String title;

  /**
   * Description.
   */
  private String description;

  /**
   * Salary Min.
   */
  private Double salaryMin;

  /**
   * Salary Max.
   */
  private Double salaryMax;

  /**
   * Latitude info.
   */
  private Double latitude;

  /**
   * Longitude info.
   */
  private Double longitude;

  /**
   * Location info.
   */
  private LocationDto location;

  /**
   * Category info.
   */
  private CategoryDto category;

  /**
   * Company info.
   */
  private CompanyDto company;

}
