package io.spring.initializr.extend;

import io.spring.initializr.generator.ProjectRequest;

import java.util.Map;
import java.util.Optional;

/**
 * 组件的代码生成实现
 *
 * @author zhanghao3
 * @date 2018/9/18
 */
public interface ModuleExtend {

    String appPoperties(ProjectRequest request, Map<String, Object> model);

    void javaFile(ProjectRequest request, Map<String, Object> model, ExtendContentCreator.ProjectDir projectDir);


    String dependencyId();

    /**
     * 部分组件存在冲突情况.冲突的组件在同一组,按照优先级选择使用
     */
    default String groupId() {
        return null;
    }

    /**
     * 在同一组的优先级.
     *
     * @return
     */
    default Integer priority() {
        return 1;
    }

    /**
     * 数据库orm的组,tk,mybatis,jdbc.
     */
    String GROUP_ORM = "orm";
}
