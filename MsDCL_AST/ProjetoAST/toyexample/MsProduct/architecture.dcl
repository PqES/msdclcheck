%Automatic Created File
only $system can-depend $java
Controller.ProductController cannot-useannotation org.springframework.stereotype.Entity
Controller.ProductController must-useannotation org.springframework.stereotype.Controller
Controller.ProductController can-declare DAO.ProductDAO

Model.Product cannot-useannotation org.springframework.stereotype.Controller
Model.Product must-useannotation javax.persistence.Entity

DAO.ProductDAO must-useannotation javax.transaction.Transactional
DAO.ProductDAO must-extend org.springframework.data.repository.CrudRepository

MsProduct.ServletInicializer must-extend org.springframework.boot.builder.SpringApplicationBuilder
MsProduct.ProductApp must-useannotation org.springframework.boot.autoconfigure.SpringBootApplication