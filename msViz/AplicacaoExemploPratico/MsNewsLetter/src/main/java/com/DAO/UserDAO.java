package com.DAO;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.entities.User;

@Repository
@Transactional
public interface UserDAO extends CrudRepository<User, String> {

	public User save(User us);
	public List<User> findAll();
	public User findByCpf(String cpf);

}
