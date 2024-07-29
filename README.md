# spring-gift-productEntity

1주차
step-1의 구현 사항
0. 데이터베이스 : 별도의 데이터베이스가 없으므로 컬렉션인 HashMap 사용
1. 상품을 추가, 삭제, 수정, 조회할 수 있는 HTTP API 구현
   1. 추가 가능 : Post에 해당하며, Product를 입력받아 HashMap에 저장한다.
   2. 삭제 기능 : Delete에 해당하며, key값을 입력받아 HashMap에서 제거한다.
   3. 수정 기능 : Put에 해당하며, key값과 Product를 입력받아 HashMap에 업데이트한다.
   4. 조회 기능 : Get에 해당하며, HashMap에 있는 모든 데이터를 불러오거나, 특정 key값에 해당하는 데이터를 불러온다.

step-2의 구현 사항
1. thymeleaf를 이용한 관리자 페이지 구현
   1. 조회 시 사용할 productEntities.html의 구현(id, name, price, imageUrl을 컨트롤러에서 받아 테이블 형태로 화면에 출력)
   2. productEntities.html에 사용할 css파일(style.css) 구현
   3. 조회 기능을 담당하는 메소드에서 productEntity.html을 사용하도록 연결
   4. 추가 기능을 웹으로 구현하기 위해 add.html 및 menu.css 추가
   5. 수정 기능을 웹으로 구현하기 위해 edit.html 및 menu.css 추가
   6. 웹 화면에서 추가, 삭제, 수정, 조회 기능을 담당하는 adminController 클래스 추가
2. step-1에서 미흡한 사항 보완
   1. json형태로 통신하는 것을 명시하기 위해 추가, 삭제, 수정을 담당하는 메소드에 @RequestBody 어노테이션 추가
   2. 추가, 삭제, 수정 시 완료 여부를 String 값으로 리턴 => ResponseEntity형태로 변경, Status와 Body를 추가하여 리턴하며, 기존의 String 내용은 Body에 담음.

step-3의 구현 사항
1. HashMap을 Database로 교체
   1. Database를 사용하기 위해 메모리에 데이터를 저장하는 h2 Database를 사용
   2. 기존의 HashMap은 제거하고, ProductDAO(Data Access Object)를 만들어 사용
   3. ProductDAO에는 기존의 HashMap에서 지원하는 추가, 삭제, 수정, 조회 기능을 구현해야 함
      0. 데이터베이스 생성 : sql구문 및 jdbcTemplate의 execute메소드 를 통해 생성
      1. 추가 기능 : 파라미터로 Product를 받으며, sql구문 및 jdbcTemplate의 update 메소드를 통해 추가
      2. 삭제 기능 : 파라미터로 Long id를 받으며, sql구문 및 jdbcTemplate의 update 메소드를 통해 삭제
      2. 수정 기능 : 파라미터로 Long id 및 Product를 받으며, sql구문 및 jdbcTemplate의 update 메소드를 통해 수정
      3. 조회 기능 : 전체 데이터를 조회할 경우 query를 통해 받으며, 파라미터로 Long id를 입력받아 queryForObject를 이용하여 받음
   4. 기타 기능
      1. url, memberDTO, password를 빠르게 입력하기 위하여 getConnection() 구현
      2. application.properties에서 url을 "jdbc:h2:mem:test"으로 지정
2. step-2에서 미흡한 사항 보완
   1. ProductController에서 추가, 삭제, 수정 기능에서 성공 여부 판별을 위해 if문 사용 : Database 사용 시 다시 조회해서 확인해야하는 불편함 있음
      => try-catch로 변경하고 에러 발생 시 실패, 에러가 발생하지 않을 경우 성공

2주차

1. step-1의 구현사항
   1. 상품 추가 및 수정 시 잘못된 값 판별
      1. 상품 이름은 공백을 포함하여 최대 15글자까지 입력 가능
         => @Size(max=15) 어노테이션 사용
      2. 특수문자 '( ), [ ], +, -, &, /, _'만 사용 가능
         => @Pattern 어노테이션에서 정규식 표현을 사용
      3. '카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능
         => @Pattern 어노테이션에서 정규식 표현을 사용
