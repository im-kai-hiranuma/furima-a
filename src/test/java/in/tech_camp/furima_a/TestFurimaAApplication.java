package in.tech_camp.furima_a;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestFurimaAApplication {

    @Bean
    @ServiceConnection
    PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>("postgres:16-alpine")
                .withDatabaseName("furima_a_dev")
                .withUsername("postgres")
                .withPassword("password");
    }

    public static void main(String[] args) {
        SpringApplication.from(FurimaAApplication::main)
                .with(TestFurimaAApplication.class)
                .run(args);
    }
}