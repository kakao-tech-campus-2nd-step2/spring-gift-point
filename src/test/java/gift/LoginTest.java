package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.Model.KakaoProperties;
import gift.Model.Member;
import gift.Service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

@SpringBootTest
public class LoginTest {

    @Autowired
    private KakaoProperties kakaoProperties;

    @Autowired
    private LoginService loginService;

    private final RestClient client = RestClient.builder().build();

    @Test
    void abstractToken(){
        var response = loginService.makeResponse("인가코드");
        String accessToken = loginService.abstractToken(response);
        assertThat(accessToken).isNotNull(); // null이 아니면 추출 성공
    }

    @Test
    void getId(){
        var response = loginService.makeResponse("인가코드");
        String accessToken = loginService.abstractToken(response);
        String id = loginService.getId(accessToken);
        assertThat(id).isNotNull(); // null이 아니면 가져오기 성공
    }

    @Test
    void signupMember(){
        var response = loginService.makeResponse("인가코드");
        String accessToken = loginService.abstractToken(response);
        String id = loginService.getId(accessToken);
        Member actual = loginService.signupMember(id,accessToken);

        assertThat(actual.getEmail()).isEqualTo(id+"@kakao.com");
        assertThat(actual.getPassword()).isEqualTo(id);
    }

    @Test
    void checkMember_caseNotMember(){
        var response = loginService.makeResponse("VPw0Io5LfRC3VJR5mJaRG3RB_Mke-wNF3ZYunZPNwVW5B-jyrdKfAwAAAAQKKiVQAAABkONPeD2t1856Xp2T3g");
        String accessToken = loginService.abstractToken(response);
        String id = loginService.getId(accessToken);
        Member actual = loginService.getMemberOrSignup(id,accessToken);

        assertThat(actual.getEmail()).isEqualTo(id+"@kakao.com");
        assertThat(actual.getPassword()).isEqualTo(id);
    }

    @Test
    void checkMember_caseMember(){
        var response = loginService.makeResponse("dLtWwhkW-uhH_FUhXRDR6C8NxE3CqaIiz_3Cg2OuinBvozE0RMloVQAAAAQKKiVRAAABkONT5r37Ewsnpgvovw");
        String accessToken = loginService.abstractToken(response);
        String id = loginService.getId(accessToken);
        Member expect = loginService.signupMember(id,accessToken);
        Member actual = loginService.getMemberOrSignup(id,accessToken);

        assertThat(actual.getEmail()).isEqualTo(expect.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expect.getPassword());
    }
}
