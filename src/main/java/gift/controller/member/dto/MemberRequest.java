package gift.controller.member.dto;

import gift.model.member.Provider;
import gift.model.member.Role;
import gift.application.member.dto.MemberCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberRequest {

    public record Register(
        @Email
        String email,
        @NotBlank
        String password,
        @NotBlank
        String name) {

        public MemberCommand.Create toCommand() {
            return new MemberCommand.Create(email, password, name, Role.USER, Provider.ORIGIN);
        }
    }

    public record Login(
        @Email
        String email,
        @NotBlank
        String password) {

        public MemberCommand.Login toCommand() {
            return new MemberCommand.Login(email, password);
        }
    }
}
