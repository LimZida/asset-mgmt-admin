# asset-mgmt-admin
사내 자산관리 서버

# 구조
### 서버 1. 메세지 큐 서버 
카프카로 이루어진 SMTP 정보에 대한 중개를 담당합니다.
하나의 토픽으로만 운영 중입니다.

### 서버 2. 이메일 전송 서버 (https://github.com/LimZida/asset-mgmt-email)
Consumer로서 이메일 정보를 수신해 메일 발송을 진행합니다.

### 서버 3. 자산관리 서버 (https://github.com/LimZida/asset-mgmt-admin)
Producer로서 자산 등록, 기간 만료, 노후화 등에 대한 메일 정보를 송신합니다.