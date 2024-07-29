# spring-gift-point

# 주문 관리 API

## 기능 목록

1. 사용자 인증
    - 카카오 로그인
    - 카카오 로그인 토큰 처리


2. 카카오 유저 위시리스트 관리
    - 카카오 유저 위시리스트 조회
    - 카카오 유저 위시리스트에 원하는 위시리스트 추가
    - 카카오 유저 위시리스트에서 원하는 위시리스트 삭제


3. 주문 처리
    - 주문 생성 (옵션 수량 차감, 위시리스트에서 삭제, 메시지 전송)


## API 요청

| 메서드 | 요청 헤더                                      | 엔드포인트               | 설명                             | 요청 본문 예시                                                                                              |
|--------|------------------------------------------------|--------------------------|----------------------------------|-------------------------------------------------------------------------------------------------------|
| GET    | 없음                                           | /kakao/login             | 카카오 로그인 페이지로 리디렉션 | 없음                                                                                                    |
| GET    | 없음                                           | /kakao                   | 카카오 인가 코드 처리            | `code` (query parameter)                                                                              |
| GET    | `Authorization: Bearer {access_token}`         | /kakao/wish              | 위시리스트 조회                  | 없음                                                                                                    |
| POST   | `Authorization: Bearer {access_token}`         | /kakao/wish/addWish      | 위시리스트에 옵션 추가           | `{ "optionId": 1 }`                                                                                   |
| DELETE | `Authorization: Bearer {access_token}`         | /kakao/wish              | 위시리스트에서 옵션 삭제         | `{ "optionId": 1 }`                                                                                   |
| POST   | `Authorization: Bearer {access_token}`         | /kakao/wish/order        | 주문 생성                        | `{ "optionId": 1, "quantity": 2, "message": "Your order success, name: ${option.name} quantity: 10 }"` |


## API 응답

| 엔드포인트               | 응답 예시                                       |
|--------------------------|------------------------------------------------|
| /kakao/login             | 302 Found                                       |
| /kakao                   | 200 OK<br>kakaoLoginSuccess 뷰와 함께 accessToken 포함|
| /kakao/wish              | 200 OK<br>kakaoWishlist 뷰와 함께 wishOptions 포함 |
| /kakao/wish/order        | 200 OK                                          |
| /kakao/wish/addWish      | 200 OK<br>"OK" 문자열                           |
| /kakao/wish              | 200 OK<br>kakaoWishlist 뷰와 함께 wishOptions 포함 |

## 예외 상황

| 엔드포인트               | 예외 상황                     | 응답 코드 |
|--------------------------|-------------------------------|-----------|
| /kakao                   | 잘못된 인가 코드              | 400       | 
| /kakao                   | 인증 실패                     | 401       |
| /kakao                   | 서버 오류                     | 500       |
| /kakao/wish              | 인증 실패                     | 401       |
| /kakao/wish              | 서버 오류                     | 500       | 
| /kakao/wish/addWish      | 잘못된 요청                   | 400       | 
| /kakao/wish/addWish      | 인증 실패                     | 401       |
| /kakao/wish/addWish      | 서버 오류                     | 500       |
| /kakao/wish              | 잘못된 요청                   | 400       | 
| /kakao/wish              | 인증 실패                     | 401       |
| /kakao/wish              | 서버 오류                     | 500       |
| /kakao/wish/order        | 잘못된 요청                   | 400       | 
| /kakao/wish/order        | 인증 실패                     | 401       |
| /kakao/wish/order        | 서버 오류                     | 500       |