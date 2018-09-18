package io.spring.initializr.extend;

import io.spring.initializr.generator.ProjectGenerator;
import io.spring.initializr.generator.ProjectRequest;
import io.spring.initializr.util.TemplateRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * @author zhanghao3
 * @date 2018/9/16
 */
@Component
public class AppPropertiesCreate {

    @Autowired
    private TemplateRenderer templateRenderer = new TemplateRenderer();

    @Autowired
    private ProjectGenerator projectGenerator;

    /**
     * 生成application.properties文件的内容
     *
     * @param request
     * @param model
     * @return
     */
    public String createAppropContent(
            ProjectRequest request, Map<String, Object> model) {
        StringBuilder sb = new StringBuilder();
        //mybatis
        request.getResolvedDependencies().stream()
                .filter(dependency -> dependency.getId().equals("mybatis")).findFirst()
                .ifPresent(value -> sb.append(templateRenderer.process("mybatis/application.properties", model)));
        System.out.println(sb.toString());
        return sb.toString();
    }

    public void createJavaFile(ProjectRequest request, Map<String, Object> model, File dir, String language) {

        //
        File src = new File(new File(dir, "src/main/" + language),
                request.getPackageName().replace(".", "/"));
        src.mkdirs();
        //扩展
        File resource = new File(dir, "src/main/resources");
        resource.mkdirs();
        File test = new File(new File(dir, "src/test/" + language),
                request.getPackageName().replace(".", "/"));
        test.mkdirs();

        //DO
        new Unit().mkdir(src, "model").writeFile("UserDO.java", "mybatis/do.java", model);
        //Mapper.xml Mapper.java
        new Unit().mkdir(src, "dao").writeFile("UserDAO.java", "mybatis/dao.java", model);
        new Unit().mkdir(resource, "mybatis/mapper").writeFile("UserDAOMapper.xml", "mybatis/mapper.xml", model);


        //main类添加注解@
        ProjectGenerator.Imports imports = new ProjectGenerator.Imports(request.getLanguage());
        ProjectGenerator.Annotations annotations = new ProjectGenerator.Annotations();

        //添加mapper扫描包的注解
        imports.add("org.mybatis.spring.annotation.MapperScan");
        annotations.add("@MapperScan(\"" + request.getPackageName() + ".dao\")");


        model.put("applicationImports", new StringBuilder(model.get("applicationImports").toString()).append("\n" + imports.toString()));
        model.put("applicationAnnotations", new StringBuilder(model.get("applicationAnnotations").toString()).append("\n" + annotations.toString()));
        //test
        new Unit().mkdir(test, "dao").writeFile("UserDAOTest.java", "mybatis/test.java", model);

    }

    private class Unit {

        private File dirFile;

        public Unit mkdir(File src, String dirName) {
            dirFile = new File(src, dirName);
            dirFile.mkdirs();
            return this;
        }

        public Unit writeFile(String fileName, String templateName, Map<String, Object> model) {
            projectGenerator.write(new File(dirFile, fileName), templateName, model);
            return this;
        }
    }
}
