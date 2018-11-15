package io.spring.initializr.extend;

import io.spring.initializr.generator.ProjectRequest;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhanghao3
 * @date 2018/9/18
 */
@Component
public class JdbcExtend implements ModuleExtend {

    @Override
    public String appPoperties(ProjectRequest request, Map<String, Object> model) {
        return "";
    }

    @Override
    public void javaFile(ProjectRequest request, Map<String, Object> model, ExtendContentCreator.ProjectDir projectDir) {
    }

    @Override
    public String dependencyId() {
        return "jdbc";
    }

    @Override
    public String groupId() {
        return ModuleExtend.GROUP_ORM;
    }

    @Override
    public Integer priority() {
        return 3;
    }
}
