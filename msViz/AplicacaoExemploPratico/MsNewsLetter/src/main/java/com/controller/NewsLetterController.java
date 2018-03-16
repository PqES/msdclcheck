package com.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.entities.User;


@RestController
public class NewsLetterController {

	protected Email email;
	
	@RequestMapping(value = "publish/{email}", method = RequestMethod.GET)
	@ResponseBody
	public String email(@PathVariable String name) {
		User user = new User("1", name);
		try {
			email.sendEmail(user);
			System.out.println("Email Send success!");
			
		}catch (Exception e) {
			System.out.println("Error!! " + e.getMessage());
		}
		return null;
		
	}
}
