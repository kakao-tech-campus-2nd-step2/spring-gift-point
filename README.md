# spring-gift-point

# 구현 기능 목록

# 사용 API 목록

## 상품 조회

### 기본 정보

| 메서드 |            URL            |
|:---:|:-------------------------:|
| GET | /api/porducts/{productId} |

### 요청

#### Parameter

- productId : 조회하고자 하는 상품의 id

### 응답

#### HttpStatus

- 정상 : 200 Ok
- 상품 조회 실패 : 400 Bad Request
- 서버 오류 : 500 Internal Server Error

#### ResponseBody

```json
{
  "id": long,
  "name": string,
  "price": long,
  "imageUrl": string,
  "options": [
    {
      "id": long,
      "name": string,
      "quantity": long
    }
  ]
}
```

## 상품 목록 조회(페이지네이션 적용)

### 기본 정보

| 메서드 |                                    URL                                    |
|:---:|:-------------------------------------------------------------------------:|
| GET | /api/porducts?page={page}&size={size}&sort={sort}&categoryId={categoryId} |

#### 요청

##### Parameter

- Page = int
- size = int
- sort = column,asc(desc)
- categoryId = long

### 응답

#### HttpStatus

- 정상 : 200 Ok
- 카테고리가 존재하지 않는 경우 : 400 Bad Request
- 요청 양식이 잘못된 경우 : 400 Bad Request
- 서버 오류 : 500 Internal server error

#### ResponseBody

- Java의 Page객체

```json
{
  contents: [
    {
      "id": long,
      "name": string,
      "imageURL": string,
      "price": int
    }
  ]
}
```

## 회원가입

| 메서드  |          URL          |
|:----:|:---------------------:|
| POST | /api/members/register |

### 요청

#### RequestBody

```json
{
  "email": string,
  "password": string
}
```

### 응답

#### HttpStatus

- 정상 : 201 Created
- 중복된 이메일이 있는 경우 : 400 BadRequest
- 요청 양식이 잘못된 경우 : 400 BadRequest
- 서버 에러 : 500 Internal Server Error

#### ResponseBody

```json
{
  "token": string
}
```

## 로그인

### 기본 정보

| 메서드  |        URL         |
|:----:|:------------------:|
| POST | /api/members/login |

### 요청

#### RequestBody

```json
{
  "email": string,
  "password": string
}
```

### 응답

#### HttpStatus

- 정상 : 200 Ok
- 요청 양식이 잘못된 경우 : 400 Bad Request
- 인증 실패 : 403 Forbidden
- 서버 에러 : 500 Internal Server Error

#### ResponseBody

```json
{
  "token": string
}
```

## 카테고리 목록 조회

### 기본 정보

| 메서드 |       URL       |
|:---:|:---------------:|
| GET | /api/categories |

### 응답

#### HttpStatus

- 정상 : 200 Ok
- 서버 에러 : 500 Internal Server Error

#### ResponseBody

```json
{
  "categories": [
    {
      "id": long,
      "name": string,
      "color": string,
      "imageUrl": string,
      "description": string
    }
  ]
}
```

## 상품 옵션 목록 조회

### 기본 정보

| 메서드 |                URL                |
|:---:|:---------------------------------:|
| GET | /api/products/{productId}/options |

### 요청

#### Paramter

- productId : 옵션을 조회하고자 하는 상품의 id

### 응답

#### HttpStatus

- 정상 : 200 Ok
- 상품이 존재하지 않는 경우 : 400 Bad Request
- 요청 양식이 잘못된 경우 : 400 Bad Request
- 서버 에러 : 500 Internal Server Error

#### ResponseBody

```json
{
  "options": [
    {
      "id": long,
      "name": string,
      "quantity": int
    }
  ]
}
```

## 위시리스트 상품 추가

### 기본 정보

| 메서드  |     URL     |
|:----:|:-----------:|
| POST | /api/wishes |

### 요청

#### Header

- Authorization : "Bearer " + 사용자 토큰

#### RequestBody

```json
{
  "productId": long
}
```

### 응답

#### HttpStatus

- 정상 : 200 Ok
- 상품이 존재하지 않는 경우 : 400 Bad Request
- 요청 양식이 잘못된 경우 : 400 Bad Request
- 인증 실패 : 403 Forbidden
- 서버 에러 : 500 Internal Server Error

## 위시리스트 상품 삭제

### 기본 정보

|  메서드   |           URL           |
|:------:|:-----------------------:|
| DELETE | /api/wishes/{productId} |

### 요청

#### RequestHeader

- Authorization : "Bearer " + 사용자 토큰

#### Parameter

- productId : 상품의 id

### 응답

#### HttpStatus

- 정상 : 204 No Content
- 상품이 존재하지 않는 경우 : 400 Bad Request
- wishlist가 존재하지 않는 경우 : 400 Bad Request
- 요청 양식이 잘못된 경우 : 400 Bad Request
- 인증 실패 : 403 Forbidden
- 서버 에러 : 500 Internal Server Error

#### Parameter

- productId : 삭제하고자 하는 상품의 id

## 위시리스트 상품 조회(페이지네이션 적용)

### 기본 정보

| 메서드 |                       URL                       |
|:---:|:-----------------------------------------------:|
| GET | /api/wishes?page={page}&size={size}&sort={sort} |

#### Parameter

- page = int
- size = long
- sort = column,asc(desc)

#### RequestHeader

- Authorization : "Bearer " + 사용자 토큰

#### HttpStatus

- 정상 : 200 Ok
- 요청 양식이 잘못된 경우 : 400 Bad Request
- 인증 실패 : 403 Forbidden
- 서버 에러 : 500 Internal Server Error

### ResponseBody

- Page객체

```json
{
  "content": [
    {
      "id": int,
      "name": string,
      "price": long,
      "imageUrl": string
    }
  ]
}
```

## 주문하기

### 상세 정보

| 메서드 |     URL     |
|:---:|:-----------:|
| GET | /api/orders |

### 요청

#### RequestHeader

- Authorization : "Bearer " + 사용자 토큰

#### RequestBody

```json
{
  "optionId": long,
  "quantity": int,
  "message": string
}
```

### 응답

#### HttpStatus

- 정상 : 201 Created
- 옵션이 없는 경우 : 400 Bad Request
- 재고 소진 : 400 Bad Request
- 요청 양식이 잘못된 경우 : 400 Bad Request
- 인증 실패 : 403 Forbidden
- 서버 에러 : 500 Internal Server Error

# 사용하지 않는 API
- 전부 Deprecated 처리
- Test Code에 대해 Disable처리