package gift.member.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Email {
    @Column(name = "email")
    private String emailValue;

    public Email() {
    }

    public Email(String emailValue) {
        this.emailValue = emailValue;
    }

    public String getEmailValue() {
        return emailValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return emailValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email that = (Email) o;
        return Objects.equals(emailValue, that.emailValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailValue);
    }
}
