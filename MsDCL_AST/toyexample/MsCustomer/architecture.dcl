module Controller: com.elena.application.MsCustomer.controller.**
module DAO: com.elena.application.MsCustomer.dao.*
module SpringBoot: org.springframework.boot.**
module Main: com.elena.application.MsCustomer.application.CustomerApp.*
Main must-depend SpringBoot
only Controller can-depend DAO
Controller must-useannotation org.springframework.web.bind.annotation.RestController
