var productController = require('../controllers/productController');

module.exports = function(app) {

    app.get('/getProducts', productController.getProducts);

}