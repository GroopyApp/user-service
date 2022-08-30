# user-service

A java microservice to manage user operations in Groopy system

## Running

### Running as a java process
To run the project locally as a java process you must run before [infrastructure](https://github.com/GroopyApp/infrastructure) docker container in order to have all the dependencies you need running.

### Running as a docker container
Running the project with docker allow us to create a complete environment, where we can also attach other services such as [room-service](https://github.com/GroopyApp/room-service)
To run or attach user-service to docker environment you simply must run:

`sh ./docker-run.sh`
