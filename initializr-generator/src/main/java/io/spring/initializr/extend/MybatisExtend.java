package io.spring.initializr.extend;

import io.spring.initializr.generator.ProjectRequest;

import java.io.File;
import java.util.Map;

/**
 * @author zhanghao3
 * @date 2018/9/18
 */
public class MybatisExtend implements ModuleExtend {

    @Override
    public String createAppropContent(ProjectRequest request, Map<String, Object> model) {
        return null;
    }

    @Override
    public void createJavaFile(ProjectRequest request, Map<String, Object> model, File dir, String language) {

    }
}
