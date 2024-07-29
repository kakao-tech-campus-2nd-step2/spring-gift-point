package gift.ControllerTest;

import gift.controller.MemberController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class MemberControllerTest {
    @Autowired
    MemberController memberController;

}
