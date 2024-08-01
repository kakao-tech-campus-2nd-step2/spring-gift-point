
# spring-gift-point

# api 명세

## 회원 API

| URL                    | Method | Function      | Description                             |
|------------------------|--------|---------------|-----------------------------------------|
| /api/register   | POST   | 회원 가입 | 새 회원을 등록하고 토큰을 받는다.      |
| /api/login     | POST   | 로그인 | 회원을 인증하고 토큰을 받는다.         |

## TODO-LIST

- [x] 회원 가입
- [x] 로그인

### 회원 가입

- **URL** : `/api/register`
- **Method** : `POST`
- **Description**
    - 회원 가입을 할 수 있다.
    - 중복된 이메일이거나 이메일 제약 조건을 위반한 경우 회원 가입을 거부한다.
      - 중복된 이메일에 대해서는 `Custom Exception : DuplicatedEmailException`을 발생시킨다.
        - `Http Status Code 409 : Conflict`와 `Error message`를 전달한다.
      - 이메일 제약 조건을 위반한 경우 `Custom Exception : EmailConstraintViolationException`을 발생시킨다. 
        - `Http Status Code 400 : Bad Request`와 `Error message`를 전달한다.
    - 회원 가입이 성공적이라면 `Http Status Code 201 : CREATED`와 `accessToken`을 발급한다.
- **Request**
  - **Type** : `application/json`
  - **Request Fields**

    | Field     | Type   | Description           | Required | Null Allowed |
    |-----------|--------|-----------------------|----------|--------------|
    | `email`   | String | 회원 이메일 주소     | Yes      | No           |
    | `password`| String | 회원 비밀번호         | Yes      | No           |
  - **Request Example**
    ```json
    {
        "email": "test@example.com",
        "password": "password123"
    }
    ```
  - **cURL Request Example**
     ```sh
     curl -X POST http://localhost:8080/api/register ^
        -H "Content-Type: application/json" ^
        -d "{\"email\": \"test@example.com\", \"password\": \"password123\"}"

- **Response**
    - **Type** : `application/json`
    - **Response Fields**

      | Field   | Type   | Description |
      |---------|--------|-------------|
      | `email` | String | 회원 이메일 주소   |
      | `token` | String | accessToken | 
  
    - **Response Example**
    ```json
      {
          "email": "test@example.com",
          "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIiwiZW1haWwiOiJ0ZXN0QGV4YW1wbGUuY29tIiwidHlwZSI6Im5vcm1hbC11c2VyIiwiaWF0IjoxNzIyMzI5OTY0LCJleHAiOjE3MjM4MDExOTN9.IByRkOMdy3mNsU86HPl_rpCYn2RWkRzICEaQ9GOm2qM"
      }      
   ```


### 로그인

- **URL** : `/api/login`
- - **Method** : `POST`
- **Description**
    - 기존 회원이 로그인을 할 수 있다.
    - 존재하지 않는 회원이라면 로그인을 거부한다.
      - 로그인이 거부된 경우 `Custom Exception : InvalidLoginException`을 발생시킨다.
        - `HTTP Status Code 401 : Unauthorized`와 `Error message`를 전달한다.
    - 로그인이 성공적이라면 `Http Status Code 200 : OK`와 `accessToken`을 발급한다.
- **Request**
    - **Type** : `application/json`
    - **Request Fields**

      | Field     | Type   | Description           | Required | Null Allowed |
      |-----------|--------|-----------------------|----------|--------------|
      | `email`   | String | 회원 이메일 주소     | Yes      | No           |
      | `password`| String | 회원 비밀번호         | Yes      | No           |
    - **Request Example**
      ```json
      {
          "email": "test@example.com",
          "password": "password123"
      }
      ```
  - **cURL Request Example**
     ```sh
     curl -X POST http://localhost:8080/api/login ^
          -H "Content-Type: application/json" ^
          -d "{\"email\": \"test@example.com\", \"password\": \"password123\"}"

- **Response**
    - **Type** : `application/json`
    - **Response Fields**

      | Field   | Type   | Description |
      |---------|--------|-------------|
      | `email` | String | 회원 이메일 주소   |
      | `token` | String | accessToken | 
  
    - **Response Example**
      ```json
      {
          "email": "test@example.com",
          "token": "accessToken123"
      }
      ```

## 카테고리 API

| Endpoint                  | Method | Description                       |
|---------------------------|--------|-----------------------------------| 
| /api/categories           | POST   | 카테고리 생성 (새 카테고리 등록)   |
| /api/categories/{categoryId} | PUT    | 카테고리 수정 (기존 카테고리 수정) |
| /api/categories           | GET    | 카테고리 목록 조회 (모든 카테고리 조회) |

