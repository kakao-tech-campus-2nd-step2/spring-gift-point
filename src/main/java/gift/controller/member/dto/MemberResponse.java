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

    public record Point(
        Integer point
    ) {

        public static Point from(MemberModel.Info model) {
            return new Point(
                model.point()
            );
        }
    }

    public record Info(
        Long id,
        String email,
        String name,
        Integer point
    ) {

        public static Info from(MemberModel.Info model) {
            return new Info(
                model.id(),
                model.email(),
                model.name(),
                model.point()
            );
        }
    }
}
