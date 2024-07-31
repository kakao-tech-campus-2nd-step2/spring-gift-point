package gift.controller.member.dto;

import gift.application.member.dto.MemberModel;

public class MemberResponse {

    public record Login(
        String name
    ) {

        public static Login from(MemberModel.InfoAndJwt model) {

            return new Login(model.info().name());
        }
    }

    public record Info(
        String email,
        String name
    ) {

        public static Info from(MemberModel.Info model) {
            return new Info(
                model.email(),
                model.name()
            );
        }
    }
}
