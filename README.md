# spring-gift-point
> 카카오테크 캠퍼스 STEP2 - 6주차 클론 코딩
 
## 목차
[* 코드 소개](#코드-소개)<br>
[* 0 단계 - 기본 코드 준비](#-0-단계----기본-코드-준비)<br>
[* 1 단계 - API 명세](#-1-단계----api-명세)<br>
[* 2 단계 - 배포하기](#-2-단계----배포하기)<br>
[* 3 단계 - 포인트](#-3-단계----포인트)<br>

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

## < 2 단계 > - 배포하기
### [ 기능 요구 사항 ]
> 지금까지 만든 선물하기 서비스를 배포하고 클라이언트와 연동할 수 있어야 한다.
- 지속적인 배포를 위한 배포 스크립트를 작성한다.
- 클라이언트와 API 연동 시 발생하는 보안 문제에 대응한다.
  - 서버와 클라이언트의 Origin이 달라 요청을 처리할 수 없는 경우를 해결한다.
- HTTPS는 필수는 아니지만 팀 내에서 논의하고 필요한 경우 적용한다.

## < 3 단계 > - 포인트
### [ 기능 요구 사항 ]
> 상품 구매에 사용할 수 있는 포인트 기능을 구현한다.
- 포인트는 사용자별로 보유한다.
- 포인트는 본인이 보유하고 있는 포인트 내에서 주문 금액의 최대 10%까지 자동 사용된다.
  - 본인의 보유 포인트가 상품 주문 금액의 10% 이상인 경우 -> 상품 주문 금액의 10%만큼 보유 포인트 차감
  - 본인의 보유 포인트가 상품 주문 금액의 10% 미만인 경우 -> 보유 포인트 전액 차감
  => 포인트가 차감된 만큼 주문시 실 결제 금액에서 차감된다.
- 포인트의 충전은 관리자가 직접 관리한다. (이벤트 성 충전 개념)
- 유저는 본인의 보유 포인트를 조회할 수 있다.