package com.zhiliao.module.api;

import com.alibaba.fastjson.JSONObject;
import com.zhiliao.common.annotation.ApiValidate;
import com.zhiliao.common.exception.ApiException;
import com.zhiliao.common.utils.JsonUtil;
import com.zhiliao.common.utils.StrUtil;
import com.zhiliao.module.web.system.service.SysUserService;
import com.zhiliao.mybatis.model.master.TSysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:系统管理员api
 *
 * @author Jin
 * @create 2017-06-05
 **/
@RestController
@RequestMapping("/api/admin")
public class AdminApiController {

    @Autowired
    private SysUserService userService;

    @ApiValidate
    @RequestMapping("/save")
    public String update(@RequestParam(value = "username",required = false) String username,
                         @RequestParam(value = "password",required = false) String password,
                         @RequestParam(value = "type",required = false) Integer type){
        if(StrUtil.isBlank(username)||StrUtil.isBlank(password)||type==null||type>2||type<0)
            throw new ApiException("接口调用失败，请检查参数！");
        TSysUser user = new  TSysUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setStatus(true);
        Integer role = type==0?9:14;

        if(userService.SaveSysUser(user,role)) {
            JSONObject result = new JSONObject();
            result.put("userId",user.getUserId());
            result.put("roleId",role);
            result.put("username",username);
            result.put("password",password);
            return JsonUtil.toSuccessResultJSON("用户添加成功", result);
        }

        throw new ApiException("用户添加失败！");
    }

}
