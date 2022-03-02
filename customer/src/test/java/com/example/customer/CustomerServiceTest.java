package com.example.customer;

import com.example.customer.exception.NoCustomersFoundException;
import com.example.customer.model.Customer;
import com.example.customer.repository.CustomerRepository;
import com.example.customer.service.serviceImpl.CustomerServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    CustomerRepository repository;

    @InjectMocks
    CustomerServiceImpl service;

    private Customer customer1;
    private Customer customer2;
    private Customer customer3;
    private Customer customerUpdate;
    private List<Customer> listCustomersByName;

    @BeforeEach
    void setUp() {

        customer1 = new Customer();
        customer1.setId(1L);
        customer1.setName("Pablo");
        customer1.setPhone("11-4422-3355");

        customer2 = new Customer();
        customer2.setId(2L);
        customer2.setName("Pablo");
        customer2.setPhone("11-3322-2325");

        customer3 = new Customer();
        customer3.setId(3L);
        customer3.setName("Pablo");
        customer3.setPhone("11-4422-3355");

        customerUpdate = new Customer();
        customerUpdate.setName("Alejandra");
        customerUpdate.setPhone("11-7799-3355");

        listCustomersByName = new ArrayList<>();
        listCustomersByName.add(customer1);
        listCustomersByName.add(customer2);
    }

    @Test
    void itShouldBringMeTheCustomerById() {
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findById(customer1.getId())).thenReturn(Optional.of(customer1));

        Customer actual = service.findById(customer1.getId());

        log.info("Result: " + actual);
        assertThat(actual.getId()).isEqualTo(customer1.getId());
        assertThat(actual.getName()).isEqualTo(customer1.getName());

    }

    @Test
    void willThrowWhenTheIdOfTheCustomerDoesNotExist() {
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findById(customer1.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(customer1.getId()))
                .isInstanceOf(NoCustomersFoundException.class)
                .hasMessageContaining("The customer does not exist");

    }

    @Test
    void itShouldBringMeAllTheCustomersByName() {
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findByName("Pablo")).thenReturn(Optional.of(listCustomersByName));

        List<Customer> actual = service.findByName("Pablo");

        log.info("Result: " + actual);
        assertThat(actual.get(0).getId()).isEqualTo(customer1.getId());
        assertThat(actual.get(1).getId()).isEqualTo(customer2.getId());
        assertThat(actual.get(0).getName()).isEqualTo("Pablo");
    }

    @Test
    void willThrowWhenThereIsNoCustomersWithThatName() {
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findByName(customer1.getName())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByName(customer1.getName()))
                .isInstanceOf(NoCustomersFoundException.class)
                .hasMessageContaining("No customers with that name found");
    }

    @Test
    void itShouldCreateACustomerSuccessfully() {
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        service.save(customer1);

        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(repository, times(1)).save(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer).isEqualTo(customer1);
    }

    @Test
    void willThrowWhenTheCustomerAlreadyExist() {
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findByNameAndPhone("Pablo", "11-4422-3355"))
                .thenReturn(Optional.of(customer1));

        assertThatThrownBy(() -> service.save(customer3))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The customer already exists");
    }

    @Test
    void itShouldUpdateACustomerSuccessfully() {
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(repository.findByIdNotAndNameAndPhone(customer1.getId(),
                customerUpdate.getName(),customerUpdate.getPhone()))
                .thenReturn(Optional.empty());

        service.update(customer1.getId(),customerUpdate);

        verify(repository, times(1)).findById(customer1.getId());
        verify(repository, times(1))
                .findByIdNotAndNameAndPhone(customer1.getId(),
                        customerUpdate.getName(),customerUpdate.getPhone());
        verify(repository, times(1)).save(customerUpdate);

        assertThat(customerUpdate.getId()).isEqualTo(customer1.getId());
        assertThat(customerUpdate.getName()).isEqualTo("Alejandra");
        assertThat(customerUpdate.getPhone()).isEqualTo("11-7799-3355");

    }

    @Test
    void willThrowWhenTheCustomerWantToUpdateDoesNotExist(){
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findById(customer1.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(customer1.getId(),customerUpdate))
                .isInstanceOf(NoCustomersFoundException.class)
                .hasMessageContaining("The customer you want to update does not exist");
    }

    @Test
    void willThrowWhenThereIsAlreadyACustomerWithThoseValues(){
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findById(customer1.getId())).thenReturn(Optional.of(customer1));
        when(repository.findByIdNotAndNameAndPhone(1L,customerUpdate.getName(), customerUpdate.getPhone()))
                .thenReturn(Optional.of(customer2));

        assertThatThrownBy(() -> service.update(customer1.getId(),customerUpdate))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("The customer was not updated because another one already has those values");
    }

    @Test
    void itShouldDeleteACustomerSuccessfully(){
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findById(customer1.getId())).thenReturn(Optional.of(customer1));

        service.delete(customer1.getId());

        verify(repository,times(1)).deleteById(customer1.getId());
    }

    @Test
    void willThrowWhenTheCustomerWantToDeleteDoesNotExist(){
        log.info("Test name: " + Thread.currentThread().getStackTrace()[1].getMethodName());

        when(repository.findById(customer1.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.delete(customer1.getId()))
                .isInstanceOf(NoCustomersFoundException.class)
                .hasMessageContaining("The customer you want to delete does not exist");
    }

}
