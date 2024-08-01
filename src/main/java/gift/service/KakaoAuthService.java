package gift.service;

import gift.dto.betweenClient.member.MemberDTO;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.UserNotFoundException;
import gift.exception.InternalServerExceptions.InternalServerException;
import java.util.Objects;
import org.springframework.stereotype.Service;


@Service
public class KakaoAuthService {
    private final KakaoTokenService kakaoTokenService;
    private final MemberService memberService;

    public KakaoAuthService(KakaoTokenService kakaoTokenService, MemberService memberService) {
        this.kakaoTokenService = kakaoTokenService;
        this.memberService = memberService;
    }

    public MemberDTO loginOrRegister(String code){
        String accessToken = kakaoTokenService.getAccessToken(code);
        MemberDTO memberDTO = kakaoTokenService.getUserInfo(accessToken);
        try{
            MemberDTO memberDTOInDb = memberService.getMember(memberDTO.getEmail());
            if(!Objects.equals(memberDTOInDb.getAccountType(), memberDTO.getAccountType()))
                throw new BadRequestException("해당 email로 가입된 계정이 이미 존재합니다.");

            memberService.login(memberDTO);
            memberService.setMemeberAccessToken(memberDTO.getEmail(), accessToken);
            return memberDTO;
        } catch (UserNotFoundException e) {
            memberService.register(memberDTO);
            memberService.setMemeberAccessToken(memberDTO.getEmail(), accessToken);
            return memberDTO;
        } catch (InternalServerException | BadRequestException e){
            throw e;
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }
}
