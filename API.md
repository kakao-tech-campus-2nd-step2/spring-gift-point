### API Readme

---

## 목차
- [Authentication API](#authentication-api)
- [Category API](#category-api)
- [Member API](#member-api)
- [Option API](#option-api)
- [Order API](#order-api)
- [Product API](#product-api)
- [Wish API](#wish-api)

---

## Authentication API
### Endpoint: `/auth`
<details>
<summary>POST: 로그인 요청</summary>

#### Request:
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "email",          // String, 필수, 유효한 이메일 형식
  "password": "password"     // String, 필수, 최소 길이: 8, 최대 길이: 100
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

##### Body:
```json
{
  "code": "A001",
  "message": "로그인 성공"
}
```

</details>

<details>
<summary>GET: 카카오 로그인 리다이렉트 요청</summary>

#### Request:
```http
GET http://localhost:8080/api/auth/kakao
```

#### Response:

##### Header:
```http
HTTP/1.1 302 Found
Location: https://kauth.kakao.com/oauth/authorize?response_type=code&client_id={client_id}&redirect_uri={redirect_uri}
```

</details>

---

## Category API
### Endpoint: `/categories`
<details>
<summary>GET: 카테고리 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/categories
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P001",
  "message": "모든 카테고리 조회 성공",
  "data": [
    {
      "id": 1,
      "name": "교환권",
      "color": "#6c95d1",
      "imageUrl": "https://example.com/image.jpg",
      "description": "카테고리 설명"
    }
  ]
}
```

</details>

<details>
<summary>GET: 특정 카테고리 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/categories/1
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P002",
  "message": "단일 카테고리 조회 성공",
  "data": {
    "id": 1,
    "name": "교환권",
    "color": "#6c95d1",
    "imageUrl": "https://example.com/image.jpg",
    "description": "카테고리 설명"
  }
}
```

</details>

<details>
<summary>POST: 새로운 카테고리 추가</summary>

#### Request:
```http
POST http://localhost:8080/api/categories
Content-Type: application/json

{
  "name": "컴퓨터",            // String, 필수, 최소 길이: 1, 최대 길이: 100
  "color": "#123",           // String, 필수, 유효한 색상 코드 형식
  "imageUrl": "http://hello",// String, 필수, 유효한 URL 형식
  "description": "description" // String, 선택, 최대 길이: 255
}
```

#### Response:

##### Header:
```http
HTTP/1.1 201 Created
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P003",
  "message": "카테고리 추가 성공"
}
```

</details>

<details>
<summary>PUT: 카테고리 업데이트</summary>

#### Request:
```http
PUT http://localhost:8080/api/categories/1
Content-Type: application/json

{
  "name": "카테카테",            // String, 필수, 최소 길이: 1, 최대 길이: 100
  "color": "#123",           // String, 필수, 유효한 색상 코드 형식
  "imageUrl": "http://hello",// String, 필수, 유효한 URL 형식
  "description": "description" // String, 선택, 최대 길이: 255
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P004",
  "message": "카테고리 수정 성공"
}
```

</details>

<details>
<summary>DELETE: 카테고리 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/categories/1
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P005",
  "message": "카테고리 삭제 성공"
}
```

</details>

---

## Member API
### Endpoint: `/members`
<details>
<summary>GET: 회원 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/members
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "M001",
  "message": "모든 회원 조회 성공",
  "data": [
    {
      "id": 1,
      "memberType": "USER",
      "email": "email",
      "password": "password",
      "nickname": "nickname",
      "accessToken": null
    },
    {
      "id": 2,
      "memberType": "USER",
      "email": "email1",
      "password": "password1",
      "nickname": "nickname1",
      "accessToken": null
    }
  ]
}
```

</details>

<details>
<summary>GET: 특정 회원 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/members/3
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "M002",
  "message": "단일 회원 조회 성공",
  "data": {
    "id": 3,
    "memberType": "USER",
    "email": "email2",
    "password": "password2",
    "nickname": "nickname2",
    "accessToken": null
  }
}
```

</details>

<details>
<summary>POST: 새로운 회원 추가</summary>

#### Request:
```http
POST http://localhost:8080/api/members
Content-Type: application/json

{
  "email": "email5",          // String, 필수, 유효한 이메일 형식
  "password": "password3",    // String, 필수, 최소 길이: 8, 최대 길이: 100
  "nickName": "nickname3"     // String, 필수, 최소 길이: 1, 최대 길이: 50
}
```

#### Response:

##### Header:
```http
HTTP/1.1 201 Created
Content-Type: application/json
```

##### Body:
```json
{
  "code": "M003",
  "message": "회원 가입 성공"
}
```

</details>

<details>
<summary>PUT: 회원 정보 업데이트</summary>

#### Request:
```http
PUT http://localhost:8080/api/members/1
Content-Type: application/json

{
  "email": "email10",          // String, 필수, 유효한 이메일 형식
  "password": "password3",     // String, 필수, 최소 길이: 8, 최대 길이: 100
  "nickName": "nickname3"      // String, 필수, 최소 길이: 1, 최대 길이: 50
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "M004",
  "message": "회원 수정 성공"
}
```

</details>

<details>
<summary>DELETE: 회원 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/members/1
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "M005",
  "message": "회원 삭제 성공"
}
```

</details>

---

## Option API
### Endpoint: `/options`
<details>
<summary>GET: 옵션 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/options
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O001",
  "message": "모든 옵션 조회 성공",
  "data": [
    {
      "id": 1,
      "name": "name1",
      "count": 20000,
      "productId": 1
    },
    {
      "id": 2,
      "name": "name2",
      "count": 20000,
      "productId": 1
    }
  ]
}
```

</details>

<details>
<summary>GET: 특정 제품의 옵션 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/options/products/1
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O002",
  "message": "특정 제품의 옵션 조회 성공",
  "data": [
    {
      "id": 1,
      "name": "name1",
      "count": 20000,
      "productId": 1
    },
    {
      "id": 2,
      "name": "name2",
      "count": 20000,
      "productId": 1
    }
  ]
}
```

</details>

<details>
<summary>GET: 특정 옵션 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/options/1
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O003",
  "message": "단일 옵션 조회 성공",
  "data": {
    "id": 1,
    "name": "name1",
    "count": 20000,
    "productId": 1
  }
}
```

</details>

<details>
<summary>POST: 새로운 옵션 추가</summary>

#### Request:
```http
POST http://localhost:8080/api/options
Content-Type: application/json

{
  "name": "123",       // String, 필수, 최소 길이: 1, 최대 길이: 100
  "count": 1000,       // Integer, 필수, 최소: 1
  "productId": 1       // Integer, 필수, 유효한 제품 ID
}
```

#### Response:

##### Header:
```http
HTTP/1.1 201 Created
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O004",
  "message": "옵션 생성 성공"
}
```

</details>

<details>
<summary>PUT: 옵션 업데이트</summary>

#### Request:
```http
PUT http://localhost:8080/api/options/1
Content-Type: application/json

{
  "name": "aaa",       // String, 필수, 최소 길이: 1, 최대 길이: 100
  "count": 123,        // Integer, 필수, 최소: 1
  "productId": 1       // Integer, 필수, 유효한 제품 ID
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O005",
  "message": "옵션 수정 성공"
}
```

</details>

<details>
<summary>DELETE: 옵션 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/options/1
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O006",
  "message": "옵션 삭제 성공"
}
```

</details>

---

## Order API
### Endpoint: `/orders`
<details>
<summary>GET: 주문 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/orders
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O007",
  "message": "모든 주문 조회 성공",
  "data": [
    {
      "id": 1,
      "count": 12,
      "message": "message",
      "optionId": 2
    }
  ]
}
```

</details>

<details>
<summary>POST: 새로운 주문 추가</summary>

#### Request:
```http
POST http://localhost:8080/api/orders
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "count": 12,         // Integer, 필수, 최소: 1
  "message": "message",// String, 선택, 최대 길이: 255
  "optionId": 1        // Integer, 필수, 유효한 옵션 ID
}
```

#### Response:

##### Header:
```http
HTTP/1.1 201 Created
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O008",
  "message": "주문 생성 성공"
}
```

</details>

<details>
<summary>PUT: 주문 업데이트</summary>

#### Request:
```http
PUT http://localhost:8080/api/orders/1
Content-Type: application/json

