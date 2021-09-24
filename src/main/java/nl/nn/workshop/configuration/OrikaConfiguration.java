package nl.nn.workshop.configuration;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaConfiguration {

  @Bean
  public MapperFacade mapperFacade() {
    return new DefaultMapperFactory.Builder().build().getMapperFacade();
  }

}