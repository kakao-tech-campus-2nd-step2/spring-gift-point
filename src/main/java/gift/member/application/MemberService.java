package gift.member.application;

import gift.auth.KakaoResponse;
import gift.auth.KakaoService;
import gift.auth.KakaoToken;
import gift.exception.type.DuplicateNameException;
import gift.exception.type.NotFoundException;
import gift.member.application.command.MemberEmailUpdateCommand;
import gift.member.application.command.MemberJoinCommand;
import gift.member.application.command.MemberLoginCommand;
import gift.member.application.command.MemberPasswordUpdateCommand;
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

    public Long join(MemberJoinCommand command) {
        return memberRepository.save(command.toMember()).getId();
    }

    public Long login(MemberLoginCommand command) {
        return memberRepository
                .findByEmailAndPassword(command.email(), command.password())
                .orElseThrow(() -> new NotFoundException("해당 회원이 존재하지 않습니다."))
                .getId();
    }

    @Transactional
    public void updateEmail(MemberEmailUpdateCommand command, Long memberId) {
        Member member = getMember(memberId);

        if (memberRepository.existsByEmail(command.email()))
            throw new DuplicateNameException("이미 사용중인 이메일입니다.");

        member.updateEmail(command.email());
    }

    @Transactional
    public void updatePassword(MemberPasswordUpdateCommand command, Long memberId) {
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
    public Long kakaoLogin(String code) {
        KakaoToken token = kakaoService.fetchToken(code);
        KakaoResponse memberInfo = kakaoService.fetchMemberInfo(token.accessToken());
        Member newMember = findOrCreateMember(memberInfo);
        kakaoService.saveToken(token, newMember);

        return newMember.getId();
    }

    private Member findOrCreateMember(KakaoResponse memberInfo) {
        return memberRepository.findByKakaoId(memberInfo.id())
                .orElseGet(() -> {
                    Member newMember = Member.ofKakao(memberInfo.id(), memberInfo.kakaoAccount().email());
                    return memberRepository.save(newMember);
                });
    }
}
