# spring-gift-order

## 6주차 구현사항
- 5주차 피드백 반영
- 조원과 협의 후 통일한 api 엔드포인트 수정
- 조원과 협의 후 로직(ex.로그인) 수정
- api 명세서 최대한 상세하게 작성
- codepipeline을 활용해서 배포 자동화 -> codebuild 단계에서 트러블 해결중

## api 명세서
http://localhost:8080/swagger-ui.html

## 현재 코드 구조
```plaintext
└── src
    └── main
        ├── java
        │   └── gift
        │       ├── client
        │       │   └── KakaoClient.java
        │       ├── config
        │       │   ├── AppConfig.java
        │       │   ├── AppConfigProperties.java
        │       │   ├── InterceptorConfig.java
        │       │   └── SwaggerConfig.java
        │       ├── controller
        │       │   ├── AdminController.java
        │       │   ├── CategoryController.java
        │       │   ├── HomeController.java
        │       │   ├── KakaoController.java
        │       │   ├── MemberController.java
        │       │   ├── OrderController.java
        │       │   ├── ProductConroller.java
        │       │   └── WishController.java
        │       ├── dto
        │       │   ├── MemberDto.java
        │       │   ├── OptionDto.java
        │       │   ├── OrderRequest.java
        │       │   ├── OrderResponse.java
        │       │   ├── ProductDto.java
        │       │   ├── SendMessageRequest.java
        │       │   └── WishRequest.java
        │       ├── entity
        │       │   ├── Catogory.java
        │       │   ├── Member.java
        │       │   ├── Option.java
        │       │   ├── Order.java
        │       │   ├── Product.java
        │       │   └── Wish.java
        │       ├── exception
        │       │   ├── ApiRequestException.java
        │       │   ├── CategoryNotFoundException.java
        │       │   ├── GlobalExceptionHandler.java
        │       │   ├── InvalidProductNameException.java
        │       │   └── ProductNotFoundException.java
        │       ├── interceptor
        │       │   └── TokenInterceptor.java
        │       ├── repository
        │       │   ├── CategoryRepository.java
        │       │   ├── MemberRepository.java
        │       │   ├── OptionRepository.java
        │       │   ├── OrderRepository.java
        │       │   ├── ProductRepository.java
        │       │   └── WishRepository.java
        │       ├── service
        │       │   ├── CategoryService.java
        │       │   ├── KakaoAuthService.java
        │       │   ├── KakaoMessageService.java
        │       │   ├── KakaoMessageTemplate.java
        │       │   ├── MemberService.java
        │       │   ├── OrderService.java
        │       │   ├── ProductService.java
        │       │   ├── TokenService.java
        │       │   └── WishService.java   
        │       ├── value
        │       │   ├── AuthorizationHeader.java
        │       │   ├── OptionName.java
        │       │   ├── OptionQuantity.java
        │       │   └── ProductName.java 
        │       └── Application.java
        └── resources
            ├── data.sql
            ├── schema.sql
            └── templates
                ├── add.html
                ├── edit.html
                ├── home.html
                ├── list.html
                ├── login.html
                └── view.html             
└── src
    └── test
        └── java
            └── gift 
                ├── controller
                │    ├── AdminControllerTest.java
                │    ├── CategoryControllerTest.java
                │    ├── KakaoControllerTest.java
                │    └── MemberControllerTest.java
                ├── entity
                │    └── ProductTest.java
                ├── Repository
                │    ├── CategoryrepositoryTest.java
                │    ├── MemberRepositoryTest.java
                │    ├── OptionRepositoryTest.java
                │    ├── ProductReposiroryTest.java
                │    └── WishRepositoryTest.java
                └── service
                     ├── CategoryServiceTest.java
                     ├── MemberServiceTest.java
                     └── ProductSerivceTest.java