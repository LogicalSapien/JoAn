package com.logicalsapien.joan.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * JoAn app Configuraton file.
 */
@Configuration
public class JoAnConfig {

  /**
   * Rest Template bean.
   * @param builder Rest Template builder.
   * @return Rest Template.
   */
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    // Do any additional configuration here
    return builder.build();
  }

}
