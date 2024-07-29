package gift.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

@Service
public class MemberAccessTokenProvider {
    private  final String secretKey = "21312BHV48DX21421C25F215NJN2DF12141JJ214GVSQ12429858236H7998325A72";

    public String createJwt(String email){

        return Jwts.builder()
            .setHeaderParam("type","jwt")
            .claim("email", email)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }
    // claim에 email만 있으므로 parsing 해 body를 얻으면 json객체로 반환 이를 get("email").toString()으로 email값만 갖고옴
   public String getEmail(String token){
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("email").toString();
   }

}
