package test;

import com.hyl.atcrowdfunding.model.User;
import com.hyl.atcrowdfunding.service.UserService;
import com.hyl.atcrowdfunding.utils.MD5Util;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: hyl
 * @date: 2019/07/15
 **/
public class TestUser {


    @Test
    public void testSaveUser(){

        ApplicationContext ioc = new ClassPathXmlApplicationContext("spring-context.xml");

        UserService userService = ioc.getBean(UserService.class);

        for (int i = 0; i < 100; i++) {

            User user = new User();
            user.setLoginacct("test"+i);
            user.setUserpswd(MD5Util.digest("123"));
            user.setUsername("test"+i);
            user.setEmail("test"+i+"@foxmail.com");
            user.setCreatetime("2019-07-15 10:35:00");

            System.out.println(userService.toString());
            try {
                userService.saveUser(user);
            }catch (Exception e){
                System.out.println(e);
            }

        }
        
    }
}
