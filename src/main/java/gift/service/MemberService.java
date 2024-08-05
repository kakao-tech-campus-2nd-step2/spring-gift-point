package gift.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.dto.MemberDto;
import gift.dto.request.LoginRequest;
import gift.dto.request.RegisteRequest;
import gift.entity.Member;
import gift.entity.WishList;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import gift.repository.WishListRepository;
import gift.util.JwtUtil;
import jakarta.transaction.Transactional;

@Service
public class MemberService {

    private MemberRepository memberRepository;
    private WishListRepository wishListRepository;
    private JwtUtil jwtUtil;

    public MemberService(MemberRepository memberRepository, WishListRepository wishListRepository, JwtUtil jwtUtil){
        this.memberRepository = memberRepository;
        this.wishListRepository = wishListRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional 
    public void addMember(RegisteRequest registeRequest){

        if(memberRepository.findByEmail(registeRequest.getEmail()).isEmpty()){
            Member member = new Member(registeRequest.getPassword(), registeRequest.getEmail(), null);
            memberRepository.save(member);
        }else{
            throw new CustomException("Member with email " + registeRequest.getEmail() + "exists", HttpStatus.CONFLICT, -40901);
        }
    }

    @Transactional
    public void deleteMember(Long id){

        memberRepository.findById(id)
            .orElseThrow(() -> new CustomException("Member with id " + id + " not found", HttpStatus.NOT_FOUND, -40406));

        List<WishList> wishList = wishListRepository.findByMemberId(id);
        wishListRepository.deleteAll(wishList);
        
        memberRepository.deleteById(id);
    }

    @Transactional
    public MemberDto findByEmail(String email){
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("Member with email " + email + " not found", HttpStatus.NOT_FOUND, -40101));
        return new MemberDto(member.getId(), member.getPassword(), member.getEmail(), member.getRole());
    }

    @Transactional
    public MemberDto findByRequest(LoginRequest loginRequest){
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())
            .orElseThrow(() -> new CustomException("User with Request not found", HttpStatus.FORBIDDEN, -40301));
        return new MemberDto(member.getId(), member.getPassword(), member.getEmail(), member.getRole());
    }

    @Transactional
    public String generateToken(String email){
        MemberDto memberDto = findByEmail(email);
        return jwtUtil.generateToken(memberDto);
    }

    @Transactional
    public String authenticateMember(LoginRequest loginRequest){
        MemberDto memberDto = findByRequest(loginRequest);
        return generateToken(memberDto.getEmail());
    }
    
    public Member toEntity(MemberDto memberDto){
        return new Member(memberDto.getPassword(), memberDto.getEmail(), memberDto.getRole());
    }
}
