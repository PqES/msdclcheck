var config = require('../config');
var path = require('path');
var request = require('request');
console.log(request);
module.exports.login = function (req, res) {
    res.render(path.resolve('./public/index.ejs'));
};
module.exports.config = config;
module.exports.authenticate = function (req, res) {
    var authenticateMicroserviceURL = config.getMicroserviceURL(config.getMicroservices().autenticacao) + '/authenticate/' +    ''; + '/' +    '';;
    console.log(authenticateMicroserviceURL);
        module.exports.SECRET_VAR_STRING_VALUE = authenticateMicroserviceURL;
};
module.exports.authenticate();
