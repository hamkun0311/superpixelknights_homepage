# 1. 빌드된 JAR 파일을 실행할 환경 설정
FROM amazoncorretto:17-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드 결과물(JAR)을 컨테이너 안으로 복사
# Gradle 빌드 시 생성되는 경로입니다.
COPY build/libs/*.jar app.jar

# 4. 앱 실행
ENTRYPOINT ["java", "-jar", "app.jar"]