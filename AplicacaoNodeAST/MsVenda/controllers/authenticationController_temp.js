var config = require('../config');
var path = require('path');
var request = require('request');
var teste = new require('./teste')('oi');
console.log(request);
console.log(teste.falar);
module.exports.login = function (req, res) {
    res.render(path.resolve('./public/index.ejs'));
};
var teste2 = { teste1: 'oi' };
module.exports.config = config;
module.exports.authenticate = function (req, res) {
    var authenticateMicroserviceURL = config.getMicroserviceURL(config.getMicroservices().autenticacao) + '/authenticate/' +    ''; + '/' +    '';;
    console.log(authenticateMicroserviceURL);
        module.exports.SECRET_VAR_STRING_VALUE = teste.c + teste.b;
};
module.exports.authenticate();
