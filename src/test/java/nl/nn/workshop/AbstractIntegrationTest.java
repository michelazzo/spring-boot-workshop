package nl.nn.workshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public abstract class AbstractIntegrationTest extends AbstractTest {

  @Autowired
  protected MockMvc mvc;

  public static PostgreSQLContainer<?> container =
      new PostgreSQLContainer<>("postgres:13.4")
          .withExposedPorts(5432, 5432)
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
    registry.add("db_url", container::getJdbcUrl);
    registry.add("db_username", container::getUsername);
    registry.add("db_password", container::getPassword);
  }

}