## TODO-LIST

- [x] 카테고리 생성
- [x] 카테고리 수정
- [x] 카테고리 목록 조회

### 카테고리 생성

- **URL**: `/api/categories`
- **Method**: `POST`
- **Description**
    - 새 카테고리를 등록한다.
- **Request**
    - **Type**: `application/json`
    - **Request Fields**

      | Field        | Type   | Description                  | Required | Null Allowed |
      |--------------|--------|------------------------------|----------|--------------|
      | `name`       | String | 카테고리 이름                | Yes      | No           |
      | `color`      | String | 카테고리 색상                | Yes      | No           |
      | `imageUrl`   | String | 카테고리 이미지 주소         | Yes      | No           |
      | `description`| String | 카테고리 설명                | Yes      | Yes          |

    - **Request Example**
      ```json
      {
          "name": "Electronics",
          "color": "#6c95d1",
          "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
          "description": ""
      }
      ```
- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description |
      |---------------|--------|-------------|
      | `id`          | number | 카테고리 고유 id  |
      | `name`        | String | 카테고리 이름     |
      | `color`       | String | 카테고리 색상     |
      | `imageUrl`    | String | 카테고리 이미지 주소 |
      | `description` | String | 카테고리 설명     |

    - **Response Example**

      ```json
      {
          "id": "12345",
          "name": "Electronics"
      }
      ```
- **cURL Request Example**
  ```sh
  curl -X POST "localhost:8080/api/categories" ^
     -H "Content-Type: application/json" ^
     -d "{\"name\": \"Electronics\", \"color\": \"#6c95d1\", \"imageUrl\": \"https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png\", \"description\": \"\"}"


### 카테고리 수정 (기존 카테고리 수정)

- **URL**: `/api/categories/{categoryId}`
- **Method**: `PUT`
- **Description**
    - 기존 카테고리의 정보를 수정한다.
    - `{categoryId}`는 수정할 카테고리의 고유 ID를 나타낸다.
- **Request**
    - **Type**: `application/json`
    - **Request Fields**

      | Field        | Type   | Description                  | Required | Null Allowed |
          |--------------|--------|------------------------------|----------|--------------|
      | `name`       | String | 카테고리 이름                | Yes      | No           |
      | `color`      | String | 카테고리 색상                | Yes      | No           |
      | `imageUrl`   | String | 카테고리 이미지 주소         | Yes      | No           |
      | `description`| String | 카테고리 설명                | Yes      | Yes          |

    - **Request Example**
      ```json
      {
          "name": "Updated Electronics",
          "color": "#4a90e2",
          "imageUrl": "https://example.com/new-image.png",
          "description": "Updated description"
      }
      ```
  - **cURL Request Example**
      ```sh
     curl -X PUT "localhost:8080/api/categories/1" ^
     -H "Content-Type: application/json" ^
     -d "{\"name\": \"Updated Electronics\", \"color\": \"#4a90e2\", \"imageUrl\": \"https://example.com/new-image.png\", \"description\": \"Updated description\"}"


- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description |
          |---------------|--------|-------------|
      | `id`          | number | 카테고리 고유 ID  |
      | `name`        | String | 카테고리 이름     |
      | `color`       | String | 카테고리 색상     |
      | `imageUrl`    | String | 카테고리 이미지 주소 |
      | `description` | String | 카테고리 설명     |

    - **Response Example**
      ```json
      {
          "id": "12345",
          "name": "Updated Electronics",
          "color": "#4a90e2",
          "imageUrl": "https://example.com/new-image.png",
          "description": "Updated description"
      }
      ```

### 카테고리 목록 조회 (모든 카테고리 조회)

- **URL**: `/api/categories`
- **Method**: `GET`
- **Description**
    - 모든 카테고리의 목록을 조회한다.
- **Request**
    - **Type**: `None`
    - **cURL Request Example**
      ```sh
      curl -X GET "localhost:8080/api/categories"
- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description             |
          |---------------|--------|-------------------------|
      | `id`          | String | 카테고리 고유 ID        |
      | `name`        | String | 카테고리 이름           |
      | `color`       | String | 카테고리 색상           |
      | `imageUrl`    | String | 카테고리 이미지 주소    |
      | `description` | String | 카테고리 설명           |

    - **Response Example**

        ```json    
          [
              {
                  id: 2920,
                  name: '생일',
                  description: '감동을 높여줄 생일 선물 리스트',
                  color: '#5949a3',
                  imageUrl:
                  'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F292020231106_MXMUB.png',
              },
              {
                  id: 2930,
                  name: '교환권',
                  description: '놓치면 후회할 교환권 특가',
                  color: '#9290C3',
                  imageUrl:
                  'https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240131153049_5a22b137a8d346e9beb020a7a7f4254a.jpg',
              }
          ]

