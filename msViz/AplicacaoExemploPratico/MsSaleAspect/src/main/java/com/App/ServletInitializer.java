package com.App;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

import com.App.SaleApp;



public class ServletInitializer extends SpringBootServletInitializer{
	@Override
	
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(SaleApp.class);
	}
}
