package com.srienath.restapp.controller;

import com.srienath.restapp.model.*;
import com.srienath.restapp.service.AdminService;
import com.srienath.restapp.service.CustomerService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customerside")
@CrossOrigin(origins = "http://localhost:3001")
public class CustomerController {

    private final CustomerService customerService;

    private final AdminService adminService;
    private final JavaMailSender mailSender; // Add this to send emails

    public CustomerController(CustomerService customerService, AdminService adminService, JavaMailSender mailSender) {
        this.customerService = customerService;
        this.adminService = adminService;
        this.mailSender = mailSender;
    }

	@GetMapping("/locations")
    public ResponseEntity<List<String>> getDistinctLocations() {
        List<String> locations = adminService.getDistinctLocations();
        return ResponseEntity.ok(locations);
    }
    
	@PostMapping("/addcustomer")
    public ResponseEntity<String> addCustomer(@RequestBody Customer customer) {
        try {
            customerService.addCustomer(customer);
            sendRegistrationEmail(customer); // Send registration email
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failure");
        }
    }
	
	private void sendRegistrationEmail(Customer customer) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customer.getEmail());
        message.setSubject("Registration Successful");
        message.setText("Dear " + customer.getUsername() + ",\n\nThank you for registering with us.\n\nBest Regards,\nThe LPG Connect Team");
        mailSender.send(message);
    }

    @DeleteMapping("/deletecustomer/{customerID}")
    public String deleteCustomer(@PathVariable int customerID) {
        String msg = "";
        try {
            customerService.deleteCustomer(customerID);
            msg = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "Failure";
        }
        return msg;
    }

    @PostMapping("/updatecustomer")
    public String updateCustomer(@RequestBody Customer customer) {
        String msg = "";
        try {
            customerService.updateCustomer(customer);
            msg = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "Failure";
        }
        return msg;
    }

    @GetMapping("/customer/{customerID}")
    public Customer viewCustomer(@PathVariable int customerID) {
        return customerService.findCustomerById(customerID);
    }

    @GetMapping("/customer/email/{email}")
    public Customer viewCustomerByEmail(@PathVariable String email) {
        return customerService.findByEmail(email);
    }
    
    @GetMapping("/customers")
    public List<Customer> viewAllCustomers() {
        return customerService.findAllCustomers();
    }

    @GetMapping("/connections")
    public List<Connection> viewAllConnections() {
        return customerService.findAllConnections();
    }
    
    @GetMapping("/customer/{email}/{password}")
	public Customer loginCustomer(@PathVariable("email") String email,@PathVariable("password") String password) {
 
		return customerService.loginCustomer(email, password);
 
	}
	
}
