# spring-gift-point
# 5주차 피드백
- [x] Gson을 JSONObject로 변경
# step 1
- [x] api 명세에 맞춰 controller path 수정
- [x] api 명세에 맞춰 변경
  - [x] 상품 목록 조회 (페이지네이션 적용)
  - [x] Request 일관 되게 변경
    - [x] json format으로 나타내기
    - [x] Request 요청에서 id 삭제
  - [x] Response 일관되게 변경
    - [x] 목록 반환은 Contents로 이름 통일
    - [x] status 값 수정 (get - 200, post - 201, put - 200, delete - 204)
  - [x] 카카오 토큰 헤더 이름 X-GATEWAY-TOKEN으로 변경