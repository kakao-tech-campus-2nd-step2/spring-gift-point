package gift.controller;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import gift.dto.MemberDto;
import gift.dto.request.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    void registerAndLoginTest(){

        String registerUrl = "http://localhost:" + port + "/members/register";
        MemberDto memberDto = new MemberDto(1L, "testPassword", "testEmail", "testRole");
        HttpEntity<MemberDto> registerRequestEntity =new HttpEntity<>(memberDto);

        ResponseEntity<String> registerResponse = restTemplate.postForEntity(registerUrl, registerRequestEntity, String.class);
        assertEquals(HttpStatus.CREATED, registerResponse.getStatusCode());
        assertNotNull(registerResponse.getBody());

        String loginUrl = "http://localhost:" + port + "/members/login";
        LoginRequest loginRequest = new LoginRequest("testPassword", "testEmail");
        HttpEntity<LoginRequest> loginRequestEntity = new HttpEntity<>(loginRequest);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(loginUrl, loginRequestEntity, String.class);
        assertEquals(HttpStatus.ACCEPTED, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());

        LoginRequest failRequest = new LoginRequest("wrongPassword", "wrongPassword");
        HttpEntity<LoginRequest> failRequestEntity = new HttpEntity<>(failRequest);

        ResponseEntity<String> failResponse =restTemplate.postForEntity(loginUrl, failRequestEntity, String.class);
        assertThat(failResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

    }

}
