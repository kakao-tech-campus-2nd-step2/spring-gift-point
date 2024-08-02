package gift.DTO;

import java.beans.ConstructorProperties;

public class TokenDTO {
   private String token;
    @ConstructorProperties({"token"})
    public TokenDTO(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
