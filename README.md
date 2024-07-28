# API 사용 가이드

## 1. 초기 설정

### 1.1. 카테고리 추가

- **URL:** `/api/categories/add`
- **Method:** `POST`
- **Description:** 새로운 카테고리를 추가합니다.
- **Parameters:**
  - **Request Body:**
    - `category` (Category): 추가할 카테고리 객체
- **Example Request:**
    ```json
    {
      "name": "Electronics"
    }
    ```
- **Example Response:**
    ```json
    {
      "id": 1,
      "name": "Electronics"
    }
    ```

## 2. 제품 관리

### 2.1. 제품 추가

- **URL:** `/api/products`
- **Method:** `POST`
- **Description:** 새로운 제품을 추가
- **Parameters:**
  - **Request Body:**
    - `name` (String): 제품 이름
    - `description` (String): 제품 설명
    - `price` (Integer): 제품 가격
- **Example Request:**
    ```json
    {
      "name": "Product 1",
      "description": "Description of Product 1",
      "price": 100
    }
    ```
- **Example Response:**
    ```json
    {
      "id": 1,
      "name": "Product 1",
      "description": "Description of Product 1",
      "price": 100
    }
    ```

### 2.2. 제품에 옵션 추가

- **URL:** `/api/options/add`
- **Method:** `POST`
- **Description:** 특정 제품에 옵션을 추가
- **Parameters:**
  - `productId` (Long): 옵션이 추가될 제품의 ID
  - `optionNames` (List<String>): 추가할 옵션 이름 목록
  - `optionQuantities` (List<Long>): 각 옵션명에 해당하는 수량 목록
- **Example Request:**
    ```json
    {
      "productId": 1,
      "optionNames": ["Color", "Size"],
      "optionQuantities": [10, 20]
    }
    ```
- **Example Response:**
    ```json
    {
      "message": "Options added successfully"
    }
    ```

### 2.3. 모든 제품 조회

- **URL:** `/api/products`
- **Method:** `GET`
- **Description:** 모든 제품 목록을 조회
- **Example Response:**
    ```json
    [
      {
        "id": 1,
        "name": "Product 1",
        "description": "Description of Product 1",
        "price": 100
      },
      {
        "id": 2,
        "name": "Product 2",
        "description": "Description of Product 2",
        "price": 200
      }
    ]
    ```

### 2.4. ID로 제품 조회

- **URL:** `/api/products/{id}`
- **Method:** `GET`
- **Description:** ID로 제품의 세부 정보를 조회
- **Parameters:**
  - `id` (Long): 검색할 제품의 ID
- **Example Response:**
    ```json
    {
      "id": 1,
      "name": "Product 1",
      "description": "Description of Product 1",
      "price": 100
    }
    ```

### 2.5. 제품 수정

- **URL:** `/api/products/{id}`
- **Method:** `PUT`
- **Description:** 특정 제품의 정보를 수정
- **Parameters:**
  - **Request Body:**
    - `name` (String): 새로운 이름
    - `description` (String): 새로운 설명
    - `price` (Integer): 새로운 가격
- **Example Request:**
    ```json
    {
      "name": "Updated Product",
      "description": "Updated Description",
      "price": 150
    }
    ```
- **Example Response:**
    ```json
    {
      "id": 1,
      "name": "Updated Product",
      "description": "Updated Description",
      "price": 150
    }
    ```

### 2.6. 제품 삭제

- **URL:** `/api/products/{id}`
- **Method:** `DELETE`
- **Description:** 지정한 ID를 가진 제품을 삭제
- **Parameters:**
  - `id` (Long): 삭제할 제품의 ID
- **Example Request:**
    ```json
    {
      "id": 1
    }
    ```
- **Example Response:** `204 No Content`

### 2.7. 제품에서 옵션 삭제

- **URL:** `/api/options/delete/{productId}`
- **Method:** `POST`
- **Description:** 특정 제품과 관련된 모든 옵션을 삭제
- **Parameters:**
  - `productId` (Long): 옵션을 삭제할 제품의 ID
- **Example Request:**
    ```json
    {
      "productId": 1
    }
    ```
- **Example Response:**
    ```json
    {
      "message": "Options deleted successfully"
    }
    ```

## 3. 회원 관리

### 3.1. 회원 추가

- **URL:** `/api/members`
- **Method:** `POST`
- **Description:** 새로운 회원을 추가
- **Parameters:**
  - **Request Body:**
    - `name` (String): 회원의 이름
    - `email` (String): 회원의 이메일
    - `password` (String): 회원의 비밀번호
