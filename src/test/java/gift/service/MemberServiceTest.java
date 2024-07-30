package gift.service;

import gift.dto.request.MemberRequest;
import gift.domain.Member;
import gift.exception.DuplicateMemberEmailException;
import gift.exception.InvalidCredentialsException;
import gift.exception.MemberNotFoundException;
import gift.repository.member.MemberSpringDataJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static gift.domain.LoginType.NORMAL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberSpringDataJpaRepository memberRepository;

    @Test
    public void testRegister() {
        MemberRequest memberRequest = new MemberRequest("test@example.com", "password");

        when(memberRepository.findByEmailAndLoginType("test@example.com", NORMAL)).thenReturn(Optional.empty());
        when(memberRepository.save(any(Member.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Member member = memberService.register(memberRequest, NORMAL);

        assertThat(member.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testDuplicateRegister() {
        MemberRequest memberRequest = new MemberRequest("test@example.com", "password");
        when(memberRepository.findByEmailAndLoginType("test@example.com", NORMAL)).thenReturn(Optional.of(new Member()));

        assertThrows(DuplicateMemberEmailException.class, () -> {
            memberService.register(memberRequest, NORMAL);
        });
    }

    @Test
    public void testAuthenticate() {
        MemberRequest memberRequest = new MemberRequest("test@example.com", "password");
        Member member = mock(Member.class);

        when(memberRepository.findByEmailAndLoginType("test@example.com", NORMAL)).thenReturn(Optional.of(member));
        when(member.getPassword()).thenReturn("password");

        Member authenticatedMember = memberService.authenticate(memberRequest, NORMAL);

        assertThat(authenticatedMember.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    public void testAuthenticateWithInvalidCredentials() {
        MemberRequest memberRequest = new MemberRequest("test@example.com", "wrongpassword");
        Member member = mock(Member.class);

        when(memberRepository.findByEmailAndLoginType("test@example.com", NORMAL)).thenReturn(Optional.of(member));
        when(member.getPassword()).thenReturn("password");

        assertThrows(InvalidCredentialsException.class, () -> {
            memberService.authenticate(memberRequest, NORMAL);
        });
    }

    @Test
    public void testAuthenticateWithNonExistentEmail() {
        MemberRequest memberRequest = new MemberRequest("nonexistent@example.com", "password");

        when(memberRepository.findByEmailAndLoginType("nonexistent@example.com", NORMAL)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> {
            memberService.authenticate(memberRequest, NORMAL);
        });
    }
}
