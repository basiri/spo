# Workforce Optimization Tool
## About the solution
The problem does not have a single mathematical answer; therefore, a heuristic algorithm such as the genetic algorithm could be an appropriate solution. The fitness of answers is based on the minimum workforce in structure. Mutation and gene removing altered in order to get the fast answer and because of the simplicity of answers. Other factors for indicating a better solution can be implemented in the geneFitnessCalculator method.
In the instruction file, is mentioned senior cleaners have a higher work capacity than Junior Cleaners, but there was no equivalency rate. In the application.properties the rates can be set.  Here is the default value:
```
SPO.srRate=10
SPO.jrRate=8
//To manage the accuracy of the answer 
SPO.depthLevel=100
//To set for the iteration number of survival selection
SPO.generationCount=10
//Maximum room of structure in the portfolio
SPO.maxRoom=100
//Minimum senior in a structure 
SPO.minSeniorInStructure=1
```

The test class using the same resource.
Although we have just one rule for acceptance of an answer (at least one senior in structure), several rules might be added to valid answer policy. The place is indicated with //TODO 
Custom input validation checked with javax.validation and  custom annotation has been implemented  for the related class in  spo\src\main\java\com\abs\spo\controller\valdator
Hiding internal error and exception implemented with the returning an object of ApiError for the client with the meaningful error code (based on imaginary agreement)
In spo\src\main\java\com\abs\spo\exception
Test code coverage has been checked with Jacoco.
Controller request body and response body accept and return JSON (there was deference in input and output sample and JSON format in the code challenge instruction file)

## Libraries & Tools
-	Spring Boot 2.1.6.RELEASE
-	Swagger API
-	Maven


### Main Class :
```
spo\src\main\java\com\abs\spo\
com.abs.spo.SpoApplication.java
```
### Controller:
```
com.abs.spo.controller.WorkforceAssignerController
test:
spo\src\test\java\com\abs\spo\controller
```
### Service: 
```
	com.abs.spo.service.WorkforceOptimizerServiceImpl
test:
spo\src\test\java\com\abs\spo\service
```

### Input Validator:
```
com.abs.spo.controller.valdator.RoomValidityValidator
```
### to run the application:
```
mvn spring-boot:run
```
### to run the test:
```
mvnw test
```
### to check the test code coverage (JACOCO):
```
spo\target\site\jacoco\index.html 
```
### To See the result:

-	Run the application
-	In browser with swagger:
```
	http://localhost:8080
```
-	With Postman
```
	POST http://localhost:8080/assign
	Body: {"rooms": [35, 21, 17], "senior": 10, "junior": 6}
```


