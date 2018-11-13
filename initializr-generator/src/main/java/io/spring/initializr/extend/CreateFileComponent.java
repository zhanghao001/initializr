package io.spring.initializr.extend;

import io.spring.initializr.generator.ProjectGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Map;

/**
 * @author zhanghao3
 * @date 2018/11/10
 */
@Component
public class CreateFileComponent {

    @Autowired
    private ProjectGenerator projectGenerator;

    private File dirFile;

    public CreateFileComponent mkdir(File src, String dirName) {
        dirFile = new File(src, dirName);
        dirFile.mkdirs();
        return this;
    }

    public CreateFileComponent writeFile(String fileName, String templateName, Map<String, Object> model) {
        projectGenerator.write(new File(dirFile, fileName), templateName, model);
        return this;
    }
}
