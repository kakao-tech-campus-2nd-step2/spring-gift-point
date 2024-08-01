package gift.application.member.dto;

import gift.model.member.Member;

public class MemberModel {

    public record Info(
        Long id,
        String email,
        String name,
        Integer point
    ) {

        public static Info from(Member member) {
            return new Info(member.getId(), member.getEmail(), member.getName(), member.getPoint());
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
