'use strict';

var port;
var http = require('http'),
    path = require('path'),
    methods = require('methods'),
    express = require('express'),
    bodyParser = require('body-parser'),
    session = require('express-session'),
    cors = require('cors'),
    passport = require('passport'),
    errorhandler = require('errorhandler'),
    mongoose = require('mongoose'),
    request = require('request'),
    fs = require('fs');

// Or, if you're not using a transpiler: 
const Eureka = require('eureka-js-client').Eureka;

// example configuration 
const client = new Eureka({});

// Create global app object
var app = express();
app.use(express.static('.'));
app.set('public', './public');
app.engine('html', require('ejs').renderFile);

// app.use(cors());

// Normal express config defaults
// app.use(require('morgan')('dev'));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


// /**
//  * Recupera as informações de um determinado usuário pelo seu username.
//  */
// app.get('/', function(req, res) {
//     res.render(path.resolve('./public/index.ejs'));
//     // request({
//     //         url: zullConfig.host + ":" + zullConfig.port + '/'
//     //     },
//     //     function(error, response, body) {
//     //         if (!error && response.statusCode == 200) {
//     //             let service = JSON.parse(response.body).service;
//     //             res.render(path.resolve('./public/index.ejs'));
//     //         } else {
//     //             console.log("error -> " + JSON.stringify(error));
//     //             console.log("response -> " + JSON.stringify(response));
//     //         }
//     //     }
//     // );
// });


var authenticationRoutes = require('./routes/authenticationRoutes');
var saleRoutes = require('./routes/saleRoutes');
var customerRoutes = require('./routes/customerRoutes');
var productRoutes = require('./routes/productRoutes');

authenticationRoutes(app);
saleRoutes(app);
customerRoutes(app);
productRoutes(app);

app.get('/info', function(req, res) {
    res.json(Object());
});

app.get('/health', function(req, res) {
    res.json(Object());
});

/**
 * Inicializa o servidor.
 */
var server = app.listen(process.env.PORT || 8084, function() {
    port = server.address().port;
    console.log('Listening on port ' + server.address().port);
})

function connectToEureka() {
    client.logger.level('debug');
    client.start(function(error) {
        console.log('########################################################');
        console.log(JSON.stringify(error) || 'Eureka registration complete');
    });
}

connectToEureka();