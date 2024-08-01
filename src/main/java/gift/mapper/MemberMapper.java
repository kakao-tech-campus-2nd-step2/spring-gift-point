package gift.mapper;

import gift.domain.member.Member;
import gift.web.dto.MemberDto;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    public MemberDto toDto(Member member) {
        return new MemberDto(member.getEmail(),
                            member.getPassword());
    }

    public Member toEntity(MemberDto memberDto) {
        return new Member(memberDto.email(), memberDto.password());
    }
}
