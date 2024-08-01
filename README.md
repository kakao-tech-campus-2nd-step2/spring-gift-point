# spring-gift-point


### /api/products

#### GET
##### Description:

서버가 클라이언트에게 제품 목록 페이지를 제공합니다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| categoryId | query |  | No | long |
| pageable | query |  | Yes | [Pageable](#Pageable) |


### /api/products/{productId}/options

#### GET
##### Description:

특정 상품에 대한 모든 옵션을 조회한다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| productId | path |  | Yes | long |

##### Responses

| Code | Description                          |
| ---- |--------------------------------------|
| 200 | 정상적으로 옵션들을 제공합니다.                    |
| 400 | 잘못된 요청입니다. 요청 양식이 잘못되거나, 매칭 상품이 없는경우 |
| 500 | 서버에 의한 오류입니다                         |

### Response Body(List형식으로 반환)
```dtd
{
  "options": [
    {
      "id": 0,
      "name": "Stirng",
      "quantity": 0
    },

    {
      "id": 1,
      "name": "Stirng",
      "quantity": 0
    },

    {
      "id": 2,
      "name": "Stirng",
      "quantity": 0
    }
  ]
}
```
### /api/products/{id}

#### GET
##### Description:

서버가 클라이언트에게 제품 하나의 정보를 제공합니다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | long |

##### Responses

| Code | Description                  |
| ---- |------------------------------|
| 200 | 정상적으로 제품 정보를 제공합니다.          |
| 400 | 잘못된 요청입니다. 상품 id 조회를 실패했습니다. |
| 500 | 서버에 의한 오류입니다.                |


### 프론트 요청 명세
```
{
    "id": 0,
    "name": "string",
    "price": 0,
    "imageUrl": "string",
    "option" : {
	    "id" : 
	    "name" : 
	    "quantity" :
    }
}
```

### /api/products?page=0&size=10&sort=name,asc&categoryId=1

#### GET
##### Description:

서버가 클라이언트에게 제품 하나의 정보를 제공합니다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| id | path |  | Yes | long |

##### Responses

| Code | Description                  |
| ---- |------------------------------|
| 200 | 정상적으로 제품 정보를 제공합니다.          |
| 400 | 잘못된 요청입니다. 상품 id 조회를 실패했습니다. |
| 500 | 서버에 의한 오류입니다.                |

### 프론트 요청 명세
```
{
	products: [
		{
				"id":,
				"name":,
				"imageURL":,
				"price":
		}
	],
	"nextPageToken": "1",
  "pageInfo": {
    "totalResults": 80,
    "resultsPerPage": 10
  }
}
```
### ResponseBody
```dtd
{
    "totalPages": 1,
    "totalElements": 3,
    "size": 10,
    "content": [
        {
            "id": 1,
            "name": "product1",
            "price": 1000,
            "imageUrl": "image.jpg",
            "categoryId": 1
        },
        {
            "id": 2,
            "name": "product2",
            "price": 2000,
            "imageUrl": "image.jpg",
            "categoryId": 2
        },
        {
            "id": 3,
            "name": "product3",
            "price": 3000,
            "imageUrl": "image.jpg",
            "categoryId": 3
        }
    ],
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "first": true,
    "last": true,
    "numberOfElements": 3,
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "unpaged": false,
        "paged": true
    },
    "empty": false
}
```
### /api/categories

#### GET
##### Description:

서버가 클라이언트에게 카테고리들을 제공합니다.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 정상적으로 카테고리를 제공합니다. |
| 500 | 서버에 의한 오류입니다. |

### Response Body (List 형식 반환)
```dtd
{
	categories : [
				{
		    "id": 1,
		    "name": "교환권",
		    "color": "#6c95d1",
		    "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
		    "description": ""
		    },
		    
		    {
		    "id": 2,
		    "name": "교환권",
		    "color": "#6c95d1",
		    "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
		    "description": ""
		    },
		    
		    {
		    "id": 3,
		    "name": "교환권",
		    "color": "#6c95d1",
		    "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
		    "description": ""
		    }
    ]
}
```

### 프론트 요구 명세 
```dtd
 id: number;
  name: string;
  imageUrl: string;
```

### api/members/register

#### POST
##### Description:

새 회원을 등록하고 토큰을 받는다.

##### Responses

| Code | Description                |
| ---- |----------------------------|
| 201 | 회원가입에 성공하였습니다.             |
| 400 | 잘못된 요청입니다.요청의 양식이 잘못된 경우|
| 500 | 서버에 의한 오류입니다.              |
##### 400 코드 발생 경우
- 중복된 이메일이 있는 경우
- 요청 양식이 잘못된 경우
### api/members/login

#### POST
##### Description:

회원을 인증하고 토큰을 받는다.

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 로그인에 성공하였습니다. |
| 400 | 요청의 양식이 잘못되었습니다. |
| 403 | 아이디 또는 비밀번호가 틀린 경우 |
| 500 | 서버에 의한 오류입니다. |

### /api/wishes

#### POST
##### Description:

회원의 위시 리스트에 상품을 추가한다. 클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 한다.

##### Responses

| Code | Description                                       |
| ---- |---------------------------------------------------|
| 201 | 위시리스트 항목 추가에 성공하였습니다.                             |
| 400 | 잘못된 요청입니다. 보통 요청의 양식이 잘못된 경우입니다.(상품이 존재하지 않는 경우+요청 양식이 잘못된 경우) |
| 403 | 유효하지 않는 토큰입니다.                                    |
| 500 | 서버에 의한 오류입니다.                                     |

## /api/wishes?page=0&size=10&sort=createdDate,desc

#### Get
##### Description:

위시 리스트 상품 조회 (페이지네이션 적용)

##### Responses

| Code | Description                                       |
| ---- |---------------------------------------------------|
|| 400 | 잘못된 요청입니다. 보통 요청의 양식이 잘못된 경우입니다.(상품이 존재하지 않는 경우+요청 양식이 잘못된 경우) |
| 403 | 유효하지 않는 토큰입니다.                                    |
| 500 | 서버에 의한 오류입니다.                                     |

### Response Body
```dtd
{
    "totalElements": 3,
    "totalPages": 1,
    "first": true,
    "last": true,
    "numberOfElements": 3,
    "size": 10,
    "content": [
        {
            "id": 1,
            "name": "product1",
            "price": 1000,
            "imageUrl": "image.jpg"
        },
        {
            "id": 2,
            "name": "product2",
            "price": 2000,
            "imageUrl": "image.jpg"
        },
        {
            "id": 3,
            "name": "product3",
            "price": 3000,
            "imageUrl": "image.jpg"
        }
    ],
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
    },
    "empty": false
}
```
### 프론트엔드 요구 명세
```dtd
{
	"id":,
	"name":,
	"price":,
	
}
```
### /api/orders

#### POST
##### Description:

새 주문을 생성한다.
클라이언트는 'Authorization' 헤더에 'Bearer {토큰}' 형식으로 토큰을 제출해야 합니다.

##### Responses

| Code | Description |
| ---- | ----------- |
| 201 | 주문을 성공하였습니다. |
| 400 | 잘못된 요청입니다. 보통 주문에 옵션 정보가 잘못되었거나, 재고가 소진된 경우입니다. |
| 403 | 유효하지 않는 토큰입니다. |
| 500 | 서버에 의한 오류입니다. |

### Response Body
```dtd
{
	"id" : 0,
	"optionId" : 0,
	"quantity" : 0,
	"orderDateTime" : "2024-07-21T10:00:00",
	"message" : "String"
}
```
### /oauth/kakao

#### GET
##### Description:

서버가 클라이언트가 제출한 인가코드를 가지고 확인 과정을 거쳐 클라이언트에게 토큰을 발급합니다.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| code | query |  | Yes | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| 200 | 토큰을 성공적으로 발급하였습니다. |
| 400 | 잘못된 요청입니다. 같은 email이 존재하거나, 잘못된 요청 양식인 경우입니다. |
| 500 | 서버에 의한 오류입니다. |
