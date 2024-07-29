package gift.controller.member;

import gift.domain.Grade;

public record MemberRequest(String email, String password, String nickName, Grade grade) {

}