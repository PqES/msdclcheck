package DAO;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import entities.Customer;



@Transactional
public interface CustomerDAO extends CrudRepository<Customer,String>{

	
	  public Customer save(Customer c);
	  public Customer findByCpf(String cpf);
	  public Customer findByName(String name);
	  //@Query
	  // public Customer findOne(Long one);
	  public void delete(Customer c);
	  public List<Customer> findAll();


	
}
