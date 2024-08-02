package gift.member.application;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.kakao.auth.dto.KakaoTokenResponse;
import gift.kakao.client.KakaoClient;
import gift.member.dao.MemberRepository;
import gift.member.dto.MemberRequest;
import gift.member.dto.MemberResponse;
import gift.member.entity.Member;
import gift.member.util.KakaoTokenMapper;
import gift.member.util.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final KakaoClient kakaoClient;

    public MemberService(MemberRepository memberRepository,
                         KakaoClient kakaoClient) {
        this.memberRepository = memberRepository;
        this.kakaoClient = kakaoClient;
    }

    public Member registerMember(MemberRequest memberRequest) {
        // 사용자 계정 중복 검증
        memberRepository.findByEmail(memberRequest.email())
                        .ifPresent(member -> {
                            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXISTS);
                        });

        return memberRepository.save(MemberMapper.toEntity(memberRequest));
    }

    public Member getMemberByIdOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Transactional
    public void checkKakaoUserAndToken(Member member) {
        if (member.isNotKakaoUser()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_KAKAO_USER);
        }

        if (member.isTokenExpired()) {
            refreshKakaoAccessToken(member.getId());
        }
    }

    @Transactional
    public void refreshKakaoAccessToken(Long id) {
        Member member = getMemberByIdOrThrow(id);
        KakaoTokenResponse refreshTokenResponse = kakaoClient.getRefreshTokenResponse(member.getKakaoRefreshToken());
        member.refreshKakaoTokens(
                KakaoTokenMapper.toTokenInfo(refreshTokenResponse)
        );
    }

    @Transactional
    public void saveMemberPoint(Member member, int point) {
        member.savePoint(point);
    }

    @Transactional
    public void subtractMemberPoint(Member member, Integer point) {
        if (member.isPointDeductible(point)) {
            member.subtractPoint(point);
            return;
        }

        throw new CustomException(ErrorCode.MEMBER_POINT_NOT_DEDUCTIBLE);
    }

    public List<MemberResponse> getAllMembers() {
        return memberRepository.findAll()
                .stream()
                .map(MemberMapper::toResponseDto)
                .toList();
    }

}
