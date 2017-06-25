package DAO;


import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

import entities.Product;


@Transactional
public interface ProductDAO extends CrudRepository<Product,String>{
	
	public List<Product> findAll();	
	
	
}