2. step-2의 구현사항
   ~~1. email과 password를 POST
   => String email, String password를 포함하는 UserInfo 클래스 생성, UserInfo를 저장하는 UserInfoDAO 클래스 생성
   2. accessToken을 return
      => accessToken 및 TokenType을 가지는 JwtToken 클래스 생성
      => 토큰을 생성하기 위해 jjwt 라이브러리 사용
      => Database에 있는 값과 일치할 경우에 토큰을 생성하여 리턴하는 LoginController 클래스 추가~~
   1. 회원가입
      - @PostMapping("/memebers/register")과 매핑, 테이블에 email값이 포함된 데이터를 카운트해서 0이면 생성, 토큰을 리턴한다.
      - 실패 시 RegisterException을 throw하며, 403 Forbidden 리턴
   2. 로그인
      - @PostMapping("/memebers/login")과 매핑, 테이블에 email값이 포함된 데이터를 찾아서 비밀번호가 일치하면 토큰을 리턴한다.
      - 실패 시 LoginException을 throw하며, 403 Forbidden 리턴
3. step-3의 구현사항
   1. 로그인 후 받은 토큰을 사용하여 사용자별 위시 리스트를 구현한다.
      => 토큰을 파싱하여 이메일 및 역할에 따라 접근 가능 여부를 판단
   2. 위시 리스트에 있는 상품을 조회할 수 있다.
      => 위시 리스트 데이터베이스에서 이메일이 일치하는 상품만 리턴한다.
   3. 위시 리스트에 상품을 추가할 수 있다.
      => 위시 리스트 데이터베이스에 추가한다.
   4. 위시 리스트에 담긴 상품을 삭제할 수 있다.
      => 위시 리스트 데이터베이스에 있을 경우 삭제한다.

3주차

1. step-1의 구현사항
   1. 지금까지 작성한 JdbcTemplate 기반 코드를 JPA로 리팩터링한다.
      => 기존의 DAO 클래스는 모두 삭제
      => Member, Product, Wishlist 클래스를 각각 Entity로 생성
      => 각각의 데이터베이스를 담당할 MemberRepository, ProductRepository, WishlistRepository 생성
      => 테스트를 위해 data.sql 추가 및 데이터 삽입
      => 테스트를 위해 각 데이터베이스 별 테스트 코드 작성, 크게 추가와 interface에 추가한 기능 확인용으로 사용

2. step-2의 구현사항
   1. 각 데이터베이스의 연관 관계를 설정
      => 연관 관계가 있는 것 : Wishlist(다) <-> Member(일) (다대일 및 일대다 관계), Product(일) <-> Wishlist(다) (다대일 및 일대다 관계)
      => 일대다 관계에서 일에 해당하는 데이터 삭제 시 다에 해당하는 데이터도 삭제해야 함(Ex. 상품 목록에 있는 상품이 사라지면 위시리스트에 추가한 상품도 사라져야 함)
   2. step-1에서 미흡한 사항 보완
      1. Repository와 매핑되는 Entity를 Controller 단계에서 그대로 사용하는 문제
         => DTO와 Entity를 분리하여 사용(리팩토링 진행)

3. step-3의 구현사항
   1. 페이지네이션의 구현
      => jpa의 기능 중 하나인 page pageable 및 page 기능을 이용하여 구현
      => 상품 목록(Product) 및 위시리스트(Wish)에 대해서만 구현
      => 추가적인 html 파일 필요(위시리스트를 위한 wish.html 추가 예정, 상품 목록은 기존의 products.html 활용)
      => Repository에 메소드 추가 -> Service 클래스 수정 -> Controller 클래스 수정 -> html 파일 수정 단계로 진행
   2. 2단계 미흡사항
      => 로그인 기능의 수정 -> 기존의 Controller 단계에서 토큰을 받아 처리하는 대신, interceptor를 통해 토큰을 검증하고 이메일을 속성에 추가

4주차