## 상품 API

| URL                                                   | Method | Function             | Description              | Status |  
|-------------------------------------------------------|--------|----------------------|--------------------------|--------|  
| /api/products                                         | POST   | 상품 생성                | 새 상품을 등록한다.              | [ ]    |
| /api/products/{productId}                             | GET    | 상품 상세 조회             | 특정 상품의 정보를 조회한다.         | [ ]    |  
| /api/products/{productId}                             | PUT    | 상품 수정                | 기존 상품의 정보를 수정한다.         | [ ]    |  
| /api/products/{productId}                             | DELETE | 상품 삭제                | 특정 상품을 삭제한다.             | [ ]    |  
| /api/products?page=0&size=10&sort=name,asc&categoryId=1 | GET    | 상품 목록 조회 (페이지네이션 적용) | 모든 상품의 목록을 페이지 단위로 조회한다. | [ ]    |  


## TODO-LIST

- [x] 상품 생성
- [x] 상품 상세 조회
- [x] 상품 수정
- [x] 상품 삭제
- [x] 상품 목록 조회(페이지네이션 적용)


### 상품 생성

- **URL**: `/api/products`
- **Method**: `POST`
- **Description**
    - 새 상품을 등록한다.
    - 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
    - 가능한 특수 문자: ( ), [ ], +, -, &, /, _
        - 그 외 특수 문자 사용 불가, "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.
            - 제약 조건을 위반한다면, `Http Status Code 400 : Bad Request` 와 `error message`를 전달한다.
- **Request**
    - **Type**: `application/json`
    - **Request Fields**

      | Field        | Type   | Description                  | Required | Null Allowed |
      |--------------|--------|------------------------------|----------|--------------|
      | `name`       | String | 상품 이름                    | Yes      | No           |
      | `price`      | Number | 상품 가격                    | Yes      | No           |
      | `categoryId` | Number | 카테고리 ID                  | Yes      | No           |
      | `imageUrl`   | String | 상품 이미지 주소             | Yes      | No           |

    - **Request Example**
      ```json
      {
          "name": "스마트폰",
          "price": 1200000,
          "categoryId": 1,
          "imageUrl": "https://example.com/smartphone.png"
      }
      ```
    - **cURL Request Example**
      ```sh
      curl -X POST "localhost:8080/api/products" ^
      -H "Content-Type: application/json" ^
      -d "{\"name\": \"스마트폰\", \"price\": 1200000, \"categoryId\": 1, \"imageUrl\": \"https://example.com/smartphone.png\"}"


- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description     |
      |---------------|--------|-----------------|
      | `id`          | Number | 상품 고유 ID    |
      | `name`        | String | 상품 이름       |
      | `price`       | Number | 상품 가격       |
      | `imageUrl`    | String | 상품 이미지 주소|

    - **Response Example**
      ```json
      {
          "id": 123,
          "name": "Smartphone",
          "price": 299.99,
          "imageUrl": "https://example.com/smartphone.png"
      }
      ```

### 상품 상세 조회

- **URL**: `/api/products/{productId}`
- **Method**: `GET`
- **Description**
    - 특정 상품의 정보를 조회한다.
    - `{productId}`는 조회할 상품의 고유 ID를 나타낸다.
- **Request**
    - **Type**: `None`
    - **Request Parameters**

      | Parameter    | Type   | Description          | Required |
          |--------------|--------|----------------------|----------|
      | `productId`  | Number | 조회할 상품의 ID     | Yes      |

    - **Request Example**
      ```sh
      curl -X GET "localhost:8080/api/products/1"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description                      |
          |---------------|--------|----------------------------------|
      | `id`          | Number | 상품 고유 ID                     |
      | `name`        | String | 상품 이름                        |
      | `price`       | Number | 상품 가격                        |
      | `imageUrl`    | String | 상품 이미지 주소                |

    - **Response Example**
      ```json
      {
          "id": 123,
          "name": "Smartphone",
          "price": 299.99,
          "imageUrl": "https://example.com/smartphone.png"
      }
      ```

  - **Error Responses**
      ```json
      {
          "statusCode" : 404,
          "message": "상품 Id : {productId}는 찾을 수 없습니다."
      }
      ```

### 상품 수정

- **URL**: `/api/products/{productId}`
- **Method**: `PUT`
- **Description**
    - 기존 상품의 정보를 수정한다.
    - `{productId}`는 수정할 상품의 고유 ID를 나타낸다.
        - 존재하지 않는 상품이라면 `custom exception : ProductNotFoundException`을 발생시킨다.
          - `HTTP Status Code 404 : Not Found`와 `Error message`를 전달한다.
    - 상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있다.
    - 가능한 특수 문자: ( ), [ ], +, -, &, /, _
        - 그 외 특수 문자 사용 불가, "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있다.
            - 제약 조건을 위반한다면, `Http Status Code 400 : Bad Request` 와 `error message`를 전달한다.
