package at.fhv;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class Application {

    public static void main(String... args) {
        System.out.println("Application starting");
        SpringApplication.run(Application.class, args);
    }
}
