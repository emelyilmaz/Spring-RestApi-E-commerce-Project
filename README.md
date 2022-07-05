# Hero-Company-Rest-Api
Spring Boot E- Commerce RestApi Project

This project offers a Rest API application that allows multiple companies to make sales and manage their sales, products, stocks, categories, and also allows customers to create orders and manage their orders.

- In the project, services were provided for roles with the jwt method.The security configuration has been created so that one role of the customer is one of the admin.
However, the entity and role relationship has been created as many-to-many so that the project can be easily adapted according to the needs.

- Login, forgot password, reset password is designed as a common service for all roles.

- In the project, the data to be consumed regularly in case of need has been worked with cache methods and the cost of the application has been brought to the optimum level. (You can check the category listing service.)

- Postman and Swagger tools have been used in the documentation of this project and have been adapted to OpenApi standards.
