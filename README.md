# spring-gift-point

## Step1
- 기능 요구사항
  - 작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영한다.
    - 팀 내에서 일관된 기준을 정하여 API 명세를 결정한다.
    - 때로는 클라이언트의 편의를 위해 API 명세를 결정하는 것이 좋다.


### 사용자 관련 API
<table>
    <tr>
        <th>URL</th>
        <th>메서드</th>
        <th>기능</th>
        <th>설명</th>
    </tr>
    <tr>
        <td>/api/user/register</td>
        <td>POST</td>
        <td>회원 가입</td>
        <td>새로운 사용자를 등록한다.</td>
    </tr>
    <tr>
        <td>/api/login</td>
        <td>POST</td>
        <td>로그인</td>
        <td>사용자가 로그인한다.</td>
    </tr>
</table>

### 카테고리 관련 API
<table>
    <tr>
        <th>URL</th>
        <th>메서드</th>
        <th>기능</th>
        <th>설명</th>
    </tr>
    <tr>
        <td>/api/category</td>
        <td>POST</td>
        <td>카테고리 생성</td>
        <td>새로운 카테고리를 생성한다.</td>
    </tr>
    <tr>
        <td>/api/category/{id}</td>
        <td>PUT</td>
        <td>카테고리 수정</td>
        <td>지정한 카테고리를 수정한다.</td>
    </tr>
    <tr>
        <td>/api/category</td>
        <td>GET</td>
        <td>카테고리 목록 조회</td>
        <td>모든 카테고리의 목록을 조회한다.</td>
    </tr>
</table>

### 상품 관련 API
<table>
    <tr>
        <th>URL</th>
        <th>메서드</th>
        <th>기능</th>
        <th>설명</th>
    </tr>
    <tr>
        <td>/api/products</td>
        <td>POST</td>
        <td>상품 생성</td>
        <td>새로운 상품을 생성한다.</td>
    </tr>
    <tr>
        <td>/api/products/{id}</td>
        <td>GET</td>
        <td>상품 조회</td>
        <td>지정한 상품의 정보를 조회한다.</td>
    </tr>
    <tr>
        <td>/api/products/{id}</td>
        <td>PUT</td>
        <td>상품 수정</td>
        <td>지정한 상품의 정보를 수정한다.</td>
    </tr>
    <tr>
        <td>/api/products/{id}</td>
        <td>DELETE</td>
        <td>상품 삭제</td>
        <td>지정한 상품을 삭제한다.</td>
    </tr>
    <tr>
        <td>/api/products</td>
        <td>GET</td>
        <td>상품 목록 조회 (페이지네이션 적용)</td>
        <td>상품 목록을 페이지 단위로 조회한다.</td>
    </tr>
</table>

### 상품 옵션 관련 API
<table>
    <tr>
        <th>URL</th>
        <th>메서드</th>
        <th>기능</th>
        <th>설명</th>
    </tr>
    <tr>
        <td>/api/products/{productId}/options</td>
        <td>POST</td>
        <td>상품 옵션 추가</td>
        <td>지정한 상품에 옵션을 추가한다.</td>
    </tr>
    <tr>
        <td>/api/products/{productId}/options/{id}</td>
        <td>PUT</td>
        <td>상품 옵션 수정</td>
        <td>지정한 상품 옵션을 수정한다.</td>
    </tr>
    <tr>
        <td>/api/products/{productId}/options/{id}</td>
        <td>DELETE</td>
        <td>상품 옵션 삭제</td>
        <td>지정한 상품 옵션을 삭제한다.</td>
    </tr>
    <tr>
        <td>/api/products/{productId}/options</td>
        <td>GET</td>
        <td>상품 옵션 목록 조회</td>
        <td>지정한 상품의 모든 옵션을 조회한다.</td>
    </tr>
</table>

### 위시 리스트 관련 API
<table>
    <tr>
        <th>URL</th>
        <th>메서드</th>
        <th>기능</th>
        <th>설명</th>
    </tr>
    <tr>
        <td>/api/wish</td>
        <td>POST</td>
        <td>위시 리스트 상품 추가</td>
        <td>회원의 위시 리스트에 상품을 추가한다.</td>
    </tr>
    <tr>
        <td>/api/wish/{id}</td>
        <td>DELETE</td>
        <td>위시 리스트 상품 삭제</td>
        <td>회원의 위시 리스트에서 상품을 삭제한다.</td>
    </tr>
    <tr>
        <td>/api/wish</td>
        <td>GET</td>
        <td>위시 리스트 상품 조회 (페이지네이션 적용)</td>
        <td>회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.</td>
    </tr>
</table>

### 주문 관련 API
<table>
    <tr>
        <th>URL</th>
        <th>메서드</th>
        <th>기능</th>
        <th>설명</th>
    </tr>
    <tr>
        <td>/api/product/order/{optionId}</td>
        <td>POST</td>
        <td>주문하기</td>
        <td>새 주문을 생성한다.</td>
    </tr>
    <tr>
        <td>/api/product/order</td>
        <td>GET</td>
        <td>주문 목록 조회 (페이지네이션 적용)</td>
        <td>주문 목록을 페이지 단위로 조회한다.</td>
    </tr>
</table>


## Step2
- 기능 요구사항
  - 지금까지 만든 선물하기 서비스를 배포하고 클라이언트와 연동할 수 있어야 한다.
    - 지속적인 배포를 위한 배포 스크립트를 작성한다.
    - 클라이언트와 API 연동 시 발생하는 보안 문제에 대응한다.
    - 서버와 클라이언트의 Origin이 달라 요청을 처리할 수 없는 경우를 해결한다.
    - HTTPS는 필수는 아니지만 팀 내에서 논의하고 필요한 경우 적용한다.

