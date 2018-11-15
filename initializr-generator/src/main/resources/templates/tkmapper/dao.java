package {{packageName}}.dao;

import {{packageName}}.model.UserDO;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author chengxu
 */
public interface UserDAO extends Mapper<UserDO> {

    UserDO getByMaxId();
}