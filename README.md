# spring-gift-point

# API 명세서

### 페이지네이션 기본값

# 회원

---

## 회원가입

`POST` `/api/members/register`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Content-Type | application/json | * |
- body

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| email | string(email) | * |
| password | string | * |

**Response**

`201 OK`

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Content-Type | application/json | * |
| Authorization | Bearer | * |

## 로그인

`POST` `/api/members/login`

https://edu.nextstep.camp/s/dGx30MLc/ls/GU7PBYCR

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Content-Type | application/json | * |
- body

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| email | string | * |
| password | string | * |

**Response**

`200 OK`

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Content-Type | application/json | * |
| Authorization | Bearer | * |

# 카테고리

---

## 카테고리 생성

`POST`  `/api/categories`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |
| Content-Type | application/json | * |
- body

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| name | string | * |
| color | string | * |
| imageUrl | string | * |
| description | string |  |

**Response**

`201 Created`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| id | long | * |
| name | string | * |
| color | string | * |
| imageUrl | string | * |
| description | string |  |

## 카테고리 조회

`GET`  `/api/categories`

**Request**

**Response**

`200 OK`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| categories | category[] | * |
- category


    | 이름 | 타입 | 필수여부 |
    | --- | --- | --- |
    | id | long | * |
    | name | string | * |
    | color | string | * |
    | imageUrl | string | * |
    | description | string |  |

## 카테고리 수정

`PUT`  `/api/categories/{categoryId}`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |
| Content-Type | application/json | * |
- body

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| name | string | * |
| color | string | * |
| imageUrl | string | * |
| description | string |  |

**Response**

`200 OK`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| id | long | * |
| name | string | * |
| color | string | * |
| imageUrl | string | * |
| description | string |  |

## 카테고리 삭제

`DELETE`  `/api/categories/{categoryId}`

**Request**

- header

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |

**Response**

`204 NO_CONTENT`

# 상품

---

## 상품 생성

`POST`  `/api/products`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |
| Content-Type | application/json | * |
- body

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| name | string | * |
| price | string | * |
| imageUrl | string | * |
| categoryId | string | * |
| options | option[] | * |

→ 옵션은 1개 이상 필수

- option


    | 이름 | 타입 | 필수여부 |
    | --- | --- | --- |
    | name | string | * |
    | quantity | string | * |

**Response**

`201 Created`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| id | long | * |
| name | string | * |
| price | string | * |
| imageUrl | string | * |
| categoryID | string | * |
| options | option[] | * |
- option


    | 이름 | 타입 | 필수여부 |
    | --- | --- | --- |
    | id | long | * |
    | name | string | * |
    | quantity | string | * |

## 상품 조회

`GET`  `/api/products/{productId}`

**Request**

**Response**

`200 OK`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| id | long | * |
| name | string | * |
| price | string | * |
| imageUrl | string | * |
| categoryID | string | * |
| options | option[] | * |

## 상품 수정

`PUT`  `/api/products/{productId}`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |
| Content-Type | application/json | * |
- body

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| name | string | * |
| price | string | * |
| imageUrl | string | * |
| categoryID | string | * |

**Response**

`200 OK`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| id | long | * |
| name | string | * |
| price | string | * |
| imageUrl | string | * |
| categoryID | string | * |
| options | option[] | * |
- option


    | 이름 | 타입 | 필수여부 |
    | --- | --- | --- |
    | id | long | * |
    | name | string | * |
    | quantity | string | * |

## 상품 삭제

`DELETE`  `/api/products/{productId}`

**Request**

- header

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |

**Response**

`204 NO_CONTENT`

## 상품 목록 조회

`GET`  `/api/products?page=0&size=10&sort=name,asc&categoryId=1`

**Request**

- parameter

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| page | int |  |
| size | int |  |
| sort | string[] |  |
| categoryId | int |  |

**Response**

`200 OK`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| content | product[] | * |
|  |  |  |
- 페이지네이션 객체 반환 통일

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/e2ca8e95-17fb-45f5-84ed-9cb81cd36ac3/4eec2422-23fc-419e-b219-66c6937cedc9/Untitled.png)

![Untitled](https://prod-files-secure.s3.us-west-2.amazonaws.com/e2ca8e95-17fb-45f5-84ed-9cb81cd36ac3/0020f3f0-e8f5-492b-91d4-0ddca55a3f10/Untitled.png)

- product


    | 이름 | 타입 | 필수여부 |
    | --- | --- | --- |
    | id | long | * |
    | name | string | * |
    | price | int | * |
    | imageUrl | string | * |
    | categoryId | long | * |
    | options | option[] | * |

# 옵션

---

## 상품 옵션 추가

`POST` `/api/products/{productId}/options`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |
| Content-Type | application/json | * |
- body

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| name | string | * |
| quantity | string | * |

**Response**

`201 Created`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| id | long | * |
| name | string | * |
| quantity | string | * |

## 상품 옵션 수정

`PUT` `/api/products/{productId}/options/{optionId}`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |
| Content-Type | application/json | * |
- body

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| name | string | * |
| quantity | string | * |

**Response**

`200 OK`

- body

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| id | long | * |
| name | string | * |
| quantity | string | * |

## 상품 옵션 삭제

`DELETE` `/api/products/{productId}/options/{optionId}`

**Request**

- header

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |

**Response**

`204 NO_CONTENT`

## 상품 옵션 목록 조회

`GET` `/api/products/{productId}/options`

**Request**

- parameter

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| productId | long | * |

**Response**

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| options | option[] | * |

# 위시리스트

---

## 위시리스트 상품 추가

`POST`  `/api/wishes`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |
| Content-Type | application/json | * |
- body

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| optionId | long | * |
| quantity | int | * |

**Response**

`201 Created`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| id | long | * |
| optionId | long | * |
| quantity | long | * |

## 위시리스트 상품 삭제

`DELETE`  `/api/wishes/{wishiId}`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |

**Response**

`204 No_Content`

## 위시 리스트 상품 조회(페이지네이션 적용)

`GET`  `/api/wishes?page=0&size=10&sort=createdDate,desc`

**Request**

- parameter

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| page | int |  |
| size | int |  |
| sort | string[] |  |
| categoryId | int |  |

**Response**

`201 Created`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| content | wish[] | * |
- Page 객체 반환

# 주문

---

## 주문하기

`POST`  `/api/orders`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |
| Content-Type | application/json | * |
- body

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| optionId | long | * |
| quantity | int | * |
| message | string |  |

**Response**

`201 Created`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| id | long | * |
| productId | long | * |
| optionId | long | * |
| orderDateTime | string | * |
| quantity | long | * |
- 주문 에러
    - **가지고 있는 포인트보다 더 많은 포인트를 쓰려고 할 때 | 쓰려고하는 포인트가 제품 가격보다 클 때**

# 포인트

---

## 조회하기

`GET`   `/api/points`

**Request**

- header

| 이름 | 값 | 필수여부 |
| --- | --- | --- |
| Authorization | Bearer | * |

**Response**

`200 Ok`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| point | int | * |

**Response 실패**

`401 Unauthorized`

| 이름 | 타입 | 필수여부 |
| --- | --- | --- |
| message | string |  |
- 포인트 차감 방식
    - 포인트 - 상품 가격

**관리자페이지 포인트 충전 기능 추가**

**포인트 조회 api 추가**

**주문하기 포인트 차감 기능 추가(부족하면 400 에러)**