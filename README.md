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

| 제목                     | 메서드 | URL                             | 요청 컨텐트 타입 / 요청 객체 | 응답 객체                                         | 설명                                           |
|----------|--------|---------------------|---------------------|-----------------------|------------------------------------------------|
| 카카오 로그인 리다이렉트  | GET    | /api/auth/kakao                 | -                            | Status: 302 Found<br>Header: Location: [Kakao Auth URL] | 카카오 로그인을 위한 리다이렉트 URL을 반환합니다. |
| 카카오 로그인            | GET    | /api/auth/kakao/callback        | -                            | Status: 200 OK<br>Header: Authorization: Bearer [Token] | 카카오 로그인 후 JWT 토큰을 반환합니다.          |

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

<details>
<summary>GET: 카카오 로그인</summary>

#### Request:
```http
GET http://localhost:8080/api/auth/kakao/callback
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

</details>

---

## Category API
### Endpoint: `/categories`

| 제목                 | 메서드 | URL                                  | 요청 컨텐트 타입 / 요청 객체                     | 응답 객체                         | 설명                                  |
|----------------------|--------|--------------------------------------|------------------------------------------------|-----------------------------------|---------------------------------------|
| 카테고리 목록 조회    | GET    | /api/categories                      | Content-Type: application/json                 | Status: 200 OK<br>Body: [categories array] | 모든 카테고리 정보를 반환합니다.       |
| 특정 카테고리 조회   | GET    | /api/categories/{id}                 | Content-Type: application/json                 | Status: 200 OK<br>Body: [category object] | ID에 해당하는 카테고리 정보를 반환합니다. |
| 새로운 카테고리 추가 | POST   | /api/categories                      | Content-Type: application/json<br>{category data} | Status: 200 OK                     | 새로운 카테고리를 추가합니다.           |
| 카테고리 삭제       | DELETE | /api/categories/{category_id}        | Content-Type: application/json                 | Status: 200 OK                     | 지정된 ID의 카테고리를 삭제합니다.       |


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
  "categories": [
    {
      "id": 1,
      "name": "교환권",
      "color": "#6c95d1",
      "image_url": "https://example.com/image.jpg",
      "description": "카테고리 설명"
    },
    {
      "id": 1,
      "name": "교환권",
      "color": "#6c95d1",
      "image_url": "https://example.com/image.jpg",
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
  "category": {
    "id": 1,
    "name": "교환권",
    "color": "#6c95d1",
    "image_url": "https://example.com/image.jpg",
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
  "name": "컴퓨터",            
  "color": "#123",           
  "image_url": "http://hello",
  "description": "description" 
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:

</details>


<details>
<summary>DELETE: 카테고리 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/categories/{category_id}
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:

</details>

---

## Member API
### Endpoint: `/members`

| 제목             | 메서드 | URL                              | 요청 컨텐트 타입 / 요청 객체                | 응답 객체                                         | 설명                        |
|------------------|--------|----------------------------------|---------------------------------------------|---------------------------------------------------|-----------------------------|
| 로그인 요청       | POST   | /api/members/login               | Content-Type: application/json<br>{credentials} | Status: 200 OK<br>Header: Authorization: Bearer [Token] | 사용자 로그인을 처리합니다. |
| 새로운 회원 추가  | POST   | /api/members/register            | Content-Type: application/json<br>{user data}    | Status: 200 OK                                    | 새로운 회원을 추가합니다.   |


<details>
<summary>POST: 로그인 요청</summary>

#### Request:
```http
POST http://localhost:8080/api/members/login
Content-Type: application/json

{
  "email": "email",          
  "password": "password"     
}
```

#### Response:


##### Header:
```http
HTTP/1.1 200 OK
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```


</details>


<details>
<summary>POST: 새로운 회원 추가</summary>

#### Request:
```http
POST http://localhost:8080/api/members/register
Content-Type: application/json