- **Request**
    - **Type**: `application/json`
    - **Request Fields**

      | Field         | Type   | Description                    | Required | Null Allowed |
          |---------------|--------|--------------------------------|----------|--------------|
      | `name`        | String | 상품 이름                      | Yes      | No           |
      | `price`       | Number | 상품 가격                      | Yes      | No           |
      | `categoryId`  | Number | 카테고리 ID                    | Yes      | No           |
      | `imageUrl`    | String | 상품 이미지 주소              | No       | Yes          |

    - **Request Example**
      ```json
      {
          "categoryId": 2,
          "name": "Updated phone",
          "price": 300,
          "imageUrl": "https://example.com/updated-smartphone.png"
      }
      ```

    - **Request URL Example**
      ```sh
      curl -X PUT "http://localhost:8080/api/products/1" ^
      -H "Content-Type: application/json" ^
      -d "{\"name\": \"Updated\", \"price\": 300, \"categoryId\": 1, \"description\": \"Updated model smartphone with better features.\", \"imageUrl\": \"https://example.com/updated-smartphone.png\"}"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description                    |
          |---------------|--------|--------------------------------|
      | `id`          | Number | 상품 고유 ID                   |
      | `name`        | String | 상품 이름                      |
      | `price`       | Number | 상품 가격                      |
      | `categoryId`  | Number | 카테고리 ID                    |
      | `description` | String | 상품 설명                      |
      | `imageUrl`    | String | 상품 이미지 주소              |

    - **Response Example**
        ```sh
          Product updated successfully
        ```

  - **Error Responses**
          
      ```json
          {
            "statusCode" : 404,
            "message": "상품 Id : {productId}는 찾을 수 없습니다."
          }
      ```


### 상품 삭제

- **URL**: `/api/products/{productId}`
- **Method**: `DELETE`
- **Description**
    - 특정 상품을 삭제한다.
    - `{productId}`는 수정할 상품의 고유 ID를 나타낸다.
        - 존재하지 않는 상품이라면 `custom exception : ProductNotFoundException`을 발생시킨다.
            - `HTTP Status Code 404 : Not Found`와 `Error message`를 전달한다.
- **Request**
    - **Type**: `None`
    - **Request Parameters**

      | Parameter    | Type   | Description          | Required |
          |--------------|--------|----------------------|----------|
      | `productId`  | Number | 삭제할 상품의 ID     | Yes      |

    - **Request URL Example**
      ```sh
      curl -X DELETE "localhost:8080/api/products/1"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description                |
          |---------------|--------|----------------------------|
      | `message`     | String | 삭제 결과 메시지           |

    - **Response Example**
      ```json
      {
          "message": "Product successfully deleted."
      }
      ```

    - **Product Not Found**
      ```json
      {
         "statusCode" : 404,
          "message": "상품 Id : {productId}는 찾을 수 없습니다."
      }
      ```



### 상품 목록 조회 (페이지네이션 적용)

- **URL**: `/api/products`
- **Method**: `GET`
- **Description**
    - 모든 상품의 목록을 페이지 단위로 조회한다.
    - 페이지네이션과 정렬 기능을 지원하며, 특정 카테고리의 상품만 조회할 수 있다.
