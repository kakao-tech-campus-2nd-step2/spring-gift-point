# spring-gift-point

# 1단계 - API 명세
## 기능 요구 사항
### 작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영한다.
- 팀 내에서 일관된 기준을 정하여 API 명세를 결정한다.
- 때로는 클라이언트의 편의를 위해 API 명세를 결정하는 것이 좋다.

### 회원 API
<table>
    <tr>
        <td> URL </td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /kakao/login </td>
        <td> GET </td>
        <td> 엑세스 토큰 발급 </td>
        <td> 인가코드를 통해 access 토큰을 받는다. </td>
    </tr>
</table>

### 카테고리 API
<table>
    <tr>
        <td> URL </td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/categories </td>
        <td> GET </td>
        <td> 카테고리 목록 조회 </td>
        <td> 모든 카테고리의 목록을 조회한다. </td>
    </tr>
</table>

### 삼품 API
<table>
    <tr>
        <td> URL </td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/products/{productId} </td>
        <td> GET </td>
        <td> 상품 조회 </td>
        <td> 특정 상품의 정보를 조회한다. </td>
    </tr>
    <tr>
        <td> /api/products?page=0&size=10&sort=name,asc&categoryId=1 </td>
        <td> GET </td>
        <td> 상품 목록 조회 (페이지네이션 적용) </td>
        <td> 모든 상품의 목록을 페이지 단위로 조회한다. </td>
    </tr>
</table>

### 상품 옵션 API
<table>
    <tr>
        <td> URL </td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/products/{productId}/options </td>
        <td> GET </td>
        <td> 상품 옵션 목록 조회 </td>
        <td> 특정 상품에 대한 모든 옵션을 조회한다. </td>
    </tr>
</table>

### 위시 리스트 API
<table>
    <tr>
        <td> URL </td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/wishes </td>
        <td> POST </td>
        <td> 위시 리스트 상품 추가 </td>
        <td> 회원의 위시 리스트에 상품을 추가한다. </td>
    </tr>
    <tr>
        <td> /api/wishes/{productId} </td>
        <td> DELETE </td>
        <td> 위시 리스트 상품 삭제 </td>
        <td> 회원의 위시 리스트에서 상품을 삭제한다. </td>
    </tr>
    <tr>
        <td> /api/wishes?page=0&size=10&sort=createdDate,desc </td>
        <td> GET </td>
        <td> 위시 리스트 상품 조회 (페이지네이션 적용) </td>
        <td> 회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다. </td>
    </tr>
</table>

### 주문 API
<table>
    <tr>
        <td> URL </td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/orders </td>
        <td> POST </td>
        <td> 주문하기 </td>
        <td> 새 주문을 생성한다. </td>
    </tr>
    <tr>
        <td> /api/orders?page=0&size=10&sort=orderDateTime,desc </td>
        <td> GET </td>
        <td> 주문 목록 조회 (페이지네이션 적용) </td>
        <td> 주문 목록을 페이지 단위로 조회한다. </td>
    </tr>
</table>

---

# 2단계 - 배포하기
## 기능 요구 사항
### 지금까지 만든 선무하기 서비스를 배포하고 클라이언트와 연동할 수 있어야 한다.
- 지속적인 배포를 위한 배포 스크립트를 작성한다.
- 클라이언트와 API 연동 시 발생하는 보안 문제에 대응한다.
    - 서버와 클라이언트의 `Origin` 이 달라 요청을 처리할 수 없는 경우를 해결한다.
- HTTPS는 필수는 아니지만 팀 내에서 논의하고 필요한 경우 적용한다.

---

# 3단계 - 포인트
## 기능 요구 사항
### 상품 구매에 사용할 수 있는 포인트 기능을 구현한다.
- 포인트는 사용자별로 보유한다.
- 포인트 차감 방법 등 나머지 기능에 대해서는 팀과 논의하여 정책을 결정하고 구현한다.
- API 명세는 팀과 협의하여 결정하고 구현한다.
- 관리자 화면에서 포인트를 충전할 수 있다.

### 팀 내에서 정한 정책
- 주문한 총금액이 5만원 이상일 경우 10% 할인하여 유저의 포인트를 차감한다.