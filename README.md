📌 StudyOlle

📝 프로젝트 소개

Spring Boot  + Thymeleaf 기반 SSR 구조의 스터디 관리 웹 애플리케이션입니다.

Docker 컨테이너 환경에서 실행되며, GitHub Actions를 통해 EC2에 자동 배포되도록 CI/CD 파이프라인을 구축했습니다.


🚀 기술 스택

Backend
- Java17
- Spring Boot
- Spring Security
- JPA
- MySQL
- Redis
- Thymeleaf

DevOps
- Docker
- Docker Compose
- Nginx
- GitHub Actions
- AWS EC2


## 추가로 적용 해야 할것
  - [ ] flyway 적용
  - [ ] 프로퍼티 파일 민감정보 암호화
  - [x] 로컬 실행 환경 docker로 변경
  - [x] 배포시 사용할 nginx / app 이미지는 doker hub에서 관리하기 
  - [x] 자동 로그인 변경 (rember-me --> jwt )
  - [ ] app 도커 이미지 용량 줄이기 ( 멀티스테이지로 이미지 만들기 )
  - [ ] CI/CD 구축 (GithubActions)
  - [ ] 배포 테스트 후 https 적용하기
  - [ ] 배포 완료 후 Terraform 적용하기
