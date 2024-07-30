# Spring-Gift API 문서
#### 개요
이 문서는 Spring-Gift 플랫폼에서 제공하는 모든 API에 대한 상세한 설명과 지침을 제공합니다.    
각 API 기능별로 세분화하여 표 형식의 개요와 함께 요청 객체 및 응답 객체의 상세 정보를 포함하고 있습니다.

API 사용 중 궁금하거나 불명확한 부분이 있으시면 언제든지 문의해 주세요.‍🙇‍♀️🙇‍♀️ 
****
### API 기능 개요
이 API 문서는 다음과 같은 기능들을 제공합니다:

#### 회원 관련 기능:
- 로그인 : 카카오 서버를 통한 로그인만 지원한다.
- JWT 토큰 관리: JWT 토큰은 카카오 로그인 성공 시 응답 헤더를 통해 발급받을 수 있습니다.

#### 상품 관련 기능:
- 상품 등록: 각 상품은 하나 이상의 카테고리와 옵션을 포함합니다. 
  - 필수 정보: 상품 등록 시, 옵션 리스트와 카테고리 정보가 요구됩니다. 상세한 형식은 요청 객체 섹션을 참조하세요.
- JWT 토큰 : 발급받은 JWT 토큰은 위시리스트와 주문 API에서 사용됩니다. 

### 참고사항 
- 모든 api는 `/api`로 시작합니다. 

---

