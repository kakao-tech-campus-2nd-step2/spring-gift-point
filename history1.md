# step1 기록

## 0단계 : 리뷰 반영 + 리펙토링

1. page parameter로 받도록 변경
2. 관리자 페이지 다시 디자인 + 관리자페이지 전용 DTO 생성, 기존 PRODUCT setter 제거
3. ~~예외 클래스 리펙토링 - HTTPSTATUS 기반으로~~
4. set과 같은 모호한 이름의 메서드 전체 수정
5. ~~product service에 대해 구조변경 - 객체지향적으로?~~
6. fetchType 에 대해
7. ~~단위테스트 코드 추가~~
8. 카카오 문구에 대해서 따로 처리로직 필요


## 0단계 기록

productservice에서 여러 repository를 받아서 처리를 해야한다는 점이 마음에 들지 않는다.
테스트하기 좋은 코드

https://kchung1995.github.io/posts/%ED%95%98%EB%82%98%EC%9D%98-Service%EA%B0%80-%EC%97%AC%EB%9F%AC-Repository%EC%97%90-%EC%9D%98%EC%A1%B4%ED%95%A0-%EB%95%8C/

repository를 묶어서 처리하는 하나의 클래스를 통해 처리하는?

책임과 역할에 대해서 생각을 해보자.

ProductService 는 product에 대한 비즈니스 로직? 을 동작. 그렇다면, 옵션이 있는지, 카테고리가 어떻게 되어있는지 등등은
해당 클래스가 검증을 하고, repository를 통해 저장하는.

### product service 변경시켜주기

1. service interface 전부 제거시켜주고 바로 구현체를 사용하도록 변경 : 필요성이 없으므로
2. 퍼사드 패턴으로 repository 묶어주기

product service : 상품 추가를 한다면, 상품 추가시 필요한 조건을 만족하는지, 비즈니스 로직에 위반되는 (카카오 문구 등) 것들이
없는 지 확인하는 책임

product facade repository : jpa 사용에서 fk에 맞는 순서를 지키기, 없는 엔티티에 대해서 등등 DB관련 책임

이런 방식으로 리펙토링을 진행해보겠다.

통합 대상은 productservice 에서 카테고리와 기프트 옵션을 체크할 수 있도록

product <-> category : N : 1 , fk의 주인은 product, 양방향
product <-> giftoption : 1 : N , fk의 주인은 giftoption

뭔가 아직 애매...

product 내에 repo에 들어있지 않은 category와 giftoption이 있으면 
그냥 product만 save 하면 내부 객체들도 자동으로 save 되는가?

-> cascade 속성에 따라서 다르다.
CascadeType.persist : 연관된 엔티티도 저장

CascadeType.PERSIST: 엔티티를 저장할 때 연관된 엔티티도 저장합니다.
CascadeType.MERGE: 엔티티를 병합할 때 연관된 엔티티도 병합합니다.
CascadeType.REMOVE: 엔티티를 삭제할 때 연관된 엔티티도 삭제합니다.
CascadeType.REFRESH: 엔티티를 새로 고침할 때 연관된 엔티티도 새로 고침합니다.
CascadeType.DETACH: 엔티티를 분리할 때 연관된 엔티티도 분리합니다.
CascadeType.ALL: 모든 CascadeType 옵션을 포함합니다.

그럼 굳이 Facade 하지 않고, repository로만 하면 될 것 같다는 생각이 든다.

근데 아직 이쪽 부분에 대해서는 잘 모른다.

그냥 만들자.


---

GiftOption은 항상 필수적으로 Product에 속해있고, 해당 Product가 제거되면 같이 사라져야한다.

product service 새로운 request와 response에 맞춰서 변경해주기

테스트 만들기...

---

## test에 대해서

Service는 비즈니스 로직에 대한 책임을 진다.
Facade는 DB에 대한 책임을 진다.

즉, 카카오라는 이름이 들어간다면, Service에서 이를 처리해주고
Product에 카테고리가 없다던지, 기프트 옵션이 없다던지, 이런 것들은 Facade에서 처리해주는 것이 맞다.

-> 생각해냄.

ProductRequest에서는 카테고리 id만을 넘겨주고, 서비스에서는 이것을 기반으로 id만 있는 카테고리 객체를 만들어서
퍼사드레포지토리로 넘겨주면 실제 id가 존재하는 지 확인은 퍼사드에서 진행.
없으면 Throw

## product에 대해

생성과 수정에 DTO를 다르게 해야하지 않을까?
생성시에는 반드시 옵션이 필요하지만, 업데이트는 필요가 없기 떄문이다.

---

# 관리자 페이지 수정하기

관리자페이지,paging을 구현하고, 그 다음 step1을 진행한다음, 계속 리펙토링해나가겠다.

---

# step1

기능 요구사항

카카오 계정 로그인 통해 인증 코드 받기

액세스 토큰 추출
secret key 잘 관리하기


