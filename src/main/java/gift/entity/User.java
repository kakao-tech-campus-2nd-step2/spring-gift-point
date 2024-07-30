package gift.entity;

import org.springframework.http.HttpStatus;

import gift.exception.InvalidUserException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Email(message = "이메일 형식일 올바르지 않습니다.")
	@NotBlank(message = "이메일 입력은 필수 입니다.")
	@Column(unique = true, nullable = false)
	private String email;
	
	@NotBlank(message = "비밀번호 입력은 필수 입니다.")
	@Column(nullable = false)
	private String password;
	
	public User() {}
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void validatePassword(String inputPassword) {
		if (!inputPassword.equals(this.password)) {
			throw new InvalidUserException("The email doesn't or thr password is incorrect.", HttpStatus.FORBIDDEN);
		}
	}
}
