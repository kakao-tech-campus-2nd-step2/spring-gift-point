# spring-gift-point

# 0단계

## API 명세서
https://impossible-repair-22e.notion.site/57ec013f9424421eb2317b11a2b9a29c?v=f3fe7340ebae425bbfa70db78123a663

# 1단계

## 구현할 기능 목록
- [ ] 작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영
  - [x] 일반 회원가입
  - [x] 일반 로그인
  - [x] 모든 상품 조회
  - [x] 특정 제품의 옵션 조회
  - [x] 특정 카테고리별 상품 목록조회
  - [x] 모든 카테고리 조회
  - [x] 위시리스트에 추가
  - [x] 위시리스트에서 삭제
  - [x] 로그인한 회원의 위시리스트 조회
  - [x] 주문 생성

# 2단계

## 구현할 기능 목록
- [x] API 오류 확인
  - [x] wish 
  - [x] product 카테고리별 상품 불러오는 api endpoint 경로 오류 수정
- [x] 배포 자동화
  - [x] 배포 스크립트 작성
    - [x] 웹을 서버에 배포하기 위한 셀 스크립트 작성
    - [x] 스크립트는 현재 실행 중인 JAR 파일을 종료하고, 새로운 JAR 파일을 복사하여 실행하는 기능을 포함
    - https://www.notion.so/18cdfba8d1e44daa978eb1c7e7ee03d9

- [x] 보안 문제 
  - [x] JWT를 사용한 인증에 문제가 없는지 프론트와 연결하면서 테스트

- [x] cors 에러 해결
  - [x] 전역 CORS 설정 : WebConfig에서 cors에러 해결 
  - [ ] ~~특정 controller에서 CORS 설정~~
  - [ ] ~~Spring Security에서 CORS 설정~~
  - [ ] ~~application에서 CORS 설정 사용~~
  
- [x] CORS 테스트 구현

- [ ] 문서화
  - [ ] 배포 및 연동 문서 작성하기
    - [ ] 배포 프로세스 문서 작성
    - [ ] API 연동 방법에 대한 문서 작성