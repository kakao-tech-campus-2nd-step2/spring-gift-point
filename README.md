# spring-gift-point

##  1단계 -  API 명세

### 작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영한다.

- **요구 조건**
    - 팀 내에서 일관된 기준을 정하여 API 명세를 결정한다. 
    - 때로는 클라이언트의 편의를 위해 API 명세를 결정하는 것이 좋다.
    - 팀에서 통일한 API 명세는 다음과 같다

**사용자 관련 API**
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

**카테고리 관련 API**
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

**상품 관련 API**
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

**상품 옵션 관련 API**
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

**위시 리스트 관련 API**
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

**주문 관련 API**
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

<br>
<br>
<br>
<br>


- **구현 기능**
    - [x] SpringDoc를 사용하기 위한 Swagger설정 클래스 작성
    - [x] 각 컨트롤러에 Swagger어노테이션 추가 








---

- **기타 리팩토링 사항**
    - [ ] 오류처리 / 인증(JWT관련) 에 대해 추가적인 학습 후에 관련 코드 리팩토링
    - [ ] User서비스 계층에 있는 jwt 관련 로직들 따로 분리해서 관리하도록 리팩토링
    - [ ] User서비스 계층의 generateToken()메서드 body에 대칭키 기반의 HS512 말고 비대팅 키 기반의 RS256로 리팩토링