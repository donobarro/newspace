setup:
	docker-compose up -d

clean:
	docker-compose down

test:
	docker exec test-runner mvn -f ./pom.xml test -Duser.home=/var/maven
