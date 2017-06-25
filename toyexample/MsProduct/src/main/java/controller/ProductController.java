package controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import DAO.ProductDAO;
import entities.Product;

@Controller
public class ProductController {
	@Autowired
	private ProductDAO productDAO;

	@RequestMapping("/index")
	@ResponseBody
	public String index() {
		return "index";
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
		String idProduct = "";
		try {

			Product p = new Product(name, description, number, value);
			productDAO.save(p);
			idProduct = String.valueOf(p.getId());
		} catch (Exception ex) {
			return "Error: " + ex.toString();
		}
		return "Sucess";

	}

	@RequestMapping("/getProducts")
	@ResponseBody
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList();
		products = productDAO.findAll().stream().filter(p -> p.getNumber() > 0).collect(Collectors.toList());
		return products;
	}

	@RequestMapping(value = "/venda", method = RequestMethod.POST)
	@ResponseBody
	public String updateStock(@RequestParam(value = "id") String[] id, @RequestParam(value = "qt") String[] qt) {
		int estoque[] = new int[id.length];
		Product products[] = new Product[id.length];
		for (int i = 0; i < estoque.length; i++) {
			products[i] = productDAO.findOne(id[i]);
			estoque[i] = products[i].getNumber() - Integer.parseInt(qt[i]);
			if (estoque[i] < 0) {
				return "fail";
			}
		}
		for (int i = 0; i < estoque.length; i++) {
			products[i].setNumber(estoque[i]);
			productDAO.save(products[i]);
		}
		return "sucess";
	}

	@RequestMapping("/getCustomers")
	@ResponseBody
	public String getCustomer() {

		String getCustomer = "http://localhost:9000/getCustomer";
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
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
	@ResponseBody
	public String publisNewsLetter() {

		String getCustomer = "http://localhost:5000/?method=Publish";
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

}
