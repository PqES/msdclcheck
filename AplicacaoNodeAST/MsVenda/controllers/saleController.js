var config = require('../config');
var path = require('path');
var request = require('request');

/**
 * Recupera a página de vendas.
 * 
 * @param {*} req requisição
 * @param {*} res resposta - página de vendas
 */
module.exports.sale = function(req, res) {
    res.render(path.resolve('./public/sale.ejs'));
}