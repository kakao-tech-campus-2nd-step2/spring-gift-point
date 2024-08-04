# spring-gift-point

http://localhost:8080/swagger-ui/index.html 으로 아래보다 더 자세한 명세서로 구현하였습니다.

# Spring Gift API
카카오 선물하기 클론 코딩 프로젝트

## Version: 1.0

### /api/wishes

#### GET
##### Description:

서버가 클라이언트에게 요청한 회원의 위시리스트를 제공합니다. 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| pageable | query |  | Yes | [Pageable](#Pageable) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 정상적으로 위시리스트를 제공합니다. |
| 400 | 요청의 양식이 잘못된 경우입니다. |
| 403 | 유효하지 않는 토큰입니다. |
| 500 | 서버에 의한 오류입니다. |

#### POST
##### Description:

서버가 클라이언트가 제출한 위시리스트 항목을 추가합니다. 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | 위시리스트 항목 추가에 성공하였습니다. |
| 400 | 잘못된 요청입니다. 보통 요청의 양식이 잘못된 경우입니다. |
| 403 | 유효하지 않는 토큰입니다. |
| 500 | 서버에 의한 오류입니다. |

### /api/orders

#### POST
##### Description:

클라이언트가 제출한 주문을 처리하고, 카카오톡으로 주문 메세지를 보냅니다.  클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | 주문을 성공하였습니다. |
| 400 | 잘못된 요청입니다. 보통 주문에 옵션 정보가 잘못되었거나, 재고가 소진된 경우입니다. |
| 403 | 유효하지 않는 토큰입니다. |
| 500 | 서버에 의한 오류입니다. |

### /api/members/register

#### POST
##### Description:

서버가 클라이언트가 제출한 사용자 정보를 가지고 회원가입을 진행합니다.

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | 회원가입에 성공하였습니다. |
| 400 | 잘못된 요청입니다. 보통 요청의 양식이 잘못된 경우입니다. |
| 500 | 서버에 의한 오류입니다. |

### /api/members/login

#### POST
##### Description:

서버가 클라이언트가 제출한 사용자 정보를 가지고 로그인을 진행합니다.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 로그인에 성공하였습니다. |
| 400 | 요청의 양식이 잘못되었습니다. |
| 403 | 아이디나 비밀번호 또는 계정 타입(소셜/기본)이 서버가 가지고 있는 것과 다릅니다. |
| 500 | 서버에 의한 오류입니다. |

### /api/products

#### GET
##### Description:

서버가 클라이언트에게 제품 목록 페이지를 제공합니다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| categoryId | query |  | No | long |
| pageable | query |  | Yes | [Pageable](#Pageable) |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 정상적으로 제품 목록 페이지를 제공합니다. |
| 400 | 카테고리가 존재하지 않거나, 요청 양식이 잘못된 경우 입니다. |
| 500 | 서버에 의한 오류입니다. |

### /api/products/{productId}/options

#### GET
##### Description:

서버가 특정 상품의 모든 옵션을 제공합니다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| productId | path |  | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 정상적으로 옵션들을 제공합니다. |
| 400 | 잘못된 요청입니다. 요청 양식이 잘못되거나, 해당 상품 아이디가 존재하지 않는 경우입니다. |
| 500 | 서버에 의한 오류입니다 |

### /api/products/{id}

#### GET
##### Description:

서버가 클라이언트에게 제품 하나의 정보를 제공합니다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 정상적으로 제품 정보를 제공합니다. |
| 400 | 잘못된 요청입니다. 보통 해당 productId가 존재하지 않는 경우입니다. |
| 500 | 서버에 의한 오류입니다. |

### /api/members/point

#### GET
##### Description:

서버가 클라이언트가 제출한 사용자 정보를 가지고 포인트가 얼마나 있는지 알려줍니다.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 포인트 조회에 성공하였습니다. |
| 401 | 유효하지 않은 토큰입니다. |
| 500 | 서버에 의한 오류입니다. |

### /api/categories

#### GET
##### Description:

서버가 클라이언트에게 카테고리들을 제공합니다.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 정상적으로 카테고리를 제공합니다. |
| 500 | 서버에 의한 오류입니다. |

### /api/wishes/{id}

#### DELETE
##### Description:

서버가 클라이언트가 제출한 위시리스트 항목을 삭제합니다. 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | long |

##### Responses

| Code | Description |
| ---- | ----------- |
| 204 | 위시리스트 항목 삭제에 성공하였습니다. |
| 400 | 잘못된 요청입니다. 보통 존재하지 않는 ID인 경우입니다. |
| 403 | 유효하지 않는 토큰입니다. |
| 500 | 서버에 의한 오류입니다. |

