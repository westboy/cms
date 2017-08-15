package com.zhiliao;

import com.zhiliao.common.template.TemplateFile;
import com.zhiliao.common.template.TemplateFileService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-08-11
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
public class TemplatFileTest {

    @Autowired
    TemplateFileService templateFileService;

    @Test
    public void listFile(){
       List<TemplateFile> list= templateFileService.findAll();
        list.forEach(templateFile -> System.out.println(templateFile.getFileName()));
    }

}
