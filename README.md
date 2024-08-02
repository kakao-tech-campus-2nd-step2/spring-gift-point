# spring-gift-point
> 카카오테크 캠퍼스 STEP2 - 6주차 클론 코딩
 
## 목차
[* 코드 소개](#코드-소개)<br>
[* 0 단계 - 기본 코드 준비](#-0-단계----기본-코드-준비)<br>
[* 1 단계 - API 명세](#-1-단계----api-명세)<br>

## 코드 소개
카카오 선물하기의 상품을 관리하는 서비스를 구현해본다

## < 0 단계 > - 기본 코드 준비
### [ 기능 요구 사항 ]
- [spring-gift-order](https://github.com/chris0825/spring-gift-order.git)의 코드를 옮겨온다.

## < 1 단계 > - API 명세
### [ 기능 요구 사항 ]
> 작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영한다.
- 팀 내에서 일관된 기준을 정하여 API 명세를 결정한다.
- 때로는 클라이언트의 편의를 위해 API 명세를 결정하는 것이 좋다.

### [ 연동 API 명세 ]
<details>
<summary> 로그인 API </summary>

### 카카오 로그인 및 회원 가입 (Authorize code & access token)
- request
> GET /kakao/login HTTP/1.1
- rseponse
```json
{
	"token": "String"
}
```
### 로그인 이후 HTTP 헤더에 로그인 시 발급된 토큰을 포함한 인증 헤더가 존재하여야 한다.
> Authorization: Bearer {token}
</details>
<details>
<summary> 카테고리 API </summary>

### 카테고리 목록 조회
- request
> GET /api/categories HTTP/1.1
- response
```json
HTTP/1.1 200 OK
Content-Type: application/json
[
  {
    "id": Long,
    "name": "String",
    "color": "String",
    "imageUrl": "String",
    "description": "String"
  }
]
```
</details>
<details>
<summary> 상품 API </summary>

### 상품 상세 조회
- request
> GET /api/products/{productId} HTTP/1.1
- response
```json
HTTP/1.1 200 OK
Content-Type: application/json
{
	"name": "String",
	"price": "int",
	"imageUrl": "String",
	"categoryId": "Long"
}
```
### 전체 상품 목록 조회
- request
> GET /api/products?page=0&size=10&sort=name,asc&categoryId=1 HTTP/1.1
- response
```json
HTTP/1.1 200 OK
Content-Type: application/json
[
  {
    "id": Long,
    "name": "String",
    "price": "int",
    "imageUrl": "String",
    "categoryId": "Long"
  }
]
```
</details>
<details>
<summary> 옵션 API </summary>

### 상품 하위 옵션 조회
- request
> GET /api/products/{productId}/options HTTP/1.1
- response
```json
HTTP/1.1 200 OK
Content-Type: application/json
[
	{
		"id": "Long",
		"name": "String",
		"quantity": "int"
	}
]
```
</details>
<details>
<summary> 위시 API </summary>

### 위시 상품 추가
- request
```json
POST /api/wishes HTTP/1.1
Content-Type: application/json
Authorization: Bearer {token}
{
	"productId": "Long"
}
```
- response
```json
HTTP/1.1 201 CREATED
Content-Type: application/json
{
	"productId": "Long"
}
```
### 위시 상품 삭제
- request
> DELETE /api/wishes/{productId} HTTP/1.1
- response
```json
HTTP/1.1 204 NO_CONTENT
```
### 위시 리스트 조회
- request
> GET /api/wishes?page=0&size=10&sort=createdDate,desc HTTP/1.1
- response
```json
HTTP/1.1 200 OK
Content-Type: application/json
[
	{
		"id": "Long",
		"product": {
			"id": "Long",
			"name": "String",
			"price": "int",
			"imageUrl": "String"
		}
	}
]
```
</details>
<details>
<summary> 주문 API </summary>
### 주문하기
- request
```json
POST /api/orders HTTP/1.1
Content-Type: application/json
Authorization: Bearer {token}
{
	"optionId": "Long",
	"quantity": "int",
	"message": "String"
}
```
- response
```json
HTTP/1.1 200 OK
Content-Type: application/json
{
	"id": "Long",
	"optionId": "Long",
	"quantity": "int",
	"orderDateTime": "YYYY-MM-DDTHH:MM:SS",
	"message": "String"
}
```
### 주문 목록 조회
- request
> GET /api/orders?page=0&size=10&sort=orderDateTime,desc HTTP/1.1
- response
```json
HTTP/1.1 200 OK
Content-Type: application/json
[
	{
		"id": "Long",
		"optionId": "Long",
		"quantity": "int",
		"orderDateTime": "YYYY-MM-DDTHH:MM:SS",
		"message": "String"
	}
]
```
</details>