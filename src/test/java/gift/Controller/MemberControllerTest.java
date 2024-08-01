//package gift.Controller;
//
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import gift.DTO.MemberDto;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.ConstraintViolationException;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import java.util.Set;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.ActiveProfiles;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class MemberControllerTest {
//
//  private final MemberController memberController;
//
//  @Autowired
//  public MemberControllerTest(MemberController memberController) {
//    this.memberController = memberController;
//  }
//
//  @Test
//  void memberSignUpTest() {
//    MemberDto memberDto = new MemberDto(1L, "a@naver.com", "abcde");
//    assertThat(memberController.SignUp(memberDto).getStatusCode()).isEqualTo(HttpStatus.CREATED);
//
//  }
//
//  @Test
//  public void validateMemberDto() {
//    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//    Validator validator = factory.getValidator();
//
//    MemberDto memberDto1 = new MemberDto(1L, "a", "abcde");
//    MemberDto memberDto2 = new MemberDto(1L, "", "abcde");
//    MemberDto memberDto3 = new MemberDto(1L, "a@naver.com", "");
//
//    Set<ConstraintViolation<MemberDto>> violations1 = validator.validate(memberDto1);
//    Set<ConstraintViolation<MemberDto>> violations2 = validator.validate(memberDto2);
//    Set<ConstraintViolation<MemberDto>> violations3 = validator.validate(memberDto3);
//
//    assertThrows(ConstraintViolationException.class, () -> {
//      if (!violations1.isEmpty()) {
//        throw new ConstraintViolationException(violations1);
//      }
//    });
//
//    assertThrows(ConstraintViolationException.class, () -> {
//      if (!violations2.isEmpty()) {
//        throw new ConstraintViolationException(violations2);
//      }
//    });
//
//    assertThrows(ConstraintViolationException.class, () -> {
//      if (!violations3.isEmpty()) {
//        throw new ConstraintViolationException(violations3);
//      }
//    });
//  }
//}