var authenticationController = require('../controllers/authenticationController');

module.exports = function(app) {

    app.get('/', authenticationController.login);

    app.get('/login', authenticationController.login);

    app.get('/autenticacao/:username/:password', authenticationController.authenticate);

    app.get('/')

}