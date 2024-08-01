1. ### 회원 API
   1. 회원가입
      1. url : /api/members/register
      2. Require data : {
         “email” : “”,
         “pasword” : “”
         }
      3. Response data -> 
         {
         “token” : “”
         }
      4. HttpStatus : Created 201
   2. 로그인
      1. url : /api/members/login
      2. Require data : {
         “email” : “”,
         “pasword” : “”
         }
      3. Response data : {
         “token” : “”
         }
      4. HttpStatus : OK 200




2. ### 카테고리 API
   1. 카테고리 전체 조회
      1.  url : /api/categories
      2. Require data :
      3. Response data :[
         {
         "id": 0,
         "name": "string",
         "color": "string",
         "imageUrl": "string",
         "description": "string"
         }
         ]
      4. HttpStatus : OK 200



3. ### 상품 API
   1. 상품 조회 개별
      2. url : /api/products/{productId}
      3. Require data : PathVariable : {productId}
      4. Response data : HttpStatus : OK 200
         {
         “id” ; 1,
         “name” : “상품 이름”,
         “price” : 1000,
         “imageUrl” : “https://asd.com”,
         “category” : {
         “id” : 1,
         “name” : “카테고리 이름”,
         "color": "#6c95d1",
         “imageUrl” : “https://asdf”,
         “description” : “설명”
         }
         }
   2. 상품 목록 조회 (페이지네이션 조회)
      1. url : /api/products?page=0&size=10&sort=name,asc&categoryId=1
      2. Require data :
      3. Response data : HttpStatus : OK 200
         {
         “totalPages”: 0,
         “totalElements”: 0,
         “size”: 0,
         “content” :[
         {
         “id” ; 1,
         “name” : “상품 이름”,
         “price” : 1000,
         “imageUrl” : “https://asd.com”,
         “category” : {
         “id” : 1,
         “name” : “카테고리 이름”,
         "color": "#6c95d1",
         “imageUrl” : “https://asdf”,
         “description” : “설명”
         },

      {
      “id” ; 2,
      “name” : “상품 이름”,
      “price” : 2000,
      “imageUrl” : “https://asd.com”,
      “category” : {
      “id” : 1,
      “name” : “카테고리 이름”,
      "color": "#6c95d1",
      “imageUrl” : “https://asdf”,
      “description” : “설명”
      }
      ],
      "number": 0,
      "sort": {
      "empty": true,
      "sorted": true,
      "unsorted": true
      },
      "first": true,
      "last": true,
      "numberOfElements": 0,
      "pageable": {
      "offset": 0,
      "sort": {
      "empty": true,
      "sorted": true,
      "unsorted": true
      },
      "paged": true,
      "pageSize": 0,
      "pageNumber": 0,
      "unpaged": true
      },
      "empty": true
      } 



4. ### 상품 옵션 API
   1. 상품 옵션 목록 조회
      1. url : /api/products/{productId}/options
      2. Require data : pathVariable : {productId}
      3. Response data : HttpStatus : ok 200
         [
         {
         “id ” : 1,
         “name” : “옵션 이름”,
         “quantity” : 100
         },
         {
         “id ” : 2,
         “name” : “옵션 이름”,
         “quantity” : 100
         }
         ]

      

5. ### 위시리스트 API
    1. 위시 리스트 상품 추가
       1. url : /api/wishes
       2. Require data : Authorization : token /
          {
          “productId” : 1
          }
       3. HttpStatus : created 201
    2. 위시 리스트 상품 삭제
       1. url : /api/wishes/{wishId}
       2. Authorization : token
       3. HttpStatus : OK 200
    3. 위시 리스트 상품 조회(페이지 네이션 적용)
       1.  url : /api/wishes?page=0&size=10&sort=createdDate,desc
       2. Authorization : token
       3. HttpStatus : OK 200
       4. {
          “totalPages”: 0,
          “totalElements”: 0,
          “size”: 0,
          “content” :[
          {
          “wishId”:1,
          “productId” ; 1,
          “productName,” : “상품 이름”,
          “productPrice” : 1000,
          “productImageUrl” : “https://asd.com”,
          “category” : {
          “id” : 1,
          “name” : “카테고리 이름”,
          “color” : “#23423”,
          “imageUrl” : “https://asdf”,
          “description” : “설명”
          },
       {
       “wishId”:2,
       “productId” ; 2,
       “productName” : “상품 이름”,
       “productPrice” : 2000,
       “productImageUrl” : “https://asd.com”,
       “category” : {
       “id” : 1,
       “name” : “카테고리 이름”,
       “color” : “#23423”,
       “imageUrl” : “https://asdf”,
       “description” : “설명”
       }
       ],
       "number": 0,
       "sort": {
       "empty": true,
       "sorted": true,
       "unsorted": true
       },
       "first": true,
       "last": true,
       "numberOfElements": 0,
       "pageable": {
       "offset": 0,
       "sort": {
       "empty": true,
       "sorted": true,
       "unsorted": true
       },
       "paged": true,
       "pageSize": 0,
       "pageNumber": 0,
       "unpaged": true
       },
       "empty": true
       } 

6. ### 주문 API
   1. 주문하기
      1. url : /api/orders
      2. Authorization : token
      3. Require data : {
         "optionId": 0,
         "quantitƒy": 0,
         "message": "string"
         }
      4. Response data : {
         "id": 1,
         "optionId": 1,
         "quantity": 2,
         "orderDateTime": "2024-07-30T06:43:26.463Z",
         "message": "Please handle this order this care."
         }
      5. HttpStatus : CREATED 201