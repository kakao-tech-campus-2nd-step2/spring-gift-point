# spring-gift-order
## step1
> ### 기능 요구 사항
> 카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.
> - 카카오계정 로그인을 통해 인증 코드를 받는다.
> - 토큰 받기를 읽고 액세스 토큰을 추출한다.
> - 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
> - (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.

### 기능 목록
#### User
- 소셜 로그인 (카카오 로그인) api를 구현한다
    - 소셜 로그인을 통해 인가 코드를 받는다.
    - 인가 코드를 사용해 토큰을 받는다.
    - 토큰을 사용해 사용자 정보(profile_nickname)를 받아온다.
    - DB에 사용자 정보가 존재한다면, JWT 토큰을 발급한다.
    - DB에 사용자 정보가 존재하지 않는다면, 회원가입을 진행한다.
    - 회원가입 시, 추가적으로 사용자 정보를 받아 DB에 저장한다.
    - 회원가입 후, JWT 토큰을 발급한다.

## step2
> ### 기능 요구 사항
> **카카오톡 메시지 API를 사용하여 주문하기 기능을 구현한다.**
> - 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
> - 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
> - 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
> - 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
> - 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.
>
> 아래 예시와 같이 HTTP 메시지를 주고받도록 구현한다.
>
> **Request**
> ```
> POST /api/orders HTTP/1.1
> Authorization: Bearer {token}
> Content-Type: application/json
> 
> {
>   "optionId": 1,
>   "quantity": 2,
>   "message": "Please handle this order with care."
> }
> ```
>
> **Response**
> ```
> HTTP/1.1 201 Created
> Content-Type: application/json
>
> {
  > "id": 1,
  > "optionId": 1,
  > "quantity": 2,
  > "orderDateTime": "2024-07-21T10:00:00",
  > "message": "Please handle this order with care."
> }
> ```

### 데이터베이스 테이블: Order

| 필드명              | 데이터 타입    | 설명           | 기타 조건                    |
|------------------|-----------|--------------|--------------------------|
| id               | Long      | 주문 고유 식별자    | Primary Key, 자동 생성       |
| optionId         | Long      | 옵션 고유 식별자    | FK (Option)  , not null  |
| userId           | Long      | 주문자 고유 식별자   | FK (AppUser)  , not null |
| quantity         | int       | 주문 수량        | 1 이상 , not null          |
| message          | String    | 수령인에게 보낼 메세지 |                          |
| registrationDate | DateTime | 생성일자         |                          |
| modificationDate | DateTime | 수정일자         |                          |
| deletionDate     | DateTime   | 삭제일자         |                          |

### 기능 목록
#### Order
- `Order` 엔티티 클래스를 작성한다.
- Order 생성 (주문하기) 기능을 구현한다.
    - 상품 옵션과 해당 수량에 따라 해당 상품 옵션의 수량이 차감된다.
    - 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
    - 주문이 완료되면 주문 내역을 카카오톡 메시지로 전송한다.

#### WishList
- 위시 리스트에 있는 상품이 주문될 경우 위시 리스트에서 삭제하는 기능을 구현한다.