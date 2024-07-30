package gift.service;//package gift.service;
//
//import gift.dto.MemberDto;
//import gift.entity.Member;
//import gift.repository.MemberRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//public class MemberServiceTest {
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    @InjectMocks
//    private MemberService memberService;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testRegister() {
//        // Given
//        MemberDto memberDto = new MemberDto(null, "test@example.com", "password", null);
//        Member member = new Member("test@example.com", "password");
//        Member savedMember = new Member("test@example.com", "password");
//        ReflectionTestUtils.setField(savedMember, "id", 1L);
//
//        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
//        when(TokenService.generateToken(anyString(), anyString())).thenReturn("mockToken");
//
//        // When
//        MemberDto result = memberService.register(memberDto);
//
//        // Then
//        assertEquals(1L, result.getId());
//        assertEquals("test@example.com", result.getEmail());
//        assertEquals("password", result.getPassword());
//        assertEquals("mockToken", result.getToken());
//
//        verify(memberRepository, times(1)).save(any(Member.class));
//    }
//
//    @Test
//    public void testLoginSuccess() {
//        // Given
//        Member member = new Member("test@example.com", "password");
//        ReflectionTestUtils.setField(member, "id", 1L);
//
//        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
//        mockStatic(TokenService.class);
//        when(TokenService.generateToken(anyString(), anyString())).thenReturn("mockToken");
//
//        // When
//        MemberDto result = memberService.login("test@example.com", "password");
//
//        // Then
//        assertEquals(1L, result.getId());
//        assertEquals("test@example.com", result.getEmail());
//        assertEquals("password", result.getPassword());
//        assertEquals("mockToken", result.getToken());
//
//        verify(memberRepository, times(1)).findByEmail(anyString());
//    }
//
//    @Test
//    public void testLoginFailure() {
//        // Given
//        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThrows(RuntimeException.class, () -> {
//            memberService.login("test@example.com", "wrongPassword");
//        });
//
//        verify(memberRepository, times(1)).findByEmail(anyString());
//    }
//}