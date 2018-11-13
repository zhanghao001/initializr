package io.spring.initializr.extend;

import io.spring.initializr.generator.ProjectGenerator;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.metadata.MetadataElement;
import io.spring.initializr.util.TemplateRenderer;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 拓展内容的生成类.
 * <p>
 * 1.application.properties部分的内容生成
 * 2.样例代码的生成.src/main/java的样例代码,测试代码,以及启动类的注解添加
 * </p>
 *
 * @author zhanghao3
 * @date 2018/9/16
 */
@Component
public class ExtendContentCreator {

    @Autowired
    private TemplateRenderer templateRenderer = new TemplateRenderer();

    @Autowired
    private ProjectGenerator projectGenerator;

    @Autowired
    private List<ModuleExtend> allModuelExtend;

    /**
     * 生成application.properties文件的内容
     *
     * @param request
     * @param model
     * @return
     */
    public String createAppProperties(
            ProjectRequest request, Map<String, Object> model) {
        StringBuilder sb = new StringBuilder();
        //mybatis和tk存在冲突,需要排除掉
        List<String> dependencyIds = request.getResolvedDependencies().stream().map(MetadataElement::getId).collect(Collectors.toList());

        Map<String, List<ModuleExtend>> groupModule = allModuelExtend.stream().filter(item -> Objects.nonNull(item.groupId())).collect(Collectors.groupingBy(ModuleExtend::groupId));
        //todo 组内的依赖,按照优先级排序

        Map<String, List<ModuleExtend>> dependencyModule = allModuelExtend.stream().collect(Collectors.groupingBy(ModuleExtend::dependencyId));
        dependencyIds.forEach(dependencyId -> {
            if (dependencyModule.containsKey(dependencyId)) {
                ModuleExtend moduleExtend = dependencyModule.get(dependencyId).get(0);
                String groupId = moduleExtend.groupId();
                if (groupModule.containsKey(groupId)) {
                    //寻找组内的所有其他依赖,如果存在,则根据优先级判断
                    List<ModuleExtend> groupMouules = groupModule.get(groupId);
                    //组的所有依赖中,按照优先级依次查找是否存在
                    ModuleExtend moduleExtend1 = groupMouules.stream().filter(dependencyIds::contains).findFirst().orElseThrow(RuntimeException::new);
                    // 转化完成,但是可能存在一些重复性的
                }
            }
        });

        return sb.toString();
    }

    public void createJavaFile(ProjectRequest request, Map<String, Object> model, File dir, String language) {

        //
        File src = new File(new File(dir, "src/main/" + language), request.getPackageName().replace(".", "/"));
        src.mkdirs();
        //扩展
        File resource = new File(dir, "src/main/resources");
        resource.mkdirs();
        File testSrc = new File(new File(dir, "src/test/" + language), request.getPackageName().replace(".", "/"));
        testSrc.mkdirs();

        ProjectDir projectDir = new ProjectDir().setSrc(src).setResources(resource).setTestSrc(testSrc);

    }

    @Data
    @Accessors(chain = true)
    public class ProjectDir {
        private File src;
        private File resources;
        private File testSrc;
        private File testResources;
    }
}
