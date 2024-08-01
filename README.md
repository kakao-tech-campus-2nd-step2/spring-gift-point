# API Documentation

## 회원

### Request

| 이름 | HTTP 메서드 | 경로 | HEADER | Request Body |
| --- | --- | --- | --- | --- |
| 회원가입 | POST | /api/members/register | Content-Type: application/json | email: {문자열(필수)}, password: {문자열(필수)} |
| 로그인 | POST | /api/members/login | Content-Type: application/json | email: {문자열(필수)}, password: {문자열(필수)} |
| 카카오 로그인/가입 | GET | /api/members/kakao/login |  |  |

### Response(성공 시)

| 이름 | 상태 코드 | 경로 | 헤더 | Response Body |
| --- | --- | --- | --- | --- |
| 회원가입 성공 | 201 | /api/members/register | Authorization: Bearer {토큰} |  |
| 로그인 성공 | 200 | /api/members/login | Authorization: Bearer {토큰} |  |

### Response(실패 시)

| 이름 | 경로 | 상태 코드 | 예외 사항 | Response Body |
| --- | --- | --- | --- | --- |
| 회원가입 | /api/members/register | 400 | 잘못된 값 입력 |  |
| 로그인 | /api/members/login | 400 | 잘못된 값 입력 (이메일 형식 틀림, null 값) |  |
| 로그인 | /api/members/login | 403 | 로그인 실패 |  |
| 카카오 로그인/가입 | /api/members/kakao/login | 500 | 서버 내부 오류 |  |

## 카테고리

### Request

| 이름 | HTTP 메서드 | 경로 | HEADER | Request Body |
| --- | --- | --- | --- | --- |
| 카테고리 조회 | GET | /api/categories |  |  |
| 카테고리 추가 | POST | /api/categories | Content-Type: application/json, Authorization: Bearer {토큰} | name: {문자열(필수)}, color: {숫자(필수)}, imageUrl: {문자열(필수)}, description: {문자열} |
| 카테고리 수정 | PUT | /api/categories/{categoryId} | Content-Type: application/json, Authorization: Bearer {토큰} | name: {문자열(필수)}, color: {숫자(필수)}, imageUrl: {문자열(필수)}, description: {문자열} |

### Response(성공 시)

| 이름 | 상태 코드 | 경로 | Response Body |
| --- | --- | --- | --- |
| 카테고리 조회 | 200 | /api/categories | List<Category> categories |
| 카테고리 추가 | 201 | /api/categories | id: {숫자}, name: {문자열}, color: {문자열}, imageUrl: {문자열}, description: {문자열} |
| 카테고리 수정 | 200 | /api/categories/{categoryId} | id: {숫자}, name: {문자열}, color: {문자열}, imageUrl: {문자열}, description: {문자열} |

### Response(실패 시)

| 이름 | 경로 | 상태 코드 | 예외 사항 | Response Body |
| --- | --- | --- | --- | --- |
| 카테고리 조회 | /api/categories | 500 | 서버 내부 오류 |  |
| 카테고리 추가 | /api/categories | 400 | 잘못 된 값 입력, 중복된 값 |  |
| 카테고리 추가 | /api/categories | 401 | 인증 오류 |  |
| 카테고리 추가 | /api/categories | 500 | 서버 내부 오류 |  |
| 카테고리 수정 | /api/categories/{categoryId} | 400 | 잘못 된 값 입력 |  |
| 카테고리 수정 | /api/categories/{categoryId} | 401 | 인증 오류 |  |
| 카테고리 수정 | /api/categories/{categoryId} | 404 | 존재하지 않는 categoryId |  |
| 카테고리 수정 | /api/categories/{categoryId} | 500 | 서버 내부 오류 |  |

## 상품 

### Request

