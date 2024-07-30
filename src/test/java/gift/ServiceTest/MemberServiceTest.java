package gift.ServiceTest;

import gift.Entity.Member;
import gift.Mapper.Mapper;
import gift.Model.MemberDto;
import gift.Repository.MemberJpaRepository;
import gift.Service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class MemberServiceTest {

    private MemberService memberService;
    private final Mapper mapper = mock(Mapper.class);
    private final MemberJpaRepository memberJpaRepository = mock(MemberJpaRepository.class);

    @BeforeEach
    public void setUp() {
        memberService = new MemberService(memberJpaRepository, mapper);
    }

    @Test
    public void testRegister() {
        // given
        MemberDto memberDto = new MemberDto(1, "1234@naver.com", "1234", "1234", false);
        Member member = new Member(1, "1234@naver.com", "1234", "1234", false);
        given(mapper.memberDtoToEntity(memberDto)).willReturn(member);

        // when
        memberService.register(memberDto);

        // then
        verify(mapper, times(1)).memberDtoToEntity(memberDto);
        verify(memberJpaRepository, times(1)).save(member);
    }

    @Test
    public void testFindByUserId() {
        // given
        MemberDto memberDto = new MemberDto(1, "1234@naver.com", "1234", "1234", false);
        Member member = new Member(1, "1234@naver.com", "1234", "1234", false);
        given(mapper.memberDtoToEntity(memberDto)).willReturn(member);

        //when
        memberService.findByUserId(memberDto.getId());

        //then
        verify(memberJpaRepository, times(1)).findById(memberDto.getId());
    }

    @Test
    public void testFindByEmail() {
        MemberDto memberDto = new MemberDto(1, "1234@naver.com", "1234", "1234", false);
        Member member = new Member(1, "1234@naver.com", "1234", "1234", false);
        given(mapper.memberToDto(member)).willReturn(memberDto);
        given(memberJpaRepository.findByEmail(memberDto.getEmail())).willReturn(Optional.of(member));

        memberService.findByEmail(memberDto.getEmail());

        verify(mapper, times(1)).memberToDto(member);
        verify(memberJpaRepository, times(1)).findByEmail(member.getEmail());
    }

    @Test
    public void testIsAdmin() {
        MemberDto memberDto = new MemberDto(1, "1234@naver.com", "1234", "1234", true);
        Member member = new Member(1, "1234@naver.com", "1234", "1234", true);
        given(mapper.memberDtoToEntity(memberDto)).willReturn(member);
        given(memberJpaRepository.findByEmail(memberDto.getEmail())).willReturn(Optional.of(member));

        memberService.isAdmin(memberDto.getEmail());

        verify(memberJpaRepository, times(1)).findByEmail(memberDto.getEmail());
    }

    @Test
    public void testAuthenticate() {
        MemberDto memberDto = new MemberDto(1, "1234@naver.com", "1234", "1234", true);
        Member member = new Member(1, "1234@naver.com", "1234", "1234", true);
        given(mapper.memberDtoToEntity(memberDto)).willReturn(member);
        given(memberJpaRepository.findByEmailAndPassword(member.getEmail(), member.getPassword())).willReturn(Optional.of(member));

        memberService.authenticate(memberDto.getEmail(), memberDto.getPassword());

        verify(memberJpaRepository, times(1)).findByEmailAndPassword(member.getEmail(), member.getPassword());
    }


}
