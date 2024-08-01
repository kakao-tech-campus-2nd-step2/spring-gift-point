package gift;

import org.apache.catalina.core.ApplicationContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void test1(){
        System.out.println(this);
        System.out.println(context);
        assertThat(context).isNotNull();
    }

}
