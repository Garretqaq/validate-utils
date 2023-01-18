package spring;

import com.dato.Application;
import com.dato.test.TestParam;
import com.dato.test.TestResultVO;
import com.dato.test.ValidatorTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SpringTest {

    @Resource
    private ValidatorTest validatorTest;

    @Test
    public void test(){
        TestParam testParam = new TestParam();
        testParam.setA("666");
        testParam.setList(Arrays.asList(1, 2, 3));
        TestResultVO test = validatorTest.test(testParam);
    }
}
