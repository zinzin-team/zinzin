# 베이스 이미지 설정
FROM openjdk:17-jdk-alpine

# Set the timezone environment variable
ENV TZ=Asia/Seoul

# Install tzdata and set the localtime
RUN apt-get update && apt-get install -y tzdata && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone && \
    apt-get clean

# maintainer 정보
LABEL maintainer="hckhong@naver.com"

# 호스트의 /tmp 경로로 컨테이너의 볼륨을 마운트
VOLUME /tmp

# 컨테이너가 사용하는 포트를 외부에 노출
EXPOSE 8080

# jar 파일을 Docker 이미지의 zinzin.jar로 추가
ADD build/libs/*.jar zinzin.jar

# 컨테이너가 시작될 때 zinzin.jar를 실행
ENTRYPOINT ["java", "-jar", "/zinzin.jar"]
