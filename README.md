# Balance Well: The Wellness App

This is a ***Spring Boot*** Web Application which main purpose is to help people define their nutritional and calorie needs in order to fulfill their specified goals.
Users can easily register themselves using the registration form or via single sign-on with their Google account. This is implemented by using ***Spring Security and OAuth2***.

Upon first login users are required to complete the provided questionnaire, which is how the web application gets to know the user and their goals. The defined goals and personal information can be revisited and changed at any time.
In addition to this, users can visually track their progress, which gets updated with every tracked change in the user's weight.

Authenticated user has access to their personal diary in which they can keep track of their day-by-day meals and can revisit old logs from previous days.
Users can search for different food entries by using the in-app search bar, which communicates with an external REST API to get nutritional details for the specified food.
Furthermore, users can add their recipes, save and search for other users' recipes, as well as directly add a recipe to a given meal within their diary.

All entries in this web application are stored in ***PostgreSQL*** database and the communication with the underlying database is done by implementing ***Spring Data JPA Repositories***.

All app visitors can subscribe with their email to the website's newsletter and as a result, they get an email message for successful registration. This is implemented by using ***Spring Mail and JavaMailSender***.

***Thymeleaf*** was used as a template engine and the technologies used for the front-end development are ***HTML, CSS, Bootstrap, and Javascript.***

You can check out the demo for the application shows all features in detail: https://youtu.be/YSWzFX5wS30
