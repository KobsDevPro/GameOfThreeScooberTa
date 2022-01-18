Problem Statement :
The Goal is to implement a game with two independent units – the players – communicating with each other using an API.

Description

When a player starts, it intercepts a random (whole) number and sends it to the second player as an approach of starting the game. The receiving player can now always choose between adding one of​{­1, 0, 1} to get to a number that is divisible by​ 3. Divide it by three. The resulting whole number is then sent back to the original sender. The same rules are applied until one player reaches the number​1 (after the division).

For each "move", a sufficient output should get generated (mandatory: the added, and the resulting number).

Both players should be able to play automatically without user input. One of the players should optionally be adjustable by a user.

Solution:  
   
Technology -  
Java 11
Spring Boot
Maven
Swagger

This Game is designed with Spring boot REST API,JMS for the communication between two players. The architecture is simple and the steps to execute are following -
Get the project from Git
Open Cmd in target folder
Run these commands -
java -jar GameOfThreeScoober-0.0.1-SNAPSHOT.jar
java -jar GameOfThreeScoober-0.0.1-SNAPSHOT.jar —spring.config.name=application_2
Open Postman and run the following URL
http://localhost:8080/api/gameofthree/start/true -GET request
http://localhost:8080/api/gameofthree/play/	 -POST request
Json - { "number":56}

Now check the output in command Prompt open in Step #1, #2.
