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
</details>