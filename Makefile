setup:
	docker-compose up -d

clean:
	docker-compose down -v --remove-orphans

test:
	docker exec test-runner mvn -f ./pom.xml test -Duser.home=/var/maven

generate-report:
	docker run -it --name allure-cli \
        -v allure-reports:/allure-reports \
        -v allure-results:/allure-results \
        -v allure-config:/allure-config \
        smartcosmos/allure-commandline generate