{
  "count": 123,        // Integer, 필수, 최소: 1
  "message": "새로운 메시지",  // String, 선택, 최대 길이: 255
  "optionId": 1        // Integer, 필수, 유효한 옵션 ID
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O009",
  "message": "주문 수정 성공"
}
```

</details>

<details>
<summary>DELETE: 주문 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/orders/1
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "O010",
  "message": "주문 삭제 성공"
}
```

</details>

---

## Product API
### Endpoint: `/products`
<details>
<summary>GET: 모든 제품 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/products/all
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P001",
  "message": "모든 제품 조회 성공",
  "data": [
    {
      "id": 1,
      "name": "test1",
      "price": 10000,
      "imageUrl": "http://example.com/image.jpg",
      "category": null
    },
    {
      "id": 2,
      "name": "test2",
      "price": 20000,
      "imageUrl": "http://example.com/image2.jpg",
      "category": null
    }
  ]
}
```

</details>

<details>
<summary>GET: 제품 페이지 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/products?page=0
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P002",
  "message": "제품 페이지 조회 성공",
  "data": {
    "page": 3,
    "products": [
      {
        "id": 1,
        "name": "test1",
        "price": 10000,
        "imageUrl": "http://example.com/image.jpg",
        "category": null
      },
      {
        "id": 2,
        "name": "test2",
        "price": 20000,
        "imageUrl": "http://example.com/image2.jpg",
        "category": null
      }
    ]
  }
}
```

