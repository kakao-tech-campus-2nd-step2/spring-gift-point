package gift.model;

import static org.assertj.core.api.Assertions.assertThat;

import gift.form.ProductAddForm;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
class ProductAddFormTest {

    ValidatorFactory factory;
    Validator validator;
    int failCount;
    ProductAddForm form;

    @BeforeEach
    void before() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        failCount = 0;
        form = new ProductAddForm();
    }


    @Test
    void validNameLength() {

        form.setPrice(100);
        form.setImageUrl("https://www.naver.com");

        //성공 길이: 1~15자
        form.setName("1"); isValidInput(form);
        form.setName("123456789012345"); isValidInput(form);
        assertThat(failCount).isEqualTo(0);

    }

    @Test
    void inValidNameLength() {
        form.setPrice(100);
        form.setImageUrl("https://www.naver.com");

        //실패 길이: 0자, 16자 이상
        form.setName(""); isValidInput(form);
        form.setName("12345678901234567"); isValidInput(form);
        assertThat(failCount).isEqualTo(2);
    }

    @Test
    void validNameNormalChar() {
        form.setPrice(100);
        form.setImageUrl("https://www.naver.com");

        form.setName("ㄱ"); isValidInput(form); form.setName("ㅎ"); isValidInput(form);
        form.setName("ㅏ"); isValidInput(form); form.setName("ㅣ"); isValidInput(form);
        form.setName("가"); isValidInput(form); form.setName("힣"); isValidInput(form);

        form.setName("a"); isValidInput(form); form.setName("z"); isValidInput(form);
        form.setName("A"); isValidInput(form); form.setName("Z"); isValidInput(form);

        form.setName("0"); isValidInput(form); form.setName("9"); isValidInput(form);

        form.setName("ㄱㅏㅎㅣ가힣azAZ09"); isValidInput(form);

        assertThat(failCount).isEqualTo(0);
    }

    @Test
    void validNameSpecialChar() {
        form.setPrice(100);
        form.setImageUrl("https://www.naver.com");

        // 가능한 특수문자 입력
        form.setName("("); isValidInput(form); form.setName(")"); isValidInput(form); form.setName("["); isValidInput(form);
        form.setName("]"); isValidInput(form); form.setName("+"); isValidInput(form); form.setName("-"); isValidInput(form);
        form.setName("&"); isValidInput(form); form.setName("/"); isValidInput(form); form.setName("_"); isValidInput(form);
        form.setName("()[]+-&/_"); isValidInput(form);

        assertThat(failCount).isEqualTo(0);
    }

    @Test
    void invalidNameSpecialChar() {
        form.setPrice(100);
        form.setImageUrl("https://www.naver.com");

        // 불가능한 특수문자 입력
        form.setName("#"); isValidInput(form); form.setName("~"); isValidInput(form); form.setName("@"); isValidInput(form);
        form.setName("$"); isValidInput(form); form.setName("%"); isValidInput(form); form.setName("^"); isValidInput(form);
        form.setName("*"); isValidInput(form); form.setName("="); isValidInput(form); form.setName("'"); isValidInput(form);
        form.setName(":"); isValidInput(form); form.setName(";"); isValidInput(form); form.setName("\""); isValidInput(form);
        form.setName("{"); isValidInput(form); form.setName("}"); isValidInput(form); form.setName("|"); isValidInput(form);
        form.setName("\\"); isValidInput(form); form.setName("<"); isValidInput(form); form.setName(">"); isValidInput(form);
        form.setName(","); isValidInput(form); form.setName("."); isValidInput(form); form.setName("?"); isValidInput(form);
        form.setName("!"); isValidInput(form);

        assertThat(failCount).isEqualTo(22);
    }

    @Test
    void validNameKakao() {
        form.setPrice(100);
        form.setImageUrl("https://www.naver.com");

        form.setName("카카"); isValidInput(form);
        form.setName("카오"); isValidInput(form);
        form.setName("카오카"); isValidInput(form);
        assertThat(failCount).isEqualTo(0);
    }

    @Test
    void invalidNameKakao() {
        form.setPrice(100);
        form.setImageUrl("https://www.naver.com");

        form.setName("카카오"); isValidInput(form);
        form.setName("카카오카카오"); isValidInput(form);
        form.setName("카카오카"); isValidInput(form);
        assertThat(failCount).isEqualTo(3);
    }

    @Test
    void validPrice() {
        form.setName("이름");
        form.setImageUrl("https://www.naver.com");

        form.setPrice(100); isValidInput(form);
        form.setPrice(50000); isValidInput(form);
        form.setPrice(1000000); isValidInput(form);
        assertThat(failCount).isEqualTo(0);

    }

    @Test
    void invalidPrice() {
        form.setName("이름");
        form.setImageUrl("https://www.naver.com");

        form.setPrice(99); isValidInput(form);
        form.setPrice(1000001); isValidInput(form);
        assertThat(failCount).isEqualTo(2);
    }


    @Test
    void validUrl() {
        form.setName("이름");
        form.setPrice(100);

        // 올바른 url 입력
        form.setImageUrl("https://www.naver.com"); isValidInput(form);
        form.setImageUrl("http://www.daum.net"); isValidInput(form);
        form.setImageUrl("https://naver.com"); isValidInput(form);
        form.setImageUrl("http://daum.net"); isValidInput(form);
        form.setImageUrl("https://link"); isValidInput(form);
        form.setImageUrl("https://www.naver.com/news"); isValidInput(form);
        form.setImageUrl("https://www.naver.com/search?q=1"); isValidInput(form);
        assertThat(failCount).isEqualTo(0);

    }

    @Test
    void invalidUrl() {
        form.setName("이름");
        form.setPrice(100);

        // 올바르지 않은 url 입력
        form.setImageUrl(""); isValidInput(form);
        form.setImageUrl("https://"); isValidInput(form);
        form.setImageUrl("http://"); isValidInput(form);
        form.setImageUrl("https:/www.naver.com"); isValidInput(form);
        form.setImageUrl("https:www.naver.com"); isValidInput(form);
        form.setImageUrl("https:///www.naver.com"); isValidInput(form);
        form.setImageUrl("https//www.naver.com"); isValidInput(form);
        assertThat(failCount).isEqualTo(7);
    }



    private void isValidInput(ProductAddForm form) {
        if(!validator.validate(form).isEmpty()) {
            failCount++;
            Set<ConstraintViolation<ProductAddForm>> violations = 
                validator.validate(form);
            for (ConstraintViolation<ProductAddForm> violation : violations) {
                System.out.println("violation.getMessage() = " + violation.getMessage());
            }
        }
    }

}