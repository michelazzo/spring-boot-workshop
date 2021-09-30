package nl.nn.workshop;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@Transactional
@DirtiesContext(methodMode = MethodMode.AFTER_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
@AutoConfigureMockMvc
public abstract class AbstractIntegrationTest extends AbstractTest {

  @Autowired
  protected MockMvc mvc;

  public static PostgreSQLContainer<?> container =
      new PostgreSQLContainer<>("postgres:13.4")
          .withUsername("workshop")
          .withPassword("workshop")
          .withDatabaseName("workshop");

  static {
    container.start();
  }

  @DynamicPropertySource
  static void exportPostgresProperties(DynamicPropertyRegistry registry) {
    container.addParameter("autoReconnect", "true");
    container.addParameter("useSSL", "false");
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
  }

}
