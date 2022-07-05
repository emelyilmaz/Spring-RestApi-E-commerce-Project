# Hero-Company-Rest-Api
Spring Boot E- Commerce RestApi Project

This project offers a Rest API application that allows multiple companies to make sales and manage their sales, products, stocks, categories, and also allows customers to create orders and manage their orders.

- In the project, services were provided for roles with the jwt method.The security configuration has been created so that one role of the customer is one of the admin.
However, the entity and role relationship has been created as many-to-many so that the project can be easily adapted according to the needs.

- Login, forgot password, reset password is designed as a common service for all roles.The url link with the verification code string query is sent to the e-mail address of the user who logs in to the forgot password service.

- In the project, the data to be consumed regularly in case of need has been worked with cache methods and the cost of the application has been brought to the optimum level. (You can check the category listing service.)

- Postman and Swagger tools have been used in the documentation of this project and have been adapted to OpenApi standards.


## Languages, Technologies and Environments Used in this Project

| Java | MySql  | Spring Boot | Spring <br> RestApi  | Spring Mail <br> Framework  | Spring Security | IntelliJ
| :------------: | :---------: | :-------------: | :------: | :-----: | :-------------: | :-----------: 
|<img src ="https://cdn.iconscout.com/icon/free/png-256/java-60-1174953.png" width ="100px" height = "100px" style="float:left" > | <img src ="https://github.com/emelyilmaz/Spring-RestApi-E-commerce-Project/blob/main/images/mysql.jpg" width ="65px" height = "65px" style="float:left " > |<img src ="https://github.com/emelyilmaz/Spring-RestApi-E-commerce-Project/blob/main/images/spring.jpg" width ="65px" height = "65px" style="float:left " > |<img src ="https://github.com/emelyilmaz/Spring-RestApi-E-commerce-Project/blob/main/images/restapi.jpg" height = "65px" > |<img src ="https://github.com/emelyilmaz/Spring-RestApi-E-commerce-Project/blob/main/images/mail.jpg" width ="65px" height = "65px" > |<img src ="https://github.com/emelyilmaz/Spring-RestApi-E-commerce-Project/blob/main/images/security.jpg" width ="65px" height = "65px" > |<img src ="https://upload.wikimedia.org/wikipedia/commons/thumb/9/9c/IntelliJ_IDEA_Icon.svg/70px-IntelliJ_IDEA_Icon.svg.png" width ="65px" height = "65px" > | 


## Project Display Image
<br>
<p>
<p>                            Database Diagram  </p>
 <a href="https://github.com/emelyilmaz/Spring-RestApi-E-commerce-Project/blob/main/images/Database%20Diagram.JPG" target="_blank">
<img src="https://github.com/emelyilmaz/Spring-RestApi-E-commerce-Project/blob/main/images/Database%20Diagram.JPG" width="550" style="max-width:100%;"></a>
</p>
<br>
<p>
<p>                                   Role table by services  </p>
 <a href="https://github.com/emelyilmaz/Spring-RestApi-E-commerce-Project/blob/main/images/Role.jpeg" target="_blank">
<img src="https://github.com/emelyilmaz/Spring-RestApi-E-commerce-Project/blob/main/images/Role.jpeg" width="550" style="max-width:100%;"></a>
</p>
<br>
