package com.elena.application.MsSaleSpring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.elena.application.MsSaleSpring.intercomm.CustomerInterface;
import com.elena.application.MsSaleSpring.intercomm.ProductInterface;

@Controller
public class SaleController {

	@Autowired
	private CustomerInterface customer;

	@Autowired
	private ProductInterface product;

	@Autowired
	private RestTemplate restTemplate;


	@RequestMapping("/login")
	String login() throws Exception {
		return "index";
	}


	/**
	 * 
	 * Retorna os produtos atraves do product feign como uma string JSON
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getProducts", method = RequestMethod.GET)
	public @ResponseBody String getProducts() {
		String produtos = product.getAllProduct();
		return produtos;
	}

	
	@RequestMapping(value = { "/sale" }, method = RequestMethod.GET)
	ModelAndView index2( ) {
		ModelAndView modelAndView = new ModelAndView();		
		return modelAndView;
	}

	@RequestMapping("/")
	String index() throws Exception {
		return "sale";
	}

	@RequestMapping("*")
	String home() {
		return "falback";
	}

	@RequestMapping("/venda/getCustomerCpf/{cpf}")
	@ResponseBody
	public String getClient(@PathVariable("cpf") String cpf) {
		if (!cpf.isEmpty()) {
			String clientJson = customer.getCustomerCpf(cpf);
			if (clientJson.isEmpty()) {
				return null;
			} else {
				return clientJson;
			}
		} else {
			return null;
		}

	}

	@RequestMapping("/venda/getProduct/{id}")
	@ResponseBody
	public String getProduct(@PathVariable("id") String id) {
		return product.getProduct(id);
	}

	

	@RequestMapping("/venda/autenticacao/{user}/{senha}")
	@ResponseBody
	public String getAuthentication(@PathVariable("user") String user, @PathVariable("senha") String senha) {
		String response = restTemplate.postForObject(
				"http://MsAuthentication/api/authenticate?username=" + user + "&password=" + senha, null, String.class);
		return response;

	}
	@RequestMapping("/atualizaProducts/{qnt}/{id}")
	@ResponseBody
	public String productUpdate(@PathVariable("qnt") int qnt, @PathVariable("id") String id){
		String response = restTemplate.postForObject(
				"http://MsProduct/updateProduct/" + qnt + "/" + id, null, String.class);
		return response;
	}
}
