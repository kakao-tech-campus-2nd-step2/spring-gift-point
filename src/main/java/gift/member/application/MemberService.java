package gift.member.application;

import gift.auth.KakaoResponse;
import gift.auth.KakaoService;
import gift.auth.KakaoToken;
import gift.exception.type.DuplicateException;
import gift.exception.type.NotFoundException;
import gift.exception.type.UnAuthorizedException;
import gift.member.application.command.MemberEmailUpdateCommand;
import gift.member.application.command.MemberRegisterCommand;
import gift.member.application.command.MemberLoginCommand;
import gift.member.application.command.MemberPasswordUpdateCommand;
import gift.member.application.response.MemberLoginServiceResponse;
import gift.member.application.response.MemberRegisterServiceResponse;
import gift.member.application.response.MemberServiceResponse;
import gift.member.domain.Member;
import gift.member.domain.MemberRepository;
import gift.wishlist.domain.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final WishlistRepository wishlistRepository;
    private final KakaoService kakaoService;

    @Autowired
    public MemberService(MemberRepository memberRepository, WishlistRepository wishlistRepository, KakaoService kakaoService) {
        this.memberRepository = memberRepository;
        this.wishlistRepository = wishlistRepository;
        this.kakaoService = kakaoService;
    }

    public MemberRegisterServiceResponse register(MemberRegisterCommand command) {
        memberRepository.findByEmail(command.email())
                .ifPresent(member -> {
                    throw new DuplicateException("이미 사용중인 이메일입니다.");
                });

        Member member = memberRepository.save(command.toMember());

        return MemberRegisterServiceResponse.from(member);
    }

    public MemberLoginServiceResponse login(MemberLoginCommand command) {
        Member member = memberRepository
                .findByEmailAndPassword(command.email(), command.password())
                .orElseThrow(() -> new UnAuthorizedException("이메일 또는 비밀번호가 일치하지 않습니다."));

        return MemberLoginServiceResponse.from(member);
    }

    @Transactional
    public void updateEmail(Long memberId, MemberEmailUpdateCommand command) {
        Member member = getMember(memberId);

        if (memberRepository.existsByEmail(command.email()))
            throw new DuplicateException("이미 사용중인 이메일입니다.");

        member.updateEmail(command.email());
    }

    @Transactional
    public void updatePassword(Long memberId, MemberPasswordUpdateCommand command) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 회원이 존재하지 않습니다."))
                .updatePassword(command.password());
    }

    public MemberServiceResponse findById(Long memberId) {
        return memberRepository.findById(memberId)
                .map(MemberServiceResponse::from)
                .orElseThrow(() -> new NotFoundException("해당 회원이 존재하지 않습니다."));
    }

    public List<MemberServiceResponse> findAll() {
        return memberRepository.findAll()
                .stream().map(MemberServiceResponse::from).toList();
    }

    @Transactional
    public void delete(Long memberId) {
        Member member = getMember(memberId);

        if (member.getKakaoId() != null) {
            kakaoService.unlink(member.getKakaoId());
        }

        wishlistRepository.deleteAllByMemberId(member.getId());
        memberRepository.delete(member);
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 회원이 존재하지 않습니다."));
    }

    @Transactional
    public MemberLoginServiceResponse kakaoLogin(String code) {
        KakaoToken token = kakaoService.fetchToken(code);
        KakaoResponse memberInfo = kakaoService.fetchMemberInfo(token.accessToken());
        Member newMember = findOrCreateMember(memberInfo);
        kakaoService.saveToken(token, newMember);

        return MemberLoginServiceResponse.from(newMember);
    }

    private Member findOrCreateMember(KakaoResponse memberInfo) {
        return memberRepository.findByKakaoId(memberInfo.id())
                .orElseGet(() -> {
                    Member newMember = Member.fromKakao(memberInfo);
                    return memberRepository.save(newMember);
                });
    }

    @Transactional
    public void addPoint(Long memberId, int amount) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        member.addPoint(member.getPoint() + amount);
    }

    @Transactional(readOnly = true)
    public int getPoint(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("해당 회원이 존재하지 않습니다."))
                .getPoint();
    }
}
