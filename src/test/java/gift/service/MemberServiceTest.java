package gift.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.dto.memberDTO.LoginRequestDTO;
import gift.dto.memberDTO.MemberDTO;
import gift.dto.memberDTO.RegisterRequestDTO;
import gift.model.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional(readOnly = true)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    private RegisterRequestDTO registerRequestDTO;
    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setUp() {
        registerRequestDTO = new RegisterRequestDTO("test@email.com", "password");
        loginRequestDTO = new LoginRequestDTO("test@email.com", "password");
    }

    @Test
    void testRegisterMember() {
        String token = memberService.registerMember(registerRequestDTO);
        Member registeredMember = memberRepository.findByEmail("test@email.com");
        assertAll(
            () -> assertNotNull(token),
            () -> assertNotNull(registeredMember),
            () -> assertEquals("test@email.com", registeredMember.getEmail()),
            () -> assertEquals("password", registeredMember.getPassword()),
            () -> assertEquals("user", registeredMember.getRole())
        );
    }

    @Test
    void testLoginMember() {
        Member member = new Member(null, "test@email.com", "password", "user");
        Member savedMember = memberRepository.save(member);
        String token = memberService.loginMember(loginRequestDTO);
        assertNotNull(token);
    }

    @Test
    void testFindMemberByEmail() {
        Member member = new Member(null, "kbm@kbm.com", "kbm", "user");
        memberRepository.save(member);

        Member foundMember = memberService.findMemberByEmail("kbm@kbm.com");

        assertAll(
            () -> assertNotNull(foundMember),
            () -> assertEquals("kbm@kbm.com", foundMember.getEmail()),
            () -> assertEquals("kbm", foundMember.getPassword()),
            () -> assertEquals("user", foundMember.getRole())
        );

    }
}