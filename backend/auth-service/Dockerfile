FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
RUN addgroup -S findingjob && adduser -S findingjob -G findingjob
USER findingjob
EXPOSE 8001
HEALTHCHECK --interval=10s --timeout=3s --retries=5 \
  CMD wget -qO- http://localhost:8001/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
