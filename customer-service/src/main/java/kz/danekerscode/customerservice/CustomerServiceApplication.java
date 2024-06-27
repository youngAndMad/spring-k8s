package kz.danekerscode.customerservice;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class CustomerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
    }
}

@Entity
class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;

    public Customer() {
    }

    public Customer(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

interface CustomerService {
    Customer createCustomer(String email);

    Customer getCustomer(Long id);

    void deleteCustomer(Long id);

    List<Customer> getCustomers();
}

@Service
class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(String email) {
        return customerRepository.save(new Customer(null, email));
    }

    @Override
    public Customer getCustomer(Long id) {
        return customerRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }
}

interface CustomerRepository extends JpaRepository<Customer, Long> {
}

record CreateCustomerRequest(String email) {
}

@FeignClient(name = "fraud-client", path = "/api/v1/fraud")
interface FraudClient {
    @GetMapping("/{customerId}")
    boolean isFraud(@PathVariable Long customerId);
}

@RestController
@RequestMapping("/api/v1/customers")
class CustomerController {
    private final CustomerService customerService;
    private final FraudClient fraudClient;

    CustomerController(CustomerService customerService, FraudClient fraudClient) {
        this.customerService = customerService;
        this.fraudClient = fraudClient;
    }

    @GetMapping("/{customerId}/fraud")
    boolean isFraud(@PathVariable Long customerId) {
        return fraudClient.isFraud(customerId);
    }

    @GetMapping
    List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @PostMapping
    Customer createCustomer(@RequestBody CreateCustomerRequest request) {
        return customerService.createCustomer(request.email());
    }

    @GetMapping("/{id}")
    Customer getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }
}