- **Request**
    - **Type**: `application/json`
    - **Request Parameters**

      | Parameter    | Type    | Description                                  | Required | Default    |
          |--------------|---------|----------------------------------------------|----------|------------|
      | `page`       | Integer | 조회할 페이지 번호 (0부터 시작)             | No       | 0          |
      | `size`       | Integer | 한 페이지에 표시할 항목 수                  | No       | 10         |
      | `sort`       | String  | 정렬 기준 (예: `name,asc` 또는 `price,desc`) | No       | `name,asc` |
      | `categoryId` | Integer | 특정 카테고리의 상품만 조회할 카테고리 ID   | No       | 없음       |

    - **Request Example**
      ```sh
      curl -X GET "localhost:8080/api/products?page=0&size=10&sort=name,asc&categoryId=1"
      ```

  - **Response**
      - **Type**: `application/json`
     **Response Fields**

        | Field           | Type   | Description   |
        |-----------------|--------|---------------|
        | `content`       | Array  | 상품 목록         |
        | `number`        | Number | 현재 페이지 번호     |
        | `totalElements` | Number | 총 상품 수        |
        | `size`          | Number | 현재 페이지의 상품의 수 |
        | `last`          | Number | 마지막 페이지 여부    |
        
          - **Item Fields**

            | Field         | Type   | Description            |
            |---------------|--------|------------------------|
            | `id`          | Number | 상품 고유 ID           |
            | `name`        | String | 상품 이름              |
            | `price`       | Number | 상품 가격              |
            | `imageUrl`    | String | 상품 이미지 주소      |

      - **Response Example**

        ```json
      
        {
            "content": [
            {
            "productId": 1,
            "name": "Updated",
            "price": 349,
            "imageUrl": "https://example.com/updated-smartphone.png"
            },
            {
            "productId": 2,
            "name": "zupdatez",
            "price": 4,
            "imageUrl": "www"
            },
            {
            "productId": 3,
            "name": "zupdatez",
            "price": 4,
            "imageUrl": "www"
            },
            {
            "productId": 4,
            "name": "zupdatez",
            "price": 4,
            "imageUrl": "www"
            },
            {
            "productId": 5,
            "name": "스마트폰",
            "price": 1200000,
            "imageUrl": "https://example.com/smartphone.png"
            },
            {
            "productId": 6,
            "name": "스마트폰",
            "price": 1200000,
            "imageUrl": "https://example.com/smartphone.png"
            },
            {
            "productId": 7,
            "name": "스마트폰",
            "price": 1200000,
            "imageUrl": "https://example.com/smartphone.png"
            }
            ],
            "number": 0,
            "totalElements": 7,
            "size": 10,
            "last": true
        }

## 상품 옵션 API

| URL                                        | Method | Function         | Description                          | Status |  
|--------------------------------------------|--------|------------------|--------------------------------------|--------|  
| /api/products/{productId}/options          | POST   | 상품 옵션 추가 | 상품에 옵션을 추가한다.             | [ ]    |  
| /api/products/{productId}/options/{optionId} | PUT    | 상품 옵션 수정 | 기존 상품 옵션의 정보를 수정한다.  | [ ]    |  
| /api/products/{productId}/options/{optionId} | DELETE | 상품 옵션 삭제 | 기존 제품 옵션을 삭제한다.          | [ ]    |  
| /api/products/{productId}/options          | GET    | 상품 옵션 목록 조회 | 특정 상품에 대한 모든 옵션을 조회한다. | [ ]    |  


## 상품 옵션 API

### 상품 옵션 추가

- **URL**: `/api/products/{productId}/options`
- **Method**: `POST`
- **Description**
    - 상품에 옵션을 추가한다.
    - `{productId}`는 수정할 상품의 고유 ID를 나타낸다.
        - 존재하지 않는 상품이라면 `custom exception : ProductNotFoundException`을 발생시킨다.
            - `HTTP Status Code 404 : Not Found`와 `Error message`를 전달한다.
    
- **Request**
    - **Type**: `application/json`
    - **Request Fields**

      | Field       | Type   | Description | Required | Null Allowed |
          |-------------|--------|-------------|----------|--------------|
      | `productId` | Number | 상품의 고유 Id   | Yes      | No           |
      | `name`      | String | 옵션 이름       | Yes      | No           |
      | `quantity`  | Number | 옵션 수량       | No       | Yes          |

    - **Request Example**
      ```json
      {
        "name" : "option1",
        "quantity" : 20000
      }
      ```

    - **Request URL Example**
      ```sh
      curl -X POST "localhost:8080/api/products/3/options" ^
      -H "Content-Type: application/json" ^
      -d "{\"name\": \"option1\", \"quantity\": 20000}"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field      | Type   | Description | Required | Null Allowed |
                |------------|--------|-------------|----------|--------------|
      | `Id`       | Number | 상품의 고유 Id   | Yes      | No           |
      | `name`     | String | 옵션 이름       | Yes      | No           |
      | `quantity` | Number | 옵션 수량       | No       | Yes          |

    - **Response Example**
      ```json
      {
        "Id" : 2, 
        "productId" : 3,
        "name" : "option1",
        "quantity" : 20000
      }
      ```

---

### 상품 옵션 수정

- **URL**: `/api/products/{productId}/options/{optionId}`
- **Method**: `PUT`
- **Description**
    - 기존 상품 옵션의 정보를 수정한다.
    - `{productId}`는 수정할 옵션이 속한 상품의 고유 ID를 나타낸다.
      - `{productId}`는 수정할 상품의 고유 ID를 나타낸다.
      - 존재하지 않는 상품이라면 `custom exception : ProductNotFoundException`을 발생시킨다.
          - `HTTP Status Code 404 : Not Found`와 `Error message`를 전달한다.
    - `{optionId}`는 수정할 옵션의 고유 ID를 나타낸다.
      - 존재하지 않는 옵션이라면 `custom exception : OptionNotFoundException`을 발생시킨다.
          - `HTTP Status Code 404 : Not Found`와 `Error message`를 전달한다.