## 목차
- [Kakao Authentication API](#kakao-authentication-api) 
- [Category API](#category-api)
- [Product API](#product-api)
- [Option API](#option-api)
- [Wish API](#wish-api)
- [Order API](#order-api)
---

## Kakao Authentication API
### Endpoint: `/auth`

| 제목                     | 메서드 | URL                             | 요청 컨텐트 타입 / 요청 객체 | 응답 객체                                                    | 설명                                           |
|----------|--------|---------------------|---------------------|----------------------------------------------------------|------------------------------------------------|
| 카카오 로그인 리다이렉트  | `GET`    | `/api/auth/kakao`                 | -                            | `Status: 302 Found`<br> `Header: Location: {Kakao Auth URL}` | 카카오 로그인을 위한 리다이렉트 URL을 반환합니다. |
| 카카오 로그인            | `GET`    | `/api/auth/kakao/callback`        | -                            | `Status: 200 OK`<br> `Header: Authorization: Bearer {Token}` | 카카오 로그인 후 JWT 토큰을 반환합니다.          |
* 이 부분 BE에서 유저를 자동으로 카카오 로그인 창으로 리다이렉트 보내고 있습니다. 따라서 FE분들은 `/kakao`로 요청 후`/callback`으로 응답을 받게 됩니다.

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

| 제목                 | 메서드    | URL                                 | 요청 컨텐트 타입 / 요청 객체                                       | 응답 객체                                | 설명                                  |
|----------------------|--------|-------------------------------------|---------------------------------------------------------|--------------------------------------|---------------------------------------|
| 카테고리 목록 조회    | `GET`  | `/api/categories `                    | `Content-Type: application/json`                          | `Status: 200 OK`<br>`Body: {categories}` | 모든 카테고리 정보를 반환합니다.       |
| 특정 카테고리 조회   | `GET`  | `/api/categories/{id}`                | `Content-Type: application/json`                          | `Status: 200 OK`<br>`Body: {category}`           | ID에 해당하는 카테고리 정보를 반환합니다. |
| 새로운 카테고리 추가 | `POST` | `/api/categories`                     | `Content-Type: application/json`<br>`body : {category}` | `Status: 200 OK`                       | 새로운 카테고리를 추가합니다.           |
| 카테고리 삭제       | `DELETE` | `/api/categories/{category_id}`       | `Content-Type: application/json`                          | `Status: 200 OK`                       | 지정된 ID의 카테고리를 삭제합니다.       |


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

body 
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



## Product API
### Endpoint: `/products`

| 제목               | 메서드 | URL                                                    | 요청 컨텐트 타입 / 요청 객체                    | 응답 객체                                        | 설명                                     |
|------------------|--------|--------------------------------------------------------|------------------------------------------------|----------------------------------------------|------------------------------------------|
| 카테고리 별 제품 페이지 조회 | `GET`    | `/api/products?page={page_num}&category={category_id}` | `Content-Type: application/json`                 | `Status: 200 OK<br>Body: {data}` | 페이지네이션을 이용한 제품 목록을 반환합니다. |
| 특정 제품 조회         | `GET`    | `/api/products/{product_id}`                           | `Content-Type: application/json`                 | `Status: 200 OK<br>Body: {product}`            | 제품 ID에 해당하는 상세 제품 정보를 반환합니다. |
| 새로운 제품 추가        | `POST`   | `/api/products`                                        | `Content-Type: application/json<br>{product data}` | `Status: 200 OK`                              | 새로운 제품을 추가합니다.                  |
| 제품 정보 업데이트       | `PUT`    | `/api/products/{product_id}`                           | `Content-Type: application/json<br>{product data}` | `Status: 200 OK`                              | 지정된 제품의 정보를 업데이트합니다.       |
| 제품 삭제            | `DELETE` | `/api/products/{product_id}`                           | `Content-Type: application/json`                 | `Status: 200 OK `                             | 지정된 제품을 삭제합니다.                 |
* 각 요청마다 요청 바디 타입이 다르니 주의하시길 바랍니다.


<details>
<summary>GET: 제품 페이지 조회</summary>

* Default 상품 반환 개수 : 20개

#### Request:
```http
/api/products?page={page_num}&category={category_id}
Content-Type: application/json
```

#### Response:

##### Header:
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body:
- `"total_page"` 는 전체 페이지 수 입니다. 예를 들어 3이 반환된 경우 페이지 개수는 전체 1,2,3 총 3개입니다.
```json
{
  "data": {
    "total_page": 3,
    "content": [
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

body
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
HTTP/1.1 200 Ok
Content-Type: application/json
```

##### Body:

</details>

<details>
<summary>PUT: 제품 정보 업데이트</summary>

#### Request:
```http
PUT http://localhost:8080/api/products/{product_id}
Content-Type: application/json

body
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

## Option API
### Endpoint: `/options`

| 제목                     | 메서드 | URL                              | 요청 컨텐트 타입 / 요청 객체                                 | 응답 객체                      | 설명                                      |
|--------------------------|--------|----------------------------------|---------------------------------------------------|----------------------------|-------------------------------------------|
| 특정 제품의 옵션 목록 조회 | `GET`    | `/api/options/{product_id}`        | `Content-Type: application/json`                    | `Status: 200 OK`<br>`Body: {options}` | 제품 ID에 해당하는 모든 옵션을 반환합니다. |
| 새로운 옵션 추가          | `POST`   | `/api/options`                     | `Content-Type: application/json`<br> `body: {option}` | `Status: 200 OK`             | 제품에 새로운 옵션을 추가합니다.           |
| 옵션 삭제                 | `DELETE` | `/api/options/{option_id}`         | `Content-Type: application/json`                    | `Status: 200 OK`             | 지정된 ID의 옵션을 삭제합니다.             |


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

body
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

## Wish API
### Endpoint: `/wishes`


| 제목                     | 메서드 | URL                            | 요청 컨텐트 타입 / 요청 객체                                                                    | 응답 객체                          | 설명                                       |
|--------------------------|--------|--------------------------------|--------------------------------------------------------------------------------------|--------------------------------|--------------------------------------------|
| 특정 페이지의 위시 목록 조회 | `GET`    | `/api/wishes?page={page_num}`    | `Authorization: Bearer {Token}`<br>`Content-Type: application/json`                      | `Status: 200 OK`<br>`Body: {data}` | 페이지네이션을 이용한 위시 목록을 반환합니다. |
| 새로운 위시 추가          | `POST`   | `/api/wishes`                    | `Authorization: Bearer {Token}`<br>`Content-Type: application/json<br>body: {wish data}` | `Status: 200 OK`                 | 새로운 위시를 추가합니다.                    |
| 위시 삭제                 | `DELETE` | `/api/wishes/{wish_id}`          | `Authorization: Bearer {Token}`<br>`Content-Type: application/json<br>body: {wish data}` | `Status: 200 OK`                 | 지정된 위시를 삭제합니다.                    |



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
    "content": [
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

body
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


## Order API
### Endpoint: `/orders`

| 제목                 | 메서드 | URL                               | 요청 컨텐트 타입 / 요청 객체                                                                 | 응답 객체                                   | 설명                                     |
|----------------------|--------|-----------------------------------|-----------------------------------------------------------------------------------|---------------------------------------------|-----------------------------------------|
| 새로운 주문 추가        | `POST`   | `/api/orders`                          | `Authorization: Bearer {Token}` <br>`Content-Type: application/json<br>body: {order}` | `Status: 200 OK`               |   새로운 주문을 추가합니다.                        |
<details>
<summary>POST: 새로운 주문 추가</summary>

#### Request:
```http
POST http://localhost:8080/api/orders
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

body 
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