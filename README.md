# spring-gift-point

## 구현할 기능 목록

### 1단계 - API 명세서
작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영한다.

팀 내에서 일관된 기준을 정하여 API 명세를 결정한다.
때로는 클라이언트의 편의를 위해 API 명세를 결정하는 것이 좋다.

<details>
<summary>회원 API</summary>

| URL                      | 메서드 | 기능      | 설명                             |
|--------------------------|--------|-----------|----------------------------------|
| /api/members/register    | POST   | 회원 가입 | 새 회원을 등록하고 토큰을 받는다. |
| /api/members/login       | POST   | 로그인    | 회원을 인증하고 토큰을 받는다.   |

</details>

<details>
<summary>카테고리 API</summary>

| URL                                  | 메서드 | 기능              | 설명                             |
|--------------------------------------|--------|-------------------|----------------------------------|
| /api/categories                      | POST   | 카테고리 생성     | 새 카테고리를 등록한다.          |
| /api/categories/{categoryId}         | PUT    | 카테고리 수정     | 기존 카테고리를 수정한다.        |
| /api/categories                      | GET    | 카테고리 목록 조회 | 모든 카테고리의 목록을 조회한다. |

</details>

<details>
<summary>상품 API</summary>

| URL                                                        | 메서드 | 기능                       | 설명                                  |
|------------------------------------------------------------|--------|----------------------------|---------------------------------------|
| /api/products                                              | POST   | 상품 생성                  | 새 상품을 등록한다.                   |
| /api/products/{productId}                                  | GET    | 상품 조회                  | 특정 상품의 정보를 조회한다.          |
| /api/products/{productId}                                  | PUT    | 상품 수정                  | 기존 상품의 정보를 수정한다.          |
| /api/products/{productId}                                  | DELETE | 상품 삭제                  | 특정 상품을 삭제한다.                 |
| /api/products?page=0&size=10&sort=name,asc&categoryId=1    | GET    | 상품 목록 조회 (페이지네이션 적용) | 모든 상품의 목록을 페이지 단위로 조회한다. |

</details>

<details>
<summary>상품 옵션 API</summary>

| URL                                                           | 메서드 | 기능                 | 설명                              |
|---------------------------------------------------------------|--------|----------------------|-----------------------------------|
| /api/products/{productId}/options                             | POST   | 상품 옵션 추가       | 상품에 옵션을 추가한다.           |
| /api/products/{productId}/options/{optionId}                  | PUT    | 상품 옵션 수정       | 기존 상품 옵션의 정보를 수정한다. |
| /api/products/{productId}/options/{optionId}                  | DELETE | 상품 옵션 삭제       | 기존 상품 옵션을 삭제한다.        |
| /api/products/{productId}/options                             | GET    | 상품 옵션 목록 조회  | 특정 상품에 대한 모든 옵션을 조회한다. |

</details>

<details>
<summary>위시 리스트 API</summary>

| URL                                                        | 메서드 | 기능                           | 설명                                              |
|------------------------------------------------------------|--------|--------------------------------|---------------------------------------------------|
| /api/wishes                                                | POST   | 위시 리스트 상품 추가          | 회원의 위시 리스트에 상품을 추가한다.              |
| /api/wishes/{wishId}                                       | DELETE | 위시 리스트 상품 삭제          | 회원의 위시 리스트에서 상품을 삭제한다.            |
| /api/wishes?page=0&size=10&sort=createdDate,desc           | GET    | 위시 리스트 상품 조회 (페이지네이션 적용) | 회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다. |

</details>

<details>
<summary>주문 API</summary>

| URL                                                        | 메서드 | 기능                    | 설명                                             |
|------------------------------------------------------------|--------|-------------------------|--------------------------------------------------|
| /api/orders                                                | POST   | 주문하기                | 새 주문을 생성한다.                               |
| /api/orders?page=0&size=10&sort=orderDateTime,desc         | GET    | 주문 목록 조회 (페이지네이션 적용) | 주문 목록을 페이지 단위로 조회한다.               |

</details>
