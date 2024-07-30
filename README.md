# spring-gift-point

---

## 1단계 - API 명세

- [Authentication API](#authentication-api)
- [Category API](#category-api)
- [Member API](#member-api)
- [Option API](#option-api)
- [Order API](#order-api)
- [Product API](#product-api)
- [Wish API](#wish-api)

---

## Authentication API
- ### Endpoint: `/oauth`

### **[GET]** 카카오 로그인 리다이렉트 요청

#### Request
```http
GET http://localhost:8080/api/oauth
```

#### Response

##### Header
```http
HTTP/1.1 302 Found
Location: https://kauth.kakao.com/oauth/authorize?response_type=code&client_id={client_id}&redirect_uri={redirect_uri}
```

### **[GET]** 카카오 로그인

#### Request
```http
GET http://localhost:8080/api/oauth/callback
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Authorization: Bearer [JWT_TOKEN]
Content-Type: application/json
```


---

## Category API
- ### Endpoint: `/categories`
### **[GET]** 카테고리 목록 조회

#### Request
```http
GET http://localhost:8080/api/categories
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
```json
{
  "categories": [
    {
      "id": 1,
      "name": "교환권",
      "color": "#ffffff",
      "image_url": "https://product-shop.com/image1.jpg",
      "description": ""
    },
    {
      "id": 2,
      "name": "상품권",
      "color": "#f1f1f1",
      "image_url": "https://product-shop.com/image2.jpg",
      "description": ""
    }
  ]
}
```


### **[GET]** 특정 카테고리 조회

#### Request
```http
GET http://localhost:8080/api/categories/1
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
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

### **[POST]** 새로운 카테고리 추가

#### Request
```http
POST http://localhost:8080/api/categories
Content-Type: application/json

{
  "name": "추가상품권",            
  "color": "#123123",           
  "image_url": "http://test-product.com",
  "description": "" 
}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body


### **[DELETE]** 카테고리 삭제

#### Request
```http
DELETE http://localhost:8080/api/categories/{category_id}
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body

---

## Member API
- ### Endpoint: `/members`

### **[POST]** 로그인 요청

#### Request
```http
POST http://localhost:8080/api/members/login
Content-Type: application/json

{
  "email": "email",          
  "password": "password"     
}
```

#### Response


##### Header
```http
HTTP/1.1 200 OK
Authorization: Bearer [JWT_TOKEN]
Content-Type: application/json
```

### **[POST]** 새로운 회원 추가

#### Request
```http
POST http://localhost:8080/api/members/register
Content-Type: application/json

{
  "email": "email5",          
  "password": "password3"
}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body

---

## Option API
- ### Endpoint: `/options`

### **[GET]** 특정 제품의 옵션 목록 조회

#### Request
```http
GET http://localhost:8080/api/options/{product_id}
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
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


### **[POST]** 새로운 옵션 추가

#### Request
```http
POST http://localhost:8080/api/options
Content-Type: application/json

{
  "name": "name",       
  "quanatity": 1000,       
  "product_id": 1       
}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body


### **[DELETE]** 옵션 삭제

#### Request
```http
DELETE http://localhost:8080/api/options/{option_id}
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body

---

## Order API
- ### Endpoint: `/orders`

### **[POST]** 새로운 주문 추가

#### Request
```http
POST http://localhost:8080/api/orders
Authorization: Bearer [JWT_TOKEN]
Content-Type: application/json

{
  "option_id": 1,
  "quantity": 3,         
  "message": "message"     
}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body


---

## Product API
- ### Endpoint: `/products`

### **[GET]** 제품 페이지 조회

##### ※ Default 상품 반환 개수 : 10개

#### Request
```http
GET http://localhost:8080/api/products?page={page_num}
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
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

### **[GET]** 특정 제품 조회

#### Request
```http
GET http://localhost:8080/api/products/{product_id}
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
```json

{
    "id": 1,
    "name": "test1",
    "price": 10000,
    "image_url": "http://example.com/image.jpg",
    "category_id": 1
}

```

### **[POST]** 새로운 제품 추가

#### Request
```http
POST http://localhost:8080/api/products
Content-Type: application/json

{
  "name": "name",        
  "price": 4500,              
  "image_url": "http://product-shop.com/image.jpg", 
  "category_name": 식품,
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

#### Response

##### Header
```http
HTTP/1.1 201 Created
Content-Type: application/json
```

##### Body
```json
{
  "code": "P004",
  "message": "제품 추가 성공"
}
```

### **[PUT]** 제품 정보 업데이트

#### Request
```http
PUT http://localhost:8080/api/products/{product_id}
Content-Type: application/json

{
  "name": "아메리카노",        
  "price": 6000,              
  "image_url": "http://product-shop.com/image.jpg", 
  "category_name": 식품             
}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body

### **[DELETE]** 제품 삭제

#### Request
```http
DELETE http://localhost:8080/api/products/{product_id}
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body

---

## Wish API
- ### Endpoint: `/wishes`

### **[GET]** 특정 페이지의 위시 목록 조회

#### Request
```http
GET http://localhost:8080/api/wishes?page={page_num}
Authorization: Bearer [JWT_TOKEN]
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
```json
{
  "data": {
    "total_page": 3,
    "wishes": [
      {
        "id": 1,
        "product_id": 1,
        "product_name": "test-1",
        "image_url": "http://product-shop.com/image1.jpg"
      },
      {
        "id": 2,
        "product_id": 2,
        "product_name": "test-2",
        "image_url": "http://product-shop.com/image2.jpg"
      }
    ]
  }
}
```

### **[POST]** 새로운 위시 추가

#### Request
```http
POST http://localhost:8080/api/wishes
Authorization: Bearer [JWT_TOKEN]
Content-Type: application/json

{
  "product_id": 2
}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body

### **[DELETE]** 위시 삭제

#### Request
```http
DELETE http://localhost:8080/api/wishes/{wish_id}
Content-Type: application/json
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body


---