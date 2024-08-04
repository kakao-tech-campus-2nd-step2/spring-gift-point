package gift.application.member.dto;

import gift.model.member.Member;
import gift.model.member.Provider;
import gift.model.member.Role;

public class MemberCommand {

    public record Create(
        String email,
        String password,
        String name,
        Role role,
        Provider provider
    ) {

        public Member toEntity() {
            return Member.create(
                null,
                email,
                password,
                name,
                role,
                provider
            );
        }
    }

    public record Login(
        String email,
        String password
    ) {

    }

}
