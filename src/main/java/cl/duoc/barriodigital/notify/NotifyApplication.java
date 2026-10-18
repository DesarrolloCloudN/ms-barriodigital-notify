package cl.duoc.barriodigital.notify;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Microservicio consumidor RabbitMQ de BarrioDigital: no tiene base de datos propia.
@SpringBootApplication
public class NotifyApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotifyApplication.class, args);
	}

}
