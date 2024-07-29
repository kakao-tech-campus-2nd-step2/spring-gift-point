# spring-gift-jpa#
KTC step2 클론코딩 선물하기 구현

API 문서
https://pollen-car-5e9.notion.site/Kakao-Gift-API-c5c6db2a153e4089a6bcf0445afaaf13

## 객체
### Product
- **id** : 상품 id
- **name** : 상품 이름
- **price** : 상품 가격
- **imageUrl** : 상품 사진 url

### Member
- **id** : 회원 id
- **email** : 회원 이메일
- **password** : 비밀번호

### Wishlist
- **id** : 위시리스트 고유 id
- **userId** : 상품을 넣은 회원 id
- **productId** : 해당 상품 id

## 기능 소개
### ProductDao
- **selectAllProduct** : 모든 상품 목록 List반환
- **selectProduct** : id에 해당하는 상품 하나를 반환
- **insertProduct** : 상품 추가
- **deleteProduct** : 상품 삭제
- **updateProduct** : 상품 수정

### IndexController
- **index** : index.html 연결
- **postform** : postform.html 연결
- **editform** : editform.html 연결

### ProductController
- **getProductsController** : 모든 상품 목록 List index.html로 반환
- **getProductsListController** : 모든 상품 목록 List반환
- **getProductByIdController** : id에 해당하는 상품 하나를 반환
- **postProductController** : 상품 추가 후 index로 리다이렉트
- **deleteProductController** : 상품 삭제 후 index로 리다이렉트
- **updateProductController** : 상품 수정 후 index로 리다이렉트

### CatchError
- **isCorrectName** : 이름의 유효성을 검사한다
- **isContainsKakao** : 이름에 Kakao가 포함되 있는지 검사한다

### MemberDao
- **selectMember** : 회원 반환
- **insertMember** : 회원 추가
-
### MemberController
- **signIn** : 회윈가입
- **login** : 로그인

### WishlistDao
- **selectAllWishlist** : 해당 유저의 위시리스트 목록 추출
- **insertWishlist** : 위시리스트 추가
- **deleteWishlist** : 위시리스트 삭제

## WishlistContoroller
- **getWishlist** : 유저 id로 해당 유저의 위시리스트 반환
- **postWishlist** : 상품 id, 유저 id로 위시리스트 추가
- **deleteProductController** : 위시리스트 id로 해당 위시리스트 삭제

### 유효성
- 클라이언트가 어떤 부분이 왜 잘못되었는지 인지할 수 있도록 응답제공
- 상품 이름 : 공백포함 최대 15자
- 특수 문자 : ( ), [ ], +, -, &, /, _ 이외 사용불가
- "카카오"가 포함된 문구는 담당 MD와 협의한 경우에만 사용 가능