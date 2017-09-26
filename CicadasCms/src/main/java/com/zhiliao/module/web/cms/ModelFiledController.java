package com.zhiliao.module.web.cms;

import com.zhiliao.common.base.BaseController;
import com.zhiliao.module.web.cms.service.ModelFiledService;
import com.zhiliao.mybatis.model.TCmsModelFiled;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.zhiliao.common.db.kit.DbTableKit.PREPARED_MODEL_FILED_NAME;

/**
 * Description:模型字段Controller
 *
 * @author Jin
 * @create 2017-05-12
 **/
@Controller
@RequestMapping("system/cms/model/filed")
public class ModelFiledController extends BaseController<TCmsModelFiled>{

    @Autowired
    private ModelFiledService filedService;

    @RequestMapping
    @Override
    public String index(@RequestParam(value = "pageCurrent",defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize",defaultValue = "100")Integer pageSize,
                        TCmsModelFiled pojo, Model model) {
        model.addAttribute("pojo",pojo);
        model.addAttribute("model",filedService.page(pageNumber,pageSize,pojo));
        return "cms/model_filed_list";
    }

    @RequiresPermissions("modelFiled:input")
    @RequestMapping("/input")
    @Override
    public String input(@RequestParam(value = "modelId",required = false)Integer modelId, Model model) {
        model.addAttribute("modelId",modelId);
        return "cms/model_filed_input";
    }

    @RequiresPermissions("modelFiled:save")
    @RequestMapping("/save")
    @ResponseBody
    @Override
    public String save(TCmsModelFiled pojo) {
        if(pojo.getFiledId()!=null)
            return filedService.update(pojo);
        return filedService.save(pojo);
    }

    @RequiresPermissions("modelFiled:delete")
    @RequestMapping("/delete")
    @ResponseBody
    @Override
    public String delete(Integer[] ids) {
        return filedService.delete(ids);
    }

    @RequestMapping("/checkFiledName")
    @ResponseBody
   public String checkFiledName(@RequestParam("filedName") String filedName){
        Boolean flag = false;
        for(String s :PREPARED_MODEL_FILED_NAME){
            if(filedName.toLowerCase().equals(s.toLowerCase())){
                flag =true;
                break;
            }
        }
        if((filedService.findModelFiledByFiledName(filedName)!=null)||flag)
            return "{\"error\": \"名字已经被占用啦\"}";
        return "{\"ok\": \"名字很棒\"}";
   }

}
