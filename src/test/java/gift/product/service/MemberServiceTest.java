package gift.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.product.dto.MemberDTO;
import gift.product.dto.TokenDTO;
import gift.product.model.Member;
import gift.product.repository.MemberRepository;
import gift.product.util.JwtUtil;
import gift.product.validation.MemberValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @InjectMocks
    private MemberService memberService;
    @Mock
    private MemberValidation memberValidation;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private PasswordEncoder passwordEncoder;

    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        memberDTO = new MemberDTO("member@email.com", "1234");
    }

    @Test
    void testSignUp() {
        //given
        doNothing().when(memberValidation).signUpValidation(anyString());
        when(memberRepository.save(any(Member.class)))
            .thenReturn(new Member());
        when(passwordEncoder.encode(any(CharSequence.class)))
            .thenReturn("encodedPassword");
        String expectedToken = "ExpectedToken";
        when(jwtUtil.generateToken(anyString()))
            .thenReturn(expectedToken);

        //when
        TokenDTO tokenDTO = memberService.signUp(memberDTO);

        //then
        assertEquals(expectedToken, tokenDTO.getToken());

        verify(passwordEncoder).encode(eq(memberDTO.getPassword()));
        verify(memberRepository).save(any(Member.class));
        verify(jwtUtil).generateToken(eq(memberDTO.getEmail()));
    }

    @Test
    void testLogin() {
        //given
        doNothing().when(memberValidation).loginValidation(memberDTO);
        String expectedToken = "ExpectedToken";
        when(jwtUtil.generateToken(anyString()))
            .thenReturn(expectedToken);

        //when
        TokenDTO tokenDTO = memberService.login(memberDTO);

        //then
        assertEquals(expectedToken, tokenDTO.getToken());
        verify(memberValidation).loginValidation(any(MemberDTO.class));
        verify(jwtUtil).generateToken(eq(memberDTO.getEmail()));
    }
}
