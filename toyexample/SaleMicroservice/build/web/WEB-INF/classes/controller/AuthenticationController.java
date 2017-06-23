/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Services.ServiceException;
import Services.Services;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationController implements Logic {

    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String user = req.getParameter("user");
        String password = req.getParameter("password");
        try {
            if (Services.serviceAuthentication(user, password)) {
                return "sale.jsp";
            } else {
                req.setAttribute("login_fail", true);
                return "index.jsp";
            }
        }catch(ServiceException | IOException ex){
            req.setAttribute("server_not_available", "Authentication server not available (" + ex.getClass().getName() + ": " + ex.getMessage() + ")");
            return "index.jsp";
        }
    }
}
