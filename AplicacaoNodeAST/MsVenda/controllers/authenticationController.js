// var configFile = '../config';
// var config = require(configFile);
var config = require('../config');
var path = require('path');
var request = require('request');


console.log(request);


/**
 * Recupera a página de login.
 *
 * @param {*} req requisição
 * @param {*} res resposta - página de login
 */
module.exports.login = function(req, res) {
    res.render(path.resolve('./public/index.ejs'));
}

module.exports.config = config;
/**
 * Realiza a autenticação de um usuário.
 *
 * @param {*} req requisição - com os parâmetros username e password
 * @param {*} res resposta - true se o usuário foi autenticado, caso contrário, false
 */



module.exports.authenticate = function(req, res) {
    let authenticateMicroserviceURL = config.getMicroserviceURL(
            config.getMicroservices().autenticacao) + '/authenticate/' + req.params.username +
        "/" + req.params.password;
    console.log(authenticateMicroserviceURL);
    request.get(authenticateMicroserviceURL,
        function(error, response, body) {
            if (!error && response.statusCode == 200) {
                res.json(JSON.parse(response.body));
            } else {
                res.json(false);
            }
        }
    );
}
