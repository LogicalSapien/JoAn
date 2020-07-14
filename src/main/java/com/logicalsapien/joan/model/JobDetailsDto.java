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
  private double salaryMin;

  /**
   * Salary Max.
   */
  private double salaryMax;

  /**
   * Latitude info.
   */
  private double latitude;

  /**
   * Longitude info.
   */
  private double longitude;

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
