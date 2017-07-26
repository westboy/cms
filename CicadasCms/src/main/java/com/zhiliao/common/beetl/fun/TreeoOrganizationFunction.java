package com.zhiliao.common.beetl.fun;

import com.zhiliao.common.utils.ControllerUtil;
import com.zhiliao.module.web.system.service.OrganizationService;
import com.zhiliao.mybatis.model.master.TSysOrg;
import org.beetl.core.Context;
import org.beetl.core.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:输出子权限节点函数
 *
 * @author Jin
 * @create 2017-04-15
 **/
@Service
public class TreeoOrganizationFunction implements Function{

    @Autowired
    private OrganizationService service;

    @Value("${system.http.protocol}")
    private String httpProtocol;

    @Override
    public Object call(Object[] objects, Context context) {

        Long pid = (Long) objects[0];
        String url = (String) objects[1];
        String layout = (String) objects[2];
        return recursion(pid,url,layout);
    }

    /* 递归函数 */
    private String recursion(Long pid,String url,String layout){
       StringBuffer sbf = new StringBuffer();
       List<TSysOrg> orgList  = service.findByPid(pid);
       if(orgList!=null&&orgList.size()>0){
           for(TSysOrg per:orgList){
               sbf.append("  <li data-id=\""+per.getId()+"\" data-pid=\""+pid+"\" data-url=\""+httpProtocol+"://"+ ControllerUtil.getDomain()+"/"+url+"/"+per.getId()+"\" data-divid=\""+layout+"\">"+per.getName()+"</li>");
               sbf.append(recursion(per.getId(),url,layout));
           }
           return  sbf.toString();
       }
       return "";
    }
}
