package gift.service;

import gift.dto.MemberDTO;
import gift.entity.MemberEntity;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    void saveMember_Success() {
        // Given
        MemberDTO memberDTO = new MemberDTO("admin@email.com", "password");

        // When / Then
        assertDoesNotThrow(() -> memberService.save(memberDTO));
        MemberEntity savedMember = memberRepository.findByEmail(memberDTO.getEmail());
        assertNotNull(savedMember, "저장된 회원은 null이 아니어야 합니다.");
        assertEquals(memberDTO.getEmail(), savedMember.getEmail(), "저장된 회원의 이메일이 일치해야 합니다.");
        assertEquals(memberDTO.getPassword(), savedMember.getPassword(), "저장된 회원의 비밀번호가 일치해야 합니다.");
    }

    @Test
    void saveMember_EmailAlreadyExists() {
        // Given
        MemberDTO memberDTO = new MemberDTO("admin@email.com", "password");
        memberRepository.save(new MemberEntity(memberDTO.getEmail(), memberDTO.getPassword()));

        // When / Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.save(memberDTO);
        });
        assertEquals("이미 가입된 이메일입니다.", exception.getMessage());
    }

    @Test
    void authenticateToken_Success() {
        // Given
        MemberDTO memberDTO = new MemberDTO("admin@email.com", "password");
        memberRepository.save(new MemberEntity(memberDTO.getEmail(), memberDTO.getPassword()));

        // When / Then
        assertDoesNotThrow(() -> memberService.authenticateToken(memberDTO));
    }

    @Test
    void authenticateToken_Failure() {
        // Given
        MemberDTO memberDTO = new MemberDTO("admin@email.com", "wrongpassword");
        memberRepository.save(new MemberEntity("admin@email.com", "password"));

        // When / Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            memberService.authenticateToken(memberDTO);
        });
        assertEquals("로그인 정보가 올바르지 않습니다.", exception.getMessage());
    }
}