- **Example Request:**
    ```json
    {
      "name": "John Doe",
      "email": "john.doe@example.com",
      "password": "password123"
    }
    ```
- **Example Response:**
    ```json
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@example.com"
    }
    ```

### 3.2. 모든 회원 조회

- **URL:** `/api/members`
- **Method:** `GET`
- **Description:** 모든 회원 목록을 조회
- **Example Response:**
    ```json
    [
      {
        "id": 1,
        "name": "John Doe",
        "email": "john.doe@example.com"
      },
      {
        "id": 2,
        "name": "Jane Doe",
        "email": "jane.doe@example.com"
      }
    ]
    ```

### 3.3. ID로 회원 조회

- **URL:** `/api/members/{id}`
- **Method:** `GET`
- **Description:** ID로 회원의 세부 정보를 조회
- **Parameters:**
  - `id` (Long): 검색할 회원의 ID
- **Example Response:**
    ```json
    {
      "id": 1,
      "name": "John Doe",
      "email": "john.doe@example.com"
    }
    ```

### 3.4. 회원 수정

- **URL:** `/api/members/{id}`
- **Method:** `PUT`
- **Description:** 특정 회원의 정보를 수정
- **Parameters:**
  - **Request Body:**
    - `name` (String): 새로운 이름
    - `email` (String): 새로운 이메일
    - `password` (String): 새로운 비밀번호
- **Example Request:**
    ```json
    {
      "name": "John Smith",
      "email": "john.smith@example.com",
      "password": "newpassword123"
    }
    ```
- **Example Response:**
    ```json
    {
      "id": 1,
      "name": "John Smith",
      "email": "john.smith@example.com"
    }
    ```

### 3.5. 회원 삭제

- **URL:** `/api/members/{id}`
- **Method:** `DELETE`
- **Description:** 지정한 ID를 가진 회원을 삭제
- **Parameters:**
  - `id` (Long): 삭제할 회원의 ID
- **Example Request:**
    ```json
    {
      "id": 1
    }
    ```
- **Example Response:** `204 No Content`

## 4. 주문 관리

### 4.1. 주문 추가

- **URL:** `/api/orders`
- **Method:** `POST`
- **Description:** 새로운 주문을 추가
- **Parameters:**
  - **Request Body:**
    - `productId` (Long): 주문할 제품의 ID
    - `quantity` (Integer): 주문 수량
- **Example Request:**
    ```json
    {
      "productId": 1,
      "quantity": 2
    }
    ```
- **Example Response:**
    ```json
    {
      "id": 1,
      "productId": 1,
      "quantity": 2,
      "totalPrice": 200
    }
    ```

### 4.2. 모든 주문 조회

- **URL:** `/api/orders`
- **Method:** `GET`
- **Description:** 모든 주문 목록을 조회
- **Example Response:**
    ```json
    [
      {
        "id": 1,
        "productId": 1,
        "quantity": 2,
        "totalPrice": 200
      },
      {
        "id": 2,
        "productId": 2,
        "quantity": 1,
        "totalPrice": 100
      }
    ]
    ```

### 4.3. ID로 주문 조회

- **URL:** `/api/orders/{id}`
- **Method:** `GET`
- **Description:** ID로 주문의 세부 정보를 조회
- **Parameters:**
  - `id` (Long): 검색할 주문의 ID
- **Example Response:**
    ```json
    {
      "id": 1,
      "productId": 1,
      "quantity": 2,
      "totalPrice": 200
    }
    ```

### 4.4. 주문 수정

- **URL:** `/api/orders/{id}`
- **Method:** `PUT`
- **Description:** 특정 주문의 정보를 수정
- **Parameters:**
  - **Request Body:**
    - `productId` (Long): 새로운 제품 ID
    - `quantity` (Integer): 새로운 수량
- **Example Request:**
    ```json
    {
      "productId": 2,
      "quantity": 3
    }
    ```
- **Example Response:**
    ```json
    {
      "id": 1,
      "productId": 2,
      "quantity": 3,
      "totalPrice": 300
    }
    ```

### 4.5. 주문 삭제

- **URL:** `/api/orders/{id}`
- **Method:** `DELETE`
- **Description:** 지정한 ID를 가진 주문을 삭제
- **Parameters:**
  - `id` (Long): 삭제할 주문의 ID
- **Example Request:**
    ```json
    {
      "id": 1
    }
    ```
- **Example Response:** `204 No Content`
