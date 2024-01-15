package cl.duoc.mgaray.guardalotwo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class GuardaloTwoBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(GuardaloTwoBackendApplication.class, args);
  }

}
