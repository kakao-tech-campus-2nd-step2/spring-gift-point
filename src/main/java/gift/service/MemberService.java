package gift.service;

import gift.DTO.MemberDTO;
import gift.auth.DTO.TokenDTO;
import gift.auth.exception.AuthException;
import gift.auth.utill.JwtToken;
import gift.entity.MemberEntity;
import gift.mapper.MemberMapper;
import gift.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final JwtToken jwtToken = new JwtToken();

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberMapper memberMapper;

    /**
     * 사용자 존재 여부 확인 메서드
     *
     * @param memberDTO 로그인 정보
     * @return 사용자 존재 여부
     */
    @Transactional(readOnly = true)
    public boolean isExist(MemberDTO memberDTO) {
        return memberRepository.existsByEmailAndPasswordAndDeleteFalse(memberDTO.getEmail(),
                memberDTO.getPassword());
    }

    /**
     * 사용자 ID 조회 메서드
     *
     * @param memberDTO 로그인 정보
     * @return 사용자 ID
     */
    @Transactional(readOnly = true)
    public long getUserId(MemberDTO memberDTO) {
        MemberEntity member = memberRepository.findByEmailAndPasswordAndDeleteFalse(
                memberDTO.getEmail(), memberDTO.getPassword());
        return member != null ? member.getId() : -1;
    }

    /**
     * 사용자의 로그인 정보를 확인하고 JWT 토큰을 생성함
     *
     * @param loginRequestDTO 사용자의 로그인 정보가 포함된 Login 객체
     * @return 생성된 JWT 토큰을 포함하는 Token 객체
     * @throws AuthException 사용자가 존재하지 않는 경우 예외를 발생시킴
     */
    @Transactional(readOnly = true)
    public TokenDTO login(MemberDTO loginRequestDTO, long tokenExpTime) {
        if (!isExist(loginRequestDTO)) {
            throw new AuthException("유저가 존재하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        long userId = getUserId(loginRequestDTO);
        var memberDetailsDTO = new MemberDTO(userId, loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword());
        return jwtToken.createToken(memberDetailsDTO, tokenExpTime);
    }

    /**
     * 사용자의 회원가입 정보를 확인하고 JWT 토큰을 생성함
     *
     * @param loginRequestDTO 사용자의 회원가입 정보가 포함된 Login 객체
     * @return 생성된 JWT 토큰을 포함하는 Token 객체
     * @throws AuthException 사용자가 회원가입에 실패한 경우 예외를 발생시킴
     */
    @Transactional
    public TokenDTO signUp(MemberDTO loginRequestDTO, long tokenExpTime) {
        try {
            var member = memberMapper.toMemberEntity(loginRequestDTO, false);
            memberRepository.save(member);

            var memberDTO = new MemberDTO(member.getId(), member.getEmail(), member.getPassword());
            return jwtToken.createToken(memberDTO, tokenExpTime);
        } catch (Exception e) {
            throw new AuthException("회원가입에 실패했습니다.", HttpStatus.FORBIDDEN);
        }
    }

    @Transactional(readOnly = true)
    public TokenDTO login(MemberDTO memberDTO) {
        return login(memberDTO, 60 * 60 * 24 * 7);
    }

    @Transactional(readOnly = true)
    public TokenDTO signUp(MemberDTO memberDTO) {
        return signUp(memberDTO, 60 * 60 * 24 * 7);
    }
}
