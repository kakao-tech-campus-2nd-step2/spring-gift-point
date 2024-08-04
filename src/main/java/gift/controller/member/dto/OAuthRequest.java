package gift.controller.member.dto;

import gift.application.member.dto.OAuthCommand;
import jakarta.validation.constraints.NotNull;

public class OAuthRequest {

    public record LoginRequest(
        @NotNull
        String authorizationCode
    ) {

        public OAuthCommand.Login toCommand() {
            return new OAuthCommand.Login(authorizationCode);
        }
    }

}
