package gift.ServiceTest;

import gift.domain.Auth.LoginRequest;
import gift.domain.Member.Member;
import gift.domain.Member.MemberRequest;
import gift.domain.WishList.WishList;
import gift.repository.MemberRepository;
import gift.service.JwtService;
import gift.service.MemberService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtService jwtService;

    private MemberService memberService;

    @BeforeEach
    public void setUp() {
        memberService = new MemberService(memberRepository,jwtService);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void joinTest() throws BadRequestException {
        MemberRequest memberRequest = new MemberRequest("testId", "testPassword");
        Member member = new Member("testId","testPassword","김민지",new LinkedList<WishList>());

        Mockito.when(memberRepository.save(any(Member.class))).thenReturn(member);
        Mockito.when(memberRepository.existsById("testId")).thenReturn(false);

        memberService.join(memberRequest);
    }

    @Test
    @DisplayName("이미 존재하는 회원 추가 테스트")
    public void testJoin_ThrowsException() {
        MemberRequest memberRequest = new MemberRequest("testId", "testPassword");
        Mockito.when(memberRepository.existsById("testId")).thenReturn(true);

        assertThatThrownBy(() -> memberService.join(memberRequest))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void testLogin_Success() {
        MemberRequest memberRequest = new MemberRequest("testId", "testPassword");
        Member member = new Member("testId", "testPassword", new LinkedList<WishList>());

        Mockito.when(memberRepository.findById(memberRequest.email())).thenReturn(Optional.of(member));
        Mockito.when(jwtService.createJWT(memberRequest.email())).thenReturn("token");

        String jwt = memberService.login(new LoginRequest(memberRequest.email(),memberRequest.password()));

        assertThat(jwt).isEqualTo("token");
    }

    @Test
    @DisplayName("잘못된 비빌번호 테스트")
    public void testLogin_wrongPassword() {
        MemberRequest memberRequest = new MemberRequest("testId", "wrongPassword");
        Member member = new Member("testId", "testPassword", "김민지",new LinkedList<WishList>());

        Mockito.when(memberRepository.findById(memberRequest.email())).thenReturn(Optional.of(member));

        assertThatThrownBy(() -> memberService.login(new LoginRequest(memberRequest.email(),memberRequest.password())))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("로그인에 실패하였습니다. 다시 시도해주세요");
    }

    @Test
    @DisplayName("존재하지 않는 멤버 테스트")
    public void testLogin_NonExistingMember() {
        MemberRequest memberRequest = new MemberRequest("TestId", "testPassword");

        Mockito.when(memberRepository.findById(memberRequest.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.login(new LoginRequest(memberRequest.email(),memberRequest.password())))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("로그인에 실패했습니다 다시 시도해주세요");
    }

    @Test
    @DisplayName("id로 멤버 찾기 테스트")
    public void testFindById_Success() {
        Member member = new Member("testId", "testPassword", "김민지", new LinkedList<WishList>());

        Mockito.when(memberRepository.findById("testId")).thenReturn(Optional.of(member));

        Member foundMember = memberService.findById("testId");

        assertThat(foundMember).isEqualTo(member);
    }

    @Test
    @DisplayName("존재하지 않는 Id로 멤버 찾기 테스트")
    public void testFindById_NotExist() {
        Mockito.when(memberRepository.findById("nonExistingId")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberService.findById("nonExistingId"))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("해당하는 회원 정보가 없습니다.");
    }
}
