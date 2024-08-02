# spring-gift-point

포인트
: 포인트는 주문금액에 보유 포인트 내에서 상품금액의 최대 10%까지 자동 차감된다.
포인트의 충전은 관리자 화면에서만 이루어진다(이벤트성).
URL           메서드  기능        설명
/admin/points GET    포인트 충전  관리자가 유저의 포인트를 충전해준다.
/api/points   GET    포인트 조회  유저 본인의 포인트 조회가 가능하다.


포인트 조회
request
http://{awsIp}:8080/api/points

Authorization: Bearer {token}

response

{
"point": 1000
}



