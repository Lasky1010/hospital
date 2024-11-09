# Hospital new application

## Installation and Setup

git clone https://github.com/Lasky1010/hospital.git

Run ``mvn clean install``

At the first launch you should create database, run application, and then run migration scripts

Run docker-compose (before it change env vars in [docker-compose.yaml](docker-compose.yaml) and specify database)
``` docker-compose up --build ```

Specify from which date to import: ```${DATEFROM}, ${DATETO}```

## Endpoints

Go to [Swagger](http://localhost:8081/api/swagger-ui.html)