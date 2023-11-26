# Stand-ups
Thys
- roles for authentication
- user gets token - token contains what role
- everyone is customer for now

Kayra
- Controller for basket to display all pizzas
- Implementing filtering allergies

Dorian
- DB for pizza
- Sample pizzas

Izzy
- Add an order
- Cancel your order
- FIFO ordering

Justin
- Making coupon class
- Activation code class
- Initialising database

Petar
- Setting up database

-----------------------------------------------------------------------
# Agendas

Milestones
- Have a milestone for each sprints

Boards

Common classes
- It is allowed, but also consider merging the microservices

Maybe merge Basket and Orders, or Coupons and Orders

Roles??

Have types for the coupons
- Do not use code for identifying types (same for users)
- Have coupon id

"Unpacking of the scenario" in the report
- Explain what the scenario requires
- Show how it connects to the microservices
- Address the story in the scenario
- Maybe in the intro, try to explain a bit more why we split them into these microservices

CICD pipeline
- we have example ones so see how we can use this
- each microservice should have a pipeline

Coupons- should be must

Design patterns (2 or 3) - We should have these implemented by the deadline (16th)
- pizzas - factory or builder
- user - decorator or composite (having more rights)