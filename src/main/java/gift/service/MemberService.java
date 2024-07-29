package gift.service;


import gift.dto.betweenClient.member.MemberDTO;
import gift.entity.Member;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.EmailAlreadyHereException;
import gift.exception.BadRequestExceptions.UserNotFoundException;
import gift.exception.InternalServerExceptions.DuplicatedUserException;
import gift.exception.InternalServerExceptions.InternalServerException;
import gift.repository.MemberRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void register(MemberDTO memberDTO) throws RuntimeException {
        try {
            Member member = memberDTO.convertToMember();
            memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
            throw new EmailAlreadyHereException("이미 있는 이메일입니다.");
        }

        catch (BadRequestException e) {
            throw e;
        }
        catch  (Exception e) {
            throw new InternalServerException(e.getMessage());
        }


    }

    @Transactional(readOnly = true)
    public void login(MemberDTO memberDTO) throws RuntimeException {
        try {
            if (memberRepository.countByEmailAndPasswordAndAccountType(
                    memberDTO.getEmail(), memberDTO.getPassword(), memberDTO.getAccountType()) < 1
            ) { throw new UserNotFoundException("아이디 또는 비밀번호가 올바르지 않습니다."); }

            if (memberRepository.countByEmailAndPasswordAndAccountType(
                    memberDTO.getEmail(), memberDTO.getPassword(), memberDTO.getAccountType()) > 1
            ) { throw new DuplicatedUserException(memberDTO.getEmail() + "is Duplicated in DB"); }
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public MemberDTO getMember(String email) throws RuntimeException {
        try {
            if (memberRepository.countByEmail(email) == 1) {
                return MemberDTO.convertToMemberDTO(memberRepository.findByEmail(email).get());
            }

            if (memberRepository.countByEmail(email) > 1) {
                throw new DuplicatedUserException(email + "is Duplicated in DB");
            }
            throw new UserNotFoundException(email + "을(를) 가지는 유저를 찾을 수 없습니다.");
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());

        }
    }

    @Transactional
    public void setMemeberAccessToken(String email, String accessToken) throws RuntimeException {
        try{
            Member member = memberRepository.findByEmail(email).orElseThrow(()
                    -> new BadRequestException(email + "을(를) 가지는 유저를 찾을 수 없습니다."));

            member.setAccessToken(accessToken);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public String getMemberAccessToken(String email) throws RuntimeException {
        try{
            Member member = memberRepository.findByEmail(email).orElseThrow(()
                    -> new BadRequestException(email + "을(를) 가지는 유저를 찾을 수 없습니다."));
            String accessToken = member.getAccessToken();
            if (accessToken.isBlank())
                throw new BadRequestException("해당 사용자의 토큰이 없습니다.");

            return accessToken;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }
}