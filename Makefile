setup:
	docker-compose up -d

clean:
	docker-compose down

test:
	docker exec test-runner mvn -f ./pom.xml test -Duser.home=/var/maven

generate-report:
	docker run \
        -v allure-reports:/allure-reports \
        -v allure-results:/allure-results \
        -v allure-config:/allure-config \
        smartcosmos/allure-commandline generate
