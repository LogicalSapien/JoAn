package com.logicalsapien.joan.service;

import com.logicalsapien.joan.model.CategoryDto;
import com.logicalsapien.joan.model.CompanyDto;
import com.logicalsapien.joan.model.JobDetailsDto;
import com.logicalsapien.joan.model.JobSearchRequestDto;
import com.logicalsapien.joan.model.JobSearchResponseDto;
import com.logicalsapien.joan.model.LocationDto;
import com.logicalsapien.joan.model.PaginationDto;
import com.logicalsapien.joan.utils.CommonUtils;
import com.logicalsapien.joan.utils.JConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Job Search Service Implementation.
 */
@Service
public class JobSearchServiceImpl implements JobSearchService {

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  ApiService apiService;
  
  /**
   * Total Count String.
   */
  private static final String TOTAL_COUNT = "totalCount";

  /**
   * Fetched Count String.
   */
  private static final String FETCHED_COUNT = "fetchedCount";

  /**
   * Count for min average String.
   */
  private static final String MIN_AVERAGE_CNT = "countForMinAverage";

  /**
   * Count for max average String.
   */
  private static final String MAX_AVERAGE_CNT = "countForMaxAverage";

  /**
   * Salary Min Sum String.
   */
  private static final String MIN_SUM = "minSum";

  /**
   * Salary Max Sum String.
   */
  private static final String MAX_SUM = "maxSum";

  /**
   * Get average salary for a particular Job Name.
   * @param jobSearchRequest Job Request
   * @return Search Result
   */
  @Override
  public JobSearchResponseDto searchJob(final JobSearchRequestDto jobSearchRequest) {
    // validate Pagination request
    if (Objects.nonNull(jobSearchRequest.getPagination())) {
      jobSearchRequest.setPagination(new PaginationDto());
    }
    validatePaginationRequest(jobSearchRequest.getPagination());
    if (Objects.nonNull(jobSearchRequest.getCountry())) {
      jobSearchRequest.setCountry("uk");
    }
    // query adzuna api
    JobSearchResponseDto responseDto = new JobSearchResponseDto();
    String urlToCall = apiService.getJobSearchApiUrl(jobSearchRequest);
    responseDto.setJobDetails(new ArrayList<>());
    // call the api
    ResponseEntity<Object> apiResponse = restTemplate
            .exchange(urlToCall, HttpMethod.GET, null,
                    new ParameterizedTypeReference<Object>() {});
    if (Objects.nonNull(apiResponse.getBody())) {
      LinkedHashMap<String, Object> responseBody = (LinkedHashMap) apiResponse.getBody();
      if (Objects.nonNull(responseBody)
              && Objects.nonNull(responseBody.get(JConstants.RESULTS))) {
        List<LinkedHashMap<String, Object>> results
                = (List<LinkedHashMap<String, Object>>) responseBody.get(JConstants.RESULTS);
        JobDetailsDto jobDetailsDto = new JobDetailsDto();
        for (LinkedHashMap<String, Object> result : results) {
          mapToJobDetailsDto(result, jobDetailsDto);
          // add results to response dto
          responseDto.getJobDetails().add(jobDetailsDto);
        }
      }
    }
    return responseDto;
  }

