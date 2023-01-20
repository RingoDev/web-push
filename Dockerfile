# Stage 1
FROM openjdk:15 as builder

# copying needed files and making .mvnw executable
COPY ./pom.xml ./pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY ./src ./src
RUN ["chmod", "+x", "mvnw"]

# building fat jar
RUN ./mvnw package -DskipTests

# extracting fat jar
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)


# Stage 2
FROM openjdk:18.0.1-jdk-slim

# adding user push to group push
RUN addgroup --system push && adduser --system push --ingroup push

# setting app user to push
USER push

# creating different layers
ARG DEPENDENCY=target/dependency
COPY --from=builder ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder ${DEPENDENCY}/BOOT-INF/classes /app

# setting entrypoint
ENTRYPOINT ["java","-cp","app:app/lib/*","com.ringodev.webpush.WebpushApplication"]
