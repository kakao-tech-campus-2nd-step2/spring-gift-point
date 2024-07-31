# spring-gift-product

## 과제 진행 요구 사항

지금까지 만든 선물하기 서비스를 배포하고 클라이언트와 연동할 수 있어야 한다.

지속적인 배포를 위한 배포 스크립트를 작성한다.
클라이언트와 API 연동 시 발생하는 보안 문제에 대응한다.
서버와 클라이언트의 Origin이 달라 요청을 처리할 수 없는 경우를 해결한다.
HTTPS는 필수는 아니지만 팀 내에서 논의하고 필요한 경우 적용한다.

## API 명세서
https://www.notion.so/api-7ec4ba5de8de4beebf34b47b7cfb0c47?p=c1f834ae81e74e2fbda76b138cdef1bf&pm=s

## 프로그래밍 요구 사항

- 자바 코드 컨벤션을 지키면서 프로그래밍한다.
  - 기본적으로 [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)를 원칙으로 한다.
  - 단, 들여쓰기는 '2 spaces'가 아닌 '4 spaces'로 한다.
- indent(인덴트, 들여쓰기) depth를 3이 넘지 않도록 구현한다. 2까지만 허용한다.
  - 예를 들어 while문 안에 if문이 있으면 들여쓰기는 2이다.
  - 힌트: indent(인덴트, 들여쓰기) depth를 줄이는 좋은 방법은 함수(또는 메서드)를 분리하면 된다.
- 3항 연산자를 쓰지 않는다.
- 함수(또는 메서드)의 길이가 15라인을 넘어가지 않도록 구현한다.
  - 함수(또는 메서드)가 한 가지 일만 잘 하도록 구현한다.
- else 예약어를 쓰지 않는다.
  - else를 쓰지 말라고 하니 switch/case로 구현하는 경우가 있는데 switch/case도 허용하지 않는다.
  - 힌트: if 조건절에서 값을 return하는 방식으로 구현하면 else를 사용하지 않아도 된다.
