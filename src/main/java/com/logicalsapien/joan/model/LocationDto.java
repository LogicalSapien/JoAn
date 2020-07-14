package com.logicalsapien.joan.model;

import java.util.List;
import lombok.Data;

/**
 * Location Dto.
 */
@Data
public class LocationDto {

  /**
   * Display Name.
   */
  private String displayName;

  /**
   * Area list.
   */
  private List<String> area;
}
