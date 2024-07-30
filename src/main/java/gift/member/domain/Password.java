package gift.member.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Password {
    @Column(name = "password")
    private String passwordValue;

    public Password() {
    }

    public Password(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return passwordValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password that = (Password) o;
        return Objects.equals(passwordValue, that.passwordValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(passwordValue);
    }
}
