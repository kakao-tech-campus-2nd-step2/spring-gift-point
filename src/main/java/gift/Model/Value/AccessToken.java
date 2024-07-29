package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class AccessToken {
    private String value;

    protected AccessToken() {
    }

    public AccessToken(String value) {
        validateAccessToken(value);
        this.value = value;
    }

    private void validateAccessToken(String value) {
        if(value == null || value.isBlank())
            throw new IllegalArgumentException("AccessToken 값은 null이 올 수 없습니다");
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof AccessToken))
            return false;

        AccessToken accessToken = (AccessToken) object;
        return Objects.equals(this.value, accessToken.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