- **Request**
    - **Type**: `application/json`
    - **Request Fields**

      | Field       | Type   | Description | Required | Null Allowed |
                |-------------|--------|-------------|----------|--------------|
      | `productId` | Number | 상품의 고유 Id   | Yes      | No           |
      | `name`      | String | 옵션 이름       | Yes      | No           |
      | `quantity`  | Number | 옵션 수량       | No       | Yes          |

    - **Request Example**
      ```json
      {
        "name" : "option1",
        "quantity" : 20000
      }
      ```

    - **Request URL Example**
      ```sh
      curl -X PUT "localhost:8080/api/products/1/options/1" ^
      -H "Content-Type: application/json" ^
      -d "{\"name\": \"updateoption1\", \"quantity\": 20000}"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description          |
          |---------------|--------|----------------------|
      | `id`          | Number | 옵션 고유 ID         |
      | `name`        | String | 옵션 이름           |
      | `value`       | String | 옵션 값             |
      | `price`       | Number | 옵션 추가 가격     |
      | `imageUrl`    | String | 옵션 이미지 주소   |

    - **Response Example**
      ```json
      {
          "id": 456,
          "name": "Color",
          "value": "Blue",
          "price": 12.00,
          "imageUrl": "https://example.com/blue-color.png"
      }
      ```

---

### 상품 옵션 삭제

- **URL**: `/api/products/{productId}/options/{optionId}`
- **Method**: `DELETE`
- **Description**
    - 기존 상품 옵션을 삭제한다.
    - `{productId}`는 삭제할 옵션이 속한 상품의 고유 ID를 나타낸다.
       - 존재하지 않는 상품이라면 `custom exception : ProductNotFoundException`을 발생시킨다.
           - `HTTP Status Code 404 : Not Found`와 `Error message`를 전달한다.
    - `{optionId}`는 수정할 옵션의 고유 ID를 나타낸다.
      - 존재하지 않는 옵션이라면 `custom exception : OptionNotFoundException`을 발생시킨다.
          - `HTTP Status Code 404 : Not Found`와 `Error message`를 전달한다.
      
- **Request**
    - **Type**: `None`
    - **Request URL Example**
      ```sh
      curl -X DELETE "localhost:8080/api/products/1/options/1"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description                |
          |---------------|--------|----------------------------|
      | `message`     | String | 삭제 결과 메시지           |

    - **Response Example**
      ```json
      {
          "message": "Option successfully deleted."
      }
      ```

- **Error Responses**

    - **Option Not Found**
      ```json
      {
          "message": "Option not found."
      }
      ```

---

### 상품 옵션 목록 조회

- **URL**: `/api/products/{productId}/options`
- **Method**: `GET`
- **Description**
    - 특정 상품에 대한 모든 옵션을 조회한다.
    - `{productId}`는 옵션을 조회할 상품의 고유 ID를 나타낸다.
        - `{productId}`는 수정할 옵션이 속한 상품의 고유 ID를 나타낸다.
        - 존재하지 않는 상품이라면 `custom exception : ProductNotFoundException`을 발생시킨다.
            - `HTTP Status Code 404 : Not Found`와 `Error message`를 전달한다.

- **Request**
    - **Type**: `None`
    - **Request URL Example**
      ```sh
      curl -X GET "localhost:8080/api/products/1/options"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field       | Type   | Description |
                      |-------------|--------|-------------|
      | `id`        | Number | 옵션 고유 ID    |
      | `name`      | String | 옵션 이름       |
      | `quantity`  | Number | 옵션 수량       |
      | `productId` | Number | 상품 고유 Id    |

    - **Response Example**
      ```json
      [
        {
          "Id": 2,
          "name": "option1",
          "quantity": 20000,
          "productId": 1
        },
        {
          "Id": 3,
          "name": "option2",
          "quantity": 20000,
          "productId": 1
        }
      ]
      ```

## 위시 리스트 API

| URL                                        | Method | Function                   | Description                                   | token |  
|--------------------------------------------|--------|----------------------------|-----------------------------------------------|-------|  
| /api/wishes                                | POST   | 위시 리스트 상품 추가 | 회원의 위시 리스트에 상품을 추가한다.       | 필요    |  
| /api/wishes/{wishId}                       | DELETE | 위시 리스트 상품 삭제 | 회원의 위시 리스트에서 상품을 삭제한다.     | 필요    |  
| /api/wishes?page=0&size=10&sort=createdDate,desc | GET    | 위시 리스트 상품 조회 (페이지네이션 적용) | 회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다. | 필요    |  


## 위시 리스트 API

### 위시 리스트 상품 추가

- **URL**: `/api/wishes`
- **Method**: `POST`
- **Description**
    - 회원의 위시 리스트에 상품을 추가한다.
    - header에 발급받은 `accessToken`을 추가해야 한다.
      - `"Authorization: Bearer YOUR_ACCESS_TOKEN"`
      - 유효하지 않은 토큰이라면 `custom exception : jwt exception`을 발생시킨다.
- **Request**
    - **Type**: `application/json`
    - **Request Fields**

      | Field         | Type   | Description                    | Required | Null Allowed |
          |---------------|--------|--------------------------------|----------|--------------|
      | `productId`   | Number | 추가할 상품의 ID               | Yes      | No           |

    - **Request Example**
      ```json
      {
          "productId": 1
      }
      ```

    - **Request URL Example**
      ```sh
      curl -X POST "http://localhost:8080/api/wishes" ^
      -H "Content-Type: application/json" ^
      -H "Authorization: Bearer YOUR_ACCESS_TOKEN" ^
      -d "{\"productId\": 1}"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description                |
          |---------------|--------|----------------------------|
      | `wishId`      | Number | 위시 리스트에서의 상품 ID  |
      | `productId`   | Number | 추가된 상품의 ID           |

    - **Response Example**
      ```json
      {
          "wishId": 456,
          "productId": 123
      }
      ```

