package com.logicalsapien.joan.model;

import lombok.Data;

/**
 * Job Search Request Dto.
 */
@Data
public class JobSearchRequestDto {

  /**
   * Country.
   */
  private String country;

  /**
   * Search Query.
   */
  private String query;

  /**
   * Exclude Result Query.
   */
  private String exclude;

  /**
   * Exclude Result Query.
   */
  private String location;

  /**
   * Min Salary search.
   */
  private float minSalary;

  /**
   * Full time filter. possible values 0-1.
   */
  private Integer fullTime;

  /**
   * Permanenent filter. possible values 0-1.
   */
  private Integer permanent;

  /**
   * Search Query.
   */
  private PaginationDto pagination;

  /**
   * Enable pagination View - if disabled it'll  show infine scroll.
   */
  private boolean paginatedView;

}
