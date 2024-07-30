package gift.service;

import gift.entity.SnsMember;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.repository.SnsMemberRepository;
import org.springframework.stereotype.Service;

@Service
public class SnsMemberService {

    private final SnsMemberRepository snsMemberRepository;

    public SnsMemberService(SnsMemberRepository snsMemberRepository) {
        this.snsMemberRepository = snsMemberRepository;
    }

    public String getOauthAccessTokenByEmail(String email) {
        SnsMember snsMember = snsMemberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return snsMember.getOauthAccessToken();
    }
}
