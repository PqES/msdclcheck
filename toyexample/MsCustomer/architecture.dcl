%Automatic Created File
only $system can-depend $java
Controller.CustomerController cannot-useannotation org.springframework.stereotype.Entity
Controller.CustomerController must-useannotation org.springframework.stereotype.Controller
Controller.CustomerController can-declare DAO.CustomerDAO

Model.Customer cannot-useannotation org.springframework.stereotype.Controller
Model.Customer must-useannotation javax.persistence.Entity

DAO.CustomerDAO must-useannotation javax.transaction.Transactional
DAO.CustomerDAO must-extend org.springframework.data.repository.CrudRepository

MsCustomer.ServletInicializer must-extend org.springframework.boot.builder.SpringApplicationBuilder
MsCustomer.CustomerApp must-useannotation org.springframework.boot.autoconfigure.SpringBootApplication