{
  "email": "email5",          
  "password": "password3"
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:

</details>

---

## Option API
### Endpoint: `/options`

| 제목                     | 메서드 | URL                              | 요청 컨텐트 타입 / 요청 객체                | 응답 객체                         | 설명                                      |
|--------------------------|--------|----------------------------------|---------------------------------------------|-----------------------------------|-------------------------------------------|
| 특정 제품의 옵션 목록 조회 | GET    | /api/options/{product_id}        | Content-Type: application/json             | Status: 200 OK<br>Body: [options array] | 제품 ID에 해당하는 모든 옵션을 반환합니다. |
| 새로운 옵션 추가          | POST   | /api/options                     | Content-Type: application/json<br>{option data} | Status: 200 OK                     | 제품에 새로운 옵션을 추가합니다.           |
| 옵션 삭제                 | DELETE | /api/options/{option_id}         | Content-Type: application/json             | Status: 200 OK                     | 지정된 ID의 옵션을 삭제합니다.             |


<details>
<summary>GET: 특정 제품의 옵션 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/options/{product_id}
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
  "options": [
    {
      "id": 1,
      "name": "name1",
      "quantity": 20000
    },
    {
      "id": 2,
      "name": "name2",
      "quantity": 20000
    }
  ]
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
  "name": "name",       
  "quanatity": 1000,       
  "product_id": 1       
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:

</details>


<details>
<summary>DELETE: 옵션 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/options/{option_id}
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:


</details>

---

## Order API
### Endpoint: `/orders`

| 제목                 | 메서드 | URL                               | 요청 컨텐트 타입 / 요청 객체                    | 응답 객체                                   | 설명                                     |
|----------------------|--------|-----------------------------------|------------------------------------------------|---------------------------------------------|-----------------------------------------|
| 새로운 주문 추가        | POST   | /api/orders                          | Authorization: Bearer [Token]<br>Content-Type: application/json<br>{order data} | Status: 200 OK               |   새로운 주문을 추가합니다.                        |
<details>
<summary>POST: 새로운 주문 추가</summary>

#### Request:
```http
POST http://localhost:8080/api/orders
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "quantity": 12,         
  "message": "message",
  "option_id": 1    
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:


</details>


---

## Product API
### Endpoint: `/products`

| 제목                 | 메서드 | URL                               | 요청 컨텐트 타입 / 요청 객체                    | 응답 객체                                   | 설명                                     |
|----------------------|--------|-----------------------------------|------------------------------------------------|---------------------------------------------|------------------------------------------|
| 제품 페이지 조회     | GET    | /api/products?page={page_num}     | Content-Type: application/json                 | Status: 200 OK<br>Body: [products page data] | 페이지네이션을 이용한 제품 목록을 반환합니다. |
| 특정 제품 조회       | GET    | /api/products/{product_id}        | Content-Type: application/json                 | Status: 200 OK<br>Body: [product object]     | 제품 ID에 해당하는 상세 제품 정보를 반환합니다. |
| 새로운 제품 추가     | POST   | /api/products                     | Content-Type: application/json<br>{product data} | Status: 201 Created<br>Body: [success message] | 새로운 제품을 추가합니다.                  |
| 제품 정보 업데이트   | PUT    | /api/products/{product_id}        | Content-Type: application/json<br>{product data} | Status: 200 OK                              | 지정된 제품의 정보를 업데이트합니다.       |
| 제품 삭제           | DELETE | /api/products/{product_id}        | Content-Type: application/json                 | Status: 200 OK                              | 지정된 제품을 삭제합니다.                 |


<details>
<summary>GET: 제품 페이지 조회</summary>

Default 상품 반환 개수 : 10개

#### Request:
```http
GET http://localhost:8080/api/products?page={page_num}
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
  "data": {
    "total_page": 3,
    "products": [
      {
        "id": 1,
        "name": "test1",
        "price": 10000,
        "image_url": "http://example.com/image.jpg",
        "category_id": 1
      },
      {
        "id": 2,
        "name": "test2",
        "price": 20000,
        "image_url": "http://example.com/image2.jpg",
        "category_id": 1
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
GET http://localhost:8080/api/products/{product_id}
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
    "id": 1,
    "name": "test1",
    "price": 10000,
    "image_url": "http://example.com/image.jpg",
    "category_id": 1
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
  "name": "name",        
  "price": 4500,              
  "image_url": "http://hello", 
  "category_id": 1,
  "options": [
    {
      "name": "name1",
      "quantity": 20000
    },
    {
      "name": "name2",
      "quantity": 20000
    }
  ]
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
PUT http://localhost:8080/api/products/{product_id}
Content-Type: application/json

{
  "name": "아메리카노2",        
  "price": 5000,              
  "image_url": "http://hello", 
  "category_id": 2             
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:

</details>

<details>
<summary>DELETE: 제품 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/products/{product_id}
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:

</details>

---

## Wish API
### Endpoint: `/wishes`


| 제목                     | 메서드 | URL                            | 요청 컨텐트 타입 / 요청 객체                    | 응답 객체                                   | 설명                                       |
|--------------------------|--------|--------------------------------|------------------------------------------------|---------------------------------------------|--------------------------------------------|
| 특정 페이지의 위시 목록 조회 | GET    | /api/wishes?page={page_num}    | Authorization: Bearer [Token]<br>Content-Type: application/json | Status: 200 OK<br>Body: [wishes data]      | 페이지네이션을 이용한 위시 목록을 반환합니다. |
| 새로운 위시 추가          | POST   | /api/wishes                    | Authorization: Bearer [Token]<br>Content-Type: application/json<br>{wish data} | Status: 200 OK                              | 새로운 위시를 추가합니다.                    |
| 위시 삭제                 | DELETE | /api/wishes/{wish_id}          | Content-Type: application/json                 | Status: 200 OK                              | 지정된 위시를 삭제합니다.                    |



<details>
<summary>GET: 특정 페이지의 위시 목록 조회</summary>

#### Request:
```http
GET http://localhost:8080/api/wishes?page={page_num}
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
  "data": {
    "total_page": 3,
    "wishes": [
      {
        "id": 21,
        "product_id": 21,
        "product_name": "test21",
        "image_url": "http://"
      },
      {
        "id": 22,
        "product_id": 21,
        "product_name": "test21",
        "image_url": "http://"
      }
    ]
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
  "product_id": 2
}
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:

</details>

<details>
<summary>DELETE: 위시 삭제</summary>

#### Request:
```http
DELETE http://localhost:8080/api/wishes/{wish_id}
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:

</details>

---