package com.zhiliao.common.utils;

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

public class QiniuUtil {

    /**
     * 七牛文件上传
     * @param accessKey
     * @param secretKey
     * @param bucketname
     * @param multipartFile
     * @return
     */
    public static String upload(String accessKey, String secretKey, String bucketname, MultipartFile multipartFile){
        Auth auth = Auth.create(accessKey, secretKey);
        /* 七牛上传不能自动获取区域，只能手动设置 */
        Zone z = Zone.zone0();
        Configuration c = new Configuration(z);
        UploadManager uploadManager = new UploadManager(c);
        String result = null;
        try {
            Response res = uploadManager.put(multipartFile.getBytes(), StrUtil.getUUID()+"."+ StrUtil.getExtensionName(multipartFile.getOriginalFilename()), auth.uploadToken(bucketname));
            result=res.bodyString();
        }catch (Exception e){
            e.printStackTrace();
        }

      return result;
    }


}