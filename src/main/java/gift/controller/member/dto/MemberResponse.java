package gift.controller.member.dto;

import gift.application.member.dto.MemberModel;
import gift.model.member.Role;

public class MemberResponse {

    public record Login(
        String name,
        Role role
    ) {

        public static Login from(MemberModel.InfoAndJwt model) {

            return new Login(model.info().name(), model.info().role());
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