  /**
   * Get average salary for a particular Job Name.
   * @param jobName Job Name to be searched on
   * @param country Country to be searched on
   * @param maxResults Maximum results to fetch and consider
   * @return Search Result
   */
  @Override
  public JobSearchResponseDto calculateAverageJobSalary(
      final String jobName, final String country, final Long maxResults) {
    // Create a map to track various counters/values
    Map<String, Double> valueMap = new HashMap<>();
    valueMap.put(TOTAL_COUNT, 0d);
    valueMap.put(FETCHED_COUNT, 0d);
    // to keep the track on the number of min salaries added
    valueMap.put(MIN_AVERAGE_CNT, 0d);
    // to keep the track on the number of max salaries added
    valueMap.put(MAX_AVERAGE_CNT, 0d);
    // sum on all minimum salaries
    valueMap.put(MIN_SUM, 0d);
    // sum on all minimum salaries
    valueMap.put(MAX_SUM, 0d);
    // query adzuna api
    long startingPage = 1;
    long resultsPerPage = 50;
    JobSearchResponseDto responseDto = new JobSearchResponseDto();
    String urlToCall = apiService.getJobSearchApiUrl(
            country, startingPage, resultsPerPage, jobName);
    if (Objects.nonNull(urlToCall)) {
      responseDto.setJobDetails(new ArrayList<>());
      // call the api as long all the results are fetched
      while (true) {
        ResponseEntity<Object> apiResponse = restTemplate
                .exchange(urlToCall, HttpMethod.GET, null,
                        new ParameterizedTypeReference<Object>() {});
        if (Objects.nonNull(apiResponse.getBody())) {
          parseResponseAndAddData(apiResponse, valueMap, responseDto);
        }
        // if max is null and so far fetched count is less than total count, iterate again
        // or if max is not null and  so far fetched is less than total max results, iterate again
        long totalResultsFetched = startingPage * resultsPerPage;
        if ((totalResultsFetched < valueMap.get(TOTAL_COUNT) && maxResults == null)
              || (maxResults != null && totalResultsFetched < maxResults
                    && totalResultsFetched < valueMap.get(TOTAL_COUNT))) {
          // continue loop and get next page
          startingPage++;
        } else {
          break;
        }
        // get next url to call
        urlToCall = apiService.getJobSearchApiUrl(
                country, startingPage, resultsPerPage, jobName);
      }
    }
    responseDto.setTotalNoJobs(valueMap.get(TOTAL_COUNT).longValue());
    responseDto.setFetchedJobs(valueMap.get(FETCHED_COUNT).longValue());
    if (valueMap.get(MIN_AVERAGE_CNT) != 0) {
      responseDto.setAverageMinSalary(
              valueMap.get(MIN_SUM) / valueMap.get(MIN_AVERAGE_CNT));
    }
    if (valueMap.get(MAX_AVERAGE_CNT) != 0) {
      responseDto.setAverageMaxSalary(
              valueMap.get(MAX_SUM) / valueMap.get(MAX_AVERAGE_CNT));
    }
    return responseDto;
  }

  /**
   * Get random job info.
   * @param imFeelingLucky I'm feeling lucky - if true returns first 100 results only.
   * @return Job search response
   */
  @Override
  public JobSearchResponseDto getRandomJobInfo(final boolean imFeelingLucky) {
    Long maxResults = null;
    if (imFeelingLucky) {
      maxResults = 100L;
    }
    return calculateAverageJobSalary(
            apiService.getRandomJobName(), apiService.getRandomCountryName(), maxResults);
  }

  private void parseResponseAndAddData(final ResponseEntity<Object> apiResponse,
                                       final Map<String, Double> valueMap,
                                       final JobSearchResponseDto responseDto) {

    LinkedHashMap<String, Object> responseBody = (LinkedHashMap) apiResponse.getBody();
    valueMap.put(TOTAL_COUNT, Double.parseDouble(responseBody.get("count").toString()));
    List<LinkedHashMap<String, Object>> results
            = (List<LinkedHashMap<String, Object>>) responseBody.get(JConstants.RESULTS);
    // iterate through results
    for (LinkedHashMap<String, Object> result : results) {
      JobDetailsDto jobDetailsDto = new JobDetailsDto();
      mapToJobDetailsDto(result, jobDetailsDto);
      if (Objects.nonNull(jobDetailsDto.getSalaryMin())
            && jobDetailsDto.getSalaryMin() > 0) {
        valueMap.put(MIN_SUM, valueMap.get(MIN_SUM) + jobDetailsDto.getSalaryMin());
        valueMap.put(MIN_AVERAGE_CNT, valueMap.get(MIN_AVERAGE_CNT) + 1);
      }
      if (Objects.nonNull(jobDetailsDto.getSalaryMax())
              && jobDetailsDto.getSalaryMax() > 0) {
        valueMap.put(MAX_SUM, valueMap.get(MAX_SUM) + jobDetailsDto.getSalaryMax());
        valueMap.put(MAX_AVERAGE_CNT, valueMap.get(MAX_AVERAGE_CNT) + 1);
      }
      // add results to response dto
      responseDto.getJobDetails().add(jobDetailsDto);
      valueMap.put(FETCHED_COUNT, valueMap.get(FETCHED_COUNT) + 1);
    }
  }

