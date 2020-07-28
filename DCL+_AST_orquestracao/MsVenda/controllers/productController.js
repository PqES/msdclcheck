var config = require('../config');
var path = require('path');
var request = require('request');

/**
 * 
 * @param {*} req 
 * @param {*} res 
 */
module.exports.getProducts = function(req, res) {
    let getProductsMicroserviceURL = config.getMicroserviceURL(config.getMicroservices().product) +
        '/getProducts';
    console.log(getProductsMicroserviceURL);
    request.get(getProductsMicroserviceURL,
        function(error, response, body) {
            if (!error && response.statusCode == 200) {
                res.json(JSON.parse(response.body));
            } else {
                res.json(false);
            }
        }
    );
}