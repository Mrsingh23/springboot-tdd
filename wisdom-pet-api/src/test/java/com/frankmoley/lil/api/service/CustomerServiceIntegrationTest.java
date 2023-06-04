package com.frankmoley.lil.api.service;

import com.frankmoley.lil.api.web.model.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY )
public class CustomerServiceIntegrationTest {

    @Autowired
    CustomerService customerService;
//    public CustomerServiceIntegrationTest(CustomerService customerService){
//        this.customerService = customerService;
//    }

    @Test
    void getAllCustomer(){
        List<Customer> customerList = customerService.getAllCustomers();
        assertEquals(5,customerList.size());
    }
}