  @SuppressWarnings("squid:S3776")
  private void mapToJobDetailsDto(final LinkedHashMap<String, Object> result,
                                  final JobDetailsDto jobDetailsDto) {
    if (Objects.nonNull(result.get("id"))) {
      jobDetailsDto.setId(result.get("id").toString());
    }
    if (Objects.nonNull(result.get("title"))) {
      jobDetailsDto.setTitle(CommonUtils.stripXss(result.get("title").toString()));
    }
    if (Objects.nonNull(result.get("description"))) {
      jobDetailsDto.setDescription(result.get("description").toString());
    }
    if (Objects.nonNull(result.get("salary_min"))) {
      jobDetailsDto.setSalaryMin(Double.parseDouble(result.get("salary_min").toString()));
    }
    if (Objects.nonNull(result.get("salary_max"))) {
      jobDetailsDto.setSalaryMax(Double.parseDouble(result.get("salary_max").toString()));
    }
    if (Objects.nonNull(result.get("latitude"))) {
      jobDetailsDto.setLatitude(Double.parseDouble(result.get("latitude").toString()));
    }
    if (Objects.nonNull(result.get("longitude"))) {
      jobDetailsDto.setLongitude(Double.parseDouble(result.get("longitude").toString()));
    }
    if (Objects.nonNull(result.get("location"))) {
      Map<String, Object> location = (Map) result.get("location");
      LocationDto locationDto = new LocationDto();
      if (Objects.nonNull(location.get(JConstants.DISPLAY_NAME))) {
        locationDto.setDisplayName(location.get(JConstants.DISPLAY_NAME).toString());
      }
      locationDto.setArea((List<String>)location.get("area"));
      jobDetailsDto.setLocation(locationDto);
    }
    if (Objects.nonNull(result.get("category"))) {
      Map<String, Object> category = (Map) result.get("category");
      CategoryDto categoryDto = new CategoryDto();
      categoryDto.setTag(category.get("tag").toString());
      categoryDto.setLabel(category.get("label").toString());
      jobDetailsDto.setCategory(categoryDto);
    }
    if (Objects.nonNull(result.get("company"))) {
      Map<String, Object> company = (Map) result.get("company");
      CompanyDto companyDto = new CompanyDto();
      if (Objects.nonNull(company.get(JConstants.DISPLAY_NAME))) {
        companyDto.setDisplayName(company.get(JConstants.DISPLAY_NAME).toString());
      }
      jobDetailsDto.setCompany(companyDto);
    }
    if (Objects.nonNull(result.get("created"))) {
      jobDetailsDto.setCreated(result.get("created").toString());
    }
    if (Objects.nonNull(result.get("contract_type"))) {
      jobDetailsDto.setContractType(result.get("contract_type").toString());
    }
    if (Objects.nonNull(result.get("redirect_url"))) {
      jobDetailsDto.setRedirectUrl(result.get("redirect_url").toString());
    }
  }

  /**
   * Validate Pagination.
   * @param paginationDto Pagination Dto.
   */
  private void validatePaginationRequest(PaginationDto paginationDto) {
    if (paginationDto.getPage() <= 0) {
      paginationDto.setPage(1);
    }
    if (paginationDto.getSize() <= 0) {
      paginationDto.setSize(10);
    }
    if (!CommonUtils.isValid(paginationDto.getSort())) {
      // remove if any no string value is there
      paginationDto.setSort(null);
    }
  }
}
