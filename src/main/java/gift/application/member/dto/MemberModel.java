package gift.application.member.dto;

import gift.model.member.Member;
import gift.model.member.Role;

public class MemberModel {

    public record Info(
        Long id,
        String email,
        String name,
        Integer point,
        Role role
    ) {

        public static Info from(Member member) {
            return new Info(member.getId(), member.getEmail(), member.getName(), member.getPoint(),
                member.getRole());
        }
    }

    public record IdAndJwt(
        Long memberId,
        String jwt
    ) {

    }

    public record InfoAndJwt(
        Info info,
        String jwt
    ) {

    }

}
