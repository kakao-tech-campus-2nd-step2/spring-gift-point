# spring-gift-product

# Step 1
> 상품을 조회, 추가, 수정, 삭제할 수 있는 간단한 HTTP API를 구현한다.
>  - HTTP 요청과 응답은 JSON 형식으로 주고받는다.
>  - 현재는 별도의 데이터베이스가 없으므로 적절한 자바 컬렉션 프레임워크를 사용하여 메모리에 저장한다.

## 상품 DB

### DB 정보
- 자료구조: Map<Long, Product>
- 저장형식: HashMap

### 속성
- id: Long - 상품의 고유 식별자
- name: String - 상품명
- price: int - 가격
- imageUrl: String - 상품 이미지 URL
- status: Boolean - 상품 활성화 상태 (true: 활성, false: 비활성)

## 기능 명세

### URL 라우팅
- 클라이언트에게 url 받기
- 해당 url에 맞는 기능 매핑해주기

### 조회
1. 대상 postId 존재하는지 조회
2. 존재하면 DB에서 데이터 가져오기
3. 데이터를 json으로 변환해서 보내기

### 추가
1. Product 객체에 request 데이터 담기
2. Product 객체를 DB에 저장
3. 성공/실패 메세지 보내기
- 상품 중복 검사는 별도로 진행하지 않음
  - 실제 카카오톡 선물하기 서비스에서도 상품 이름에 대한 중복 검사를 하지 않는 것으로 보임

### 수정
1. 대상 postId 존재 확인
2. 상품이 존재하면 Product 객체에 request 데이터 담음
3. Product 객체에서 null이 아닌 요소를 골라서 DB값 수정
4. 성공/실패 메세지 보내기

### 삭제
1. 대상 postId 존재 확인
2. 상품이 존재하면 삭제
3. 성공/실패 메세지 보내기

## API 명세
 
### 1. 상품 단일 조회
DB에 저장되어 있는 단일 상품 정보를 반환
#### request
- url: **`[GET] /api/product?product={productId}`**
- queryString
  - productId : 검색 제품의 productId
- body : 없음

#### response
- 응답 성공
  - HTTP 코드
  - response parameter
  ```
  - product
      - id
      - name
      - price
      - imageUrl
  ```

응답 실패
- HTTP 코드
- 실패 원인 메세지


### 2. 단일 상품 추가
새로운 단일 상품 정보를 DB에 추가
#### request
- url: **`[POST] /api/product`**
- queryString:없음
- body
    ```
  - product
        - name
        - price
        - imageUrl
  ```
    

#### response
응답 성공
- HTTP 코드
응답 실패
- HTTP 코드
- 실패 메세지


### 3. 단일 상품 수정
이미 DB에 존재하는 단일 상품 정보를 수정
#### request
- url: **`[PATCH] /api/product`**
- queryString:없음
- body
  - 수정할 항목은 값 추가
  - 수정하지 않는 항목은 비어둠
      ```
      - product
            - productId : 수정할 상품 ID
            - name : 수정값 (null 허용)
            - price : 수정값 (null 허용)
            - imageUrl: 수정값 (null 허용)
      ```

#### response
응답 성공
- HTTP 코드
- 수정한 상품의 productId

응답 실패
- HTTP 코드
- 실패 메세지


### 4. 단일 상품 삭제
이미 DB에 존재하는 단일 상품 정보를 삭제
#### request
- url: **`[PATCH] /api/product/deleted?product={productId}`**
- queryString: 삭제할 상품 productId
- body : 없음

#### response
응답 성공
- HTTP 코드
- 성공 메세지

응답 실패
- HTTP 코드
- 실패 메세지


---
# Step 2
> **상품을 조회, 추가, 수정, 삭제할 수 있는 관리자 화면을 구현한다.**
> - Thymeleaf를 사용하여 서버 사이드 렌더링을 구현한다.
> - 기본적으로는 HTML 폼 전송 등을 이용한 페이지 이동을 기반으로 하지만, 자바스크립트를 이용한 비동기 작업에 관심이 있다면 이미 만든 상품 API를 이용하여 AJAX 등의 방식을 적용할 수 있다.
> - 상품 이미지의 경우, 파일을 업로드하지 않고 URL을 직접 입력한다.

## 기능 명세서

