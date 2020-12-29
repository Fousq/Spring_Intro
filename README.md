# Spring_Web

## Sources

* Spring Data Access Reference  - https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html
* Guide to Hibernate 4 with Spring - https://www.baeldung.com/hibernate-4-spring
* Spring Integration Testing - https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#testing
* Hibernate Getting Started Guide - https://docs.jboss.org/hibernate/orm/5.4/quickstart/html_single/

## Main Task

Based on the codebase created during Spring Web module

1. Using the Hibernate ORM framework, update existing models. (0.5 point)
2. Add new model to the application – UserAccount, it should store the amount of prepaid money user has in the system, which should be used during booking procedure. Add methods for refilling the account to the BookingFacade class. Add DAO and service objects for new entity. Add ticketPrice field to Event entity. (0.5 point)
3. Create database schema for storing application entities. Provide SQL script with schema creation DDL for DBMS of your choice. (0.5 points)
4. Update DAO objects so that they inherit from one of the Spring Data interfaces, for example – CrudRepository. They would store and retrieve application entities from database. Use transaction management to perform actions in a transaction where it necessary (define the approach to implementing transactions with a mentor). Configure Hibernate for work with DBMS that you choose. (1.5 point)
5. Update ticket booking methods to check and withdraw money from user account according to the ticketPrice for a particular event.(0.5 point)
6. Cover code with unit tests. Code should contain proper logging (0.5 point)
7. Add integration tests for newly implemented scenarios. (0.5 point)