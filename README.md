# spring-gift-point

작성한 API 문서를 기반으로 팀 내에서 지금까지 만든 API를 검토하고 통일하여 변경 사항을 반영한다.

팀 내에서 일관된 기준을 정하여 API 명세를 결정한다.
때로는 클라이언트의 편의를 위해 API 명세를 결정하는 것이 좋다.

###3단계 요구사항
Point 구현

우리 조의 포인트
주문 시 마다 사용할 포인트를 넘겨 받는다.

포인트 할인 후 금액의 10%만큼 포인트를 획득한다.

Request
{
    "optionId": 1,
    "quantity": 2,
    "message": "Please handle this order with care."
    "usePoint": true,
    "point": 1000
}

Response
성공 시, 200 OK
{
    "orderId": 1,
    "totalPrice": 50000, // 총 금액
    "discountedPrice": 1000, //할인될 금액
    "accumulatedPoint": 4900 // (총금액 - 할인될 금액) / 10
}
