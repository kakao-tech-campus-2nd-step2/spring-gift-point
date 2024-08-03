# spring-gift-point




# step1

## Refactor: 회원 기능 API 수정

- **회원 가입 API 구현**  
  Endpoint: `/api/members/register`

- **로그인 API 구현**  
  Endpoint: `/api/members/login`

## Refactor: 카테고리 기능 API 수정

- **카테고리 생성 API 구현**  
  Endpoint: `/api/categories`

- **카테고리 수정 API 구현**  
  Endpoint: `/api/categories/{categoryId}`

- **카테고리 목록 조회 API 구현**  
  Endpoint: `/api/categories`

## Refactor: 상품 기능 API 수정

- **상품 생성 API 구현**  
  Endpoint: `/api/products`

- **상품 조회 API 구현**  
  Endpoint: `/api/products/{productId}`

- **상품 수정 API 구현**  
  Endpoint: `/api/products/{productId}`

- **상품 삭제 API 구현**  
  Endpoint: `/api/products/{productId}`

- **상품 목록 조회 API 구현 (페이지네이션 적용)**  
  Endpoint: `/api/products?page=0&size=10&sort=name,asc&categoryId=1`

## Refactor: 상품 옵션 기능 API 수정

- **상품 옵션 추가 API 구현**  
  Endpoint: `/api/products/{productId}/options`

- **상품 옵션 수정 API 구현**  
  Endpoint: `/api/products/{productId}/options/{optionId}`

- **상품 옵션 삭제 API 구현**  
  Endpoint: `/api/products/{productId}/options/{optionId}`

- **상품 옵션 목록 조회 API 구현**  
  Endpoint: `/api/products/{productId}/options`

## Refactor: 위시 리스트 기능 API 수정

- **위시 리스트 상품 추가 API 구현**  
  Endpoint: `/api/wishes`

- **위시 리스트 상품 삭제 API 구현**  
  Endpoint: `/api/wishes/{wishId}`

- **위시 리스트 상품 조회 API 구현 (페이지네이션 적용)**  
  Endpoint: `/api/wishes?page=0&size=10&sort=createdDate,desc`

## Refactor: 주문 기능 API 수정

- **주문하기 API 구현**  
  Endpoint: `/api/orders`

- **주문 목록 조회 API 구현 (페이지네이션 적용)**  
  Endpoint: `/api/orders?page=0&size=10&sort=orderDateTime,desc`
