/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Services.ServiceException;
import Services.Services;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ClientController implements Logic {

    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String clientJson;
        try {
            String cpf = req.getParameter("cpf");
            clientJson = Services.serviceGetClient(cpf);
        } catch (ServiceException | IOException ex) {
            req.setAttribute("server_not_available", "Client server not available (" + ex.getClass().getName() + ": " + ex.getMessage() + ")");
            return "sale.jsp";
        }
        String productsJson = "[]";
        try {
            productsJson = Services.serviceGetProducts();
        } catch (ServiceException | IOException ex) {
            req.setAttribute("server_not_available", "Product server not available (" + ex.getClass().getName() + ": " + ex.getMessage() + ")");
            return "sale.jsp";
        }
        req.setAttribute("jsonProducts", productsJson);
        req.setAttribute("client", clientJson);
        req.setAttribute("client_search", true);
        return "sale.jsp";
    }
}
