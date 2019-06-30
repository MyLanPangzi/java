package com.histcat.springboot.jpa;

import com.histcat.springboot.jpa.dao.UserDao;
import com.histcat.springboot.jpa.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaApplicationTests {

    @Autowired
    UserDao userDao;

    @Test
    public void contextLoads() {
    }

    @Test
    public void test() {
        User user = User
                .builder()
                .username("test")
                .password("123")
                .gender(1)
                .build();

        userDao.save(user);
        assertNotNull(user.getId());
    }
}
