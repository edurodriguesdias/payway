FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace/app

COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN ./mvnw dependency:go-offline -B

COPY src src

RUN ./mvnw clean package -DskipTests -B

RUN mkdir -p target/dependency && \
    cd target/dependency && \
    jar -xf ../*.jar

FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache curl

WORKDIR /workspace

RUN addgroup -g 1001 spring && \
    adduser -u 1001 -G spring -s /bin/sh -D spring

COPY --from=build /workspace/app/target/dependency/BOOT-INF/lib /workspace/lib
COPY --from=build /workspace/app/target/dependency/META-INF /workspace/META-INF
COPY --from=build /workspace/app/target/dependency/BOOT-INF/classes /workspace/app

RUN chown -R spring:spring /workspace

USER spring:spring

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-cp", "/workspace/app:/workspace/lib/*", \
    "com.example.payway.PaywayApplication"]
