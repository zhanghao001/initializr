package {{packageName}}.dao;

import {{packageName}}.model.TkUserDO;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Random;

/**
 * @author chengxu
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class TkUserDAOTest {

    @Autowired
    private TkUserDAO tkUserDAO;

    private static final String SEX_WOMAN = "WOMAN";
    private static final String SEX_MAN = "MAN";

    private TkUserDO generateOne() {
        TkUserDO tkUserDO = new TkUserDO();
        tkUserDO.setUserName("userName-" + new Random().nextInt());
        tkUserDO.setPassword("password-" + new Random().nextInt());
        tkUserDO.setUserSex(new Random().nextBoolean() ? SEX_MAN : SEX_WOMAN);
        tkUserDO.setNickName("nickName-" + new Random().nextInt());
        return tkUserDO;
    }

    @Test
    public void testInsert() {
        tkUserDAO.insert(generateOne());
        tkUserDAO.insert(generateOne());
        tkUserDAO.insert(generateOne());
        Assert.assertEquals(3, tkUserDAO.selectAll().size());
    }

    @Test
    public void testDelete() {
        TkUserDO tkUserDO = generateOne();
        tkUserDAO.insertSelective(tkUserDO);
        Long id = tkUserDO.getId();
        System.out.println(id);
        long delete_row = tkUserDAO.deleteByPrimaryKey(id);
        Assert.assertEquals(1l, delete_row);

        TkUserDO one = tkUserDAO.selectByPrimaryKey(id);
        Assert.assertNull(one);
    }

    @Test
    public void testUpdate() {
        TkUserDO tkUserDO = generateOne();
        tkUserDAO.insert(tkUserDO);
        Long id = tkUserDO.getId();

        TkUserDO user = tkUserDAO.selectByPrimaryKey(id);
        System.out.println(user.toString());
        user.setNickName("neo");
        tkUserDAO.updateByPrimaryKey(user);

        TkUserDO newOne = tkUserDAO.selectByPrimaryKey(id);
        Assert.assertTrue(("neo".equals(newOne.getNickName())));
    }

    @Test
    public void testQuery() {
        List<TkUserDO> users = tkUserDAO.selectAll();
        Assert.assertEquals(0l, users.size());

        String newName = "to update";
        TkUserDO tkUserDO = generateOne();
        tkUserDO.setUserName(newName);
        tkUserDAO.insert(tkUserDO);

        //简单的查询,只支持equals
        TkUserDO toQuery = new TkUserDO();
        toQuery.setUserName(newName);
        List<TkUserDO> selectRs = tkUserDAO.select(toQuery);
        Assert.assertEquals(1l, selectRs.size());
        Assert.assertEquals(newName, selectRs.get(0).getUserName());
        //复杂查询,基本支持所有语法
        Example example = new Example(TkUserDO.class);
        example.selectProperties("userName");
        example.createCriteria().andEqualTo("userName",newName);
        selectRs = tkUserDAO.selectByExample(example);
        Assert.assertEquals(1l, selectRs.size());
        Assert.assertEquals(newName, selectRs.get(0).getUserName());
        Assert.assertNull(selectRs.get(0).getPassword());
    }
}