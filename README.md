# NewSpace

## Prerequisites:

Docker 20+, Docker-compose 2.1.x

## How to run:
In the main folder use Terminal to run the following commands:
1. `make setup` - runs docker-compose with 2 containers: test-runner and allure-docker-service
2. `make test` - runs Java tests in test-runner container

If the allure-docker-service is running, the report can be found under the following links:

Latest report: http://localhost:5050/allure-docker-service/projects/default/reports/latest/index.html

In order to clean just run:
`make clean`