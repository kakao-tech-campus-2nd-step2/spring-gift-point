package gift.api.member.service;

import gift.api.member.dao.MemberDao;
import gift.api.member.dto.MemberRequest;
import gift.api.member.exception.EmailAlreadyExistsException;
import gift.global.exception.ForbiddenMemberException;
import gift.global.exception.UnauthorizedMemberException;
import gift.global.utils.JwtUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Transactional
    public Long register(MemberRequest memberRequest) {
        if (memberDao.hasMemberByEmail(memberRequest.email())) {
            throw new EmailAlreadyExistsException();
        }
        return memberDao.saveMember(memberRequest.toEntity());
    }

    public void login(MemberRequest memberRequest, String token) {
        if (memberDao.hasMemberByEmailAndPassword(memberRequest.email(), memberRequest.password())) {
            Long id = memberDao.findMemberByEmail(memberRequest.email()).getId();
            if (token.equals(JwtUtil.generateAccessToken(id, memberRequest.email(), memberRequest.role()))) {
                return;
            }
            throw new UnauthorizedMemberException();
        }
        throw new ForbiddenMemberException();
    }
}
