# spring-gift-point

## STEP1
- 새로 합의한 API명세서대로 기존 API를 수정한다.
  https://stump-corn-6da.notion.site/API-93cca34e38ed46b09b18262b9fb75a94?pvs=4
  https://stump-corn-6da.notion.site/API-551105c194b94196b3dfeb03220298c4?pvs=4

## STEP2
- 배포 스크립트 작성

## STEP3
- 포인트 충전, 조회 기능 추가
  - 포인트는 Integer 형식으로 최댓값을 넘겨 충전하려고 할 시 충전 실패. 400 상태코드로 응답
- 주문시 포인트 차감 기능 추가
  - 포인트가 부족할 시 주문 실패. 400 상태코드로 응답
- 관리자 페이지 리펙토링
  - 사용자 관련 기능 추가
    - 미인증 상태일경우 login.html 로 redirect.
    - 로그인 성공시 index.html 로 이동. 기존 상품 관련 페이지가 아닌 포인트 충전 및 타 페이지로 이동 가능한 페이지.
  - 상품 페이지 수정
    - 상품 등록, 조회, 수정, 삭제 기능 수정