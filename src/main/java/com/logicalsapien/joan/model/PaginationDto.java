package com.logicalsapien.joan.model;

import lombok.Data;

/**
 * Model to handle pagination request.
 */
@Data
public class PaginationDto {

  /**
   * Page number starting 0.
   */
  private int page;

  /**
   * Number of items per page.
   */
  private int size;

  /**
   * Sort field.
   */
  private String sort;

  /**
   * Total count.
   */
  private int count;

}
