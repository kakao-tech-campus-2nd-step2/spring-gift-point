# spring-gift-point

## 🚀 1단계 - API 명세

### 개요
- 프론트엔드 협업을 위해 API 검토 및 응답/요청 형식 통일

### 기능 목록
- [X] 팀 내에서 일관된 기준에 따라 API 명세 결정

    <details>
      <summary>확정된 팀 내 API 명세</summary>
    프론트엔드에서 필요로 하는 API에 대해 요청/응답 통일
    
    ### 회원 API
    
    |  | Route | Require Data | Response Data | Method | etc | http status |
      | --- | --- | --- | --- | --- | --- | --- |
    | 회원 가입 | /api/members/register | { "email": "string", "password": "W-^hCQiccwY" } | { "email": "string", "token": "string" } | POST |  | 201 (CREATED) |
    | 로그인 | /api/members/login | { "email": "string", "password": "W-^hCQiccwY" } | { "email": "string", "token": "string" } | POST |  | 200 (OK) |
    
    ### 카테고리 API
    
    |  | Route | Require Data | Response Data | Method | etc | http status |
      | --- | --- | --- | --- | --- | --- | --- |
    | 카테고리 목록 조회 | /api/categories | {} | [ { "id": 0, "name": "string", "color": "string", "imageUrl": "string", "description": "string" } ] | GET |  | 200 (OK) |
    
    ### 상품 API
    
    |  | Route | Require Data | Response Data | Method | etc | http status |
      | --- | --- | --- | --- | --- | --- | --- |
    | 상품 목록 가져오기(페이지네이션) | /api/products?page=0&size=10&sort=name,asc | { "page": 0, "size": 1, "sort": [ "string" ] } | { "totalElements": 0, "totalPages": 0, "size": 0, "content": [ { "id": 1, "name": "Product1", "price": 1000, "imageUrl": "https://via.placeholder.com/150?text=product1", "categoryId": 1, "categoryName": "교환권" } ], "number": 0, "sort": [ { "direction": "string", "nullHandling": "string", "ascending": true, "property": "string", "ignoreCase": true } ], "first": true, "last": true, "numberOfElements": 0, "pageable": { "offset": 0, "sort": [ { "direction": "string", "nullHandling": "string", "ascending": true, "property": "string", "ignoreCase": true } ], "paged": true, "pageSize": 0, "pageNumber": 0, "unpaged": true }, "empty": true } | GET |  | 200 (OK) |
    | 상품 생성 | /api/products |  |  | POST |  |  |
    | 상품 상세 조회 | /api/products/{productId} |  | { "id": 1, "name": "Product1", "price": 1000, "imageUrl": "https://via.placeholder.com/150?text=product1", "categoryId": 1, "categoryName": "교환권" } | GET |  | 200 (OK) |
    
    ### 주문 API
    
    |  | Route | Require Data | Response Data | Method | etc | http status |
      | --- | --- | --- | --- | --- | --- | --- |
    | 주문하기 | /api/orders | { "optionId": 0, "quantity": 0, "message": "string" } | { "id": 0, "optionId": 0, "quantity": 0, "orderDateTime": "2024-07-30T06:42:17.486Z", "message": "string" } | POST |  | 201 (CREATED) |
    
    ### 위시리스트 API
    
    |  | Route | Require Data | Response Data | Method | etc | http status |
      | --- | --- | --- | --- | --- | --- | --- |
    | 위시리스트에 추가하기 | /api/wishes | { "productId": 1 } | { "id": 1, "memberId": 1, "productId": 1 } | POST |  | 201 (CREATED) |
    | 위시리스트 상품 삭제 | /api/wishes/{wishId} | {} | {} | DELETE |  | 200 (OK) |
    | 위시리스트 상품 조회 (페이지네이션) | /api/wishes?page=0&size=10&sort=productId,desc | { "page": 0, "size": 1, "sort": [ "string" ] } | { "totalPages": 0, "totalElements": 0, "first": true, "last": true, "size": 0, "content": [ { "id": 1, "memberId": 1, "productId": 1 } ], "number": 0, "sort": { "empty": true, "sorted": true, "unsorted": true }, "numberOfElements": 0, "pageable": { "offset": 0, "sort": { "empty": true, "sorted": true, "unsorted": true }, "paged": true, "pageNumber": 0, "pageSize": 0, "unpaged": true }, "empty": true } | GET |  | 200 (OK) |
    
    ### 상품 옵션 API
    
    |  | Route | Require Data | Response Data | Method | etc | http status |
      | --- | --- | --- | --- | --- | --- | --- |
    | 상품 옵션 추가 | /api/products/{productId}/options |  |  | POST |  |  |
    | 상품 옵션 목록 조회 | /api/products/{productId}/options |  |  | GET |  |  |
    | 상품 옵션 수정 | /api/products/{productId}/options/{optionId} |  |  | PUT |  |  |
    | 상품 옵션 삭제 | /api/products/{productId}/options/{optionId} |  |  | DELETE |  |  |
    
    통일할 API:
    - 회원 API - 회원 가입, 로그인
    - 카테고리 API - 카테고리 목록 조회
    - 상품 API - 상품 목록 가져오기(페이지네이션), 상품 상세 조회
    - 주문 API - 주문하기
    - 위시리스트 API - 위시리스트에 추가하기, 위시리스트 상품 삭제, 위시리스트 상품 조회 (페이지네이션)

</details>

- [ ] API 수정하여 형식 통일
    - [ ] 회원 API 통일
        - [ ] 회원가입: URL, 메소드 통일
        - [ ] 회원가입: 요청, 응답 통일
        - [ ] 로그인: URL, 메소드 통일
        - [ ] 로그인: 요청, 응답 통일

    - [ ] 카테고리 API
        - [ ] 카테고리 목록 조회: URL, 메소드 통일
        - [ ] 카테고리 목록 조회: 요청, 응답 통일

    - [ ] 상품 API
        - [ ] 상품 목록 가져오기(페이지네이션): URL, 메소드 통일
        - [ ] 상품 목록 가져오기(페이지네이션): 요청, 응답 통일
        - [ ] 상품 상세 조회: URL, 메소드 통일
        - [ ] 상품 상세 조회: 요청, 응답 통일

    - [ ] 주문 API
        - [ ] 주문하기: URL, 메소드 통일
        - [ ] 주문하기: 요청, 응답 통일

    - [ ] 위시리스트 API
        - [ ] 위시리스트에 추가하기: URL, 메소드 통일
        - [ ] 위시리스트에 추가하기: 요청, 응답 통일
        - [ ] 위시리스트 상품 삭제: URL, 메소드 통일
        - [ ] 위시리스트 상품 삭제: 요청, 응답 통일
        - [ ] 위시리스트 상품 조회 (페이지네이션): URL, 메소드 통일
        - [ ] 위시리스트 상품 조회 (페이지네이션): 요청, 응답 통일


### 기술 스택
- Java 21
- Spring Boot 3.3.1
- Gradle 8.4