# 1. JDK 이미지
FROM eclipse-temurin:17-jdk

# 2. 작업 디렉토리 생성
WORKDIR /app

# 3. JAR 복사
COPY build/libs/studyolle-0.0.1-SNAPSHOT.jar app.jar

# 4. 포트 노출
EXPOSE 8080

# 5. 앱 실행
ENTRYPOINT ["java","-jar","app.jar" ,"--spring.profiles.active=dev"]