package com.elena.application.MsCustomer.intercomm;



import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(value="MsProduct",  configuration = FeignConfiguration.class)
public interface CustomerProductInterface {
	
	@RequestMapping( method = RequestMethod.GET,value ="/product/getProduct/{id}")
	String getProductID(@PathVariable("id") String id);

	@RequestMapping( method = RequestMethod.GET,value ="/getProducts")
	String getAllProducts(@PathVariable("id") String id);

}