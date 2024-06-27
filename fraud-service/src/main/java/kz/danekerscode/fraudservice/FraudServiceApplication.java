package kz.danekerscode.fraudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class FraudServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(FraudServiceApplication.class, args);
    }

}

@RestController
@RequestMapping("/api/v1/fraud")
class FraudController {

    @GetMapping("{customerId}")
    boolean isFraud(@PathVariable Long customerId) {
        return customerId % 2 == 0;
    }

}
