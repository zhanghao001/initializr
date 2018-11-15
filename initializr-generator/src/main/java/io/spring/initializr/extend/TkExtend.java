package io.spring.initializr.extend;

import io.spring.initializr.generator.ProjectGenerator;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.util.TemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author zhanghao3
 * @date 2018/9/18
 */
@Component
public class TkExtend implements ModuleExtend {


    @Autowired
    private TemplateRenderer templateRenderer = new TemplateRenderer();

    @Autowired
    private CreateFileComponent fileComponent;

    @Override
    public String appPoperties(ProjectRequest request, Map<String, Object> model) {
        return templateRenderer.process("tkmapper/application.properties", model);
    }

    @Override
    public void javaFile(ProjectRequest request, Map<String, Object> model, ExtendContentCreator.ProjectDir projectDir) {

        //DO
        fileComponent.mkdir(projectDir.getSrc(), "model").writeFile("UserDO.java", "tkmapper/do.java", model);
        //Mapper.xml Mapper.java
        fileComponent.mkdir(projectDir.getSrc(), "dao").writeFile("UserDAO.java", "tkmapper/dao.java", model);
        fileComponent.mkdir(projectDir.getResources(), "mybatis/mapper").writeFile("UserDAOMapper.xml", "tkmapper/mapper.xml", model);

        //main类添加注解@
        ProjectGenerator.Imports imports = new ProjectGenerator.Imports(request.getLanguage());
        ProjectGenerator.Annotations annotations = new ProjectGenerator.Annotations();

        //添加mapper扫描包的注解
        imports.add("tk.mybatis.spring.annotation.MapperScan");
        annotations.add("@MapperScan(\"" + request.getPackageName() + ".dao\")");

        model.put("applicationImports", new StringBuilder(model.get("applicationImports").toString()).append("\n" + imports.toString()));
        model.put("applicationAnnotations", new StringBuilder(model.get("applicationAnnotations").toString()).append("\n" + annotations.toString()));
        //test
        fileComponent.mkdir(projectDir.getTestSrc(), "dao").writeFile("UserDAOTest.java", "tkmapper/test.java", model);
    }

    @Override
    public String dependencyId() {
        return "tkMapper";
    }

    @Override
    public String groupId() {
        return ModuleExtend.GROUP_ORM;
    }

    @Override
    public Integer priority() {
        return 1;
    }
}
