package com.laila.pet_symptom_tracker.securityconfig;

import com.laila.pet_symptom_tracker.mainconfig.ColoredLogger;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {
  private final Logger log = Logger.getLogger(CorsConfig.class.getName());
  private final String corsClient;

  public CorsConfig(@Value("${pet_symptom_tracker.cors}") String corsClient) {
    this.corsClient = corsClient;
  }

  @Bean
  CorsConfigurationSource corsConfiguration() {
    /*TODO NOTE TO SELF in postman moet je de OPTIONS request gebruiken, bij headers geef je mee:
      - Origin: address van de client.
      - Acces-Control-Request-Method: Soort request, post patch etc.
      - Acces-Control-Request-Headers: Authorization, Content-Type
    */

    ColoredLogger.logCustomInColor(0, 0, 0, "Cors config loaded, client: " + corsClient);
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of(corsClient));
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(
        List.of("Authorization", "Cache-Control", "Content-Type", "Accept", "X-Requested-With"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  FilterRegistrationBean<CorsFilter> corsFilterBean() {

    FilterRegistrationBean<CorsFilter> bean =
        new FilterRegistrationBean<>(new CorsFilter(corsConfiguration()));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }
}
