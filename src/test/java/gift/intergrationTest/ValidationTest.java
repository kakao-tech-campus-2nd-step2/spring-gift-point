package gift.intergrationTest;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.user.UserForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ValidationTest {
    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate restTemplate;
    private final String testEmail = "test@test!";
    private final String wrongPw = "";

    @Test
    void testInvalidPassword() {
        var url = "http://localhost:" + port + "/register";
        UserForm userForm = new UserForm(testEmail, wrongPw);

        ResponseEntity<String> result1 = restTemplate.postForEntity(url, userForm, String.class);
        ResponseEntity<String> result2 = restTemplate.postForEntity(url, userForm, String.class);

        assertThat(result1.getStatusCode()).as("첫번째 요청 시도").isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(result2.getStatusCode()).as("두번째 요청 시도").isEqualTo(HttpStatus.BAD_REQUEST);
        System.out.println(result1.getBody());
    }
}