1. step-1의 구현사항
   1. 상품 정보에 카테고리를 추가
      => category Repository 추가, 가지는 값은 Long id(Product와 OneToOne 매핑), String name, String color, String imageUrl, String descripton (1)
      => category의 Long id를 매핑 없이 하고, ProductEntity에 OneToOne으로 카테고리 추가 (2)
   2. 테스트 코드 추가
      => 이전의 Repository들처럼 create, read 기능에 대한 테스트 코드 작성
      
2. step-2의 구현사항
   1. 상품 정보에 옵션 추가
      1. 상품에는 항상 하나 이상의 옵션이 있어야 한다.
      2. 옵션 이름은 최대 50글자까지 가능, 중복 불가
      3. 특수 문자는 ( ), [ ], +, -, &, /, _ 만 가능
      4. 옵션 수량은 1개 이상 1억개 미만
   => OptionDTO 클래스 추가(Long id, String name, Long quantity)
   => OptionEntity 클래스 추가(Long id, ProductEntity product, String name, Long quantity)
   => OptionRepository 클래스 추가
   => OptionException 예외 추가
   => ~~ProductEntity 및 ProductDTO에 옵션 추가(이거 꼭 해야하나?? 오히려 나중에 사용자가 위시리스트에 제품을 담을 때 옵션을 선택해야 하므로 위시리스트에 연결이 맞지 않을까)~~
   => data.sql 및 schma.sql에 데이터 및 테이블(options) 추가
   => OptionService 클래스 추가(기본적인 CRUD 지원)
   => OptionRestController 클래스 추가
   2. 테스트 코드 추가
      => Repository 단계에 대한 테스트 코드 작성 (+시간 남을 경우 Controller 단계에 대한 테스트 코드 추가로 작성, Mockito 이용)

3. step-3의 구현사항
   1. 옵션의 수량 차감 기능 구현
      => 별도의 http api 필요 x
      => 서비스 또는 엔티티 클래스에서 기능 구현 후 나중에 사용 (서비스 클래스 단계에서 구현)
   2. 구현 방법
      => 파라미터로 옵션 및 차감 개수를 받음
      => 해당 옵션을 데이터베이스에서 불러옴
      => 옵션의 수량에서 차감한 후, 0보다 큰지 확인, 0보다 작으면 exception 발생
      => 다시 데이터베이스에 해당 옵션 저장

5주차

1. step-1의 구현사항 
   1. 카카오 로그인 구현
      => 기존 로그인은 그대로 두기
      => oauth를 통해 토큰 발행 -> 해당 토큰을 추가해 access token 발급
      => 숨겨야 할 데이터인 api key, client id, redirect uri는 별도의 proerties 파일 생성하여 .gitignore에 추가

2. step-2의 구현사항
   1. 카카오톡 메시지 API를 사용하여 주문하기 기능을 구현
      => 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
      => 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
      => 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
      => 나에게 보내기를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
      => 메시지는 메시지 템플릿의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.\
   2. 예상 개발 과정
      1. OrderEntity 클래스 생성
      2. OrderRequest, OrderResponse 클래스 생성
      3. OrderRepository 인터페이스 생성
      4. OrderService 클래스 생성
      5. OrderRestController 클래스 생성(/api/order POST 추가)
   3. 카카오톡 나에게 메시지 보내기
      => text 타입으로 전송
      => OrderResponse의 내용을 전달

3. step-3의 구현사항
   1. api 문서 만들기
   - Excel(또는 구글 스프레드 시트)을 이용하여 api 명세
     => 형식이 없고 귀찮음… 그러나 편함
   - swagger을 이용하여 api 문서 자동화
     => Postman처럼 api 요정 툴로써의 기능을 사용할 수 있음.
     => 어플리케이션 코드에 많은 양의 어노테이션이 추가되어야 함
   - spring Rest Docs를 사용하여 문서 자동화
     => Controller 단위의 테스트 코드 작성 필수 -> 테스트 코드가 필수적이며 테스트에 실패할 경우 api 문서가 생성되지 않는 문제
     => 테스트 코드가 성공할 때에만 api 문서가 생성되기 때문에 api 스펙 정보와 api 문서 내의 정보의 불일치로 인해 생기는 문제를 방지해줌
- 세 가지 방법 중 이번에는 swagger를 통해 api 문서를 명세화하려 함