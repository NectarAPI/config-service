package ke.co.nectar.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ke.co.nectar.config")
@ConfigurationPropertiesScan("ke.co.nectar.config.configurations")
@EnableJpaRepositories(basePackages="ke.co.nectar.config.repository")
public class NectarConfigServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NectarConfigServiceApplication.class, args);
	}

}
