package com.frankmoley.lil.api.service;

import com.frankmoley.lil.api.data.entity.CustomerEntity;
import com.frankmoley.lil.api.data.repository.CustomerRepository;
import com.frankmoley.lil.api.web.controller.ControllerUtils;
import com.frankmoley.lil.api.web.error.NotFoundException;
import com.frankmoley.lil.api.web.model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

  @InjectMocks
  CustomerService customerService;

  @Mock
  CustomerRepository customerRepository;

  @Test
  void getAllCustomers(){
    Mockito.doReturn(getMockCustomers(2)).when(customerRepository).findAll();
    List<Customer> customers = this.customerService.getAllCustomers();
    assertEquals(2, customers.size());
  }

  @Test
  void getCustomer(){
    CustomerEntity entity = getMockCustomer();
    Optional<CustomerEntity> optional = Optional.of(entity);
    Mockito.doReturn(optional).when(customerRepository).findById(entity.getCustomerId());
    Customer customer = this.customerService.getCustomer(entity.getCustomerId().toString());
    assertEquals("firstName", customer.getFirstName());
  }

  @Test
  void getCustomer_notExists() {
    CustomerEntity entity = getMockCustomer();
    Optional<CustomerEntity> optional = Optional.empty();
    Mockito.doReturn(optional).when(customerRepository).findById(entity.getCustomerId());
    assertThrows(NotFoundException.class, ()->this.customerService.getCustomer(entity.getCustomerId().toString()), "exception not throw as expected");
  }

  @Test
  void findByEmailAddress(){
    CustomerEntity entity = getMockCustomer();
    Mockito.doReturn(entity).when(customerRepository).findByEmailAddress(entity.getEmailAddress());
    Customer customer = customerService.findByEmailAddress(entity.getEmailAddress());
    assertNotNull(customer);
    assertEquals("parker", customer.getLastName());
  }

  /*  public Customer addCustomer(Customer customer){
    Customer existing = this.findByEmailAddress(customer.getEmailAddress());
    if(existing != null){
      throw new ConflictException("customer with email already exists");
    }
    CustomerEntity entity = this.translateWebToDb(customer, true);
    entity = this.customerRepository.save(entity);
    return this.translateDbToWeb(entity);
  }*/

  @Test
  void addCustomer(){
    CustomerEntity entity = getMockCustomer();
    Customer customer = getMockCustomerData();

    when(customerRepository.findByEmailAddress(entity.getEmailAddress())).thenReturn(null);
    //any : It is commonly used when setting up mock behavior for methods that have arguments.
    when(customerRepository.save(any(CustomerEntity.class))).thenReturn(entity);
    Customer customerVerify = customerService.addCustomer(customer);
    assertEquals("email@test.com", customerVerify.getEmailAddress());
    assertNotNull(customer);

  }

  private Customer getMockCustomerData() {
    return (new Customer(
            "123232",
            "firstName", "parker",
            "email@test.com", "555-515-1234", "1234 Main Street"));
  }


  private CustomerEntity getMockCustomer() {
    return (new CustomerEntity(
            UUID.randomUUID(),
            "firstName", "parker",
            "email@test.com", "555-515-1234", "1234 Main Street"));
  }

  private Iterable<CustomerEntity> getMockCustomers(int size){
    List<CustomerEntity> customers = new ArrayList<>(size);
    for(int i=0;i<size;i++){
      customers.add(new CustomerEntity(UUID.randomUUID(), "firstName" + i, "lastName" + i,
          "email"+i+"@test.com", "555-515-1234", "1234 Main Street"));
    }
    return customers;
  }

}
