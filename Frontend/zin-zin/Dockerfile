# 베이스 이미지 설정
FROM node:20-alpine

# maintainer 정보
LABEL maintainer="hckhong@naver.com"

# 작업 디렉토리 설정
WORKDIR /app

# package.json과 package-lock.json 파일을 복사
COPY package*.json ./

# 의존성 설치
RUN npm install

# 어플리케이션 빌드를 위해 프로젝트 소스를 복사
COPY . .

# 어플리케이션 빌드
RUN npm run build

# 어플리케이션을 실행하기 위한 설정
ENV NODE_ENV production

# 어플리케이션 시작
CMD ["npm", "start"]

# 컨테이너가 사용하는 포트를 외부에 노출
EXPOSE 8000
