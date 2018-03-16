package com.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.entities.User;

@RestController
public class Autenticate {

	protected Logger logger = Logger.getLogger(Autenticate.class.getName());
	
	@Autowired
	private UserServices user;

	private Autenticate() {
	}

	@RequestMapping(value = "autenticate/{name}/{password}", method = RequestMethod.GET)
	@ResponseBody
	public boolean autenticate(@PathVariable String name, @PathVariable String password) throws Exception {

		User ufinded = user.findUserByName(name);
		if (ufinded == null) {
			logger.info("User not found!! ");
			return false;
		}else {
			String p = ufinded.getPassword();
			System.out.println("Password: " + p);
			if (ufinded.getPassword().equals(password)) {
				System.out.println("NOME: " + ufinded.getName());
				return true;
			} else {
				System.out.println("Password error! ");
				return false;
			}
		}
		
		

	}

	@RequestMapping(value = "addUser/{name}/{password}",method = RequestMethod.GET)
	@ResponseBody
	public boolean createUser(@PathVariable String name, @PathVariable String password) {
		try {
			User c = new User(name, password);
			user.saveUser(c);
			System.out.println("User:  " + c.getName());
			return true;

		} catch (Exception ex) {
			System.out.println("Error create client! " + ex.toString());
			return false;
		}
	}

	@RequestMapping(value = "/getUsers")
	@ResponseBody
	public List<String> getAllCustomers() {
		return user.findAllUsers();
	}
	
	@RequestMapping(value = "/getUser/{name}",method = RequestMethod.GET)
	@ResponseBody
	public User findUserByName(@PathVariable String name) throws Exception {
		User u = user.findUserByName(name);
		return u;
	}

}
