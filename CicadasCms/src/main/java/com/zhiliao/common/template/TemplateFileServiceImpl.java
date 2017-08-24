package com.zhiliao.common.template;

import com.google.common.collect.Lists;
import com.zhiliao.common.exception.SystemException;
import com.zhiliao.common.utils.PathUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-08-11
 **/
@Service
public class TemplateFileServiceImpl implements TemplateFileService{

    private final String TEMPLATE_PATH = PathUtil.getRootClassPath()+ File.separator+"templates"+File.separator+"www";

    @Override
    public List<TemplateFile> findAll() {
        List<TemplateFile> result = Lists.newArrayList();
        File mainFile = new File(TEMPLATE_PATH);
        if(!mainFile.exists()||mainFile.listFiles().length<1) throw new SystemException("请检查模板目录是否存在！");
        /*只遍历三级级目录*/
        for(File file :mainFile.listFiles()){
            TemplateFile templateFile = new TemplateFile();
            templateFile.setFileName(file.getName());
            templateFile.setFilePath(file.getAbsolutePath());
            templateFile.setIsDirectory(file.isDirectory());
            if(file.isDirectory()){
                File subFile = new File(file.getAbsolutePath());
                List<TemplateFile> childList = Lists.newArrayList();
                for(File subfile :subFile.listFiles()) {
                    TemplateFile subTemplateFile = new TemplateFile();
                    subTemplateFile.setFileName(subfile.getName());
                    subTemplateFile.setFilePath(subfile.getAbsolutePath());
                    subTemplateFile.setIsDirectory(subfile.isDirectory());
                    if(subfile.isDirectory()){
                        List<TemplateFile> subChildList = Lists.newArrayList();
                        File subChildFile = new File(subfile.getAbsolutePath());
                        if(!subChildFile.exists()||mainFile.listFiles().length<1) continue;
                        for(File subChildfile :subChildFile.listFiles()) {
                            TemplateFile subChildTemplateFile = new TemplateFile();
                            subChildTemplateFile.setFileName(subChildfile.getName());
                            subChildTemplateFile.setFilePath(subChildfile.getAbsolutePath());
                            subChildTemplateFile.setIsDirectory(subChildfile.isDirectory());
                            subChildList.add(subChildTemplateFile);
                        }
                        subTemplateFile.setChildList(subChildList);
                    }
                    childList.add(subTemplateFile);
                }
                templateFile.setChildList(childList);
            }
            result.add(templateFile);
        }
        return result;
    }

    @Override
    public TemplateFile findByPath(String path) {
        File file = new File(path);
        if(!file.exists()) throw new SystemException("模板不存在请检查！");
        TemplateFile templateFile = new TemplateFile();
        templateFile.setFileName(file.getName());
        templateFile.setFilePath(file.getAbsolutePath());
        templateFile.setContent(this.readTemplateFileContent(file));
        return templateFile;
    }


    public static void main(String[]args) {
        List<TemplateFile> list = new TemplateFileServiceImpl().findAll();
        for (TemplateFile templateFile : list){
           System.out.println(templateFile.getFileName());
           if(templateFile.getIsDirectory()){
               for (TemplateFile t : templateFile.getChildList()){
                   System.out.println(templateFile.getFileName()+"---->"+t.getFileName()+"["+t.getFilePath()+"]");
                   if(t.getIsDirectory()){
                       for (TemplateFile st : t.getChildList()){ System.out.println(t.getFileName()+"---------->"+ st.getFileName()+"["+ st.getFilePath()+"]");}
                   }
               }
           }
        }
//      System.out.println(new TemplateFileServiceImpl().findByPath("E:\\work\\idea_app\\CMS\\CicadasCms\\CicadasCms\\target\\classes\\templates\\www\\blog\\about.html").getContent());
    }

    public  String readTemplateFileContent(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            while((len = inputStream.read(buffer)) != -1) {
              bos.write(buffer, 0, len);
            }
            bos.close();
            return new String(bos.toByteArray(), "utf-8");
        }catch (Exception e){
            throw  new SystemException(e.getMessage());
        }
    }

    @Async
    public  void writeTemplateFileContent(TemplateFile templateFile){
        try {
            OutputStream outputStream = new FileOutputStream(new File(templateFile.getFilePath()));
            OutputStreamWriter os = new OutputStreamWriter(outputStream, "utf-8");
            os.write(templateFile.getContent());
            os.flush();
            os.close();
        }catch (Exception e){
            throw  new SystemException(e.getMessage());
        }
    }


    @Override
    public String delete() {
        return null;
    }
}