</details>

<details>
<summary>GET: 특정 제품 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/products/1
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P003",
  "message": "단일 제품 조회 성공",
  "data": {
    "id": 1,
    "name": "test1",
    "price": 10000,
    "imageUrl": "http://example.com/image.jpg",
    "category": null
  }
}
```

</details>

<details>
<summary>POST: 새로운 제품 추가</summary>

#### Request:
```http
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "!@#!$Q@#%",        // String, 필수, 최소 길이: 1, 최대 길이: 100
  "price": 4500,              // Integer, 필수, 최소: 0
  "imageUrl": "http://hello", // String, 필수, 유효한 URL 형식
  "categoryId": 1             // Integer, 필수, 유효한 카테고리 ID
}
```

#### Response:

##### Header:
```http
HTTP/1.1 201 Created
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P004",
  "message": "제품 추가 성공"
}
```

</details>

<details>
<summary>PUT: 제품 정보 업데이트</summary>

#### Request:
```http
PUT http://localhost:8080/api/products/1
Content-Type: application/json

{
  "name": "아메리카노2",        // String, 필수, 최소 길이: 1, 최대 길이: 100
  "price": 5000,              // Integer, 필수, 최소: 0
  "imageUrl": "http://hello", // String, 필수, 유효한 URL 형식
  "categoryId": 2             // Integer, 필수, 유효한 카테고리 ID
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P005",
  "message": "제품 수정 성공"
}
```

</details>

<details>
<summary>DELETE: 제품 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/products/3
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "P006",
  "message": "제품 삭제 성공"
}
```

</details>

---

## Wish API
### Endpoint: `/wishes`
<details>
<summary>GET: 모든 위시 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/wishes/all
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "W001",
  "message": "모든 위시 리스트 조회 성공",
  "data": [
    {
      "id": 1,
      "productId": 1,
      "productName": "test1",
      "imageUrl": "http://example.com/image.jpg",
      "productCount": 10
    },
    {
      "id": 2,
      "productId": 2,
      "productName": "test2",
      "imageUrl": "http://example.com/image2.jpg",
      "productCount": 10
    }
  ]
}
```

</details>

<details>
<summary>GET: 특정 페이지의 위시 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/wishes?page=2
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "W002",
  "message": "특정 페이지의 위시 리스트 조회 성공",
  "data": {
    "id": 2,
    "productId": 2,
    "productName": "test2",
    "imageUrl": "http://example.com/image2.jpg",
    "productCount": 10
  }
}
```

</details>

<details>
<summary>GET: 특정 위시 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/wishes/2
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "W003",
  "message": "단일 위시 조회 성공",
  "data": {
    "id": 2,
    "productId": 2,
    "productName": "test2",
    "imageUrl": "http://example.com/image2.jpg",
    "productCount": 10
  }
}
```

</details>

<details>
<summary>POST: 새로운 위시 추가</summary>

#### Request:
```http
POST http://localhost:8080/api/wishes
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "memberId": 1,     // Integer, 필수, 유효한 회원 ID
  "productId": 2,    // Integer, 필수, 유효한 제품 ID
  "productCount": 5  // Integer, 필수, 최소: 1
}
```

#### Response:

##### Header:
```http
HTTP/1.1 201 Created
Content-Type: application/json
```

##### Body:
```json
{
  "code": "W004",
  "message": "위시 추가 성공"
}
```

</details>

<details>
<summary>PUT: 위시 업데이트</summary>

#### Request:
```http
PUT http://localhost:8080/api/wishes/1
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "memberId": 1,     // Integer, 필수, 유효한 회원 ID
  "productId": 2,    // Integer, 필수, 유효한 제품 ID
  "productCount": 10 // Integer, 필수, 최소: 1
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "W005",
  "message": "위시 수정 성공"
}
```

</details>

<details>
<summary>DELETE: 위시 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/wishes/1
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
```json
{
  "code": "W006",
  "message": "위시 삭제 성공"
}
```

</details>

---
