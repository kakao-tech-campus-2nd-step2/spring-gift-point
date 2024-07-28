# spring-gift-enhancement

---

## Step1

### 요구 사항
카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

카카오계정 로그인을 통해 인증 코드를 받는다.  
토큰 받기를 읽고 액세스 토큰을 추출한다.  
앱 키, 인가 코드가 절대 유출되지 않도록 한다.  
특히 시크릿 키는 GitHub나 클라이언트 코드 등 외부에서 볼 수 있는 곳에 추가하지 않는다.  
(선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.  


하지만 지금과 같이 클라이언트가 없는 상황에서는 아래와 같은 방법으로 인가 코드를 획득한다.  

내 애플리케이션 > 앱 설정 > 앱 키로 이동하여 REST API 키를 복사한다.  
https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&redirect_uri=http://localhost:8080&client_id={REST_API_KEY}에 접속하여 카카오톡 메시지 전송에 동의한다.  
http://localhost:8080/?code={AUTHORIZATION_CODE}에서 인가 코드를 추출한다.  


### 리뷰 내용


### 구현 기능
이전 과정에서 수정할 것들
- [ ] 카테고리 테스트 작성
- [ ] 옵션 테스트 작성
- [ ] 옵션 UI 구현
- [ ] 불필요한 ResultResponseDto 제거
- [ ] Stock 을 별도 테이블로 분리하기

step1 에서 구현할 것들
- [ ] 카카오 로그인 구현
- [ ] 카카오 로그인 테스트






