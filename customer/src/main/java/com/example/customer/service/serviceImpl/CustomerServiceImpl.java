package com.example.customer.service.serviceImpl;

import com.example.customer.exception.NoCustomersFoundException;
import com.example.customer.model.Customer;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    private final RestTemplate restTemplate;

    protected String serviceUrl = "http://PRODUCT-MICROSERVICE";

    @Override
    public List<?> getProductsForCustomerId(Long id){
        List<?> productList = restTemplate.getForObject(serviceUrl + "/id/" + id, List.class);

        if(productList == null)
            throw new IllegalStateException("no hay productos");
        else
            return productList;
    }

    @Override
    public Customer findById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer
                .flatMap(entity -> customer)
                .orElseThrow(() -> new NoCustomersFoundException("The customer does not exist"));
    }

    @Override
    public List<Customer> findByName(String name) {
        Optional<List<Customer>> customerList = customerRepository.findByName(name);
        return customerList
                .flatMap(list -> customerList)
                .orElseThrow(() -> new NoCustomersFoundException("No customers with that name found"));
    }

    @Override
    public void save(Customer customer) {
        if(exist(customer))
            throw new IllegalStateException("The customer already exists");
        else
            customerRepository.save(customer);
    }

    @Override
    public void update(Long id, Customer customerUpdate) {
        Optional<Customer> wantedCustomer = customerRepository.findById(id);
        customerUpdate.setId(id);
        if(wantedCustomer.isEmpty())
            throw new NoCustomersFoundException("The customer you want to update does not exist");
        if(!sameValues(wantedCustomer.get(),customerUpdate)){
            if(existAnotherCustomerWithThoseValues(customerUpdate)) {
                throw new IllegalStateException("The customer was not updated because another one already has those values");
            }
            customerRepository.save(customerUpdate);
        }
    }

    @Override
    public void delete(Long id) {
        Optional<Customer> wantedCustomer = customerRepository.findById(id);
        if (wantedCustomer.isPresent())
            customerRepository.deleteById(id);
        else
            throw new NoCustomersFoundException("The customer you want to delete does not exist");

    }

    private Boolean exist(Customer customer){
        return customerRepository
                .findByNameAndPhone(customer.getName(), customer.getPhone())
                .isPresent();
    }

    private Boolean sameValues(Customer wantedCustomer, Customer customerUpdate){
        return wantedCustomer.getName().equals(customerUpdate.getName()) &&
                wantedCustomer.getPhone().equals(customerUpdate.getPhone());
    }

    private Boolean existAnotherCustomerWithThoseValues(Customer customerUpdate){
        return customerRepository
                .findByIdNotAndNameAndPhone(customerUpdate.getId(),
                        customerUpdate.getName(),
                        customerUpdate.getPhone()).isPresent();
    }
}
