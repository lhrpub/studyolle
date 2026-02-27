FROM nginx:1.25

# 기본 conf 제거
RUN rm /etc/nginx/conf.d/default.conf

# 우리가 만든 설정 파일 복사
COPY default.conf /etc/nginx/conf.d/default.conf