### 메인 페이지
**상품 목록 조회**
  - 상품 목록을 조회할 수 있는 페이지를 구현한다.
  - 상품 목록은 테이블 형태로 출력한다.
  - 상품 목록은 상품명, 가격, 이미지 URL를 출력한다.
  - 상품 목록은 상품명을 클릭하면 상품 상세 페이지로 이동한다.

**상품 추가 버튼**
  - 상품 추가 버튼을 구현한다.
  - 상품 추가 버튼을 클릭하면 상품 추가(수정) 페이지로 이동한다.

**상품 수정 버튼**
  - 상품 수정 버튼을 구현한다.
  - 상품 수정 버튼을 클릭하면 상품 추가(수정) 페이지로 이동한다.

**상품 삭제 버튼**
  - 상품 삭제 버튼을 구현한다.
  - 상품 삭제 버튼을 클릭하면 삭제 확인창이 뜬다
  - 확인 버튼을 누르면 상품이 삭제된다.
  
### 상품 상세 조회 페이지
  - 상품 상세 페이지를 구현한다.
  - 상품 상세 페이지는 상품명, 가격, 이미지 URL를 출력한다.

### 상품 추가(수정) 페이지
  - 상품 추가(수정) 페이지를 구현한다.
  - 상품명, 가격, 이미지 URL을 입력할 수 있는 폼을 구현한다.
  - 추가 버튼을 눌렀을 땐 비어있는 란을 보여준다
  - 수정 버튼을 눌렀을 땐 상품의 기존 정보를 보여준다
  - 확인 버튼을 누르면 상품이 추가(수정)된다.

## API 수정 사항
### 조회
- 전체 상품 조회 API를 추가한다.
  - 상품 객체 목록을 JSON 형태로 반환한다.

### 수정
- 수정 API 방식을 변경한다.
  - 기존 수정 API 방식은 null이 아닌 상품 항목만 수정되는 방식이었다.
  - 수정 API 방식을 변경하여, 전체 상품 정보를 받아서 기존 DB를 대체하는 방식으로 변경한다.

---
## Step 3
> 자바 컬렉션 프레임워크를 사용하여 메모리에 저장하던 상품 정보를 데이터베이스에 저장한다.
> - `JdbcTemplate`, `SimpleJdbcinsert` 및 `JdbcClient` 같은 도구를 사용할 수 있다.

## 기능 명세서
### DB 정보
- DBMS: H2
- DB api: JdbcTemplate

### 데이터베이스 초기화
> 사용하는 테이블은 애플리케이션이 실행될 때 구축되어야 한다.

- 스키마 스크립트와 데이터 스크립트를 따로 작성한다.
#### 스키마 스크립트
  - 테이블 생성, 인덱스 설정
  - `CREATE TABLE`, `CREATE INDEX` 등의 DDL을 사용
#### 데이터 스크립트
  - 초기 데이터 삽입
  - `INSERT INTO` 등의 DML을 사용
#### 스프링 부트 설정
  - 스키마 및 데이터 스크립트의 위치는 각각 `spring.sql.init.schema-locations` 및 `spring.sql.init.data-locations`를 사용하여 사용자 지정할 수 있다.
  - 스크립트 실행은 기본적으로 내장 메모리 데이터베이스(H2 같은)를 사용할 때 수행
  - `spring.sql.init.mode`를 `always`로 설정하여 애플리케이션 시작 시 스크립트를 실행할 수 있다.

### 기존 해쉬맵 db 함수 대체
기존 해쉬맵 db 함수를 JdbcTemplate을 이용해 직접 구현한다.
1. 아이디로 상품 조회
2. 전체 상품 조회
3. 상품 추가
4. 상품 수정
5. 상품 삭제

### Exception 처리
1. `EmptyResultDataAccessException`
  - 특정 ID로 조회 시 결과가 없을 때 발생
  - 클라이언트에게 요청한 상품이 없다는 메시지를 제공
2. `DataIntegrityViolationException`
    - 필수 필드 누락, 데이터 타입 불일치 등 데이터 무결성 제약 조건 위반 시 발생
    - 입력 데이터를 확인하라는 메시지를 제공
3. `DataAccessException`
    - SQL 실행 실패나 데이터베이스 연결 문제 등 기타 데이터 접근 중 발생하는 예외를 처리
    - 데이터베이스 오류 메시지를 제공
4. `Exception`
   - 예상치 못한 기타 예외들을 처리
   - 시스템 내에서 발생할 수 있는 다른 모든 예외의 "catch-all" 역할