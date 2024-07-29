package gift.service;

import static org.assertj.core.api.Assertions.assertThat;

import gift.common.util.JwtUtil;
import gift.dto.MemberRequest;
import gift.entity.Role;
import gift.repository.MemberRepository;
import gift.repository.MemberRoleRepository;
import gift.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MemberRoleRepository memberRoleRepository;

    private MemberService memberService;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        memberService = new MemberService(memberRepository, jwtUtil, roleRepository,
            memberRoleRepository);

        Role userRole = new Role("USER");
        roleRepository.save(userRole);
    }

    @Test
    @DisplayName("회원 가입")
    void testRegisterMember() {
        // given
        MemberRequest memberRequest = new MemberRequest("user@example.com", "password");

        // when
        String token = memberService.register(memberRequest);

        // then
        assertThat(token).isNotNull();
        assertThat(memberRepository.findByEmail("user@example.com").get().getEmail()).isEqualTo(
            memberRequest.getEmail());

    }

    @Test
    @DisplayName("회원 로그인")
    void testLoginMember() {
        // given
        MemberRequest memberRequest = new MemberRequest("user@example.com", "password");
        memberService.register(memberRequest);

        // when
        String token = memberService.login(memberRequest.getEmail(), memberRequest.getPassword());

        // then
        assertThat(token).isNotNull();
    }

}