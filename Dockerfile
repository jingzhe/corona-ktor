FROM openjdk:17-jdk
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/install/corona-ktor/ /app/
WORKDIR /app/bin
CMD ["./corona-ktor"]