package gift.product.validation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.product.dto.MemberDTO;
import gift.product.exception.DuplicateException;
import gift.product.exception.LoginFailedException;
import gift.product.model.Member;
import gift.product.repository.MemberRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberValidationTest {
    @InjectMocks
    private MemberValidation memberValidation;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    private MemberDTO memberDTO;
    private Member member;

    @BeforeEach
    void setUp() {
        memberDTO = new MemberDTO("member@email.com", "1234");
        member = new Member("member@email.com", "encodedPassword");
    }

    @Test
    void testSignUp() {
        // given
        when(memberRepository.findByEmail(anyString()))
            .thenReturn(Optional.empty());

        // when
        memberValidation.signUpValidation(memberDTO.getEmail());

        // then
        verify(memberRepository).findByEmail(memberDTO.getEmail());
    }

    @Test
    void testSignUpDuplicateEmail() {
        // given
        when(memberRepository.findByEmail(anyString()))
            .thenReturn(Optional.of(member));

        // when & then
        Assertions.assertThrows(DuplicateException.class, () -> {
            memberValidation.signUpValidation(memberDTO.getEmail());
        });
    }

    @Test
    void testLogin() {
        // given
        when(memberRepository.findByEmail(memberDTO.getEmail()))
            .thenReturn(Optional.of(member));
        when(passwordEncoder.matches(anyString(), anyString()))
            .thenReturn(true);

        // when
        memberValidation.loginValidation(memberDTO);

        // then
        verify(memberRepository).findByEmail(memberDTO.getEmail());
        verify(passwordEncoder).matches(memberDTO.getPassword(), member.getPassword());
    }

    @Test
    void testLoginWrongPassword() {
        // given
        when(memberRepository.findByEmail(memberDTO.getEmail()))
            .thenReturn(Optional.of(member));
        when(passwordEncoder.matches(anyString(), anyString()))
            .thenReturn(false);

        // when & then
        Assertions.assertThrows(LoginFailedException.class, () -> {
            memberValidation.loginValidation(memberDTO);
        });
        verify(memberRepository).findByEmail(memberDTO.getEmail());
        verify(passwordEncoder).matches(memberDTO.getPassword(), member.getPassword());
    }

    @Test
    void testLoginWrongEmail() {
        // given
        when(memberRepository.findByEmail(memberDTO.getEmail()))
            .thenReturn(Optional.empty());

        // when & then
        Assertions.assertThrows(LoginFailedException.class, () -> {
            memberValidation.loginValidation(memberDTO);
        });
        verify(memberRepository).findByEmail(memberDTO.getEmail());
    }
}