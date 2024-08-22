# 베이스 이미지를 Amazon Corretto 17 Alpine JDK로 설정
FROM amazoncorretto:17-alpine-jdk

# 빌드된 JAR 파일의 경로를 ARG로 설정
ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
ARG ENV

# JAR 파일을 이미지의 app.jar로 복사
COPY ${JAR_FILE} app.jar

# ENTRYPOINT 명령어를 설정하여 컨테이너 실행 시 JAR 파일을 실행
ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=${PROFILES} -Dserver.env=${ENV} -jar app.jar"]
# ENTRYPOINT ["sh", "-c", "java -Dspring.profiles.active=dev -Dserver.env=dev -jar app.jar"]
