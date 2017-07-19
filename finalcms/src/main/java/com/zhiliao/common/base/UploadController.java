package com.zhiliao.common.base;

import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.UploadUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@RestController
@RequestMapping("/upload")
public class UploadController {

    @RequestMapping
    public String upload(@RequestParam("file") MultipartFile multipartFile,
                         HttpServletRequest request,UploadUtil uploadUtil) throws Exception {
        Map result = uploadUtil.uploadFile(multipartFile,request);
        if ((Boolean) result.get("success"))
            return JsonUtil.toUploadSUCCESS("上传成功！", (String) result.get("fileName"));
        return JsonUtil.toUploadRROR("上传失败！");
    }


    @RequestMapping("/wangEditorUpload")
    public String kindEditorUpload(@RequestParam("file") MultipartFile multipartFile,
                         HttpServletRequest request,UploadUtil uploadUtil) throws Exception {
        Map result = uploadUtil.uploadFile(multipartFile,request);
        if ((Boolean) result.get("success"))
            return (String) result.get("fileName");
        return "error|服务器端错误";
    }

}
