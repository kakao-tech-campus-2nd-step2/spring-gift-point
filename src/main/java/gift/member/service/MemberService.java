package gift.member.service;

import gift.auth.dto.RegisterResDto;
import gift.auth.exception.LoginFailedException;
import gift.auth.token.AuthToken;
import gift.auth.token.AuthTokenGenerator;
import gift.member.dto.MemberPointsResDto;
import gift.member.dto.MemberReqDto;
import gift.member.dto.MemberResDto;
import gift.member.entity.Member;
import gift.member.exception.MemberAlreadyExistsByEmailException;
import gift.member.exception.MemberCreateException;
import gift.member.exception.MemberDeleteException;
import gift.member.exception.MemberNotEnoughPointException;
import gift.member.exception.MemberNotFoundByIdException;
import gift.member.exception.MemberUpdateException;
import gift.member.points.PointsStrategy;
import gift.member.repository.MemberRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthTokenGenerator authTokenGenerator;
    private final PointsStrategy pointsStrategy;

    public MemberService(MemberRepository memberRepository, AuthTokenGenerator authTokenGenerator, PointsStrategy pointsStrategy) {
        this.memberRepository = memberRepository;
        this.authTokenGenerator = authTokenGenerator;
        this.pointsStrategy = pointsStrategy;
    }

    @Transactional
    public void processOrderPoints(Member member, Integer pointsToUse, Integer price) {
        Integer points = member.getPoints();
        if (points < pointsToUse) {
            throw MemberNotEnoughPointException.EXCEPTION;
        }
        member.usePoints(pointsToUse);  // 포인트 사용

        Integer pointsToAdd = pointsStrategy.calculatePointsToAdd(price);
        member.addPoints(pointsToAdd);  // 포인트 적립
    }

    @Transactional(readOnly = true)
    public MemberPointsResDto getMemberPoints(MemberResDto memberDto) {
        Member member = findMemberByIdOrThrow(memberDto.id());
        return new MemberPointsResDto(member.getPoints());
    }

    @Transactional(readOnly = true)
    public List<MemberResDto> getMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberResDto getMember(Long memberId) {
        Member findMember = findMemberByIdOrThrow(memberId);
        return new MemberResDto(findMember);
    }

    @Transactional
    public RegisterResDto register(MemberReqDto memberReqDto) {
        checkDuplicateEmail(memberReqDto.email());  // 중복되는 이메일이 있으면 예외 발생

        // 일반 사용자로 회원 가입
        // 관리자 계정은 데이터베이스에서 직접 추가
        Member newMember;
        try {
            newMember = memberRepository.save(memberReqDto.toEntity());
        } catch (Exception e) {
            throw MemberCreateException.EXCEPTION;
        }

        AuthToken authToken = authTokenGenerator.generateToken(new MemberResDto(newMember));
        return new RegisterResDto(newMember.getEmail(), authToken.accessToken());
    }

    @Transactional
    public void updateMember(Long memberId, MemberReqDto memberReqDto) {
        Member findMember = findMemberByIdOrThrow(memberId);
        try {
            findMember.update(memberReqDto);    // 변경 감지 이용
        } catch (Exception e) {
            throw MemberUpdateException.EXCEPTION;
        }
    }

    @Transactional
    public void deleteMember(Long memberId) {
        Member findMember = findMemberByIdOrThrow(memberId);
        try {
            memberRepository.delete(findMember);
        } catch (Exception e) {
            throw MemberDeleteException.EXCEPTION;
        }
    }

    public Member findMemberByIdOrThrow(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> MemberNotFoundByIdException.EXCEPTION
        );
    }

    public Member findMemberByEmailOrThrow(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> LoginFailedException.EXCEPTION    // 로그인 실패 시 이메일 존재 여부를 알리지 않기 위해 EmailNotFoundException 대신 LoginFailedException 사용
        );
    }

    private void checkDuplicateEmail(String email) {
        boolean isExist = memberRepository.existsByEmail(email);

        if (isExist) {
            throw MemberAlreadyExistsByEmailException.EXCEPTION;
        }
    }

    @Transactional
    public MemberResDto loginOrRegisterByEmail(String email, String accessToken) {
        Member member = memberRepository.findByEmail(email)
                        .orElseGet(() -> memberRepository.save(new Member(email, "1234")));
        member.changeKakaoAccessToken(accessToken);
        return new MemberResDto(member);
    }
}
