package gift.member;

import static gift.exception.ErrorMessage.MEMBER_NOT_FOUND;
import static gift.exception.ErrorMessage.WRONG_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import gift.member.dto.MemberRequestDTO;
import gift.member.entity.Member;
import gift.token.JwtProvider;
import gift.token.MemberTokenDTO;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    JwtProvider jwtProvider;

    @MockBean
    MemberRepository memberRepository;

    @Test
    void register() {
    }

    @Nested
    @DisplayName("[Unit] verify password test")
    class loginTest {

        @Test
        @DisplayName("success")
        void success() {
            //given
            MemberRequestDTO inputMemberDTO = new MemberRequestDTO("aaa@email.com", "password");
            Member except = new Member("aaa@email.com", "password");

            //when
            when(memberRepository.findById(inputMemberDTO.getEmail()))
                .thenReturn(Optional.of(except));

            //then
            assertThat(memberService.login(inputMemberDTO))
                .isEqualTo(jwtProvider.generateToken(
                        new MemberTokenDTO(inputMemberDTO.getEmail())
                    )
                );
        }

        @Test
        @DisplayName("member not found error")
        void memberNotFoundError() {
            //given
            MemberRequestDTO inputMemberDTO = new MemberRequestDTO("aaa@email.com", "password");

            //when
            when(memberRepository.findById(inputMemberDTO.getEmail()))
                .thenThrow(new IllegalArgumentException(MEMBER_NOT_FOUND));

            //then
            assertThatThrownBy(() -> memberService.login(inputMemberDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MEMBER_NOT_FOUND);
        }

        @Test
        @DisplayName("wrong password error")
        void wrongPasswordError() {
            //given
            MemberRequestDTO inputMemberDTO = new MemberRequestDTO("aaa@email.com",
                "wrong-password");
            Member except = new Member("aaa@email.com", "right-password");

            //when
            when(memberRepository.findById(inputMemberDTO.getEmail()))
                .thenReturn(Optional.of(except));

            //then
            assertThatThrownBy(() -> memberService.login(inputMemberDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(WRONG_PASSWORD);
        }
    }
}