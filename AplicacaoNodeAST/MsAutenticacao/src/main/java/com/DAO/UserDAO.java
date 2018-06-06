package com.DAO;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.entities.User;


@Qualifier("user")
@Transactional
@Repository
public interface UserDAO extends CrudRepository<User,String>{
	  
	  public User save(User c);
	  public User findByName(String name);
	  public List<User> findAll();

}
