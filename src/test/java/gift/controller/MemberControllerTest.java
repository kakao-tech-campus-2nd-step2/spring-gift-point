//package gift.controller;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//import gift.classes.RequestState.RequestStateDTO;
//import gift.classes.RequestState.RequestStatus;
//import gift.classes.RequestState.SecureRequestStateDTO;
//import gift.domain.Role;
//import gift.dto.MemberDto;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@Transactional
//class MemberControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    @Transactional
//    void registerTest() {
//        var url = "http://localhost:" + port + "/api/member/register";
//        MemberDto memberDto = new MemberDto(null, "testemail", "password", Role.ADMIN);
//
//        HttpEntity<MemberDto> request = new HttpEntity<>(memberDto);
//
//        ResponseEntity<RequestStateDTO> response = restTemplate.exchange(url, HttpMethod.POST,
//            request, RequestStateDTO.class);
//
//        System.out.println("abec" + response.getBody().getStatus());
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals(RequestStatus.success, response.getBody().getStatus());
//    }
//
//    @Test
//    @Transactional
//    void loginTest() {
//        var registerUrl = "http://localhost:" + port + "/api/member/register";
//        MemberDto memberDto = new MemberDto(null, "testemail2", "password", Role.ADMIN);
//
//        HttpEntity<MemberDto> registerRequest = new HttpEntity<>(memberDto);
//        ResponseEntity<RequestStateDTO> registerResponse = restTemplate.exchange(registerUrl,
//            HttpMethod.POST, registerRequest, RequestStateDTO.class);
//
//        assertEquals(200, registerResponse.getStatusCodeValue());
//        assertNotNull(registerResponse.getBody());
//        assertEquals(RequestStatus.success, registerResponse.getBody().getStatus());
//
//        // 로그인 테스트
//        var loginUrl = "http://localhost:" + port + "/api/member/login";
//        HttpEntity<MemberDto> loginRequest = new HttpEntity<>(memberDto);
//
//        ResponseEntity<SecureRequestStateDTO> response = restTemplate.exchange(loginUrl,
//            HttpMethod.POST, loginRequest, SecureRequestStateDTO.class);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals(RequestStatus.success, response.getBody().getStatus());
//        assertNotNull(response.getBody().getSecure());
//
//    }
//}