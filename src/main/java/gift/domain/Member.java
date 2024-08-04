package gift.domain;

import gift.domain.base.BaseTimeEntity;
import gift.domain.constants.Platform;
import gift.domain.vo.Email;
import gift.domain.vo.Password;
import gift.domain.vo.Point;
import gift.web.validation.exception.client.BadRequestException;
import gift.web.validation.exception.client.IncorrectPasswordException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Entity
public class Member extends BaseTimeEntity {

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'GIFT'")
    private Platform platform;

    @Embedded
    private Point point;

    protected Member() {
    }

    public static class Builder extends BaseTimeEntity.Builder<Builder> {

        private Email email;
        private Password password;
        private String name;
        private Platform platform;

        public Builder email(Email email) {
            this.email = email;
            return this;
        }

        public Builder password(Password password) {
            this.password = password;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder platform(Platform platform) {
            this.platform = platform;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Member build() {
            return new Member(this);
        }
    }

    private Member(Builder builder) {
        super(builder);
        email = builder.email;
        password = builder.password;
        name = builder.name;
        platform = builder.platform;
    }

    public void matchPassword(final String password) {
        if (!this.password.matches(password)) {
            throw new IncorrectPasswordException();
        }
    }

    public Point subtractPoint(final Integer point) {
        if (this.point.getValue() < point) {
            throw new BadRequestException("포인트가 부족합니다.");
        }
        return this.point.subtract(point);
    }

    public Point addPoint(final Integer point) {
        return this.point.add(point);
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Platform getPlatform() {
        return platform;
    }

    public Point getPoint() {
        return point;
    }

}