| 이름 | HTTP 메서드 | 경로 | HEADER | Request Body |
| --- | --- | --- | --- | --- |
| 상품 목록 조회 | GET | /api/products?page=0&size=10&sort=name,asc&categoryId=1 | Content-Type: application/json | page: {숫자(필수)}, size: {숫자(필수)}, sort: {문자열(필수)} |
| 상품 조회 | GET | /api/products/{productId} |  |  |
| 상품 추가 | POST | /api/products | Content-Type: application/json, Authorization: Bearer {토큰} | name: {문자열(필수)}, price: {숫자(필수)}, imageUrl: {문자열(필수)}, categoryId: {숫자(필수)}, option: {name: {문자열(필수)}, quantity: {숫자(필수}} |
| 상품 수정 | PUT | /api/products/{productId} | Content-Type: application/json, Authorization: Bearer {토큰} | name: {문자열(필수)}, price: {숫자(필수)}, imageUrl: {문자열(필수)}, categoryId: {숫자(필수)} |
| 상품 삭제 | DELETE | /api/products/{productId} | Authorization: Bearer {토큰} |  |

### Response(성공 시)

| 이름 | 상태 코드 | 경로 | Response Body |
| --- | --- | --- | --- |
| 상품 목록 조회 | 200 | /api/products?page=0&size=10&sort=name,asc&categoryId=1 | content: {Page<Product> products} |
| 상품 조회 | 200 | /api/products/{productId} | id: {숫자}, name: {문자열}, price: {숫자}, imageUrl: {문자열}, categoryId: {숫자}, options: {List<Option>} |
| 상품 추가 | 201 | /api/products | id: {숫자}, name: {문자열}, price: {숫자}, imageUrl: {문자열}, categoryId: {숫자}, option: {name: {문자열}, quantity: {숫자}} |
| 상품 수정 | 200 | /api/products/{productId} | id: {숫자}, name: {문자열}, price: {숫자}, imageUrl: {문자열}, categoryId: {숫자} |
| 상품 삭제 | 204 | /api/products/{productId} |  |

### Response(실패 시)

| 이름 | 경로 | 상태 코드 | 예외 사항 | Response Body |
| --- | --- | --- | --- | --- |
| 상품 목록 조회 | /api/products?page=0&size=10&sort=name,asc&categoryId=1 | 400 | 잘못된 값 입력 |  |
| 상품 목록 조회 | /api/products?page=0&size=10&sort=name,asc&categoryId=1 | 404 | 존재하지 않는 page or categoryId |  |
| 상품 목록 조회 | /api/products?page=0&size=10&sort=name,asc&categoryId=1 | 500 | 서버 내부 오류 |  |
| 상품 추가 | /api/products | 400 | 잘못 된 값 입력 |  |
| 상품 추가 | /api/products | 401 | 인증 오류 |  |
| 상품 추가 | /api/products | 500 | 서버 내부 오류 |  |
| 상품 수정 | /api/products/{productId} | 400 | 잘못 된 값 입력 |  |
| 상품 수정 | /api/products/{productId} | 401 | 인증 오류 |  |
| 상품 수정 | /api/products/{productId} | 404 | 존재하지 않는 productId |  |
| 상품 수정 | /api/products/{productId} | 500 | 서버 내부 오류 |  |
| 상품 삭제 | /api/products/{productId} | 400 | 잘못 된 값 입력 |  |
| 상품 삭제 | /api/products/{productId} | 401 | 인증 오류 |  |
| 상품 삭제 | /api/products/{productId} | 404 | 존재하지 않는 productId |  |
| 상품 삭제 | /api/products/{productId} | 500 | 서버 내부 오류 |  |

## 옵션 

### Request

| 이름 | HTTP 메서드 | 경로 | HEADER | Request Body |
| --- | --- | --- | --- | --- |
| 상품 옵션 목록 조회 | GET | /api/products/{productId}/options |  |  |
| 상품 옵션 추가 | POST | /api/products/{productId}/options | Content-Type: application/json, Authorization: Bearer {토큰} | name: {문자열(필수)}, quantity: {숫자(필수)} |
| 상품 옵션 수정 | PUT | /api/products/{productId}/options/{optionId} | Content-Type: application/json, Authorization: Bearer {토큰} | name: {문자열(필수)}, quantity: {숫자(필수)} |
| 상품 옵션 삭제 | DELETE | /api/products/{productId}/options/{optionId} | Authorization: Bearer {토큰} |  |

### Response(성공 시)

| 이름 | 상태 코드 | 경로 | Response Body |
| --- | --- | --- | --- |
| 상품 옵션 목록 조회 | 200 | /api/products/{productId}/options | List<Option> options |
| 상품 옵션 추가 | 201 | /api/products/{productId}/options | id: {숫자}, name: {문자열}, quantity: {숫자}, productId: {숫자} |
| 상품 옵션 수정 | 200 | /api/products/{productId}/options/{optionId} | id: {숫자}, name: {문자열}, quantity: {숫자}, productId: {숫자} |
| 상품 옵션 삭제 | 204 | /api/products/{productId}/options/{optionId} |  |

### Response(실패 시)

| 이름 | 경로 | 상태 코드 | 예외 사항 | Response Body |
| --- | --- | --- | --- | --- |
| 상품 옵션 목록 조회 | /api/products/{productId}/options | 404 | 존재하지 않는 productId |  |
| 상품 옵션 목록 조회 | /api/products/{productId}/options | 500 | 서버 내부 오류 |  |
| 상품 옵션 추가 | /api/products/{productId}/options | 400 | 잘못 된 값 입력 |  |
| 상품 옵션 추가 | /api/products/{productId}/options | 401 | 인증 오류 |  |
| 상품 옵션 추가 | /api/products/{productId}/options | 500 | 서버 내부 오류 |  |
| 상품 옵션 수정 | /api/products/{productId}/options/{optionId} | 400 | 잘못 된 값 입력 |  |
| 상품 옵션 수정 | /api/products/{productId}/options/{optionId} | 401 | 인증 오류 |  |
| 상품 옵션 수정 | /api/products/{productId}/options/{optionId} | 404 | 존재하지 않는 optionId |  |
| 상품 옵션 수정 | /api/products/{productId}/options/{optionId} | 500 | 서버 내부 오류 |  |
| 상품 옵션 삭제 | /api/products/{productId}/options/{optionId} | 400 | 잘못 된 값 입력 |  |
| 상품 옵션 삭제 | /api/products/{productId}/options/{optionId} | 401 | 인증 오류 |  |
| 상품 옵션 삭제 | /api/products/{productId}/options/{optionId} | 404 | 존재하지 않는 optionId |  |
| 상품 옵션 삭제 | /api/products/{productId}/options/{optionId} | 500 | 서버 내부 오류 |  |

## 위시리스트

### Request

| 이름 | HTTP 메서드 | 경로 | HEADER | Request Body |
| --- | --- | --- | --- | --- |
| 위시 리스트 조회 | GET | /api/wishes?page=0&size=10&sort=name,asc | Content-Type: application/json, Authorization: Bearer {토큰} | page: {숫자(필수)}, size: {숫자(필수)}, sort: {문자열(필수)}, categoryId: {숫자(필수)} |
| 위시리스트 상품 추가 | POST | /api/wishes | Content-Type: application/json, Authorization: Bearer {토큰} | optionId: {숫자(필수)} |
| 위시리스트 상품 삭제 | DELETE | /api/wishes/{wishId} | Authorization: Bearer {토큰} | - |

### Response (성공 시)

| 이름 | 상태 코드 | 경로 | Response Body |
| --- | --- | --- | --- |
| 위시 리스트 조회 | 200 | /api/wishes?page=0&size=10&sort=name,asc | content: {Page<Wish> wishes} |
| 위시리스트 상품 추가 | 201 | /api/wishes | id: {숫자}, productId: {숫자} |
| 위시리스트 상품 삭제 | 204 | /api/wishes/{wishId} | - |

### Response (실패 시)

| 이름 | 경로 | 상태 코드 | 예외 사항 | Response Body |
| --- | --- | --- | --- | --- |
| 위시 리스트 조회 | /api/wishes?page=0&size=10&sort=name,asc | 400 | 잘못 된 값 입력 | - |
| 위시 리스트 조회 | /api/wishes?page=0&size=10&sort=name,asc | 404 | 존재하지 않는 page | - |
| 위시 리스트 조회 | /api/wishes?page=0&size=10&sort=name,asc | 500 | 서버 내부 오류 | - |
| 위시리스트 상품 추가 | /api/wishes | 400 | 잘못 된 값 입력 | - |
| 위시리스트 상품 추가 | /api/wishes | 401 | 인증 오류 | - |
| 위시리스트 상품 추가 | /api/wishes | 500 | 서버 내부 오류 | - |
| 위시리스트 상품 삭제 | /api/wishes/{wishId} | 400 | 잘못 된 값 입력 | - |
| 위시리스트 상품 삭제 | /api/wishes/{wishId} | 401 | 인증 오류 | - |
| 위시리스트 상품 삭제 | /api/wishes/{wishId} | 404 | 잘못된 Id 입력 | - |
| 위시리스트 상품 삭제 | /api/wishes/{wishId} | 500 | 서버 내부 오류 | - |

## 주문 

### Request

| 이름 | HTTP 메서드 | 경로 | HEADER | Request Body |
| --- | --- | --- | --- | --- |
| 주문하기 | GET | /api/orders | Content-Type: application/json, Authorization: Bearer {토큰} | optionId: {숫자(필수)}, quantity: {숫자(필수)}, message: {문자열(필수)} |

### Response (성공 시)

| 이름 | 상태 코드 | 경로 | Response Body |
| --- | --- | --- | --- |
| 주문하기 | 200 | /api/orders | optionId: {숫자}, quantity: {숫자}, orderDateTime: {문자열}, message: {문자열} |

### Response (실패 시)

| 이름 | 경로 | 상태 코드 | 예외 사항 | Response Body |
| --- | --- | --- | --- | --- |
| 주문하기 | /api/orders | 400 | 잘못된 수량 입력 | - |
| 주문하기 | /api/orders | 401 | 인증 오류 | - |
| 주문하기 | /api/orders | 404 | 존재하지 않는 productId | - |
| 주문하기 | /api/orders | 500 | 서버 내부 오류 | - |