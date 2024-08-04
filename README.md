# spring-gift-point
---

# step 1

## API 명세

### member

- [x] 일반 회원가입 ✅
    - 이미 있는 이메일이면 409 Conflict
- [x] 일반 로그인 ✅
    - 존재하지 않는 이메일/틀린 비번이면 401 Unauthorized

### product

- [x] 제품 옵션 조회 ✅
    - 제품이 없으면 404 Not Found
- [x] 특정 카테고리별 상품 조회 ✅
    - 테스트 작성해야 함

### category

- [x] 모든 카테고리 조회 ✅

### wish

- [x] 위시리스트에 추가 ✅
- [x] 위시리스트 삭제 ✅
- [x] 로그인한 회원의 위시리스트 조회 ✅

### order

- [x] 주문 생성 ✅

---

# step 2

## 배포하기

---

# step 3

## 포인트

### 기능 요구 사항

상품 구매에 사용할 수 있는 포인트 기능을 구현
- [x] 관리자 화면에서 포인트를 충전 가능
  * 관리자(admin@naver.com)만 /api/members/charge에 접근할 수 있도록 filter 추가
- [x] 포인트는 사용자별로 보유 가능
  * 마이페이지에서 확인
  * /me
  * /api/members/me

## 리뷰 반영 수정 사항
- [x] 옵션 subtract 낙관적 락 적용
- [x] 디폴트 카테고리, 옵션 id를 -1로
- [x] 배포 스크립트
- [x] aop 적용
- [ ] entity, dto, response, request 로 나누기 + camelCase, snake_case 수정
