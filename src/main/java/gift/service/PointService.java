package gift.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import gift.dto.request.PointRequest;
import gift.dto.response.GetWishListResponse;
import gift.dto.response.PointResponse;
import gift.dto.response.WishListResponse;
import gift.entity.Member;
import gift.entity.WishList;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;

@Service
public class PointService {

    private MemberRepository memberRepository;
    private JwtUtil jwtUtil;

    public PointService(MemberRepository memberRepository, JwtUtil jwtUtil){
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
    }
    
    public PointResponse getPoint(String token){

        Long memberId  = (long)jwtUtil.extractAllClaims(token).get("id");
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException("Member with token not exist", HttpStatus.UNAUTHORIZED, -40101));
        
        return new PointResponse(member.getPoint());  
    }

    public PointResponse chargePoint(String token, PointRequest pointRequest, Long memberId){
        
        String role = jwtUtil.extractAllClaims(token).get("role");
        if(role != "admin"){
            return new CustomException("only admin can charge point", HttpStatus.FORBIDDEN, -40303);
        }

        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new CustomException("Member with token not exist", HttpStatus.UNAUTHORIZED, -40101))
        
        member.chargePoint(pointRequest.getPoint());
        return new PointResponse(member.getPoint());
    }
}
