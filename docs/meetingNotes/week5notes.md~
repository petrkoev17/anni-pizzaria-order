# Demo
Dorian showing the Basket microservice:
Database, tables, request to get pizzas and ingredients, add pizzas to repository.
You get error if pizza you want to add already exists or if one of the ingredients on a pizza doesn't exist.


# Stand-up

Thijs:
* Implement endpoints for roles (managers and stores)
* Started working on order overview

Dorian:
* Configured pipeline for the project and basket pipeline
* Fixed checkstyle
* Begun creating a commons folder

Justin:
* Implemented method to calculate price in Coupon using Strategy design pattern
* Each coupon type has different method ^
* Started working on endpoints to get pizzas

Kayra:
* Add endpoint to add pizza to repository
* Fix repository issue with creating new pizza
* Implemented request model for Pizza to fix issue with endpoint
* Working on implementing communication between User and Basket to add pizzas to the basket

Izzy:
* added Coupon and LocalDateTime to Order
* Added some checks for users who try to cancel orders like userId and time
* Tried to create a Commons folder, but failed


# Assignment

UML Diagram and architecture
* Make sure the component diagram reflects changes made to the system
* If we can have a complete UML diagram this evening, we will get a bit of feedback tomorrow

Design patterns
* Decide on them as soon as possible

Database
* Make a schema for them
* Make sure different microservices don't share one database
* It is important to have a database schema before you code everything

Should we hand in the schema with assignment 1?
Yes.
Does it count for the pages?
It is seperate.

# Gateway
What is the functionality of the gateway?
The entry point into the system for the user. Every request they make goes to the gateway and 
this gateway redirects it. It is a seperate endpoint. See the link Alex sent for more information. Slides 44-46.
In terms of coding, having a gateway takes more time than not having one. You could give up the gateway if you do not have much time. The other services are more important. 
Make sure this decision is reflected in the document. The gateway usually checks for permissions, but you can change the responsibility as long as you document it.
The user shouldn't be able to connect to the microservices if we have a gateway. We should decide on the gateway today or tomorrow so we can explain it in the assigment.
