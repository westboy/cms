package com.zhiliao.common.template;

import java.util.List;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-08-11
 **/
public class TemplateFile {

    private String fileName;

    private String filePath;

    private Boolean isDirectory;

    private String content;

    private List<TemplateFile> childList;

    public List<TemplateFile> getChildList() {
        return childList;
    }

    public void setChildList(List<TemplateFile> childList) {
        this.childList = childList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Boolean getIsDirectory() {
        return isDirectory;
    }

    public void setIsDirectory(Boolean _isDirectory) {
        isDirectory = _isDirectory;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
