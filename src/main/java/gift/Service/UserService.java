package gift.Service;

import gift.Exception.Login.LoginException;
import gift.Model.Role;
import gift.Model.DTO.MemberDTO;
import gift.Model.Entity.MemberEntity;
import gift.Repository.MemberRepository;
import gift.Token.JwtToken;
import gift.Token.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;

@Service
public class UserService {
        private final MemberRepository memberRepository;
        private final JwtTokenProvider jwtTokenProvider;
        private final RestTemplate restTemplate;

        @Value("${redirectUri}")
        private String redirectUri;
        @Value("${clientId}")
        private String clientId;
        @Value("${restApiKey}")
        private String restApiKey;

        public UserService(MemberRepository memberRepository, JwtTokenProvider jwtTokenProvider,RestTemplate restTemplate){
            this.memberRepository = memberRepository;
            this.jwtTokenProvider = jwtTokenProvider;
            this.restTemplate = restTemplate;
        }

        public JwtToken register(MemberDTO memberDTO){
            if(memberRepository.findByEmail(memberDTO.email()).isPresent())
                throw new LoginException("중복된 이메일이 있습니다.");

            MemberEntity memberEntity = new MemberEntity(memberDTO.email(), memberDTO.password(), Role.CONSUMER);
            memberRepository.save(memberEntity);
            return new JwtToken(jwtTokenProvider.createToken(memberEntity));
        }

        public JwtToken login(MemberDTO memberDTO){
            Optional<MemberEntity> member = memberRepository.findByEmail(memberDTO.email());

            if(member.isEmpty() || !member.get().getPassword().equals(memberDTO.password()))
                throw new LoginException("회원 정보가 일치하지 않습니다.");

            return new JwtToken(jwtTokenProvider.createToken(member.get()));
        }

        public String getKakaoLoginUrl(){
            return "https://kauth.kakao.com/oauth/authorize" +
                    "?scope=talk_message" +
                    "&response_type=code" +
                    "&redirect_uri="+ redirectUri +
                    "&client_id=" + restApiKey;
        }

        public ResponseEntity<?> kakaoLogin(String authorizationCode){
            var url = "https://kauth.kakao.com/oauth/token";
            var headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
            var body = new LinkedMultiValueMap<String, String>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", clientId);
            body.add("redirect_uri", redirectUri);
            body.add("code", authorizationCode);
            var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));

            return restTemplate.exchange(request, String.class);
        }
}
