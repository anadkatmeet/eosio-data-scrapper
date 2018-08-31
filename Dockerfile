FROM openjdk:8
WORKDIR /
COPY target/scala-2.12/data-scrapper-0.1.jar data-scrapper.jar
CMD java -jar data-scrapper.jar