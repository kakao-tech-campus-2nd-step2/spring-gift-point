package gift.controller.auth;

import gift.domain.Grade;
import java.util.UUID;

public record LoginResponse(UUID id, String email, String nickname, Grade grade) {

    public boolean isAdmin() {
        return grade != null && grade == Grade.ADMIN;
    }
}