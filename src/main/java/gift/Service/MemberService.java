package gift.Service;

import gift.DTO.ResponseProductListOfCategoryDTO;
import gift.Exception.CategoryNotFoundException;
import gift.Exception.EmailAlreadyExistsException;
import gift.Exception.ForbiddenException;
import gift.Exception.MemberNotFoundException;
import gift.Model.Entity.Category;
import gift.Model.Entity.Member;
import gift.DTO.RequestMemberDTO;
import gift.Model.Entity.Product;
import gift.Model.Value.Email;
import gift.Model.Value.Point;
import gift.Repository.MemberRepository;
import gift.Util.JwtUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;


    public MemberService(MemberRepository memberRepository, JwtUtil jwtUtil){
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public String signUpUser(RequestMemberDTO requestMemberDTO){
        Optional<Member> optionalMember = memberRepository.findByEmail(new Email(requestMemberDTO.email()));
        if(optionalMember.isPresent())
            throw new EmailAlreadyExistsException("이미 존재하는 이메일입니다");
        Member member =  memberRepository.save(new Member(requestMemberDTO.email(), requestMemberDTO.password(), 0));
        return jwtUtil.generateToken(member);
    }

    @Transactional(readOnly = true)
    public String loginUser(RequestMemberDTO requestMemberDTO) throws ForbiddenException {
        Member member = memberRepository.findByEmail(new Email(requestMemberDTO.email())).orElseThrow(() -> new ForbiddenException("아이디가 존재하지 않습니다"));
        String temp = member.getPassword().getValue();
        if (!(temp.equals(requestMemberDTO.password())))
            throw new ForbiddenException("비밀번호가 틀렸습니다");

        return jwtUtil.generateToken(member);
    }

    @Transactional(readOnly = true)
    public Member getUserByToken(String token) {
        return memberRepository.findByEmail(new Email(jwtUtil.getSubject(token))).orElseThrow(()-> new MemberNotFoundException("매칭되는 멤버가 없습니다"));
    }

    @Transactional
    public void subtractPoint(Member member, int totalPrice) {
        member.subtractPoint(totalPrice);
    }

    @Transactional
    public void refundPoints(Member member, int point) {
        member.addPoint(point);
    }

    @Transactional
    public void addPoints(Long memberId, int point) {
        Member member = memberRepository.findById(memberId).orElseThrow(()-> new MemberNotFoundException("멤버가 존재하지 않습니다"));
        member.addPoint(point);
    }
    @Transactional(readOnly = true)
    public int getPoints(Member member) {
        return member.getPoint().getValue();
    }

    @Transactional(readOnly = true)
    public Page<Member> getAllMembers(Pageable pageable) {
        Page<Member> memberPage= memberRepository.findAll(pageable);
        return memberPage;
    }

    @Transactional(readOnly = true)
    public Member selectMember(Long id) {
        return memberRepository.findById(id).orElseThrow(()-> new MemberNotFoundException("멤버를 찾을 수 없습니다"));
    }

    @Transactional
    public void deleteMember(Long id){
        memberRepository.deleteById(id);
    }
}
