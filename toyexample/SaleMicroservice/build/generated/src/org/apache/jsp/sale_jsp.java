package org.apache.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import client.Client;

public final class sale_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private org.apache.jasper.runtime.TagHandlerPool _jspx_tagPool_c_if_test;

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _jspx_tagPool_c_if_test = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
  }

  public void _jspDestroy() {
    _jspx_tagPool_c_if_test.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html;charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html>\n");
      out.write("    <head>\n");
      out.write("        <title>TODO supply a title</title>\n");
      out.write("        <meta charset=\"UTF-8\">\n");
      out.write("        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
      out.write("        <link rel=\"stylesheet\" type=\"text/css\" href=\"css/bootstrap.min.css\" />\n");
      out.write("        <script src=\"js/jquery-3.2.1.min.js\" /></script>\n");
      out.write("        <script src=\"js/bootstrap.min.js\" /></script>\n");
      out.write("        <script src=\"js/angular.min.js\" /></script>\n");
      out.write("        <script>\n");
      out.write("            var saleApp = angular.module('SaleApp', []);\n");
      out.write("            saleApp.controller('SaleController', ['$scope', function ($scope) {\n");
      out.write("                $scope.products = [{id:1, name:\"product1\", qtd:1}, {id:2, name:\"product2\", qtd:2}];\n");
      out.write("                $scope.cart = {};\n");
      out.write("                $scope.addProduct = function(){\n");
      out.write("                    $scope.cart[$scope.productSelected.id] = $scope.productSelected;\n");
      out.write("                }\n");
      out.write("                $scope.removeProduct = function(id){\n");
      out.write("                    delete $scope.cart[id];\n");
      out.write("                }\n");
      out.write("            }]);\n");
      out.write("        </script>\n");
      out.write("    </head>\n");
      out.write("    <style>\n");
      out.write("        .page-header{\n");
      out.write("            background-color: #ddd;\n");
      out.write("            margin-top: 0;\n");
      out.write("        }\n");
      out.write("    </style>\n");
      out.write("    <body>\n");
      out.write("        <div class=\"page-header\">\n");
      out.write("            <h1>Microservices - Toy Example</h1>\n");
      out.write("        </div>\n");
      out.write("        <div class=\"container\" ng-app=\"SaleApp\" ng-controller=\"SaleController\">\n");
      out.write("            ");
      if (_jspx_meth_c_if_0(_jspx_page_context))
        return;
      out.write("\n");
      out.write("            ");
      if (_jspx_meth_c_if_1(_jspx_page_context))
        return;
      out.write("\n");
      out.write("            <form action=\"service\" method=\"post\" class=\"form-inline\">\n");
      out.write("                <div class=\"form-group\">\n");
      out.write("                    <label for=\"cpf_client\">Client CPF</label>\n");
      out.write("                    <input type=\"text\" class=\"form-control\" name=\"cpf\" id=\"cpf_client\"/>\n");
      out.write("                </div>\n");
      out.write("                <input type=\"hidden\" value=\"ClientController\" name=\"logic\" />\n");
      out.write("                <input type=\"submit\" class=\"btn btn-default\" value=\"send\" />\n");
      out.write("            </form>\n");
      out.write("            ");
      if (_jspx_meth_c_if_2(_jspx_page_context))
        return;
      out.write("\n");
      out.write("            ");
      if (_jspx_meth_c_if_3(_jspx_page_context))
        return;
      out.write("\n");
      out.write("        </div>\n");
      out.write("    </body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_c_if_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_if_0 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _jspx_tagPool_c_if_test.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_if_0.setPageContext(_jspx_page_context);
    _jspx_th_c_if_0.setParent(null);
    _jspx_th_c_if_0.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${sale_submitted && success}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null)).booleanValue());
    int _jspx_eval_c_if_0 = _jspx_th_c_if_0.doStartTag();
    if (_jspx_eval_c_if_0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("                A venda foi efetuada com sucesso\n");
        out.write("            ");
        int evalDoAfterBody = _jspx_th_c_if_0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_if_0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_c_if_test.reuse(_jspx_th_c_if_0);
      return true;
    }
    _jspx_tagPool_c_if_test.reuse(_jspx_th_c_if_0);
    return false;
  }

  private boolean _jspx_meth_c_if_1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_if_1 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _jspx_tagPool_c_if_test.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_if_1.setPageContext(_jspx_page_context);
    _jspx_th_c_if_1.setParent(null);
    _jspx_th_c_if_1.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${sale_submitted && !success}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null)).booleanValue());
    int _jspx_eval_c_if_1 = _jspx_th_c_if_1.doStartTag();
    if (_jspx_eval_c_if_1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("                Não foi possível realizar a venda\n");
        out.write("            ");
        int evalDoAfterBody = _jspx_th_c_if_1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_if_1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_c_if_test.reuse(_jspx_th_c_if_1);
      return true;
    }
    _jspx_tagPool_c_if_test.reuse(_jspx_th_c_if_1);
    return false;
  }

  private boolean _jspx_meth_c_if_2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_if_2 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _jspx_tagPool_c_if_test.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_if_2.setPageContext(_jspx_page_context);
    _jspx_th_c_if_2.setParent(null);
    _jspx_th_c_if_2.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${client_search && client != null}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null)).booleanValue());
    int _jspx_eval_c_if_2 = _jspx_th_c_if_2.doStartTag();
    if (_jspx_eval_c_if_2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("                <hr>\n");
        out.write("                <h3> Client: ");
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${client.name}", java.lang.String.class, (PageContext)_jspx_page_context, null));
        out.write(' ');
        out.write('(');
        out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${client.CPF}", java.lang.String.class, (PageContext)_jspx_page_context, null));
        out.write(") </h3>\n");
        out.write("\n");
        out.write("                <div class=\"form-inline\">\n");
        out.write("                    <div class=\"form-group\">\n");
        out.write("                        <label for=\"product\">Product</label>\n");
        out.write("                        <select id=\"product\" class=\"form-control\" ng-model=\"productSelected\" ng-options=\"p.name for p in products\" >\n");
        out.write("                        </select>\n");
        out.write("                    </div>\n");
        out.write("                    <div class=\"form-group\">\n");
        out.write("                        <label for=\"quantity\">quantity</label>\n");
        out.write("                        <input type=\"number\" class=\"form-control\" id=\"quantity\"/>\n");
        out.write("                    </div>\n");
        out.write("                    <button type=\"button\" class=\"btn btn-default\" ng-click=\"addProduct()\">Add in Sale</button>\n");
        out.write("                </div>\n");
        out.write("                <form action=\"service\" method=\"post\">\n");
        out.write("                    <table class=\"table\">\n");
        out.write("                        <thead>\n");
        out.write("                            <th>ID</th>\n");
        out.write("                            <th>Product</th>\n");
        out.write("                            <th>Quantity</th>\n");
        out.write("                            <th>Remove</th>\n");
        out.write("                        </thead>\n");
        out.write("                        <tbody>\n");
        out.write("                            <tr ng-repeat=\"(id, product) in cart\">\n");
        out.write("                                <td>{{id}}</td>\n");
        out.write("                                <td>{{product.name}}</td>\n");
        out.write("                                <td>{{product.qtd}}</td>\n");
        out.write("                                <td><button type=\"button\" class=\"btn btn-default\" ng-click=\"removeProduct(id)\">remove</button></td>\n");
        out.write("                                <input type=\"hidden\" name=\"product_id\" value=\"{{id}}\" />\n");
        out.write("                                <input type=\"hidden\" name=\"product_qtd\" value=\"{{product.qtd}}\" />\n");
        out.write("                                <input type=\"hidden\" name=\"logic\" value=\"SaleController\" />\n");
        out.write("                            </tr>\n");
        out.write("                        </tbody>\n");
        out.write("                    </table>\n");
        out.write("                    <input type=\"submit\" value=\"cadastrar venda\" class=\"btn btn-default\" />\n");
        out.write("                </form>\n");
        out.write("            ");
        int evalDoAfterBody = _jspx_th_c_if_2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_if_2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_c_if_test.reuse(_jspx_th_c_if_2);
      return true;
    }
    _jspx_tagPool_c_if_test.reuse(_jspx_th_c_if_2);
    return false;
  }

  private boolean _jspx_meth_c_if_3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  c:if
    org.apache.taglibs.standard.tag.rt.core.IfTag _jspx_th_c_if_3 = (org.apache.taglibs.standard.tag.rt.core.IfTag) _jspx_tagPool_c_if_test.get(org.apache.taglibs.standard.tag.rt.core.IfTag.class);
    _jspx_th_c_if_3.setPageContext(_jspx_page_context);
    _jspx_th_c_if_3.setParent(null);
    _jspx_th_c_if_3.setTest(((java.lang.Boolean) org.apache.jasper.runtime.PageContextImpl.evaluateExpression("${client_search && client == null}", java.lang.Boolean.class, (PageContext)_jspx_page_context, null)).booleanValue());
    int _jspx_eval_c_if_3 = _jspx_th_c_if_3.doStartTag();
    if (_jspx_eval_c_if_3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      do {
        out.write("\n");
        out.write("                Client não encontrado\n");
        out.write("            ");
        int evalDoAfterBody = _jspx_th_c_if_3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
    }
    if (_jspx_th_c_if_3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _jspx_tagPool_c_if_test.reuse(_jspx_th_c_if_3);
      return true;
    }
    _jspx_tagPool_c_if_test.reuse(_jspx_th_c_if_3);
    return false;
  }
}
