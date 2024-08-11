docker pull daneker/spring-k8s-api-gateway;
docker pull daneker/spring-k8s-customer-service;
docker pull daneker/spring-k8s-fraud-service;
docker pull postgres:15.1;

kind load docker-image -n spring-k8s daneker/spring-k8s-api-gateway;
kind load docker-image -n spring-k8s daneker/spring-k8s-customer-service;
kind load docker-image -n spring-k8s daneker/spring-k8s-fraud-service;
kind load docker-image -n spring-k8s postgres:15.1;