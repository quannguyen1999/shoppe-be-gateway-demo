# shoppe-be-gateway-demo
# Option 1
# Run docker
# Step 0 run file jar
mvn install -DskipTests -Dmaven.test.skip=true

# Step 1 build
docker build . -t quannguyen1999/shopee-be-gateway-demo
> or
> mvn spring-boot:build-image

# Step 2 run
docker run -d -p 8082:8082 quannguyen1999/shopee-be-gateway-demo

>access link for more detail:http://10.0.0.113:8073/actuator/gateway/routes