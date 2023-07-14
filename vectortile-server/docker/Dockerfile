FROM openjdk:8-jdk-alpine AS builder
RUN apk update && \
    apk upgrade && \
    apk add maven && \
    rm -rf /var/cache/apk/*
WORKDIR /workspace/app
ADD pom.xml .
ADD src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -f pom.xml install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../vectortile-server-*-exec.jar)

FROM openjdk:8-jre-alpine
RUN addgroup -S app &&adduser -S app_runner -G app
USER app_runner
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=builder --chown=app_runner:app ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=builder --chown=app_runner:app ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=builder --chown=app_runner:app ${DEPENDENCY}/BOOT-INF/classes /app
COPY --from=builder ${DEPENDENCY} /app
ENTRYPOINT ["java","-cp","app:app/lib/*","org.gbif.maps.TileServerApplication"]
EXPOSE 8080
