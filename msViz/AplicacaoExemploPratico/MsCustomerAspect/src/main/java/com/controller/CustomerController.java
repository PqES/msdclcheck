package com.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.entities.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.intercomm.CustomerProductInterface;

@RestController
public class CustomerController {

	@Autowired
	private CustomerService customer;

	@Autowired
	private CustomerProductInterface product;

	@Autowired
	private RestTemplate restTemplate;
	private CustomerController() {
	}

	protected Logger logger = Logger.getLogger(CustomerController.class.getName());

	@RequestMapping("/index")
	@ResponseBody
	public String index() {
		return "index";
	}

	@RequestMapping("*")
	public String fallbackMethod() {
		return "fallback method";
	}

	@RequestMapping(value = "/customer/add/{name}/{cpf}/{email}", method = RequestMethod.GET)
	@ResponseBody
	public String addCustomer(@PathVariable String name, @PathVariable String cpf, @PathVariable String email) {

		try {
			Customer c = new Customer(name, cpf, email);
			System.out.println("Cliente:  " + c.getName());
			customer.saveCustomer(c);
			return "Success! ";

		} catch (Exception ex) {
			return "Erro ao criar Cliente!: " + ex.toString();
		}

	}

	
	@RequestMapping("/customer/getCustomers")
	@ResponseBody
	public String getAllCustomers2() throws IOException {
		String products = writeListToJsonArray(customer.findAllCustomer());
		return products;
	}


	@RequestMapping("/customer/getCustomerCpf/{cpf}")
	@ResponseBody
	public Customer getClientePorCpf(@PathVariable String cpf) {
		Customer c = customer.findCustomerByCpf(cpf);
		return c;
	}

	@RequestMapping("/customer/products/{id}")
	@ResponseBody
	private String productID(@PathVariable("id") String id) {
		try {
			return product.getProductID(id);// id);

		} catch (Exception ex) {
			return "Produto nao encontrado: " + ex.toString();
		}

	}
	
	@RequestMapping("getAllProducts")
	@ResponseBody
	private String getAllProducts() {
		try {
			String response = restTemplate.postForObject(
					"http://MsProduct/getAllProducts", null, String.class);
			return response;

		} catch (Exception ex) {
			return "Produto nao encontrado: " + ex.toString();
		}

	}
	

	@RequestMapping("/customer/delete/{cpf}")
	@ResponseBody
	public String delete(@PathVariable String cpf) {
		try {
			customer.deleteCustomer(cpf);
			return "Cliente deletado com sucesso!";

		} catch (Exception ex) {
			return "Erro ao deletar o cliente " + ex.toString();
		}
		
	}
	@RequestMapping("/getCep")
	@ResponseBody
	public String getCep(){
		
		String getCustomer = "https://viacep.com.br/ws/37200000/json/"; 
        try{
            URL url = new URL(getCustomer);
            HttpURLConnection requestCustomer = (HttpURLConnection) url.openConnection();
            System.err.println("Req: " + requestCustomer.toString());
            requestCustomer.connect();             
            JsonParser jp = new JsonParser();          
            JsonElement getCustomerResult = jp.parse(new InputStreamReader((InputStream) requestCustomer.getContent()));
            
            return getCustomerResult.toString();            
            
        }catch (Exception e){
            System.out.println("" + e.getMessage());
            e.printStackTrace();
            return "Erro:" + e.getMessage();
        }       		
	}
	public String writeListToJsonArray(List<Customer> lista) throws IOException {

		final StringWriter sw = new StringWriter();
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(sw, lista);

		sw.close();
		return sw.toString();
	}

	

}
