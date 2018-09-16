package io.spring.initializr.extend;

import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.metadata.Dependency;

import java.util.Map;

/**
 * @author zhanghao3
 * @date 2018/9/16
 */
public class AppPropertiesCreate {

    public static String createContent(Map<String, Object> model, ProjectRequest request) {
        StringBuilder sb = new StringBuilder();
        request.getResolvedDependencies().stream().filter(dependency -> dependency.getId().equals("mybatis")).findFirst().ifPresent(value -> sb.append(value));
        return sb.toString();
    }
}
