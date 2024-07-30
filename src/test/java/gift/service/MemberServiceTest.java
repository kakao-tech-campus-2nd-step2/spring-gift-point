package gift.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.member.MemberRepository;
import gift.member.MemberService;
import gift.member.model.Member;
import gift.member.model.MemberRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
    }

    @Test
    void selectLoginMemberTest() {
        given(memberRepository.findById(any())).willReturn(
            Optional.of(new Member("test", "test", "test", "test")));

        memberService.selectLoginMemberById(1L);

        then(memberRepository).should().findById(any());
    }

    @Test
    void insertMemberTest() {
        given(memberRepository.save(any())).willReturn(new Member("test", "test", "test", "test"));

        memberService.insertMember(new MemberRequest("test", "test", "test", "test"));

        then(memberRepository).should().save(any());
    }

}
