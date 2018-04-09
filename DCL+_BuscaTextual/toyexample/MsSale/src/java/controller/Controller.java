package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/service")
public class Controller extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String parametro = request.getParameter("logic");
        String nomeDaClasse = "controller." + parametro;
        try {
            Class<?> classe;
            classe = Class.forName(nomeDaClasse);
            Logic logica = (Logic) classe.newInstance();
            String pagina = logica.executa(request, response);
            request.getRequestDispatcher(pagina).forward(request, response);
        } catch (ClassNotFoundException ex) {
            for (String str : request.getParameterMap().keySet()) {
                System.out.println("Param: " + str);
            }
            throw new ServletException("A lógica de negócio causou uma exceção > ClassNotFoundException" + parametro, ex);
        } catch (InstantiationException ex) {
            throw new ServletException("A lógica de negócio causou uma exceção > InstantiationException" + parametro, ex);
        } catch (IllegalAccessException ex) {
            throw new ServletException("A lógica de negócio causou uma exceção > IllegalAccessException" + parametro, ex);
        } catch (Exception ex) {
            throw new ServletException("A lógica de negócio causou uma exceção > Exception" + parametro, ex);
        }
    }
}
