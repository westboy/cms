package com.zhiliao.module.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:内容api
 *
 * @author Jin
 * @create 2017-05-31
 **/
@RestController
@RequestMapping("/api/")
public class ContentInterface {

    @ApiOperation("测试接口")
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    @ResponseBody
    public String getContent(){
        return "这是一个接口";
    }

}
