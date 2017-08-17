package com.zhiliao.module.web.client;

import com.zhiliao.common.base.BaseController;
import com.zhiliao.mybatis.model.TClientUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description:前台用户中心控制器
 *
 * @author Jin
 * @create 2017-04-07
 **/
@Controller
@RequestMapping("/UCenter")
public class UCenterController extends BaseController<TClientUser>{
    @Override
    public String index(@RequestParam(value = "pageNumber",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSzie",defaultValue = "100") Integer pageSize, TClientUser pojo, Model model) {
        return null;
    }

//    @RequestMapping
//    @ResponseBody
//    public String test(HttpServletRequest request){
//        if(request.getUserPrincipal()!=null){
//            SsoInfoImpl principal = (SsoInfoImpl)request.getUserPrincipal();
//            Map<String,Object> ssoInfo=(principal!=null?principal.getAttributes():null);
////            String username=(principal!=null?principal.getAttributes().get("username").toString():null);
////            System.out.println("username:"+username);
//            ssoInfo.forEach((key, value) -> {
//                        try {
//                           String s= (String) value;
//                            s=new String(s.getBytes("iso-8859-1"),"utf-8");
//                            System.out.println(key + "->" +s);
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    );
//            return "front/default/index";
//        }
//
//        return "no message";
//    }


    @Override
    public String input(@RequestParam(value = "id",required = false)Integer Id, Model model) {
        return null;
    }

    @Override
    public String save(TClientUser pojo) {
        return null;
    }

    @Override
    public String delete(@RequestParam(value = "ids",required = false)Integer[] ids) {
        return null;
    }
}
