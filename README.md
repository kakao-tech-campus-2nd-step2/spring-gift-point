# spring-gift-point
## 기능 요구 사항
### 작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영한다.
- 팀 내에서 일관된 기준을 정하여 API 명세를 결정한다.
- 때로는 클라이언트의 편의를 위해 API 명세를 결정하는 것이 좋다.

### 회원 API
<table>
    <tr>
        <td> URL <td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /kakao/login <td>
        <td> GET </td>
        <td> 엑세스 토큰 발급 </td>
        <td> 인가코드를 통해 access 토큰을 받는다. </td>
    </tr>
</table>

### 카테고리 API
<table>
    <tr>
        <td> URL <td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/categories <td>
        <td> GET </td>
        <td> 카테고리 목록 조회 </td>
        <td> 모든 카테고리의 목록을 조회한다. </td>
    </tr>
</table>

### 삼품 API
<table>
    <tr>
        <td> URL <td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/products/{productId} <td>
        <td> GET </td>
        <td> 상품 조회 </td>
        <td> 특정 상품의 정보를 조회한다. </td>
    </tr>
    <tr>
        <td> /api/products?page=0&size=10&sort=name,asc&categoryId=1 <td>
        <td> GET </td>
        <td> 상품 목록 조회 (페이지네이션 적용) </td>
        <td> 모든 상품의 목록을 페이지 단위로 조회한다. </td>
    </tr>
</table>

### 상품 옵션 API
<table>
    <tr>
        <td> URL <td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/products/{productId}/options <td>
        <td> GET </td>
        <td> 상품 옵션 목록 조회 </td>
        <td> 특정 상품에 대한 모든 옵션을 조회한다. </td>
    </tr>
</table>

### 위시 리스트 API
<table>
    <tr>
        <td> URL <td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/wishes <td>
        <td> POST </td>
        <td> 위시 리스트 상품 추가 </td>
        <td> 회원의 위시 리스트에 상품을 추가한다. </td>
    </tr>
    <tr>
        <td> /api/wishes/{productId} <td>
        <td> DELETE </td>
        <td> 위시 리스트 상품 삭제 </td>
        <td> 회원의 위시 리스트에서 상품을 삭제한다. </td>
    </tr>
    <tr>
        <td> /api/wishes?page=0&size=10&sort=createdDate,desc <td>
        <td> GET </td>
        <td> 위시 리스트 상품 조회 (페이지네이션 적용) </td>
        <td> 회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다. </td>
    </tr>
</table>

### 주문 API
<table>
    <tr>
        <td> URL <td>
        <td> 메서드 </td>
        <td> 기능 </td>
        <td> 설명 </td>
    </tr>
    <tr>
        <td> /api/orders <td>
        <td> POST </td>
        <td> 주문하기 </td>
        <td> 새 주문을 생성한다. </td>
    </tr>
    <tr>
        <td> /api/orders?page=0&size=10&sort=orderDateTime,desc <td>
        <td> GET </td>
        <td> 주문 목록 조회 (페이지네이션 적용) </td>
        <td> 주문 목록을 페이지 단위로 조회한다. </td>
    </tr>
</table>