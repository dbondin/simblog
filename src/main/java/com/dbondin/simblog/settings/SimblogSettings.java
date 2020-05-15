package com.dbondin.simblog.settings;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "simblog")
@Data
public class SimblogSettings {

  @Data
  public static final class Jwt {
      private String secret;
      private long tokenTtl;
  }
  
  private Jwt jwt;
  private String defaultUsername;
  private String defaultPassword;
}
