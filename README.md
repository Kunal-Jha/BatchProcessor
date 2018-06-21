# BatchProcessor

BatchProcessor which can take a batch of requests for meeting and write them to DB and return successful requests sorted by meeting Start Time.

## Assumptions
1. The input is newLine seperated between each booking request. 
2. The data will fit in memory as DB is in-memory.
3. There is no provision for alerting users of failed bookings.
4. Syntactically correct inputs.

## Running the code

The code can be run easily by executing the `./run.sh`. This file contains the mvn packaging and jar execution command. 
Once the server is running the client can be accessed at 

`http://localhost:8080/timetable-creation`

Once the RequestBody is sent the output is returned in string format. 

## Technology Stack

1. Java 8
2. Maven to package the code. 
3. SpringBoot and Spring Batchfor the web service. 
4. HSQLDb for in memory data.
 
