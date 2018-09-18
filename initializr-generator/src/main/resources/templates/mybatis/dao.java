package {{packageName}}.dao;

import java.util.List;

import {{packageName}}.model.UserDO;

/**
 * @author chengxu
 */
public interface UserDAO {
	
	List<UserDO> getAll();

	UserDO getOne(Long id);

	long insert(UserDO user);

    long update(UserDO user);

    long delete(Long id);
}