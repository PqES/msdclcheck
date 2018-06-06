var zullConfig = require('./zull_config');
var microservices = require('./microservices');

console.log(microservices.autenticacao)
console.log("ZullConfig:\n" + JSON.stringify(zullConfig));
console.log("\nmicroservices:\n" + JSON.stringify(microservices));

module.exports.getZullConfig = function() {
    return zullConfig;
}

module.exports.getZullURL = function() {
    return "http://" + zullConfig.host + ":" + zullConfig.port;
}

module.exports.getMicroserviceURL = function(microservice) {
    return module.exports.getZullURL() + "/" + microservice;
}

module.exports.getMicroservices = function() {
    return microservices;
}