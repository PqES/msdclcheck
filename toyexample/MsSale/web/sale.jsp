<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>MsSale</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
        <script src="js/jquery-3.2.1.min.js" /></script>
        <script src="js/bootstrap.min.js" /></script>
        <script src="js/angular.min.js" /></script>
        <script>
            window.products = [];
            window.client = null;
            <c:if test="${jsonProducts != null}">
                window.products = $.parseJSON('${jsonProducts}');
                if(!(window.products instanceof Array)){
                    window.products = [window.products];
                }
            </c:if>
            <c:if test="${client != null}">
                window.client = $.parseJSON('${client}');
            </c:if>
            var saleApp = angular.module('SaleApp', []);
            saleApp.controller('SaleController', ['$scope','$window', function ($scope, $window) {
                $scope.products = window.products;
                $scope.client = window.client;
                $scope.cart = {};
                $scope.total = 0;
                $scope.addProduct = function(){
                    if(!($scope.cart[$scope.productSelected.id])){
                        if(isNaN($scope.qt)){
                            alert('Invalid quantity');
                        }else{
                            var id = $scope.productSelected.id;
                            var name = $scope.productSelected.name;
                            var price = parseFloat($scope.productSelected.value);
                            var qt = $scope.qt;
                            $scope.cart[$scope.productSelected.id] = {id: id, name: name, qt: qt, price: price};
                            $scope.total += qt * price;
                        }
                    }
                }
                $scope.removeProduct = function(id){
                    $scope.total -= $scope.cart[id].qt * $scope.cart[id].price;
                    delete $scope.cart[id];
                }
                $scope.selectedItemChanged = function(){
                    $scope.qtStock = $scope.productSelected.number;
                }
            }]);
        </script>
    </head>
    <style>
        body{
            padding: 0;
        }
        .page-header{
            background-color: #ddd;
            margin-top: 0;
        }
        .header{
            margin-left: 10px;
            background-color: #ddd;
        }
        
    </style>
    <body>
        
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <a class="navbar-brand" href="#">Microservices - Toy Example</a>
                </div>
            </div>
        </nav>
        <div class="container" ng-app="SaleApp" ng-controller="SaleController">
            <c:if test="${server_not_available != null}">
                <div class="alert alert-danger">
                    ${server_not_available}
                </div>
            </c:if>
            <c:if test="${sale_submitted && success}">
                <div class="alert alert-success">
                    The sale was successfully registered.
                </div>
            </c:if>
            <c:if test="${sale_submitted && !success}">
                <div class="alert alert-danger">
                    Could not register the sale
                </div>
            </c:if>
            <c:if test="${client_search && client == null}">
                <div class="alert alert-danger">
                    Customer not found
                </div>
            </c:if>
            <form action="service" method="post">
                <input type="hidden" value="ClientController" name="logic" />
                <label for="cpf">Customer CPF:</label>
                <div class="form-inline">
                    <input name="cpf" id="cpf" class="form-control" />
                    <input type="submit" class="btn btn-default" id="btSearch" value="send" />
                </div>
            </form>
            <c:if test="${client_search && client != null}">
                <hr>
                <h3> Customer: {{client.name}} ( {{client.email}} - {{client.cpf}}) </h3>
                
                <div>
                    <div class="form-group col-md-3">
                        <label>Product</label>
                        <select id="product" class="form-control" ng-model="productSelected" ng-options="p.name for p in products"  ng-change='selectedItemChanged()'>
                        </select>
                    </div>
                    <div class="form-group col-md-1">
                        <label>Qt Stock</label>
                        <input class="form-control" type="number" ng-model="qtStock" disabled>
                    </div>
                    <div class="form-group col-md-1">
                        <label>Qt</label>
                        <input class="form-control" type="number" ng-model="qt" min="1" max="{{qtStock}}">
                    </div>
                    <div class="form-group col-md-2">
                        <label>&nbsp;</label>
                        <button type="button"class="btn btn-success form-control" ng-click="addProduct()"> Add in cart</button>
                    </div>
                </div>
                <form action="service" method="post">
                    <input type="hidden" name="logic" value="SaleController" />
                    <table class="table">
                        <thead>
                            <th>ID</th>
                            <th>Product</th>
                            <th>Quantity</th>
                            <th>Unit Price</th>
                            <th>Total</th>
                            <th>Remove</th>
                        </thead>
                        <tbody>
                            <tr ng-repeat="(id, product) in cart">
                                <td>{{id}}</td>
                                <td>{{product.name}}</td>
                                <td>{{product.qt}}x</td>
                                <td>$ {{product.price}}</td>
                                <td>$ {{product.price * product.qt}}</td>
                                <td><button type="button" class="btn btn-danger" ng-click="removeProduct(id)">remove</button></td>
                                <input type="hidden" name="product_id" value="{{id}}" />
                                <input type="hidden" name="product_qtd" value="{{product.qt}}" />
                            </tr>
                        </tbody>
                    </table>
                    <span><b>Total: $ {{total}}</b></span><br/>
                    <input type="submit" value="register sale" class="btn btn-default" />
                </form>
            </c:if>
        </div>
    </body>
</html>
