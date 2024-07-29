package gift.controller.member;

import gift.domain.Grade;
import java.util.UUID;

public record MemberResponse(UUID id, String email, String password, String nickName,
                             Grade grade) {

}