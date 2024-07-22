package gift.member.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class Nickname {
    @Column(name = "nickname")
    private String nicknameValue;

    public Nickname() {
    }

    public Nickname(String nicknameValue) {
        this.nicknameValue = nicknameValue;
    }

    public String getNicknameValue() {
        return nicknameValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return nicknameValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Nickname that = (Nickname) o;
        return Objects.equals(nicknameValue, that.nicknameValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nicknameValue);
    }
}
