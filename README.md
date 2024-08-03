# spring-gift-point

---
<details>
<summary><strong>1단계 - API 명세</strong></summary>

- 팀원들과 API 명세서 작성
- 작성된 API 명세서 바탕으로 코드 수정
  - Model 수정
    - Category
      - color, imageUrl, description 추가
    - Member
      - name 지우기
    - Wishlist
      - product 대신 option 사용
  - DB 수정
    - schema.sql
    - data.sql
  - DTO 수정 및 만들기
    - Category
      - CategoryRequestDTO
      - CategoryResponseDTO
    - Member
      - LoginRequestDTO
      - RegisterRequestDTO
    - Option
      - OptionRequestDTO
      - OptionResponseDTO
    - Order
      - OrderRequestDTO
      - OrderResponseDTO
    - Product
      - ProductAddRequestDTO
      - ProductAddResponseDTO
      - ProductGetResponseDTO
      - ProductUpdateRequestDTO
      - ProductUpdateResponseDTO
    - Page
      - PageRequestDTO
      - ProductPageResponseDTO
      - WishlistPageResponseDTO
    - Wishlist
      - WishlistRequestDTO
      - WishlistResponseDTO
  - Repository 수정
  - Service 수정
    - 생성한 DTO를 사용하도록 수정
  - Controller 수정
    - 생성한 DTO를 사용하도록 수정
    - Controller -> RestController
- 커스텀 예외 만들기
  - 커스텀 예외 사용하기
</details>

---

<details>
<summary><strong>2단계 - 배포하기</strong></summary>

- 배포 쉘 스크립트 작성
- CORS 작성
- CORS 테스트 코드 작성
</details>

---

<details>
<summary><strong>3단계 - 포인트</strong></summary>

- Member에 포인트 추가하기
- 포인트 조회 기능 만들기
- 주문 시 포인트 차감 기능 만들기
  - 주문 가격보다 포인트 부족하면 에러
- 관리자 화면에서 포인트 충전 가능하게 하기
</details>