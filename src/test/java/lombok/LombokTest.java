package lombok;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Intro:
 * @Author: WangJiongDa(yunkai)
 * @Date: 2018/10/9
 * @Time: 上午11:20
 */
public class LombokTest {

    private static final Logger logger = LoggerFactory.getLogger(LombokTest.class);

    @Test
    public void lombokTest() {
        User user = new User();
        user.setUserName("yunkai");
        logger.info("userName = {}", user.getUserName());
    }
}
