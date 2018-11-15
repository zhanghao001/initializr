package {{packageName}}.dao;

import {{packageName}}.{{applicationName}}Tests;
import {{packageName}}.model.UserDO;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Random;

/**
 * @author chengxu
 */
@Transactional
@Rollback
public class UserDAOTest extends {{applicationName}}Tests {

    @Autowired
    private UserDAO userDAO;

    private static final String SEX_WOMAN = "WOMAN";
    private static final String SEX_MAN = "MAN";

    private UserDO generateOne() {
        UserDO tkUserDO = new UserDO();
        tkUserDO.setUserName("userName-" + new Random().nextInt());
        tkUserDO.setPassword("password-" + new Random().nextInt());
        tkUserDO.setUserSex(new Random().nextBoolean() ? SEX_MAN : SEX_WOMAN);
        tkUserDO.setNickName("nickName-" + new Random().nextInt());
        return tkUserDO;
    }

    @Test
    public void testInsert() {
        userDAO.insert(generateOne());
        userDAO.insert(generateOne());
        userDAO.insert(generateOne());
        Assert.assertEquals(3, userDAO.selectAll().size());
    }

    @Test
    public void testDelete() {
        UserDO tkUserDO = generateOne();
        userDAO.insertSelective(tkUserDO);
        Long id = tkUserDO.getId();
        System.out.println(id);
        long delete_row = userDAO.deleteByPrimaryKey(id);
        Assert.assertEquals(1l, delete_row);

        UserDO one = userDAO.selectByPrimaryKey(id);
        Assert.assertNull(one);
    }

    @Test
    public void testUpdate() {
        UserDO tkUserDO = generateOne();
        userDAO.insert(tkUserDO);
        Long id = tkUserDO.getId();

        UserDO user = userDAO.selectByPrimaryKey(id);
        System.out.println(user.toString());
        user.setNickName("neo");
        userDAO.updateByPrimaryKey(user);

        UserDO newOne = userDAO.selectByPrimaryKey(id);
        Assert.assertTrue(("neo".equals(newOne.getNickName())));
    }

    @Test
    public void testQuery() {
        List<UserDO> users = userDAO.selectAll();
        Assert.assertEquals(0l, users.size());

        String newName = "to update";
        UserDO UserDO = generateOne();
        UserDO.setUserName(newName);
        userDAO.insert(UserDO);

        //简单的查询,只支持equals
        UserDO toQuery = new UserDO();
        toQuery.setUserName(newName);
        List<UserDO> selectRs = userDAO.select(toQuery);
        Assert.assertEquals(1l, selectRs.size());
        Assert.assertEquals(newName, selectRs.get(0).getUserName());
        //复杂查询,基本支持所有语法
        Example example = new Example(UserDO.class);
        example.selectProperties("userName");
        example.createCriteria().andEqualTo("userName",newName);
        selectRs = userDAO.selectByExample(example);
        Assert.assertEquals(1l, selectRs.size());
        Assert.assertEquals(newName, selectRs.get(0).getUserName());
        Assert.assertNull(selectRs.get(0).getPassword());
        //mybatis配置的方法
        UserDO byMaxId = userDAO.getByMaxId();
        Assert.assertEquals(newName, byMaxId.getUserName());
    }
}