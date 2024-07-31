# spring-gift-point

---

## 1단계 - API 명세

### [API 명세서 노션 링크](https://fuchsia-tabletop-6fc.notion.site/22-438e773c1c0d475da1e87b4ef4ef42d8)

- [인증 API](#카카오-인증-api)
- [카테고리 API](#카테고리-api)
- [회원 API](#회원-api)
- [옵션 API](#옵션-api)
- [주문 API](#주문-api)
- [상품 API](#상품-api)
- [위시 리스트 API](#위시-리스트-api)

---

## 카카오 인증 API
- ### Endpoint: `/oauth/kakao`


### **[GET]** 카카오 로그인 주소 요청

#### Request
```http
GET http://localhost:8080/api/oauth/kakao/url
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
```http
{
    "url": "https://kauth.kakao.com/oauth/authorize?response_type=code&redirect_uri={redirect_uri}&client_id={client_id}"
}
```



### **[GET]** 카카오 로그인

#### Request
```http
GET http://localhost:8080/api/oauth/kakao/code
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
```http
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzIyMzkwMjA3LCJleHAiOjE3MjI0NzY2MDd9.ckp2ysE1oUYk1fFVY75FYVMhRV9dv0aQjoyDh17DOP8"
}
```



---

## 카테고리 API
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
```http
{
  "id": 2
  "name": "추가상품권",            
  "color": "#123123",           
  "image_url": "http://test-product.com",
  "description": "" 
}
```



### **[DELETE]** 카테고리 삭제

#### Request
```http
DELETE http://localhost:8080/api/categories/{category_id}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
```

##### Body




### **[PUT]** 카테고리 수정

#### Request
```http
PUT http://localhost:8080/api/categories/1
Content-Type: application/json

{
  "name": "교환권",            
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
```json
{
  "id": 1,
  "name": "교환권",
  "color": "#123123",
  "image_url": "http://test-product.com",
  "description": ""
}
```




---

## 회원 API
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
Content-Type: application/json
```

##### Body
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzIyMzg1ODA1LCJleHAiOjE3MjI0NzIyMDV9.vSM6pZVa7eiRHiQIn6KKx7Zf6WlPtjqZp9QNjI6o7hg"
}
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
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzIyMzg1ODA1LCJleHAiOjE3MjI0NzIyMDV9.vSM6pZVa7eiRHiQIn6KKx7Zf6WlPtjqZp9QNjI6o7hg"
}
```




---

## 옵션 API
- ### Endpoint: `/options`


### **[GET]** 특정 상품의 옵션 목록 조회

#### Request
```http
GET http://localhost:8080/api/products/1/options
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
POST http://localhost:8080/api/products/1/options
Content-Type: application/json

{
  "name": "name",       
  "quanatity": 1000     
}
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
  "id": 2,
  "name": "name",       
  "quantity": 1000
}
```




### **[PUT]** 옵션 수정

#### Request
```http
POST http://localhost:8080/api/products/1/options/2
Content-Type: application/json

{
  "name": "옵션명 1",
  "quantity": 100
}
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
  "name": "name",       
  "quantity": 1000,       
  "product_id": 1       
}
```




### **[DELETE]** 옵션 삭제

#### Request
```http
DELETE http://localhost:8080/api/products/1/options/2
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
```

##### Body




---

## 주문 API
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



### **[GET]** 주문 목록 조회

#### Request
```http
GET http://localhost:8080/api/orders
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
[
    {
        "id": 1,
        "optionId": 1,
        "quantity": 2,
        "orderDateTime": "2024-07-31T10:43:43.701352",
        "message": "Please handle this order with care."
    },
    {
        "id": 2,
        "optionId": 1,
        "quantity": 2,
        "orderDateTime": "2024-07-31T10:43:46.520243",
        "message": "Please handle this order with care."
    },
    {
        "id": 3,
        "optionId": 1,
        "quantity": 2,
        "orderDateTime": "2024-07-31T10:43:47.677637",
        "message": "Please handle this order with care."
    }
]
```



---

## 상품 API
- ### Endpoint: `/products`

### **[GET]** 상품 페이지 조회

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
[
  {
    "id": 3,
    "name": "아이스 카페 아메리카노 T",
    "price": 4500,
    "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
    "category": {
      "id": 1,
      "name": "교환권23",
      "color": "#6c91d1",
      "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
      "description": ""
    }
  },
  {
    "id": 4,
    "name": "아이스 카페 아메리카노 T",
    "price": 4500,
    "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
    "category": {
      "id": 1,
      "name": "교환권23",
      "color": "#6c91d1",
      "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
      "description": ""
    }
  },
  {
    "id": 5,
    "name": "아이스 카페 아메리카노 T",
    "price": 4500,
    "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
    "category": {
      "id": 1,
      "name": "교환권23",
      "color": "#6c91d1",
      "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
      "description": ""
    }
  },
  {
    "id": 6,
    "name": "아이스 카페 아메리카노 T",
    "price": 4500,
    "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
    "category": {
      "id": 1,
      "name": "교환권23",
      "color": "#6c91d1",
      "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
      "description": ""
    }
  },
  {
    "id": 7,
    "name": "아이스 카페 아메리카노 T",
    "price": 4500,
    "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
    "category": {
      "id": 1,
      "name": "교환권23",
      "color": "#6c91d1",
      "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
      "description": ""
    }
  }
]
```




### **[GET]** 특정 상품 조회

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




### **[POST]** 새로운 상품 추가

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
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
```json
{
  "id": 2,
  "name": "name",
  "price": 4500,
  "image_url": "http://product-shop.com/image.jpg",
  "category_name": "식품",
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




### **[PUT]** 상품 정보 업데이트

#### Request
```http
PUT http://localhost:8080/api/products/{product_id}
Content-Type: application/json

{
  "name": "아메리카노",        
  "price": 6000,              
  "image_url": "http://product-shop.com/image.jpg", 
  "category_name": "식품"             
}
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
  "id": 2,
  "name": "아메리카노",        
  "price": 6000,              
  "image_url": "http://product-shop.com/image.jpg", 
  "category_name": "식품"             
}
```


### **[DELETE]** 상품 삭제

#### Request
```http
DELETE http://localhost:8080/api/products/{product_id}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
```

##### Body

---

## 위시 리스트 API
- ### Endpoint: `/wishes`

### **[GET]** 위시 목록 조회

#### Request
```http
GET http://localhost:8080/api/wishes?page=0&size=10&sort=createdDate,desc
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
[
  {
    "id": 1,
    "option": {
      "id": 1,
      "name": "옵션명 1",
      "quantity": 100,
      "product": {
        "id": 1,
        "name": "아이스 카페 아메리카노 T",
        "price": 4500,
        "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
        "category": {
          "id": 1,
          "name": "교환권23",
          "color": "#6c91d1",
          "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
          "description": ""
        }
      }
    }
  },
  {
    "id": 2,
    "option": {
      "id": 2,
      "name": "옵션명 2",
      "quantity": 100,
      "product": {
        "id": 1,
        "name": "아이스 카페 아메리카노 T",
        "price": 4500,
        "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
        "category": {
          "id": 1,
          "name": "교환권23",
          "color": "#6c91d1",
          "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
          "description": ""
        }
      }
    }
  },
  {
    "id": 3,
    "option": {
      "id": 3,
      "name": "옵션명 3",
      "quantity": 100,
      "product": {
        "id": 2,
        "name": "아이스 카페 아메리카노 S",
        "price": 4500,
        "imageUrl": "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
        "category": {
          "id": 1,
          "name": "교환권23",
          "color": "#6c91d1",
          "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
          "description": ""
        }
      }
    }
  }
]
```

### **[POST]** 새로운 위시 추가

#### Request
```http
POST http://localhost:8080/api/wishes
Authorization: Bearer [JWT_TOKEN]
Content-Type: application/json

{
  "optionId": 2
}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
Content-Type: application/json
```

##### Body
```http
{
    "id": 4,
    "option": {
        "id": 1
    }
}
```

### **[DELETE]** 위시 삭제

#### Request
```http
DELETE http://localhost:8080/api/wishes/{wish_id}
```

#### Response

##### Header
```http
HTTP/1.1 200 OK
```

##### Body


---