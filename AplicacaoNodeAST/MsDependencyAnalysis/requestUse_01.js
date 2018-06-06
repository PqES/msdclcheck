var request = require('request');

request.get(
    "http:localhost:8080/msVendaGet",
    function(error, response, body) {
        if (!error && response.statusCode == 200) {
            res.json(JSON.parse(response.body));
        } else {
            res.json(false);
        }
    }
);

request.post(
    "http:localhost:8080/msVendaPost",
    function(error, response, body) {
        if (!error && response.statusCode == 200) {
            res.json(JSON.parse(response.body));
        } else {
            res.json(false);
        }
    }
);

request.put(
    "http:localhost:8080/msVendaPut",
    function(error, response, body) {
        if (!error && response.statusCode == 200) {
            res.json(JSON.parse(response.body));
        } else {
            res.json(false);
        }
    }
);

request.delete(
    "http:localhost:8080/msVendaDelete",
    function(error, response, body) {
        if (!error && response.statusCode == 200) {
            res.json(JSON.parse(response.body));
        } else {
            res.json(false);
        }
    }
);

request.patch(
    "http:localhost:8080/msVendaPatch",
    function(error, response, body) {
        if (!error && response.statusCode == 200) {
            res.json(JSON.parse(response.body));
        } else {
            res.json(false);
        }
    }
);

var fRequest = function() {
    request(
        "http:localhost:8080/msVendaRequest",
        function(error, response, body) {
            if (!error && response.statusCode == 200) {
                res.json(JSON.parse(response.body));
            } else {
                res.json(false);
            }
        }
    );
}


// -----------------------------
// request.get({
//         url: "http:localhost:8080/msVenda"
//     },
//     function(error, response, body) {
//         if (!error && response.statusCode == 200) {
//             res.json(JSON.parse(response.body));
//         } else {
//             res.json(false);
//         }
//     }
// );

// request.get({
//     url: "http:localhost"
// }).then(function(error, response, body) {
//     if (!error && response.statusCode == 200) {
//         res.json(JSON.parse(response.body));
//     } else {
//         res.json(false);
//     }
// });