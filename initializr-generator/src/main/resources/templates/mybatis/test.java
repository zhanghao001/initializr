package {{packageName}}.dao;

import java.util.List;
import java.util.Random;

import {{packageName}}.dao.UserDAO;
import {{packageName}}.model.UserDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author chengxu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    private static final String SEX_WOMAN = "WOMAN";
    private static final String SEX_MAN = "MAN";

    private UserDO generateOne() {
        UserDO userDO = new UserDO();
        userDO.setUserName("userName-" + new Random().nextInt());
        userDO.setPassword("password-" + new Random().nextInt());
        userDO.setUserSex(new Random().nextBoolean() ? SEX_MAN : SEX_WOMAN);
        userDO.setNickName("nickName-" + new Random().nextInt());
        return userDO;
    }

    @Test
    public void testInsert() {
        userDAO.insert(generateOne());
        userDAO.insert(generateOne());
        userDAO.insert(generateOne());

        Assert.assertEquals(3, userDAO.getAll().size());
    }

    @Test
    public void testDelete() {
        UserDO userDO = generateOne();
        userDAO.insert(userDO);
        Long id = userDO.getId();

        long delete_row = userDAO.delete(id);
        Assert.assertEquals(1l, delete_row);

        UserDO one = userDAO.getOne(id);
        Assert.assertNull(one);
    }

    @Test
    public void testUpdate() {

        UserDO userDO = generateOne();
        userDAO.insert(userDO);
        Long id = userDO.getId();

        UserDO user = userDAO.getOne(id);
        System.out.println(user.toString());
        user.setNickName("neo");
        userDAO.update(user);

        UserDO newOne = userDAO.getOne(id);
        Assert.assertTrue(("neo".equals(newOne.getNickName())));
    }


    @Test
    public void testQuery() {
        List<UserDO> users = userDAO.getAll();
        if (users == null || users.size() == 0) {
            System.out.println("is null");
        } else {
            System.out.println(users.toString());
        }
    }

}
