package gift.member.service;

import gift.member.domain.*;
import gift.member.dto.MemberServiceDto;
import gift.member.exception.DuplicateEmailException;
import gift.member.exception.DuplicateNicknameException;
import gift.member.exception.MemberNotFoundException;
import gift.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private MemberServiceDto memberServiceDto;

    @BeforeEach
    void setUp() {
        member = new Member(1L, MemberType.USER, new Email("email@example.com"), new Password("password"), new Nickname("nickname"));
        memberServiceDto = new MemberServiceDto(1L, MemberType.USER, new Email("email@example.com"), new Password("password"), new Nickname("nickname"));
    }

    @Test
    void testGetAllMembers() {
        // given
        given(memberRepository.findAll()).willReturn(List.of(member));

        // when
        List<Member> members = memberService.getAllMembers();

        // then
        assertThat(members).hasSize(1);
        assertThat(members.getFirst()).isEqualTo(member);
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void testGetMemberById() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        // when
        Member foundMember = memberService.getMemberById(member.getId());

        // then
        assertThat(foundMember).isEqualTo(member);
        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetMemberById_NotFound() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> memberService.getMemberById(member.getId()))
                .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    void testCreateMember() {
        // given
        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.existsByNickname(any())).willReturn(false);
        given(memberRepository.save(any(Member.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Member createdMember = memberService.createMember(memberServiceDto);

        // then
        assertThat(createdMember).isEqualTo(memberServiceDto.toMember());
        verify(memberRepository, times(1)).existsByEmail(any());
        verify(memberRepository, times(1)).existsByNickname(any());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testCreateMember_DuplicateEmail() {
        // given
        given(memberRepository.existsByEmail(any())).willReturn(true);

        // when / then
        assertThatThrownBy(() -> memberService.createMember(memberServiceDto))
                .isInstanceOf(DuplicateEmailException.class);

        verify(memberRepository, times(1)).existsByEmail(any());
        verify(memberRepository, times(0)).existsByNickname(any());
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    void testCreateMember_DuplicateNickname() {
        // given
        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.existsByNickname(any())).willReturn(true);

        // when / then
        assertThatThrownBy(() -> memberService.createMember(memberServiceDto))
                .isInstanceOf(DuplicateNicknameException.class);

        verify(memberRepository, times(1)).existsByEmail(any());
        verify(memberRepository, times(1)).existsByNickname(any());
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    void testUpdateMember() {
        // given
        given(memberRepository.existsById(anyLong())).willReturn(true);
        given(memberRepository.existsByEmail(any())).willReturn(false);
        given(memberRepository.existsByNickname(any())).willReturn(false);
        given(memberRepository.save(any(Member.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Member updatedMember = memberService.updateMember(memberServiceDto);

        // then
        assertThat(updatedMember).isEqualTo(memberServiceDto.toMember());
        verify(memberRepository, times(1)).existsById(anyLong());
        verify(memberRepository, times(1)).existsByEmail(any());
        verify(memberRepository, times(1)).existsByNickname(any());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void testUpdateMember_NotFound() {
        // given
        given(memberRepository.existsById(anyLong())).willReturn(false);

        // when / then
        assertThatThrownBy(() -> memberService.updateMember(memberServiceDto))
                .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository, times(1)).existsById(anyLong());
        verify(memberRepository, times(0)).existsByEmail(any());
        verify(memberRepository, times(0)).existsByNickname(any());
        verify(memberRepository, times(0)).save(any(Member.class));
    }

    @Test
    void testDeleteMember() {
        // given
        given(memberRepository.existsById(anyLong())).willReturn(true);

        // when
        memberService.deleteMember(1L);

        // then
        verify(memberRepository, times(1)).existsById(anyLong());
        verify(memberRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteMember_NotFound() {
        // given
        given(memberRepository.existsById(anyLong())).willReturn(false);

        // when / then
        assertThatThrownBy(() -> memberService.deleteMember(1L))
                .isInstanceOf(MemberNotFoundException.class);

        verify(memberRepository, times(1)).existsById(anyLong());
        verify(memberRepository, times(0)).deleteById(anyLong());
    }
}
