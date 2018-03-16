package com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.DAO.UserDAO;
import com.entities.User;

@Service
public class UserServices {

	public UserServices() {
	}

	@Autowired
	public UserDAO user;

	protected Logger logger = Logger.getLogger(UserServices.class.getName());

	public void saveUser(User u) {
		try {
			user.save(u);
			logger.info("User " + u.getName() + "create succes!");

		} catch (ArrayIndexOutOfBoundsException e) {
			throw new UnsupportedOperationException("Not supported yet.");
		}

	}

	public User findUserByName(String name) throws Exception {
		try {
			User u = user.findByName(name);
			logger.info("User " + u.getName() + "create succes!");
			return u;

		} catch (Exception e) {

			throw new Exception("User not found! ");

		}

	}

	public List<String> findAllUsers() {
		List<User> users = new ArrayList<>();
		return user.findAll().stream().map(a -> a.getName()).collect(Collectors.toList());

	}
}
