# spring-gift-point
# 1단계 - 카카오 로그인

## 요구사항
### 기능 요구사항
작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영한다.
- 팀 내에서 일관된 기준을 정하여 API 명세를 결정한다.
- 때로는 클라이언트의 편의를 위해 API 명세를 결정하는 것이 좋다.

# 2단계 - 배포하기

## 요구사항
### 기능 요구사항
지금까지 만든 선물하기 서비스를 배포하고 클라이언트와 연동할 수 있어야 한다.
1. 지속적인 배포를 위한 배포 스크립트를 작성한다.
2. 클라이언트와 API 연동 시 발생하는 보안 문제에 대응한다.
3. 서버와 클라이언트의 Origin이 달라 요청을 처리할 수 없는 경우를 해결한다.
4. HTTPS는 필수는 아니지만 팀 내에서 논의하고 필요한 경우 적용한다.

# 3단계 - 포인트

## 요구사항
### 기능 요구사항
상품 구매에 사용할 수 있는 포인트 기능을 구현한다.

1. 포인트는 사용자별로 보유한다.
2. 포인트는 구매 금액의 절반을 적립한다.
3. 상품 구매 시 보유 포인트 이하의 포인트를 사용하여 결제할 수 있다.
4. 관리자 화면에서 포인트를 충전할 수 있다.