- **Error Responses**

    - **Product Not Found**
      ```json
      {
          "message": "Product not found."
      }
      ```
---

### 위시 리스트 상품 삭제

- **URL**: `/api/wishes/{wishId}`
- **Method**: `DELETE`
- **Description**
    - 회원의 위시 리스트에서 상품을 삭제한다.
    - header에 발급받은 `accessToken`을 추가해야 한다.
      - `"Authorization: Bearer YOUR_ACCESS_TOKEN"`
      - 유효하지 않은 토큰이라면 `custom exception : jwt exception`을 발생시킨다.
    - `{wishId}`는 삭제할 위시 리스트 아이템의 고유 ID를 나타낸다.
      - `{wishId}`에 해당하는 상품이 존재하지 않는다면 `custom exception : WishNotFoundException`을 발생시킨다.
          - `HTTP Status Code 404 : Not Found`와 `Error message`를 전달한다.
- **Request**
    - **Type**: `None`
    - **Request URL Example**
      ```sh
      curl -X DELETE "localhost:8080/api/wishes/1"
      -H "Authorization: Bearer YOUR_ACCESS_TOKEN" 
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description                |
          |---------------|--------|----------------------------|
      | `message`     | String | 삭제 결과 메시지           |

    - **Response Example**
      ```json
      {
          "message": "Product successfully removed from the wish list."
      }
      ```

- **Error Responses**

    - **Wish Item Not Found**
      ```json
      {
          "message": "Wish item not found."
      }
      ```

---

### 위시 리스트 상품 조회 (페이지네이션 적용)

- **URL**: `/api/wishes?page=0&size=10&sort=createdDate,desc`
- **Method**: `GET`
- **Description**
    - 회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.
    - header에 발급받은 `accessToken`을 추가해야 한다.
        - `"Authorization: Bearer YOUR_ACCESS_TOKEN"`
        - 유효하지 않은 토큰이라면 `custom exception : jwt exception`을 발생시킨다.

- **Request**
    - **Type**: `None`
    - **Request Parameters**

      | Parameter     | Type   | Description                | Required |
          |---------------|--------|----------------------------|----------|
      | `page`        | Number | 페이지 번호 (기본값: 0)    | No       |
      | `size`        | Number | 페이지당 항목 수 (기본값: 10) | No       |
      | `sort`        | String | 정렬 기준 및 방향 (기본값: createdDate,desc) | No       |

    - **Request URL Example**
      ```sh
      curl -X GET "localhost:8080/api/wishes?page=0&size=10&sort=createdDate,desc"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description                |
          |---------------|--------|----------------------------|
      | `totalItems`  | Number | 총 위시 리스트 아이템 수   |
      | `totalPages`  | Number | 총 페이지 수              |
      | `currentPage` | Number | 현재 페이지 번호           |
      | `items`       | Array  | 위시 리스트 상품 목록     |

        - **Item Fields**

          | Field         | Type   | Description            |
                |---------------|--------|------------------------|
          | `wishId`      | Number | 위시 리스트에서의 상품 ID  |
          | `productId`   | Number | 상품의 ID              |
          | `createdDate` | String | 상품이 추가된 날짜     |

    - **Response Example**
      ```json
      {
        "content": [
          {
          "wishId": 1,
          "productResponseDTO": {
            "Id": 1,
            "name": "스마트폰",
            "imageUrl": "https://example.com/smartphone.png",
            "price": 1200000
            }
           }
        ],
        "number": 0,
        "totalElements": 1,
        "size": 10,
        "last": true
      }
      ```

