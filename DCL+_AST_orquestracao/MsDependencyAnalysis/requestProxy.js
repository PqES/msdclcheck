var http = require('http');
var request = require('request');

var r = request.defaults({ 'proxy': 'http://localhost:8092' })

console.log(http.createServer(function(req, resp) {
    console.log(req);
    if (req.url === '/doodle.png') {
        r.get('https://www.google.com.br/imgres?imgurl=https%3A%2F%2Fdoodle.com%2Fgraphics%2Fstatic%2FfacebookSharingThumbnail.png&imgrefurl=https%3A%2F%2Fdoodle.com%2F&docid=LCLaWN-A6LPASM&tbnid=LMIyqnLag-vuWM%3A&vet=10ahUKEwjAzfW_q_HZAhXHC5AKHfa9BXUQMwiBAigEMAQ..i&w=300&h=200&bih=678&biw=1301&q=doodle&ved=0ahUKEwjAzfW_q_HZAhXHC5AKHfa9BXUQMwiBAigEMAQ&iact=mrc&uact=8').pipe(resp);
    }
}).listen(8091));