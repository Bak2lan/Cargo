package aist.cargo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CargoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CargoApplication.class, args);
	}

}
