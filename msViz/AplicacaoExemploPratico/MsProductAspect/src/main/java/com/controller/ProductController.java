package com.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.DAO.ProductDAO;
import com.entities.Product;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

@RestController
public class ProductController {

	@Autowired
	private ProductService productService;

	

	@RequestMapping("/index")
	@ResponseBody
	public String index() { 
		return "index";
	}

	public ProductController() {

	}

	@RequestMapping("*")
	@ResponseBody
	public String fallbackMethod() {
		return "fallback method";
	}

	@RequestMapping(value = "add/{name}/{description}/{number}/{value}", method = RequestMethod.GET)
	@ResponseBody
	public String addProduct(@PathVariable String name, @PathVariable String description, @PathVariable int number,
			@PathVariable double value) {
		try {
			Product p = new Product(name, description, number, value);
			productService.saveProduct(p);

		} catch (Exception ex) {
			return "Error creating the user: " + ex.toString();
		}
		return "Success! ";

	}

	/**
	 * Mudei o mapping, pois o mapping original (/product/getProducts/) não
	 * estava funcionando
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "getProducts") // , produces = "application/json")
	@ResponseBody
	public List<Product> getProducts() throws IOException {
		
		return productService.getAllProducts();

	}
	@RequestMapping(value = "getAllProducts") // , produces = "application/json")
	@ResponseBody
	public String getAllProducts() throws IOException {
		String productJson= writeListToJsonArray(productService.getAllProducts());
		return productJson;

	}

	@RequestMapping(value = "/updateProduct/{qnt}/{id}", method = RequestMethod.POST)
	@ResponseBody
	public String updateStock(@RequestParam(value = "qnt") String[] id, @RequestParam(value = "id") String[] qt) {
		int estoque[] = new int[id.length];
		Product products[] = new Product[id.length];
		for (int i = 0; i < estoque.length; i++) {
			products[i] = productService.findOneProduct(id[i]);
			estoque[i] = products[i].getNumber() - Integer.parseInt(qt[i]);
			if (estoque[i] < 0) {
				return "fail";
			}
		}
		for (int i = 0; i < estoque.length; i++) {
			products[i].setNumber(estoque[i]);
			productService.saveProduct(products[i]);
		}
		return "success";
	}

	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ResponseBody
	public String publisNewsLetter() {

		String getCustomer = "http://localhost:5001/?method=Publish";
		try {
			URL url = new URL(getCustomer);
			HttpURLConnection requestCustomer = (HttpURLConnection) url.openConnection();
			requestCustomer.connect();

			JsonParser jp = new JsonParser();
			JsonElement getCustomerResult = jp.parse(new InputStreamReader((InputStream) requestCustomer.getContent()));
			return getCustomerResult.toString();

		} catch (Exception e) {
			System.out.println("" + e.getMessage());
			e.printStackTrace();
			return "Erro:" + e.getMessage();
		}

	}

	@RequestMapping(value = "/product/getProduct/{id}")
	@ResponseBody
	public String getProductByID(@PathVariable("id") String id) {
		Product productName;
		productName = productService.findProductByID(id);
		return productName.getName();
	}

	/**
	 * Converte uma lista em um JSON (fonte:
	 * https://stackoverflow.com/questions/13514570/jackson-best-way-writes-a-java-list-to-a-json-array)
	 * 
	 * @param lista
	 * @return lista convertida para JSON
	 * @throws IOException
	 */
	public String writeListToJsonArray(List<Product> lista) throws IOException {

		final StringWriter sw = new StringWriter();
		final ObjectMapper mapper = new ObjectMapper();
		mapper.writeValue(sw, lista);

		sw.close();
		return sw.toString();
	}

}
