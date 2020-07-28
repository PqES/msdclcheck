var http = require('http');
var request = require('request');

console.log(http.createServer(function(req, resp) {
    console.log(req);
}).listen(8092));