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


public class SaleController implements Logic {

    
    
    @Override
    public String executa(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ids[] = req.getParameterValues("product_id");
        String qtds[] = req.getParameterValues("product_qtd");
        if(ids != null && qtds != null){
            try{
                boolean success = Services.serviceUpdateStock(ids, qtds);
                req.setAttribute("sale_submitted", true);
                req.setAttribute("success", success);
            } catch (ServiceException | IOException ex) {
                req.setAttribute("server_not_available", "Product server not available (" + ex.getClass().getName() + ": " + ex.getMessage() + ")");
            }
        }else{
            req.setAttribute("sale_submitted", true);
            req.setAttribute("success", false);
        }
        return "sale.jsp";
    }
    
}
