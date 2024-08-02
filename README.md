# spring-gift-point

## step0 구현 기능

- 5주차 코드 클론
- JWT토큰을 생성하기 위한 파라미터를 담은 DTO명 수정
- 카카오 토큰 만료 여부는 카카오로부터 조회시 검증한다. 
- 함수명은 소문자로 시작해야 함
- 주석 들여쓰기 수정
- HttpHeaders는 전역변수로 구현할 필요 없음

## step1 구현 기능

- 1명의 백엔드 코드를 기준으로 api명세서 통합
  - 기존 fe이 사용하던 명세서는 요청 request만 존재하고 response는 명시되어 있지 않음
  - 따라서 response의 형식은 자유임
  - 선정결과 본인(강원대_BE 김동혁)의 request, response의 형태를 따르게 됨
  - fe가 사용하던 명세와 be의 명세를 대조하는 엑셀 작성
    - 버전을 명시하기 위해 스프레드시트가 아닌 엑셀 사용
    - fe는 엑셀과 swagger를 참조해서(서버로 빌드해 놓음) 기능을 작성함
    - be는 엑셀과 swagger를 참조해서(서버로 빌드해 놓음) 입출력 명세를 통일함
    - 본인은 나머지 be의 의견 취합 및 반영, 엑셀 작성을 담당함
- 유저생성 url 변경
  - /api/user/register
  - 직관적 회원가입을 명시하기 위함
- 상품 주문시 상품id를 요구하지 않음
  - 상품 옵션 id로 충분히 검증 가능
- 상품 주문시 주문내역 저장
  - 주문 내역을 조회하기 위해서 변경함
  - 주문 내역 호출 로그를 조회하는 방식으로 주문내역을 내보일 예정이었지만
  - 명시적으로 서버에 저장하게 변경
- 상품 주문 리스트 조회
  - /api/product/order/{optionId}
  - 로그인한 유저의 상품 주문 내역을 반환한다.
  - 유저id, 상품옵션id, 재고를 반환한다.

## step2 구현 기능

- 지속적인 배포를 위한 배포 스크립트를 작성
  - github action을 적용해서 ci/cd를 구성했다.
  - 서버는 aws lightsail을 사용했다.
  - 서버os가 우분투 22.04를 적용했다.
  - 'build' 브렌치에 push가 되거나 pull이 되면 수행이된다.
  - github에서 제공하는 가상환경에서 진행된다.
    - 브렌치 체크아웃
    - jdk 설정
    - gradle로 bootJar를 생성
    - aws인증
    - scp로 .jar를 서버로 전당
    - (기존 프로젝트가 빌드되어 있다면)기존에 빌드된 .jar ps를 종료
    - 새로운 .jar 빌드
  - build 브렌치에서만 동작하게 설정하였다.
    - main이나 유저명으로 된 브렌치는 개발용으로 하고 빌드를 위한 브렌치를 따로 설정했다. 
- 기존에 변경된 url을 테스트 코드에 적용
  - 유저생성
  - 상품주문
  - 상품 주문 리스트 조회

## step3 구현 기능

- 포인트 관련 기능 구현
  - 유저
    - 포인트 충전(list)
    - 포인트 결제(list)
  - 상품 옵션
    - 해당 옵션 구매 내역(list)
  - 상품
    - 적용할 수 있는 할인 정책(list)
  - 포인트 충전
    - id
    - price
    - user(user entity)
    - transactionDate(create)
  - 포인트 결제
    - id
    - regularPrice(정가)
    - paymentAmount(결제액)
    - user(user entity)
    - discountPolicy(discountPolicy entity)
    - productOptionId(구매 대상)
    - transactionDate(create)
  - 할인 정책
    - id
    - target(product entity)(상품 단위로 할인 진행)
    - discountType(enum)(fix, percent)
    - discount(1~100, 금액)
      - discountType과 discount을 조합해서 사용
    - createDate(create)
    - endDate(기한)(기한이 없다면 9999년 12월 31일로 설정)
    - DiscountAmountLimit(할인 한도)
    - PointPayment(list)
    - remark
- api목록
  - "/api/point/charge"
    - (로그인한 유저)포인트 충전내역 리스트 조회
    - (로그인한 유저)포인트 충전내역 조회
    - (로그인한 유저)포인트 충전
    - 결제는 없다고 가정
    - 숫자만 넣으면 충전되게 구현
    - (로그인한 유저)포인트 충전 취소

  - "/api/point/payment"
    - (로그인한 유저)포인트 사용 내역 리스트 조회
    - (로그인한 유저)포인트 사용 내역 조회
    - 결제시 포인트 사용
      - 할인 적용
      - 결제내역 저장
    - 결제 취소
  - "/api/discountPolicy"
    - 할인정책 생성
    - 할인정책 리스트 조회
    - 할인정책 조회
    - 할인정책 삭제
  - 추후 유저별 권한이 생긴다면 할인 정책은 어드민만 가능함