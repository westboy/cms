package com.zhiliao.common.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zhiliao.common.exception.SystemException;
import jodd.datetime.JDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

/**
 * Description:
 *
 * @author Jin
 * @create 2017-07-19
 **/
@Component
public class UploadUtil {

    @Value("${qiniu.access.key}")
    private String accessKey;
    @Value("${qiniu.secret.key}")
    private String secretKey;
    @Value("${qiniu.bucketname}")
    private String bucketname;
    @Value("${qiniu.cdn.domain}")
    private String domain;
    @Value("${qiniu.upload.on}")
    private String qiniuUpload;

    public Map uploadFile(MultipartFile multipartFile, HttpServletRequest request){
        Map resultMap = Maps.newHashMap();
        resultMap.put("success", false);
        JDateTime jt = new JDateTime();
        String fileType = getFileType(multipartFile.getOriginalFilename());
        String fileName = System.currentTimeMillis() + "." + fileType;
        if (!Boolean.parseBoolean(qiniuUpload)) {
            String newName = File.separator + "upload" + File.separator + jt.getYear()
                    + File.separator + jt.getMonth() + File.separator + jt.getDay() + File.separator + fileName;
            String uploadPath = PathUtil.getWebRootPath()+File.separator + "WEB-INF" +File.separator+"classes"+ File.separator + "static";
            if (!multipartFile.isEmpty()) {
                File file = new File(uploadPath + newName);
                if (!file.getParentFile().mkdirs())/*如果不存在就创建*/
                    file.getParentFile().mkdirs();
                try {
                    this.writeFile(multipartFile.getBytes(),file);
                } catch (Exception e) {
                   throw  new SystemException(e.getMessage());
                }
                resultMap.put("success", true);
                resultMap.put("fileName", request.getScheme()+"://"+ControllerUtil.getDomain() + "/static" + newName.replace(File.separator, "/"));
            }
        } else {
            String result = QiniuUtil.upload(accessKey, secretKey, bucketname, multipartFile);
            if (!StrUtil.isBlank(result)) {
                String fileKey = JSON.parseObject(result).getString("key");
                String fileUrl = domain + "/" + fileKey;
                if (StrUtil.getExtensionName(fileName).equals("jpg") || StrUtil.getExtensionName(fileName).equals("JPG") || StrUtil.getExtensionName(fileName).equals("png") || StrUtil.getExtensionName(fileName).equals("PNG") || StrUtil.getExtensionName(fileName).equals("jpeg") || StrUtil.getExtensionName(fileName).equals("JPEG")) {
                    fileUrl += "?imageslim";
                }
                resultMap.put("success", true);
                resultMap.put("fileName",fileUrl);

            }

        }
        return resultMap;
    }

    public static String getFileType(String fileName) {
        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        return type;
    }

    private synchronized void writeFile(byte[] bytes,File file) throws Exception{
        BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(file));
        stream.write(bytes);
        stream.close();
    }

}
