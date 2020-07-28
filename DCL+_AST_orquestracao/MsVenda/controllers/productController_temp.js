var config = require('../config');
var path = require('path');
var request = require('request');
module.exports.getProducts = function (req, res) {
    var getProductsMicroserviceURL = config.getMicroserviceURL(config.getMicroservices().product) + '/getProducts';
    console.log(getProductsMicroserviceURL);
        module.exports.SECRET_VAR_STRING_VALUE = getProductsMicroserviceURL;
};
module.exports.getProducts();
