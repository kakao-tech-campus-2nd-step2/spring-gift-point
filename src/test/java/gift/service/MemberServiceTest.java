package gift.service;

import gift.entity.Member;
import gift.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String encodeCredentials(String email, String password) {
        String credentials = email + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(credentials.getBytes());
    }

    @Test
    void testSignUpSuccess() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        String encodedCredentials = encodeCredentials(email, password);
        when(memberRepository.save(any(Member.class))).thenReturn(new Member(email, password, anyString()));

        // When
        String token = memberService.signUp(encodedCredentials);

        // Then
        assertNotNull(token);
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    void testSignUpFailure() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        String encodedCredentials = encodeCredentials(email, password);
        when(memberRepository.save(any(Member.class))).thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> memberService.signUp(encodedCredentials));
    }

    @Test
    void testLoginSuccess() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        String encodedCredentials = encodeCredentials(email, password);
        when(memberRepository.existsByToken(anyString())).thenReturn(true);

        // When
        String token = memberService.login(encodedCredentials);

        // Then
        assertNotNull(token);
        verify(memberRepository).existsByToken(anyString());
    }

    @Test
    void testLoginFailure() {
        // Given
        String email = "test@example.com";
        String password = "wrong_password";
        String encodedCredentials = encodeCredentials(email, password);
        when(memberRepository.existsByToken(anyString())).thenReturn(false);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> memberService.login(encodedCredentials));
    }

    @Test
    void testIsValidTokenTrue() {
        // Given
        String token = "valid_token";
        when(memberRepository.existsByToken(token)).thenReturn(true);

        // When
        boolean result = memberService.isValidToken(token);

        // Then
        assertTrue(result);
        verify(memberRepository).existsByToken(token);
    }

    @Test
    void testIsValidTokenFalse() {
        // Given
        String token = "invalid_token";
        when(memberRepository.existsByToken(token)).thenReturn(false);

        // When
        boolean result = memberService.isValidToken(token);

        // Then
        assertFalse(result);
        verify(memberRepository).existsByToken(token);
    }

    @Test
    void testIsValidTokenException() {
        // Given
        String token = "exception_token";
        when(memberRepository.existsByToken(token)).thenThrow(new RuntimeException("Database error"));

        // When
        boolean result = memberService.isValidToken(token);

        // Then
        assertFalse(result);
        verify(memberRepository).existsByToken(token);
    }
}