FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
RUN groupadd -r findingjob && useradd -r -g findingjob findingjob
USER findingjob
EXPOSE 8002
HEALTHCHECK --interval=10s --timeout=3s --retries=5 \
  CMD wget -qO- http://localhost:8002/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
