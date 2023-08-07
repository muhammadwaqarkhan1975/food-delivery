# Functionality
# Continuous Monitoring
Tickets APIs sorted as per the priority
Create a ticket on certain conditions
OAuth 2 implementation
Food Delivery System
Food Delivery System APIs:
New Order API's
Customer API's
Rider Location API's
Rider Rating API's
Food Deliveries API's
Database Setup:
To set up the database, follow these steps:

Create a database with the name "food-delivery" on a PostgreSQL server.
Execute the "food-delivery.sql" script available in the project.
Continuous Monitoring:
The system offers two solutions for continuous monitoring:

Solution 1:
By default, the system executes continuous monitoring based on a Cron job.
On each iteration, it fetches Pageable records, with the page size being configurable.
Each iteration processes all records in parallel based on the EC2 instance size.
The following properties need to be set up based on the server capacity:
spring.food.delivery.batch.page.size
spring.food.delivery.pool.size
spring.food.delivery.max.pool.size
spring.food.delivery.queue.capacity
spring.food.delivery.priority.run.job=spring
Solution 2:
If the system is deployed on a cloud infrastructure, each iteration will fetch only the IDs of jobs and produce messages in the queue.
Based on the load, spinning up more EC2 instances will divide the load.
The following properties need to be turned on for the queue:
spring.food.delivery.priority.run.job=sqs
cloud.aws.credentials.access-key
cloud.aws.credentials.secret-key
food.delivery.monitoring.queue=dev-food-delivery
spring.food.delivery.monitoring.consumer
Auth 2 Implementation:
A default user has been created for login with the following credentials:

URL: http://localhost:8080/oauth/token
Client ID: FoodDelivery
Client Secret: password
User: waqar@khan.com
Password: password
Please refer to the code and documentation for more detailed information on the implementation and usage of this Food Delivery Monitoring System.