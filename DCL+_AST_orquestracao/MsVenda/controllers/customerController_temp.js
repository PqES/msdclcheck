var config = require('../config');
var path = require('path');
var request = require('request');
module.exports.getCustomerCpf = function (req, res) {
    var cpf =     '';;
    var getCustomerCpfMicroserviceURL = config.getMicroserviceURL(config.getMicroservices().customer) + '/authenticate/' + cpf;
    console.log(getCustomerCpfMicroserviceURL);
        module.exports.SECRET_VAR_STRING_VALUE = getCustomerCpfMicroserviceURL;
};
module.exports.getCustomerCpf();
