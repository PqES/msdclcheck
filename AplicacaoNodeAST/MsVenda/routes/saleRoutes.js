var saleController = require('../controllers/saleController');

module.exports = function(app) {

    app.get('/sale', saleController.sale);

}