package com.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@NotNull
	private String name;
	
	@NotNull
	private String password;

	public User(String name, String password){
		this.name = name;
		this.password = password;
	}

	public User(int id, String name, String password){
		this.name = name;
		this.password = password;
	}
	public User() {}
	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}

	public String toString() {
		return String.format("Cliente[ name='%s']: ",
				name);
	}

}
