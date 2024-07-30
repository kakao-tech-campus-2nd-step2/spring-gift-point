# spring-gift-order

# 기능 요구사항

- 상품을 조회, 추가, 수정, 삭제할 수 있는 API를 구현한다.
- H2 database를 연결하여 저장하도록 한다.
    - DAO는 JPA를 사용한다.
    - 이때, Member, Wishlist는 1:N 양방향 매핑.
    - Product, Wishlist도 1:N 양방향 매핑.
- 관리자 웹페이지를 구현한다.
    - Thymeleaf를 사용하여 서버 사이드 렌더링으로 구현한다.
    - 상품 이미지는 파일 업로드가 아닌 URL을 직접 입력한다.
- 상품 정보 입력 값을 검증한다.
    - 상품 이름은 공백 포함 최대 15자
    - 상품 이름은 한글/영어/숫자/특수 문자로 이루어진다.
        - 특수 문자는 (, ), [, ], +, -, &, /, _ 만 사용 가능하다.
    - "카카오"가 포함된 문구는 담당 MD와 협의한 뒤에 사용할 수 있다.
- 회원가입/로그인 기능을 가진다.
    - 카카오 인증을 통한 로그인도 지원한다.
    - 로그인 시, JWT를 발급하여 클라이언트에게 반환한다.
- 위시 리스트 기능을 가진다.
    - 상품 조회 페이지에서 위시 리스트에 추가 버튼을 눌러 추가할 수 있다.
    - 위시 리스트에서 삭제 버튼을 통해 삭제할 수 있다.
- 상품 목록과 위시리스트에 페이지네이션 기능과 정렬 기능을 지원한다.
- 카테고리 기능을 가진다.
    - 상품과 카테고리를 별도의 모델로 구성하며 연관 관계를 가진다.
        - 상품과 카테고리는 N:1 연관 관계.
    - 상품 등록시에 카테고리를 지정한다.
    - 카테고리를 조회, 추가, 수정, 삭제가 가능하다.
- 상품은 옵션을 가진다.
    - 상품은 반드시 하나 이상의 옵션을 가져야 한다.
        - 상품 추가 시에 옵션을 1개 이상 등록한다.
        - 옵션이 1개라면 삭제할 수 없다.
    - 각 상품의 대한 옵션에 대한 CRUD를 지원한다.
    - 옵션에는 다음과 같은 제약조건이 존재한다.
        1. 옵션명은 공백 포함 최대 50자까지 입력 가능.
        2. 같은 상품 내의 옵션명은 중복 불가능.
        3. 옵션명은 영문, 한글, 숫자, (, ), [, ], +, -, &, /, _ 만 입력 가능.
        4. 옵션 수량은 1개 이상 1억개 미만.
    - 구매 개수 만큼 옵션을 차감시키는 기능을 제공한다.
- 상품 주문을 통해 나에게 카카오톡 메시지 보내기 기능을 제공한다.
    - 주문한 상품 옵션에 대한 정보가 카카오톡 API를 통해 나에게 메시지로 전송된다.
    - 구매한 상품 옵션의 개수만큼 차감한다.
- 모든 회원의 주문 내역을 조회하는 페이지네이션 API를 제공한다.

---

# API 명세

## Product API

### 상품 조회 페이지

- 모든 상품들의 정보를 나타내는 페이지를 서버 사이드 렌더링하여 반환.

```
GET /api/products
```

### 상품 추가 페이지

- 추가할 상품의 정보를 폼 태그에 입력하는 페이지.

```
GET /api/products/prduct
```

### 상품 추가

- HTML 폼 정보에 맞게 상품을 추가하고 목록 페이지로 리다이렉션.

```
POST /api/products/product
```

### 상품 수정 페이지

- 상품의 정보를 수정할 수 있는 폼 태그 페이지.

```
GET /api/products/product/{id}
```

### 상품 수정

- 수정된 정보에 맞게 상품을 수정하고 목록 페이지로 리다이렉션.

```
POST /api/products/product
```

### 상품 삭제

- 해당하는 상품을 삭제.

```
DELETE /api/products/product/{id}
```

## Member API

### 회원가입 페이지

```
GET /api/members/register
```

### 회원가입

```
POST /api/members/register
```

### 로그인 페이지

```
GET /api/members/login
```

### 로그인

```
POST /api/members/login
```

### 카카오 로그인

```
GET /api/members/login/kakao
```

### 카카오 로그인 redirect URL

```
GET /api/members/login/kakao/oauth/token
```

## Wishlist API

### 위시 리스트 조회

```
GET /api/members/wishlist
```

### 위시 리스트에 상품 추가

```
POST /api/members/wishlist/{id}
```

### 위시 리스트 상품 삭제

```
DELETE /api/members/wishlist/{id}
```

## Category API

### 카테고리 조회

```
GET /api/categories
```

### 카테고리 추가 페이지

```
GET /api/categories/addForm
```

### 카테고리 수정 페이지

```
GET /api/categories/{id}
```

### 카테고리 추가

```
POST /api/categories
```

### 카테고리 수정

```
PUT /api/categories
```

### 카테고리 삭제

```
DELETE /api/categories/{id}
```

## Option API

### 옵션 조회

```
GET /api/products/product/{productId}/options
```

### 옵션 추가 페이지

```
GET /api/products/product/{productId}/options/addForm
```

### 옵션 수정 페이지

```
GET /api/products/product/{productId}/options/{optionId}
```

### 옵션 추가

```
POST /api/products/product/{productId}/options
```

### 옵션 수정

```
PUT /api/products/product/{productId}/options
```

### 옵션 삭제

```
DELETE /api/products/product/{productId}/options/{optionId}
```

## Order API

### 주문

```
POST /api/orders HTTP/1.1
Authorization: Bearer {token}
Content-Type: application/json
{
    "productId": 1,
    "optionId": 1,
    "quantity": 5,
    "message": "상품 메시지"
}
```

### 모든 주문 내역 조회

```
GET /api/orders/list
```