package com.elena.application.MsSaleSpring.intercomm;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="MsCustomer", configuration = FeignConfiguration.class)
public interface CustomerInterface {
	
	@RequestMapping( method = RequestMethod.GET, value="/getCustomerCpf/{cpf}")
	public String getCustomerCpf(@PathVariable("cpf") String cpf);
	
	@RequestMapping( method = RequestMethod.GET, value="/getCustomers")
	public String getAllCustomers();
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
