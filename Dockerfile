FROM openjdk:17
EXPOSE 8080
ADD target/food-delivery.jar
ENTRYPOINT ["java", "-jar" , "food-delivery-1.0-SNAPSHOT.jar"]