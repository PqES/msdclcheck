var config = require('../config');
var path = require('path');
var request = require('request');

/**
 * 
 * @param {*} req 
 * @param {*} res 
 */
module.exports.getCustomerCpf = function(req, res) {
    let cpf = req.params.cpf;
    let getCustomerCpfMicroserviceURL = config.getMicroserviceURL(config.getMicroservices().customer) + '/authenticate/' + cpf;
    console.log(getCustomerCpfMicroserviceURL);
    request.get(getCustomerCpfMicroserviceURL,
        function(error, response, body) {
            if (!error && response.statusCode == 200) {
                res.json(JSON.parse(response.body));
            } else {
                res.json(false);
            }
        }
    );
}