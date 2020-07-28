var customerController = require('../controllers/customerController');

module.exports = function(app) {

    app.get('/getCustomerCpf/:cpf', customerController.getCustomerCpf);

}