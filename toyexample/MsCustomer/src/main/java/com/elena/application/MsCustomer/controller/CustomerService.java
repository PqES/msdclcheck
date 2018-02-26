package com.elena.application.MsCustomer.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elena.application.MsCustomer.dao.CustomerDAO;
import com.elena.application.MsCustomer.entities.Customer;

@Service
public class CustomerService {
	
	@Autowired
	private CustomerDAO customer;
	
	
	
	protected Logger logger = Logger.getLogger(CustomerService.class.getName());

	public void saveCustomer(Customer c) {
		try {
			System.out.println("cliente que chegou? "+c.getName());
			customer.save(c);
		
			logger.info("Cliente criado com Sucesso!");

		} catch (ArrayIndexOutOfBoundsException e) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

	}

	public Customer findCustomerByCpf(String cpf) {

		try {
			Customer c = customer.findByCpf(cpf);
			logger.info("Cliente criado com Sucesso!");
			return c;

		} catch (ArrayIndexOutOfBoundsException e) {

			throw new UnsupportedOperationException("Not supported yet.");

		}
	}

	public Customer findCustomerByName(String name) {
		try {
			Customer c = customer.findByName(name);
			logger.info("Cliente criado com Sucesso!");
			return c;

		} catch (ArrayIndexOutOfBoundsException e) {

			throw new UnsupportedOperationException("Not supported yet.");

		}

	}

	public String deleteCustomer(String cpf) {
		Customer c = customer.findByCpf(cpf);
		try {
			customer.delete(c);
		} catch (Exception ex) {
			return "Error deleting the user:" + ex.toString();
		}
		return "Customer deleted!";
	}

	public List<String> findAllCustomer() {
		List<Customer> customers = new ArrayList<>();		
	//	customer.findAll().forEach(customers::add);
		return customer.findAll().stream().map(a -> a.getName()).collect(Collectors.toList());

	}

	public String getProductsInCustomer(String id) {
		try {
			return "";// product.getProductID(id);
		

		} catch (ArrayIndexOutOfBoundsException e) {

			throw new UnsupportedOperationException("Not supported yet.");

		}

	}

}