- **Error Responses**

    - **Internal Server Error**
      ```json
      {
          "message": "An error occurred while retrieving the wish list."
      }
      ```

## 주문 API

| URL                                        | Method | Function         | Description                           | Status |  
|--------------------------------------------|--------|------------------|---------------------------------------|--------|  
| /api/orders                                | POST   | 주문하기 | 새 주문을 생성한다.                  | [ ]    |  
| /api/orders?page=0&size=10&sort=orderDateTime,desc | GET    | 주문 목록 조회 (페이지네이션 적용) | 주문 목록을 페이지 단위로 조회한다. | [ ]    |


## 주문 API

### 주문하기

- **URL**: `/api/orders`
- **Method**: `POST`
- **Description**
    - 새 주문을 생성한다.
- **Request**
    - **Type**: `application/json`
    - **Request Fields**

      | Field              | Type   | Description | Required | Null Allowed |
          |--------------------|--------|-------------|----------|--------------|
      | `optionId`         | Number | 주문 옵션 Id    | Yes      | No           |
      | `quantity`         | Number | 주문 상품 수량    | Yes      | No           |
      | `message`          | String | 주문 message  | Yes      | No           |
     
    - **Request Example**
      ```json
      {
        "optionId": 1,
        "quantity": 2,
        "message": "Please handle this order with care."
      }
      ```

    - **Request URL Example**
      ```sh
      curl -X POST localhost:8080/api/orders ^
      -H "Authorization: Bearer {token}" ^
      -H "Content-Type: application/json" ^
      -d "{ \"optionId\": 1, \"quantity\": 2, \"message\": \"Please handle this order with care.\"
      }"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field           | Type   | Description |
          |-----------------|--------|-------------|
      | `id`            | Number | 생성된 주문의 ID  |
      | `optionId`      | Number | 주문 옵션 Id    |
      | `quantity`      | Array  | 주문 상품 수량    |
      | `orderDateTime` | Number | 주문 시간       |
      | `message`       | String | 주문 message  |

    - **Response Example**
      ```json
      {
        "id": 1,
        "optionId": 1,
        "quantity": 2,
        "orderDateTime": "2024-07-21T10:00:00",
        "message": "Please handle this order with care."
      }
      ```


---

### 주문 목록 조회 (페이지네이션 적용)

- **URL**: `/api/orders?page=0&size=10&sort=orderDateTime,desc`
- **Method**: `GET`
- **Description**
    - 주문 목록을 페이지 단위로 조회한다.
- **Request**
    - **Type**: `None`
    - **Request Parameters**

      | Parameter     | Type   | Description                | Required |
          |---------------|--------|----------------------------|----------|
      | `page`        | Number | 페이지 번호 (기본값: 0)    | No       |
      | `size`        | Number | 페이지당 항목 수 (기본값: 10) | No       |
      | `sort`        | String | 정렬 기준 및 방향 (기본값: orderDateTime,desc) | No       |

    - **Request URL Example**
      ```sh
      curl -X GET "localhost:8080/api/orders?page=0&size=10&sort=orderDateTime,desc"
      ```

- **Response**
    - **Type**: `application/json`
    - **Response Fields**

      | Field         | Type   | Description                |
          |---------------|--------|----------------------------|
      | `totalItems`  | Number | 총 주문 수                 |
      | `totalPages`  | Number | 총 페이지 수              |
      | `currentPage` | Number | 현재 페이지 번호           |
      | `items`       | Array  | 주문 목록                  |

        - **Item Fields**

          | Field        | Type   | Description              |
                |--------------|--------|--------------------------|
          | `orderId`    | Number | 주문의 ID                |
          | `userId`     | Number | 주문한 사용자의 ID       |
          | `totalPrice` | Number | 총 주문 금액            |
          | `address`    | String | 배송 주소               |
          | `orderDate`  | String | 주문 생성 날짜          |

    - **Response Example**
      ```json
      {
        "totalItems": 20,
        "totalPages": 2,
        "currentPage": 0,
        "items": [
          {
            "orderId": 1011,
            "userId": 789,
            "totalPrice": 299.99,
            "address": "123 Main Street, City, Country",
            "orderDate": "2024-07-30T15:00:00Z"
          },
          {
            "orderId": 1010,
            "userId": 788,
            "totalPrice": 159.99,
            "address": "456 Elm Street, City, Country",
            "orderDate": "2024-07-29T14:00:00Z"
          }
        ]
      }
      ```

- **Error Responses**

    - **Internal Server Error**
      ```json
      {
          "message": "An error occurred while retrieving the order list."
      }
      ```
