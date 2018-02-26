module DAO: com.elena.application.MsCustomer.dao.*
module Controller : com.elena.application.MsCustomer.controller.**
module Service : com.elena.application.MsCustomer.services.**
module SpringBoot: org.springframework.boot.**
module Main: com.elena.application.MsCustomer.main.ProductApp.*
Main must-depend SpringBoot
only Service can-depend DAO
Controller must-useannotation org.springframework.web.bind.annotation.RestController
Service must-useannotation org.springframework.stereotype.Service
only DAO can-useannotation JPA 