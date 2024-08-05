## 포인트 전략

### 정책
1. 사용자별로 포인트 적립해주기!

2. 구매시 0.5% 적립

## API 문서
1. 적립 포인트 조회
```
GET http://localhost:8080/api/points
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json
```

응답
```
{
  "point": 1200 
}
```


2. 관리자 화면에서만 포인트 추가 될 수 있도록 :
3. 구매시 자동 적립

(사용자가 포인트 사용하는 부분 구현할건지 말건지!?)
4. 구매시 포인트 사용 어떻게 할건지?
   -> 구매 요청 객체?

```
POST http://localhost:8080/api/orders
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

body 
{
  "quantity": 12,         
  "message": "message",
  "option_id": 1
  "using_point" : 300
}
```



